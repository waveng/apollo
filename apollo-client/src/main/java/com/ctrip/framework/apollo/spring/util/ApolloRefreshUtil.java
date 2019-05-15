package com.ctrip.framework.apollo.spring.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.ctrip.framework.apollo.spring.annotation.AutoRefresh;
import com.ctrip.framework.apollo.spring.annotation.RefreshDisabled;
import com.ctrip.framework.apollo.spring.annotation.RefreshEnabled;
import com.ctrip.framework.apollo.spring.annotation.RefreshField;
import com.ctrip.framework.apollo.spring.boot.BeanFactoryMetadata;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author wangbo(wangle_r@163.com)
 */
public class ApolloRefreshUtil {
  
  public static ConfigurationProperties findConfigurationPropertiesAnnotation(Class<?> clazz, String beanName, BeanFactoryMetadata beans) {
    ConfigurationProperties annotation = AnnotationUtils.findAnnotation(clazz, ConfigurationProperties.class);
    if (annotation == null) {
      annotation = beans.findFactoryAnnotation(beanName, ConfigurationProperties.class);
    }
    return annotation;
  }

  public static AutoRefresh findAutoRefreshAnnotation(Class<?> clazz, String beanName, BeanFactoryMetadata beans) {
    AutoRefresh annotation = AnnotationUtils.findAnnotation(clazz, AutoRefresh.class);
    if (annotation == null) {
      annotation = beans.findFactoryAnnotation(beanName, AutoRefresh.class);
    }
    return annotation;
  }

  public static RefreshDisabled findRefreshDisabledAnnotation(Class<?> clazz, String beanName, BeanFactoryMetadata beans) {
    RefreshDisabled annotation = AnnotationUtils.findAnnotation(clazz, RefreshDisabled.class);
    if (annotation == null) {
      annotation = beans.findFactoryAnnotation(beanName, RefreshDisabled.class);
    }
    return annotation;
  }

  public static RefreshEnabled findRefreshEnabledAnnotation(Class<?> clazz, String beanName, BeanFactoryMetadata beans) {
    RefreshEnabled annotation = AnnotationUtils.findAnnotation(clazz, RefreshEnabled.class);
    if (annotation == null) {
      annotation = beans.findFactoryAnnotation(beanName, RefreshEnabled.class);
    }
    return annotation;
  }
  
  /**
   * Determine whether &#064;{@link RefreshEnabled}, &#064;{@link RefreshDisabled} exists, and enable or disable automatic refresh of records，
   * If it does not exist, it will refresh by default
   */
  public static boolean hasRefresh(Class<?> clazz) {
    
    /*
     * @RefreshEnabled, @Refreshdisabled has a higher priority than @Autorefresh and @RefreshField
     */
    RefreshEnabled refreshEnabled = AnnotationUtils.findAnnotation(clazz, RefreshEnabled.class);
    if(refreshEnabled != null) {
      return true;
    }
    
    
    RefreshDisabled refreshDisabled = AnnotationUtils.findAnnotation(clazz, RefreshDisabled.class);
    if(refreshDisabled != null) {
      return false;
    }
    
    //There is no @RefreshEnabled, @Refreshdisabled, @AutoRefresh,  @RefreshField, it will refresh by default 
    return true;
  }
  
  /**
   * Determine whether &#064;{@link RefreshEnabled}, &#064;{@link RefreshDisabled}, &#064;{@link RefreshField}, &#064;{@link AutoRefresh} exists, and enable or disable automatic refresh of records，
   * If it does not exist, it will refresh by default
   */
  public static boolean hasRefresh(Class<?> clazz, Method method) {
    
    /*
     * @RefreshEnabled, @Refreshdisabled has a higher priority than @Autorefresh and @RefreshField
     */
    RefreshEnabled refreshEnabled = AnnotationUtils.findAnnotation(clazz, RefreshEnabled.class);
    if(refreshEnabled != null) {
      return true;
    }
    
    
    RefreshDisabled refreshDisabled = AnnotationUtils.findAnnotation(clazz, RefreshDisabled.class);
    if(refreshDisabled != null) {
      return false;
    }
    
    /*
     * Both @autorefresh and @refreshfield exist. @refreshfield has a higher priority
     */
    RefreshField refreshField = method.getAnnotation(RefreshField.class);
    if(refreshField != null) {
      return refreshField.value();
    }
    
    /*
     * There is no @RefreshEnabled, @Refreshdisabled, @RefreshField, Determines whether @Autorefresh exists on the class
     */
    AutoRefresh refreshs = AnnotationUtils.findAnnotation(clazz, AutoRefresh.class);
    if(refreshs != null) {
      return refreshs.value();
    }
    
    //There is no @RefreshEnabled, @Refreshdisabled, @AutoRefresh,  @RefreshField, it will refresh by default
    return true;
  }
  
  
  /**
   * Determine whether &#064;{@link RefreshEnabled}, &#064;{@link RefreshDisabled}, &#064;{@link RefreshField}, &#064;{@link AutoRefresh} exists, and enable or disable automatic refresh of records，
   * If it does not exist, it will refresh by default
   */
  public static boolean hasRefresh(Class<?> clazz, Field field) {
    
    /*
     * @RefreshEnabled, @Refreshdisabled has a higher priority than @Autorefresh and @RefreshField
     */
    RefreshEnabled refreshEnabled = AnnotationUtils.findAnnotation(clazz, RefreshEnabled.class);
    if(refreshEnabled != null) {
      return true;
    }
    
    
    RefreshDisabled refreshDisabled = AnnotationUtils.findAnnotation(clazz, RefreshDisabled.class);
    if(refreshDisabled != null) {
      return false;
    }
    
    /*
     * Both @autorefresh and @refreshfield exist. @refreshfield has a higher priority
     */
    RefreshField refreshField = field.getAnnotation(RefreshField.class);
    if(refreshField != null) {
      return refreshField.value();
    }
    
    /*
     * There is no @RefreshEnabled, @Refreshdisabled, @RefreshField, Determines whether @Autorefresh exists on the class
     */
    AutoRefresh refreshs = AnnotationUtils.findAnnotation(clazz, AutoRefresh.class);
    if(refreshs != null) {
      return refreshs.value();
    }
    
    //There is no @RefreshEnabled, @Refreshdisabled, @AutoRefresh,  @RefreshField, it will refresh by default 
    return true;
  }
  
}
