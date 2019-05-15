package com.ctrip.framework.apollo.spring.boot;

import java.lang.annotation.Annotation;
/**
 * @author wangbo(wangle_r@163.com)
 */
public interface BeanFactoryMetadata {
  <A extends Annotation> A findFactoryAnnotation(String beanName, Class<A> type);
}
