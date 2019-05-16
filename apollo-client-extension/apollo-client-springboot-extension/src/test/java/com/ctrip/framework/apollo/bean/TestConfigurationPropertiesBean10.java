package com.ctrip.framework.apollo.bean;

import com.ctrip.framework.apollo.spring.annotation.RefreshDisabled;
import com.ctrip.framework.apollo.spring.annotation.RefreshField;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("test")
@RefreshDisabled
public class TestConfigurationPropertiesBean10 {

    @RefreshField(true)
    private int timeout;

    private int batch;

    @RefreshField(true)
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
