package com.ctrip.framework.apollo;
/**
 * @author wangbo(wangle_r@163.com)
 */
public interface ClientConfigConsts {
  final String APOLLO_AUTO_UPDATE_INJECTED_SPRING_PROPERTIE = "apollo.autoUpdateInjectedSpringProperties";
  final String APOLLO_LONG_POLLING_INITIAL_DELAY_IN_MILLS = "apollo.longPollingInitialDelayInMills";
  final String APOLLO_CACHE_DIR = "apollo.cacheDir";
  final String APOLLO_CONFIG_CACHE_SIZE = "apollo.configCacheSize";
  final String APOLLO_CONNECT_TIMEOUT = "apollo.connectTimeout";
  final String APOLLO_LOAD_CONFIG_QPS = "apollo.loadConfigQPS";
  final String APOLLO_REFRESH_INTERVAL = "apollo.refreshInterval";
  final String APOLLO_READ_TIMEOUT = "apollo.readTimeout";
  final String APOLLO_LONG_POLL_QPS = "apollo.longPollQPS";
  final String APOLLO_CONFIG_SERVICE = "apollo.configService";
}
