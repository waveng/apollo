package com.ctrip.framework.apollo.spring.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.ctrip.framework.apollo.build.MockInjector;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.internals.SimpleConfig;
import com.ctrip.framework.apollo.internals.YamlConfigFile;
import com.ctrip.framework.apollo.spring.AbstractSpringIntegrationTest;
import com.ctrip.framework.apollo.spring.XmlConfigPlaceholderTest.TestXmlBean;
import com.ctrip.framework.apollo.util.ConfigUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component; 
@SuppressWarnings("resource")
public class JavaConfigPlaceholderAutoUpdateTest extends AbstractSpringIntegrationTest {

  private static final String TIMEOUT_PROPERTY = "timeout";
  private static final int DEFAULT_TIMEOUT = 100;
  private static final String BATCH_PROPERTY = "batch";
  private static final int DEFAULT_BATCH = 200;
  private static final String FX_APOLLO_NAMESPACE = "FX.apollo";
  private static final String SOME_KEY_PROPERTY = "someKey";
  private static final String ANOTHER_KEY_PROPERTY = "anotherKey";

  
@Test
  public void testRefreshEnabledWithOneNamespace() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
        String.valueOf(initialBatch));

    SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    Properties newProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(newBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithOneYamlFile() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    YamlConfigFile configFile = prepareYamlConfigFile("application.yaml",
        readYamlContentAsConfigFileProperties("case1.yaml"));

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig12.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    configFile.onRepositoryChange("application.yaml", readYamlContentAsConfigFileProperties("case1-new.yaml"));

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(newBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithValueAndXmlProperty() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
        String.valueOf(initialBatch));

    SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig8.class);

    RefreshEnabledBean javaConfigBean = context.getBean(RefreshEnabledBean.class);
    TestXmlBean xmlBean = context.getBean(TestXmlBean.class);

    assertEquals(initialTimeout, javaConfigBean.getTimeout());
    assertEquals(initialBatch, javaConfigBean.getBatch());
    assertEquals(initialTimeout, xmlBean.getTimeout());
    assertEquals(initialBatch, xmlBean.getBatch());

    Properties newProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, javaConfigBean.getTimeout());
    assertEquals(newBatch, javaConfigBean.getBatch());
    assertEquals(newTimeout, xmlBean.getTimeout());
    assertEquals(newBatch, xmlBean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithYamlFileWithValueAndXmlProperty() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    YamlConfigFile configFile = prepareYamlConfigFile("application.yaml",
        readYamlContentAsConfigFileProperties("case1.yaml"));

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig13.class);

    RefreshEnabledBean javaConfigBean = context.getBean(RefreshEnabledBean.class);
    TestXmlBean xmlBean = context.getBean(TestXmlBean.class);

    assertEquals(initialTimeout, javaConfigBean.getTimeout());
    assertEquals(initialBatch, javaConfigBean.getBatch());
    assertEquals(initialTimeout, xmlBean.getTimeout());
    assertEquals(initialBatch, xmlBean.getBatch());

    configFile.onRepositoryChange("application.yaml", readYamlContentAsConfigFileProperties("case1-new.yaml"));

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, javaConfigBean.getTimeout());
    assertEquals(newBatch, javaConfigBean.getBatch());
    assertEquals(newTimeout, xmlBean.getTimeout());
    assertEquals(newBatch, xmlBean.getBatch());
  }

  @Test
  public void testRefreshEnabledDisabled() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    MockConfigUtil mockConfigUtil = new MockConfigUtil();
    mockConfigUtil.setAutoUpdateInjectedSpringProperties(false);

    MockInjector.setInstance(ConfigUtil.class, mockConfigUtil);

    Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
        String.valueOf(initialBatch));

    SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    Properties newProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithMultipleNamespaces() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    Properties applicationProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout));
    Properties fxApolloProperties = assembleProperties(BATCH_PROPERTY, String.valueOf(initialBatch));

    SimpleConfig applicationConfig = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, applicationProperties);
    SimpleConfig fxApolloConfig = prepareConfig(FX_APOLLO_NAMESPACE, fxApolloProperties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig2.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    Properties newApplicationProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout));

    applicationConfig.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newApplicationProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    Properties newFxApolloProperties = assembleProperties(BATCH_PROPERTY, String.valueOf(newBatch));

    fxApolloConfig.onRepositoryChange(FX_APOLLO_NAMESPACE, newFxApolloProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(newBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithMultipleNamespacesWithSameProperties() throws Exception {
    int someTimeout = 1000;
    int someBatch = 2000;
    int anotherBatch = 3000;
    int someNewTimeout = 1001;
    int someNewBatch = 2001;

    Properties applicationProperties = assembleProperties(BATCH_PROPERTY, String.valueOf(someBatch));
    Properties fxApolloProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(someTimeout), BATCH_PROPERTY, String.valueOf(anotherBatch));

    prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, applicationProperties);
    SimpleConfig fxApolloConfig = prepareConfig(FX_APOLLO_NAMESPACE, fxApolloProperties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig2.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(someTimeout, bean.getTimeout());
    assertEquals(someBatch, bean.getBatch());

    Properties newFxApolloProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(someNewTimeout),
        BATCH_PROPERTY, String.valueOf(someNewBatch));

    fxApolloConfig.onRepositoryChange(FX_APOLLO_NAMESPACE, newFxApolloProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(someNewTimeout, bean.getTimeout());
    assertEquals(someBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithMultipleNamespacesWithSamePropertiesWithYamlFile() throws Exception {
    int someTimeout = 1000;
    int someBatch = 2000;
    int anotherBatch = 3000;
    int someNewBatch = 2001;

    YamlConfigFile configFile = prepareYamlConfigFile("application.yml",
        readYamlContentAsConfigFileProperties("case2.yml"));
    Properties fxApolloProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(someTimeout), BATCH_PROPERTY, String.valueOf(anotherBatch));

    prepareConfig(FX_APOLLO_NAMESPACE, fxApolloProperties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig14.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(someTimeout, bean.getTimeout());
    assertEquals(someBatch, bean.getBatch());

    configFile.onRepositoryChange("application.yml", readYamlContentAsConfigFileProperties("case2-new.yml"));

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(someTimeout, bean.getTimeout());
    assertEquals(someNewBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithNewProperties() throws Exception {
    int initialTimeout = 1000;
    int newTimeout = 1001;
    int newBatch = 2001;

    Properties applicationProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout));

    SimpleConfig applicationConfig = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, applicationProperties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(DEFAULT_BATCH, bean.getBatch());

    Properties newApplicationProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    applicationConfig.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newApplicationProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(newBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithNewPropertiesWithYamlFile() throws Exception {
    int initialTimeout = 1000;
    int newTimeout = 1001;
    int newBatch = 2001;

    YamlConfigFile configFile = prepareYamlConfigFile("application.yaml",
        readYamlContentAsConfigFileProperties("case3.yaml"));

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig12.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(DEFAULT_BATCH, bean.getBatch());

    configFile.onRepositoryChange("application.yaml", readYamlContentAsConfigFileProperties("case3-new.yaml"));

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(newBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithIrrelevantProperties() throws Exception {
    int initialTimeout = 1000;

    String someIrrelevantKey = "someIrrelevantKey";
    String someIrrelevantValue = "someIrrelevantValue";

    String anotherIrrelevantKey = "anotherIrrelevantKey";
    String anotherIrrelevantValue = "anotherIrrelevantValue";

    Properties applicationProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), someIrrelevantKey, someIrrelevantValue);

    SimpleConfig applicationConfig = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, applicationProperties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(DEFAULT_BATCH, bean.getBatch());

    Properties newApplicationProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout),
        anotherIrrelevantKey, String.valueOf(anotherIrrelevantValue));

    applicationConfig.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newApplicationProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(DEFAULT_BATCH, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithDeletedProperties() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;

    Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
        String.valueOf(initialBatch));

    SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    Properties newProperties = new Properties();

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(DEFAULT_TIMEOUT, bean.getTimeout());
    assertEquals(DEFAULT_BATCH, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithDeletedPropertiesWithYamlFile() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;

    YamlConfigFile configFile = prepareYamlConfigFile("application.yaml",
        readYamlContentAsConfigFileProperties("case4.yaml"));

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig12.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    configFile.onRepositoryChange("application.yaml", readYamlContentAsConfigFileProperties("case4-new.yaml"));

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(DEFAULT_TIMEOUT, bean.getTimeout());
    assertEquals(DEFAULT_BATCH, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithMultipleNamespacesWithSamePropertiesDeleted() throws Exception {
    int someTimeout = 1000;
    int someBatch = 2000;
    int anotherBatch = 3000;

    Properties applicationProperties = assembleProperties(BATCH_PROPERTY, String.valueOf(someBatch));
    Properties fxApolloProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(someTimeout), BATCH_PROPERTY, String.valueOf(anotherBatch));

    SimpleConfig applicationConfig = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, applicationProperties);
    prepareConfig(FX_APOLLO_NAMESPACE, fxApolloProperties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig2.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(someTimeout, bean.getTimeout());
    assertEquals(someBatch, bean.getBatch());

    Properties newProperties = new Properties();

    applicationConfig.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(someTimeout, bean.getTimeout());
    assertEquals(anotherBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithTypeMismatch() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    String newBatch = "newBatch";

    Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
        String.valueOf(initialBatch));

    SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    Properties newProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, newBatch);

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(300);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());
  }

  @Test
  public void testRefreshEnabledWithTypeMismatchWithYamlFile() throws Exception {
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;

    YamlConfigFile configFile = prepareYamlConfigFile("application.yaml",
        readYamlContentAsConfigFileProperties("case5.yaml"));

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig12.class);

    RefreshEnabledBean bean = context.getBean(RefreshEnabledBean.class);

    assertEquals(initialTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());

    configFile.onRepositoryChange("application.yaml", readYamlContentAsConfigFileProperties("case5-new.yaml"));

    TimeUnit.MILLISECONDS.sleep(300);

    assertEquals(newTimeout, bean.getTimeout());
    assertEquals(initialBatch, bean.getBatch());
  }

  
  @Test
  public void testRefreshDisabled() throws Exception {
    
    int initialTimeout = 1000;
    int initialBatch = 2000;
    int newTimeout = 1001;
    int newBatch = 2001;

    Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
        String.valueOf(initialBatch));

    SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RefreshBean1.class);

    assertTrue( context.containsBean("refreshDisabledBean"));
    assertTrue( context.containsBean("refreshEnabledBean"));
    
    RefreshEnabledBean bean1 = context.getBean(RefreshEnabledBean.class);
    RefreshDisabledBean bean2 = context.getBean(RefreshDisabledBean.class);

    assertEquals(initialTimeout, bean1.getTimeout());
    assertEquals(initialBatch, bean1.getBatch());
    assertEquals(initialTimeout, bean2.getTimeout());
    assertEquals(initialBatch, bean2.getBatch());

    Properties newProperties =
        assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(100);

    assertEquals(newTimeout, bean1.getTimeout());
    assertEquals(newBatch, bean1.getBatch());
    
    assertEquals(initialTimeout, bean2.getTimeout());
    assertEquals(initialBatch, bean2.getBatch());
  }

  @Test
  public void testRefreshFieldWithDeletedPropertiesWithNoDefaultValue() throws Exception {
      int initialTimeout = 1000;
      int initialBatch = 2000;
      int newTimeout = 1001;
      int newBatch = 2001;

      Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
          String.valueOf(initialBatch));

      SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4.class);

    TestJavaConfigBean4 bean4 = context.getBean(TestJavaConfigBean4.class);
    TestJavaConfigBean5 bean5 = context.getBean(TestJavaConfigBean5.class);

    assertEquals(initialTimeout, bean4.getTimeout());
    assertEquals(initialBatch, bean4.getBatch());
    assertEquals(initialTimeout, bean5.getTimeout());
    assertEquals(initialBatch, bean5.getBatch());

    Properties newProperties =
            assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(600);

    assertEquals(initialTimeout, bean4.getTimeout());
    assertEquals(newBatch, bean4.getBatch());
    assertEquals(initialTimeout, bean5.getTimeout());
    assertEquals(newBatch, bean5.getBatch());
  }

  
  @Test
  public void testAutoRefreshDisabledWithDeletedPropertiesWithNoDefaultValue() throws Exception {
      int initialTimeout = 1000;
      int initialBatch = 2000;
      int newTimeout = 1001;
      int newBatch = 2001;

      Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
          String.valueOf(initialBatch));

      SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4.class);

    TestJavaConfigBean6 bean6 = context.getBean(TestJavaConfigBean6.class);
    TestJavaConfigBean7 bean7 = context.getBean(TestJavaConfigBean7.class);
    TestJavaConfigBean8 bean8 = context.getBean(TestJavaConfigBean8.class);

    assertEquals(initialTimeout, bean6.getTimeout());
    assertEquals(initialBatch, bean6.getBatch());
    assertEquals(initialTimeout, bean7.getTimeout());
    assertEquals(initialBatch, bean7.getBatch());
    assertEquals(initialTimeout, bean8.getTimeout());
    assertEquals(initialBatch, bean8.getBatch());

    Properties newProperties =
            assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch));

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(600);

    assertEquals(initialTimeout, bean6.getTimeout());
    assertEquals(newBatch, bean6.getBatch());
    
    assertEquals(initialTimeout, bean7.getTimeout());
    assertEquals(newBatch, bean7.getBatch());
    
    assertEquals(initialTimeout, bean8.getTimeout());
    assertEquals(initialBatch, bean8.getBatch());
  }
  
  @Test
  public void testAutoRefresEnabledhWithDeletedPropertiesWithNoDefaultValue() throws Exception {
      int initialTimeout = 1000;
      int initialBatch = 2000;
      int newTimeout = 1001;
      int newBatch = 2001;
      String someValidValue = "[{\"a\":\"someString\", \"b\":10}]";
      
      String newsomeValidValue = "[{\"a\":\"newsomeString\", \"b\":1000}]";
      
      Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
          String.valueOf(initialBatch), "jsonProperty", someValidValue);

      SimpleConfig config = prepareConfig(ConfigConsts.NAMESPACE_APPLICATION, properties);

      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4.class);

    TestJavaConfigBean9 bean9 = context.getBean(TestJavaConfigBean9.class);
    TestJavaConfigBean10 bean10 = context.getBean(TestJavaConfigBean10.class);

    assertEquals(initialTimeout, bean9.getTimeout());
    assertEquals(initialBatch, bean9.getBatch());
    assertEquals("someString", bean9.getJsonBeanList().get(0).getA());
    assertEquals(10, bean9.getJsonBeanList().get(0).getB());
    
    assertEquals(initialTimeout, bean10.getTimeout());
    assertEquals(initialBatch, bean10.getBatch());
    assertEquals("someString", bean10.getJsonBeanList().get(0).getA());
    assertEquals(10, bean10.getJsonBeanList().get(0).getB());
    
    

    Properties newProperties =
            assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY, String.valueOf(newBatch), "jsonProperty", newsomeValidValue);

    config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

    TimeUnit.MILLISECONDS.sleep(600);

    assertEquals(initialTimeout, bean9.getTimeout());
    assertEquals(newBatch, bean9.getBatch());
    assertEquals("newsomeString", bean9.getJsonBeanList().get(0).getA());
    assertEquals(1000, bean9.getJsonBeanList().get(0).getB());
    
    assertEquals(initialTimeout, bean10.getTimeout());
    assertEquals(newBatch, bean10.getBatch());
    assertEquals("someString", bean10.getJsonBeanList().get(0).getA());
    assertEquals(10, bean10.getJsonBeanList().get(0).getB());
    
  }
  

  @Configuration
  @EnableApolloConfig
  static class AppConfig1 {
    @Bean
    RefreshEnabledBean testJavaConfigBean() {
      return new RefreshEnabledBean();
    }
  }

  @Configuration
  @EnableApolloConfig({"application", "FX.apollo"})
  static class AppConfig2 {
    @Bean
    RefreshEnabledBean testJavaConfigBean() {
      return new RefreshEnabledBean();
    }
  }

  @Configuration
  @ComponentScan(includeFilters = {@Filter(type = FilterType.ANNOTATION, value = {Component.class})},
      excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = {Configuration.class})})
  @EnableApolloConfig
  static class AppConfig4 {
  }

  @Configuration
  @EnableApolloConfig
  static class AppConfig5 {
    @Bean
    TestJavaConfigBean4 testJavaConfigBean() {
      return new TestJavaConfigBean4();
    }
  }

  @Configuration
  @EnableApolloConfig
  static class AppConfig6 {
    @Bean
    TestJavaConfigBean5 testJavaConfigBean() {
      return new TestJavaConfigBean5();
    }
  }

  @Configuration
  @EnableApolloConfig
  @ImportResource("spring/XmlConfigPlaceholderTest1.xml")
  static class AppConfig8 {
    @Bean
    RefreshEnabledBean testJavaConfigBean() {
      return new RefreshEnabledBean();
    }
  }


  @Configuration
  @EnableApolloConfig("application.yaMl")
  static class AppConfig12 {
      
    @Bean
    RefreshEnabledBean testJavaConfigBean() {
      return new RefreshEnabledBean();
    }
  }

  @Configuration
  @EnableApolloConfig("application.yaml")
  @ImportResource("spring/XmlConfigPlaceholderTest11.xml")
  static class AppConfig13 {
    @Bean
    RefreshEnabledBean testJavaConfigBean() {
      return new RefreshEnabledBean();
    }
  }

  @Configuration
  @EnableApolloConfig({"application.yml", "FX.apollo"})
  static class AppConfig14 {
    @Bean
    RefreshEnabledBean testJavaConfigBean() {
      return new RefreshEnabledBean();
    }
  }

  @RefreshEnabled
  static class RefreshEnabledBean {

    @Value("${timeout:100}")
    private int timeout;
    private int batch;

    @Value("${batch:200}")
    public void setBatch(int batch) {
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }
  
  @Configuration
  @EnableApolloConfig
  static class RefreshBean1 {
    @Bean
    RefreshEnabledBean refreshEnabledBean() {
      return new RefreshEnabledBean();
    }
    
    @Bean
    RefreshDisabledBean refreshDisabledBean() {
      return new RefreshDisabledBean();
    }
  }
  
  @RefreshDisabled
  static class RefreshDisabledBean {

    @Value("${timeout:100}")
    private int timeout;
    private int batch;

    @Value("${batch:200}")
    public void setBatch(int batch) {
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }
  
  /**
   * This case won't get auto updated
   */
  @Component
  static class TestJavaConfigBean1 {
    private final int timeout;
    private final int batch;

    @Autowired
    public TestJavaConfigBean1(@RefreshField(AutoRefresh.disabled) @Value("${timeout:100}") int timeout, @Value("${batch:200}") int batch) {
      this.timeout = timeout;
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }

  /**
   * This case won't get auto updated
   */
  @Component
  static class TestJavaConfigBean2 {
    private final int timeout;
    private final int batch;

    @Autowired
    public TestJavaConfigBean2(@RefreshField(AutoRefresh.disabled) @Value("${timeout:100}") int timeout, @RefreshField(AutoRefresh.enabled) @Value("${batch:200}") int batch) {
      this.timeout = timeout;
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }

  /**
   * This case won't get auto updated
   */
  @Component
  @AutoRefresh(AutoRefresh.enabled)
  static class TestJavaConfigBean3 {
    private final int timeout;
    private final int batch;

    @Autowired
    public TestJavaConfigBean3(@RefreshField(AutoRefresh.disabled) @Value("${timeout:100}") int timeout, @RefreshField(AutoRefresh.enabled) @Value("${batch:200}") int batch) {
      this.timeout = timeout;
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }

  /**
   * This case won't get auto updated
   */
  @Component
  static class TestJavaConfigBean4 {

      @RefreshField(AutoRefresh.disabled)
      @Value("${timeout}")
    private int timeout;
    private int batch;

    @Value("${batch}")
    public void setBatch(int batch) {
      this.batch = batch;
    }
    
    public int getTimeout() {
      return timeout;
    }
    
    public int getBatch() {
      return batch;
    }
  }
  @Component
  static class TestJavaConfigBean5 {

      @RefreshField(AutoRefresh.disabled)
    @Value("${timeout}")
    private int timeout;
      
    private int batch;

    @RefreshField(AutoRefresh.enabled)
    @Value("${batch}")
    public void setBatch(int batch) {
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }
  @Component
  @AutoRefresh(AutoRefresh.disabled)
  static class TestJavaConfigBean6 {

      @RefreshField(AutoRefresh.disabled)
    @Value("${timeout}")
    private int timeout;
      
    private int batch;

    @RefreshField(AutoRefresh.enabled)
    @Value("${batch}")
    public void setBatch(int batch) {
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }
  @Component
  @AutoRefresh(AutoRefresh.disabled)
  static class TestJavaConfigBean7 {

    @Value("${timeout}")
    private int timeout;
      
    private int batch;

    @RefreshField(AutoRefresh.enabled)
    @Value("${batch}")
    public void setBatch(int batch) {
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }
  @Component
  @AutoRefresh(AutoRefresh.disabled)
  static class TestJavaConfigBean8 {

    @Value("${timeout}")
    private int timeout;
    
    private int batch;

    @Value("${batch}")
    public void setBatch(int batch) {
      this.batch = batch;
    }

    public int getTimeout() {
      return timeout;
    }

    public int getBatch() {
      return batch;
    }
  }
  @Component
  @AutoRefresh(AutoRefresh.enabled)
  static class TestJavaConfigBean9 {

      @RefreshField(AutoRefresh.disabled)
    @Value("${timeout}")
    private int timeout;
    
    private int batch;

    @ApolloJsonValue("${jsonProperty:}")
    private List<JsonBean> jsonBeanList;
    
    @Value("${batch}")
    public void setBatch(int batch) {
        this.batch = batch;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public int getBatch() {
        return batch;
    }

      public List<JsonBean> getJsonBeanList() {
          return jsonBeanList;
      }
  }
  
  @Component
  @AutoRefresh(AutoRefresh.enabled)
  static class TestJavaConfigBean10 {
      
      @RefreshField(AutoRefresh.disabled)
      @Value("${timeout}")
      private int timeout;
      
      private int batch;
      
      @RefreshField(AutoRefresh.disabled)
      @ApolloJsonValue("${jsonProperty:}")
      private List<JsonBean> jsonBeanList;
      
      @Value("${batch}")
      public void setBatch(int batch) {
          this.batch = batch;
      }
      
      public int getTimeout() {
          return timeout;
      }
      
      public int getBatch() {
          return batch;
      }

    public List<JsonBean> getJsonBeanList() {
        return jsonBeanList;
    }

      
  }
  
 

  static class JsonBean {

      private String a;
      private int b;

      String getA() {
        return a;
      }

      public void setA(String a) {
        this.a = a;
      }

      int getB() {
        return b;
      }

      public void setB(int b) {
        this.b = b;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        JsonBean jsonBean = (JsonBean) o;

        if (b != jsonBean.b) {
          return false;
        }
        return a != null ? a.equals(jsonBean.a) : jsonBean.a == null;
      }

      @Override
      public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + b;
        return result;
      }
    }
}
