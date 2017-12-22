package com.suragreat.base.service.config;

import feign.*;
import feign.codec.Decoder;
import feign.httpclient.ApacheHttpClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("feign")
public class FeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    private Integer connectionTimeout = 5000;
    private Integer readTimeout = 10000;
    private Integer retry = 0;
    private Long period = 100L;
    private Long maxPeriod = 1000L;

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(Long maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    @Bean
    @Autowired
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    @Bean
    public Client feignClient() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return new ApacheHttpClient(httpClient);
    }

    @Bean
    public Decoder feignDecoder() {
        List<HttpMessageConverter<?>> hmConverterList = new ArrayList<>();
        messageConverters.getObject().forEach((c) -> {
            if (c instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) c;
                List<MediaType> supportedMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                if (CollectionUtils.isEmpty(supportedMediaTypes)) {
                    supportedMediaTypes.add(MediaType.APPLICATION_JSON);
                    supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
                }
                addMediaType(supportedMediaTypes);
                jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
            }
            hmConverterList.add(c);
        });

        HttpMessageConverters converters = new HttpMessageConverters(hmConverterList);
        ObjectFactory<HttpMessageConverters> objectFactory = new ObjectFactory<HttpMessageConverters>() {

            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return converters;
            }
        };
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    private void addMediaType(List<MediaType> supportedMediaTypes) {
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(connectionTimeout, readTimeout);
    }

    @Bean
    Retryer feignRetryer() {
        if (retry > 0) {
            return new Retryer.Default(period, maxPeriod, retry + 1); // the first call is also one attempt try
        } else {
            return Retryer.NEVER_RETRY;
        }
    }
}
