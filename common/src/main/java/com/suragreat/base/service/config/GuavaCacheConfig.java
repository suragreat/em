package com.suragreat.base.service.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring.cache.guava")
@EnableCaching(proxyTargetClass = true)
public class GuavaCacheConfig {

    private String spec;

    @Bean
    @ConditionalOnProperty(prefix = "spring.redis", name = "host", matchIfMissing = true, havingValue = "host")
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(CacheBuilder.from(spec));
        return cacheManager;
    }


    public void setSpec(String spec) {
        this.spec = spec;
    }
}
