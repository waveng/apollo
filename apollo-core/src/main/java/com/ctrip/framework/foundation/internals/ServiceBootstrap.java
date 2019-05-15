package com.ctrip.framework.foundation.internals;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.ctrip.framework.apollo.core.spi.Ordered;
import com.google.common.collect.Lists;

public class ServiceBootstrap {
  /**
   * the smaller order has higher priority
   */
  public static final Comparator<Ordered> ASC =  new Comparator<Ordered>() {
    @Override
    public int compare(Ordered o1, Ordered o2) {
      return Integer.compare(o1.getOrder(), o2.getOrder());
    }
  };
  /**
   * Larger orders have higher priority
   */
  public static final Comparator<Ordered> DESC =  new Comparator<Ordered>() {
    @Override
    public int compare(Ordered o1, Ordered o2) {
      return Integer.compare(o2.getOrder(), o1.getOrder());
    }
  };
  
  public static <S> S loadFirst(Class<S> clazz) {
    Iterator<S> iterator = loadAll(clazz);
    if (!iterator.hasNext()) {
      throw new IllegalStateException(String.format(
          "No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
          clazz.getName()));
    }
    return iterator.next();
  }
  
  public static <S> Iterator<S> loadAll(Class<S> clazz) {
    ServiceLoader<S> loader = ServiceLoader.load(clazz);
    return loader.iterator();
  }
  
  /**
   * Sort the results
   * @param <S>
   * @param clazz
   * @param comparator
   * @return
   */
  public static <S extends Ordered> List<S> loadAll(Class<S> clazz, Comparator<Ordered> comparator) {
    Iterator<S> iterator = loadAll(clazz);
    if (!iterator.hasNext()) {
      throw new IllegalStateException(String.format(
          "No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
          clazz.getName()));
    }
    List<S> loaders = Lists.newArrayList(iterator);
    if(loaders.size() > 1) {
      Collections.sort(loaders, comparator);
    }
    return loaders;
  }

  /**
   * Sort the results first， returns the first of the sorted results
   * @param <S>
   * @param clazz
   * @param comparator
   * @return
   */
  public static <S extends Ordered> S loadFirst(Class<S> clazz, Comparator<Ordered> comparator) {
    List<S> loaders = loadAll(clazz, comparator);
    return loaders.get(0);
  }
  
}
