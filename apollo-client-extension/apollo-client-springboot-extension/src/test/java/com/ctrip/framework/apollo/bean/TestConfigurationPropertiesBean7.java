package com.ctrip.framework.apollo.bean;

import com.ctrip.framework.apollo.spring.annotation.AutoRefresh;
import com.ctrip.framework.apollo.spring.annotation.RefreshField;

import org.springframework.boot.context.properties.ConfigurationProperties;

@AutoRefresh(false)
@ConfigurationProperties("test")
public class TestConfigurationPropertiesBean7 {

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