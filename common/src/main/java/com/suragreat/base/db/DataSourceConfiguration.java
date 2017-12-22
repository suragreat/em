package com.suragreat.base.db;

import java.util.Properties;

import javax.sql.DataSource;

import com.suragreat.base.db.util.DecryptDataSourceTool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfiguration {
    private Properties primary;

    public Properties getPrimary() {
        return primary;
    }

    public void setPrimary(Properties primary) {
        this.primary = primary;
    }

    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @Primary
    @ConditionalOnProperty(name="spring.datasource.primary.driverClassName")
    public DataSource primaryDataSource() throws Exception {
        return DecryptDataSourceTool.buildDataSource(getPrimary());
    }

}
