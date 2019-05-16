package com.ctrip.framework.apollo.bean;

import com.ctrip.framework.apollo.spring.annotation.AutoRefresh;
import com.ctrip.framework.apollo.spring.annotation.RefreshField;

import org.springframework.boot.context.properties.ConfigurationProperties;

@AutoRefresh(true)
@ConfigurationProperties("test")
public class TestConfigurationPropertiesBean6 {

    @RefreshField(false)
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
