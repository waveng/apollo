package com.ctrip.framework.apollo.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("test")
public class TestConfigurationPropertiesBean8 {

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
