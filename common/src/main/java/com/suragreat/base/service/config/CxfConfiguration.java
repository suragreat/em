package com.suragreat.base.service.config;

import com.suragreat.base.constant.ServerErrorEnum;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "wsdl")
public class CxfConfiguration {
    private static final String SERVICE_UES = "UesRemoteService";
    private static long DEFAULT_CONNECTION_TIMEOUT = 5000;
    private static long DEFAULT_READ_TIMEOUT = 10000;
    private Map<String, Long> connectionTimeout;
    private Map<String, Long> readTimeout;
    private Map<String, String> endpoint;

    public Map<String, Long> getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Map<String, Long> connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Map<String, Long> getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Map<String, Long> readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Map<String, String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Map<String, String> endpoint) {
        this.endpoint = endpoint;
    }

    public long getConnectionTimeout(String service) {
        if (connectionTimeout != null) {
            Long timeout = connectionTimeout.get(service);
            if (timeout != null) {
                return timeout.longValue();
            }
        }
        return DEFAULT_CONNECTION_TIMEOUT;
    }

    public long getReadTimeout(String service) {
        if (readTimeout != null) {
            Long timeout = readTimeout.get(service);
            if (timeout != null) {
                return timeout.longValue();
            }
        }
        return DEFAULT_READ_TIMEOUT;
    }

    public String getEndpoint(String service) {
        if (endpoint != null) {
            String wsdl = endpoint.get(service);
            if (wsdl != null) {
                return wsdl;
            }
        }
        ServerErrorEnum.ILLGAL_CONFIGURATION.throwsException("wsdl.endpoint." + service);
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> T cxfClient(Class<T> clz, String serviceName) {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(clz);
        jaxWsProxyFactoryBean.setAddress(getEndpoint(serviceName));
        T service = (T) jaxWsProxyFactoryBean.create();
        Client proxy = ClientProxy.getClient(service);
        HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(getConnectionTimeout(serviceName));
        policy.setReceiveTimeout(getReadTimeout(serviceName));
        conduit.setClient(policy);
        return service;
    }

}
