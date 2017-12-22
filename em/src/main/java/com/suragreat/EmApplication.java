package com.suragreat;

import com.suragreat.base.filter.LoggerFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@EnableSwagger2
@ConfigurationProperties(prefix = "web.filter")
public class EmApplication {
    private Map<String, String> urlPatterns;
    private Map<String, String> excludeUrls;

    public Map<String, String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(Map<String, String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public Map<String, String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(Map<String, String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public Collection<String> getUrlPatterns(String filterName) {
        String patterns = urlPatterns.get(filterName);
        if (StringUtils.isEmpty(patterns)) {
            return Collections.emptyList();
        }
        return Arrays.asList(StringUtils.split(patterns, ","));
    }

    public static void main(String[] args) {
        SpringApplication.run(EmApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean loggerFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LoggerFilter loggerFilter = new LoggerFilter();
        registrationBean.setFilter(loggerFilter);
        registrationBean.setUrlPatterns(getUrlPatterns("logger"));
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
