package com.suragreat.base.db;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suragreat.base.db.interceptor.SlowSqlInterceptor;

@Configuration
@ConfigurationProperties(prefix = "monitoring.slowsql")
public class MyBatisConfiguration {
    private long maxWaitingMills = 500;
    
    
    public long getMaxWaitingMills() {
        return maxWaitingMills;
    }

    public void setMaxWaitingMills(long maxWaitingMills) {
        this.maxWaitingMills = maxWaitingMills;
    }

    @Bean
    public Interceptor slowSqlInterceptor() {
        return new SlowSqlInterceptor(maxWaitingMills);
    }

}
