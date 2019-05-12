package com.ctrip.framework.apollo.demo.spring.springBootDemo.autorefreshannotation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.ctrip.framework.apollo.spring.annotation.RefreshEnabled;
import com.ctrip.framework.apollo.spring.annotation.RefreshField;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Component
@RefreshEnabled
public class RefreshEnabledBean {
  private static final Logger logger = LoggerFactory.getLogger(RefreshEnabledBean.class);

  private int timeout;
  private int batch;
  private List<JsonBean> jsonBeans;

  /**
   * ApolloJsonValue annotated on fields example, the default value is specified as empty list - []
   * <br />
   * jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
   */
  @ApolloJsonValue("${jsonBeanProperty:[]}")
  private List<JsonBean> anotherJsonBeans;

  /**
   * 不应该起作用
   * @param batch
   */
  @Value("${batch:100}")
  @RefreshField(RefreshField.disabled)
  public void setBatch(int batch) {
    logger.info("updating batch, old value: {}, new value: {}", this.batch, batch);
    this.batch = batch;
  }

  @Value("${timeout:200}")
  public void setTimeout(int timeout) {
    logger.info("updating timeout, old value: {}, new value: {}", this.timeout, timeout);
    this.timeout = timeout;
  }

  /**
   * ApolloJsonValue annotated on methods example, the default value is specified as empty list - []
   * <br />
   * jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
   */
  @ApolloJsonValue("${jsonBeanProperty:[]}")
  public void setJsonBeans(List<JsonBean> jsonBeans) {
    logger.info("updating json beans, old value: {}, new value: {}", this.jsonBeans, jsonBeans);
    this.jsonBeans = jsonBeans;
  }

  @Override
  public String toString() {
    return String.format("[RefreshEnabledBean] timeout: %d, batch: %d, jsonBeans: %s", timeout, batch, jsonBeans);
  }

  private static class JsonBean{

    private String someString;
    private int someInt;

    @Override
    public String toString() {
      return "JsonBean{" +
          "someString='" + someString + '\'' +
          ", someInt=" + someInt +
          '}';
    }
  }
}
