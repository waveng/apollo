package com.ctrip.framework.apollo.spring.config;

import com.ctrip.framework.apollo.ClientConfigConsts;
import com.ctrip.framework.apollo.spring.annotation.AutoRefresh;

import org.springframework.boot.context.properties.ConfigurationProperties;

public interface PropertySourcesConstants {
  String APOLLO_PROPERTY_SOURCE_NAME = "ApolloPropertySources";
  String APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME = "ApolloBootstrapPropertySources";
  String APOLLO_BOOTSTRAP_ENABLED = "apollo.bootstrap.enabled";
  String APOLLO_BOOTSTRAP_EAGER_LOAD_ENABLED = "apollo.bootstrap.eagerLoad.enabled";
  String APOLLO_BOOTSTRAP_NAMESPACES = "apollo.bootstrap.namespaces";
  
  /**
   * Whether or not the springboot ConfigurationProperties automatic update is enabled. If not enabled by default, the configuration will only be valid for classes annotated with {@link ConfigurationProperties}.
   * Usage:
   * <ol>
   * <li>
   * Automatic update is enabled for classes under the specified package. When the configuration value is package path (packages), automatic refresh is only enabled for classes under the specified package annotated by ConfigurationProperties. This can be the fully qualified name of the class, 
   * </li>
   * <li>
   * Auto-refresh is enabled for all classes. When the configuration value is true, auto-refresh is enabled for all classes annotated with ConfigurationProperties
   * </li>
   * <li>
   * Disable automatic updates, and will not be disabled if not configured or configured to false
   * </li>
   * <li>
   * Individual class updates are specified through annotations. It is possible to enable automatic updates of individual classes through the {@link AutoRefresh} annotation, and even if the {@link ClientConfigConsts#APOLLO_AUTO_UPDATE_CONFIGURATION_PROPERTIES} is not enabled
   * </li>
   * <ol>
   */
  String APOLLO_AUTO_REFRESH_CONFIGURATION_PROPERTIE = "apollo.autoRefreshConfigurationProperties";
}
