package com.ctrip.framework.apollo.bean;

import com.ctrip.framework.apollo.spring.annotation.AutoRefresh;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("test")
@AutoRefresh(true)
public class TestConfigurationPropertiesBean3 {

    private int timeout;

    private int batch;

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getBatch() {
        return batch;
    }
}
