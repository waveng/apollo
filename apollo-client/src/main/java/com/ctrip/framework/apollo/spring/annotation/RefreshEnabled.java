package com.ctrip.framework.apollo.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Used to enable automatic update of field values in spring and springboot.<br/><br />
 * 
 * Want the annotation play a role, "Apollo. AutoUpdateInjectedSpringProperties = true" must be set.<br/> <br />
 * 
 * Support springboot &#064;configurationproperties annotation of the class.<br/><br />
 * 
 * &#064;RefreshEnabled, &#064;RefreshDisabled has a higher priority than &#064;Autorefresh and &#064;RefreshField
 * 
 * @author wangbo(wangle_r@163.com)
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface RefreshEnabled {
  String[] namespaces() default  {};

  String[] interestedKeys() default {};

  String[] interestedKeyPrefixes() default {};
}
