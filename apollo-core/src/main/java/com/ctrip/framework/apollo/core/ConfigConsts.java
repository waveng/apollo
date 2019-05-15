package com.ctrip.framework.apollo.core;

public interface ConfigConsts {
  String NAMESPACE_APPLICATION = "application";
  String CLUSTER_NAME_DEFAULT = "default";
  String CLUSTER_NAMESPACE_SEPARATOR = "+";
  String APOLLO_CLUSTER_KEY = "apollo.cluster";
  String APOLLO_META_KEY = "apollo.meta";
  String CONFIG_FILE_CONTENT_KEY = "content";
  String NO_APPID_PLACEHOLDER = "ApolloNoAppIdPlaceHolder";
  long NOTIFICATION_ID_PLACEHOLDER = -1;
  String APOLLO_APP_ID = "apollo.app.id";
  /**
   * It is recommended to use {@link #APOLLO_APP_ID}
   */
  String APP_ID = "app.id";
  String APOLLO_IDC = "apollo.idc";
  
  String APOLLO_ENV = "apollo.env";
  /**
   * It is recommended to use {@link #APOLLO_ENV}
   */
  String ENV =  "env";
}
