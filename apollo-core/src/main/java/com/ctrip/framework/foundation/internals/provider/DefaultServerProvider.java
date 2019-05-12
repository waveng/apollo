package com.ctrip.framework.foundation.internals.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.ctrip.framework.foundation.internals.Utils;
import com.ctrip.framework.foundation.internals.io.BOMInputStream;
import com.ctrip.framework.foundation.spi.provider.Provider;
import com.ctrip.framework.foundation.spi.provider.ServerProvider;

public class DefaultServerProvider implements ServerProvider {
  private static final String[] IDCS = {ConfigConsts.APOLLO_IDC, "idc"};
  private static final String[] ENVS = {ConfigConsts.APOLLO_ENV, ConfigConsts.ENV};
  
  private static final Logger logger = LoggerFactory.getLogger(DefaultServerProvider.class);
  private static final String SERVER_PROPERTIES_LINUX = "/opt/settings/server.properties";
  private static final String SERVER_PROPERTIES_WINDOWS = "C:/opt/settings/server.properties";

  private String m_env;
  private String m_dc;

  private Properties m_serverProperties = new Properties();

  @Override
  public void initialize() {
    try {
      String path = Utils.isOSWindows() ? SERVER_PROPERTIES_WINDOWS : SERVER_PROPERTIES_LINUX;

      File file = new File(path);
      if (file.exists() && file.canRead()) {
        logger.info("Loading {}", file.getAbsolutePath());
        FileInputStream fis = new FileInputStream(file);
        initialize(fis);
        return;
      }

      initialize(null);
    } catch (Throwable ex) {
      logger.error("Initialize DefaultServerProvider failed.", ex);
    }
  }

  @Override
  public void initialize(InputStream in) {
    try {
      if (in != null) {
        try {
          m_serverProperties.load(new InputStreamReader(new BOMInputStream(in), StandardCharsets.UTF_8));
        } finally {
          in.close();
        }
      }

      initEnvType();
      initDataCenter();
    } catch (Throwable ex) {
      logger.error("Initialize DefaultServerProvider failed.", ex);
    }
  }

  @Override
  public String getDataCenter() {
    return m_dc;
  }

  @Override
  public boolean isDataCenterSet() {
    return m_dc != null;
  }

  @Override
  public String getEnvType() {
    return m_env;
  }

  @Override
  public boolean isEnvTypeSet() {
    return m_env != null;
  }

  @Override
  public String getProperty(String name, String defaultValue) {
    if (ConfigConsts.APOLLO_ENV.equals(name) || ConfigConsts.ENV.equalsIgnoreCase(name)) {
      String val = getEnvType();
      return val == null ? defaultValue : val;
    } else if (ConfigConsts.APOLLO_IDC.equals(name) ||  "dc".equalsIgnoreCase(name)) {
      String val = getDataCenter();
      return val == null ? defaultValue : val;
    } else {
      String val = m_serverProperties.getProperty(name, defaultValue);
      return val == null ? defaultValue : val.trim();
    }
  }

  @Override
  public Class<? extends Provider> getType() {
    return ServerProvider.class;
  }

  private void initEnvType() {
    /**
     * support ‘apollo.env’ 
     */
    for (String env : ENVS) {
      if(initEnvType(env)) {
        return;
      }
    }
    
    // 4. Set environment to null.
    m_env = null;
    logger.info("Environment is set to null. Because it is not available in either (1) JVM system property 'env', (2) OS env variable 'ENV' nor (3) property 'env' from the properties InputStream.");
  }

  private boolean initEnvType(String env) {
    // 1. Try to get environment from JVM system property
    m_env = System.getProperty(env);
    if (!Utils.isBlank(m_env)) {
      m_env = m_env.trim();
      logger.info("Environment is set to [{}] by JVM system property 'env'.", m_env);
      return true;
    }
 
    // 2. Try to get environment from OS environment variable
    m_env = System.getenv(StringUtils.normalizeSyctemEnv(env));
    if (!Utils.isBlank(m_env)) {
      m_env = m_env.trim();
      logger.info("Environment is set to [{}] by OS env variable 'ENV'.", m_env);
      return true;
    }
 
    // 3. Try to get environment from file "server.properties"
    m_env = m_serverProperties.getProperty(env);
    if (!Utils.isBlank(m_env)) {
      m_env = m_env.trim();
      logger.info("Environment is set to [{}] by property 'env' in server.properties.", m_env);
      return true;
    }
    return false;
  }
  
  private void initDataCenter() {
    /**
     * support ‘apollo.idc’ 
     */
    for (String idc : IDCS) {
      if(initDataCenter(idc)) {
        return;
      }
    }
    // 4. Set Data Center to null.
    m_dc = null;
    logger.debug("Data Center is set to null. Because it is not available in either (1) JVM system property 'idc', (2) OS env variable 'IDC' nor (3) property 'idc' from the properties InputStream.");
  }
  private boolean initDataCenter(String idc) {
    // 1. Try to get environment from JVM system property
    m_dc = System.getProperty(idc);
    if (!Utils.isBlank(m_dc)) {
      m_dc = m_dc.trim();
      logger.info("Data Center is set to [{}] by JVM system property 'idc'.", m_dc);
      return true;
    }

    // 2. Try to get idc from OS environment variable
    m_dc = System.getenv(StringUtils.normalizeSyctemEnv(idc));
    if (!Utils.isBlank(m_dc)) {
      m_dc = m_dc.trim();
      logger.info("Data Center is set to [{}] by OS env variable 'IDC'.", m_dc);
      return true;
    }

    // 3. Try to get idc from from file "server.properties"
    m_dc = m_serverProperties.getProperty(idc);
    if (!Utils.isBlank(m_dc)) {
      m_dc = m_dc.trim();
      logger.info("Data Center is set to [{}] by property 'idc' in server.properties.", m_dc);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "environment [" + getEnvType() + "] data center [" + getDataCenter() + "] properties: " + m_serverProperties
        + " (DefaultServerProvider)";
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
