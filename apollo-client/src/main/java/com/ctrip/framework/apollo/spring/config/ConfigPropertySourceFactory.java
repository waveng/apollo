package com.ctrip.framework.apollo.spring.config;

import java.util.List;
import java.util.Map;

import com.ctrip.framework.apollo.Config;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConfigPropertySourceFactory {

  private final Map<String, ConfigPropertySource> configPropertySources = Maps.newLinkedHashMap();

  public ConfigPropertySource getConfigPropertySource(String name, Config source) {
    if(configPropertySources.containsKey(name)) {
      return configPropertySources.get(name);
    }
    ConfigPropertySource configPropertySource = new ConfigPropertySource(name, source);
    configPropertySources.put(name, configPropertySource);
    return configPropertySource;
  }

  public List<ConfigPropertySource> getAllConfigPropertySources() {
    return Lists.newLinkedList(configPropertySources.values());
  }
}
