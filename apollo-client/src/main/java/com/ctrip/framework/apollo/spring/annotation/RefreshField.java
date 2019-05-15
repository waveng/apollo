package com.ctrip.framework.apollo.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to enable or disable automatic update of field values in spring and springboot, This can be used on fields, and methods.<br/><br />
 * 
 * Want the annotation play a role, "Apollo. AutoUpdateInjectedSpringProperties = true" must be set.<br/> <br />
 * 
 * Support springboot &#064;Configurationproperties annotation of the class. But it can only be used on classes and set methods.<br/><br />
 * 
 * Default enabled automatic update 
 * 
 * @author wangbo(wangle_r@163.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface RefreshField {
  public static final boolean enabled = true;
  public static final boolean disabled = false;
  /**
   * enabled / disabled auto refresh, default enabled
   */
  boolean value() default enabled;
}
