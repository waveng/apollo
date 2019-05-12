package com.ctrip.framework.apollo.spring.boot;

import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;

public abstract class AbstractRefreshConfigurationProperties implements RefreshConfigurationProperties{
  /**
   * Update the property values of the object through the bean object
   * 
   * @param bean
   * @param changeEvent
   */
  @Override
  public void refreshBindingBean(Object bean, ConfigChangeEvent changeEvent) {
    String beanName = bean.getClass().getName();
    refreshBinding(bean, beanName, changeEventToProperties(changeEvent));
  }

  /**
   * Update the property value of the object associated with that beanName through beanName
   * 
   * @param beanName
   * @param changeEvent
   */
  @Override
  public void refreshBinding(String beanName, ConfigChangeEvent changeEvent) {
    Object bean = this.getApplicationContext().getBean(beanName);
    refreshBinding(bean, beanName, changeEventToProperties(changeEvent));
  }

  /**
   * @param bean
   * @param beanName
   * @param changeEvent
   */
  @Override
  public void refreshBinding(Object bean, String beanName, ConfigChangeEvent changeEvent) {
    refreshBinding(bean, beanName, changeEventToProperties(changeEvent));
  }
  
  private Properties changeEventToProperties(ConfigChangeEvent changeEvent) {
    Set<String> keys = changeEvent.changedKeys();
    Properties properties = new Properties();
    for (String key : keys) {
      properties.put(key, changeEvent.getChange(key).getNewValue());
    }
    return properties;
  }

  protected abstract ApplicationContext getApplicationContext();
}
