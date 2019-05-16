package com.ctrip.framework.apollo.spring.boot;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean1;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean10;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean11;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean2;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean3;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean4;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean5;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean6;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean7;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean8;
import com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean9;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.internals.ConfigRepository;
import com.ctrip.framework.apollo.internals.SimpleConfig;
import com.ctrip.framework.apollo.spring.AbstractSpringIntegrationTest;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@SuppressWarnings("unused")
@RunWith(Enclosed.class)
public class ConfigurationPropertiesAutoUpdateTest extends AbstractSpringIntegrationTest {

    private static final String TIMEOUT_PROPERTY = "test.timeout";
    private static final String BATCH_PROPERTY   = "test.batch";

    static int                  initialTimeout   = 1000;
    static int                  initialBatch     = 2000;

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesApplication.class)
    @DirtiesContext
    public static class testConfigurationProperties {
        private static SimpleConfig mockedConfig;

        @ApolloConfig
        private SimpleConfig        config;

        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));

            ConfigRepository configRepository = mock(ConfigRepository.class);

            when(configRepository.getConfig()).thenReturn(properties);

            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);

            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);

            mockedConfig = config;
        }

        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        
        @Autowired(required = false)
        private ApplicationContext              context;

        @Autowired(required = false)
        private TestConfigurationPropertiesBean bean;

        @Test
        public void testDisabledRefresh() throws Exception {

            int newTimeout = 1001;
            int newBatch = 2001;

            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());

            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));

            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

            TimeUnit.MILLISECONDS.sleep(100);

            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());

        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationProperties1Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesRefreshDisabled {
        private static SimpleConfig mockedConfig;
    
        @ApolloConfig
        private SimpleConfig        config;
    
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
    
            ConfigRepository configRepository = mock(ConfigRepository.class);
    
            when(configRepository.getConfig()).thenReturn(properties);
    
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
    
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
    
            mockedConfig = config;
        }
    
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
    
        @Autowired(required = false)
        private TestConfigurationPropertiesBean1 bean;
    
        @Test
        public void testRefreshDisabled() throws Exception {
    
            int newTimeout = 1001;
            int newBatch = 2001;
    
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
    
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
    
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
    
            TimeUnit.MILLISECONDS.sleep(100);
    
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
    
        }
    }
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesBean2Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesRefreshEnabled {
        private static SimpleConfig mockedConfig;
    
        @ApolloConfig
        private SimpleConfig        config;
    
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
    
            ConfigRepository configRepository = mock(ConfigRepository.class);
    
            when(configRepository.getConfig()).thenReturn(properties);
    
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
    
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
    
            mockedConfig = config;
        }
    
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
    
        @Autowired(required = false)
        private TestConfigurationPropertiesBean2 bean;
    
        @Test
        public void testRefreshEnabled() throws Exception {
    
            int newTimeout = 1001;
            int newBatch = 2001;
    
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
    
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
    
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
    
            TimeUnit.MILLISECONDS.sleep(100);
    
            assertEquals(newTimeout, bean.getTimeout());
            assertEquals(newBatch, bean.getBatch());
    
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesBean4Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesAutoRefreshDisable  {
        private static SimpleConfig mockedConfig;
    
        @ApolloConfig
        private SimpleConfig        config;
    
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
    
            ConfigRepository configRepository = mock(ConfigRepository.class);
    
            when(configRepository.getConfig()).thenReturn(properties);
    
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
    
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
    
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
    
        @Autowired(required = false)
        private TestConfigurationPropertiesBean4 bean;
    
        @Test
        public void testAutoRefreshDisable() throws Exception {
    
            int newTimeout = 1001;
            int newBatch = 2001;
    
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
    
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
    
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
    
            TimeUnit.MILLISECONDS.sleep(100);
    
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
    
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesBean3Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesAutoRefreshEnabled {
        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringBootTest(classes = TestConfigurationPropertiesBean4Application.class)
        @DirtiesContext
        public static class testConfigurationPropertiesAutoRefreshDisable  {
            private static SimpleConfig mockedConfig;
        
            @ApolloConfig
            private SimpleConfig        config;
        
            @BeforeClass
            public static void beforeClass() throws Exception {
                doSetUp();
                System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
                Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                        String.valueOf(initialBatch));
        
                ConfigRepository configRepository = mock(ConfigRepository.class);
        
                when(configRepository.getConfig()).thenReturn(properties);
        
                SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
        
                mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
        
                mockedConfig = config;
            }
        
            @Autowired(required = false)
            private ApplicationContext              context;
        
            @Autowired(required = false)
            private TestConfigurationPropertiesBean4 bean;
        
            @Test
            public void testAutoRefreshDisable() throws Exception {
        
                int newTimeout = 1001;
                int newBatch = 2001;
        
                assertEquals(initialTimeout, bean.getTimeout());
                assertEquals(initialBatch, bean.getBatch());
        
                Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                        String.valueOf(newBatch));
        
                config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
        
                TimeUnit.MILLISECONDS.sleep(100);
        
                assertEquals(initialTimeout, bean.getTimeout());
                assertEquals(initialBatch, bean.getBatch());
        
            }
        }

        private static SimpleConfig mockedConfig;
    
        @ApolloConfig
        private SimpleConfig        config;
    
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
    
            ConfigRepository configRepository = mock(ConfigRepository.class);
    
            when(configRepository.getConfig()).thenReturn(properties);
    
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
    
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
    
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
    
        @Autowired(required = false)
        private TestConfigurationPropertiesBean3 bean;
    
        @Test
        public void testAutoRefreshEnabled() throws Exception {
    
            int newTimeout = 1001;
            int newBatch = 2001;
    
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
    
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
    
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
    
            TimeUnit.MILLISECONDS.sleep(100);
    
            assertEquals(newTimeout, bean.getTimeout());
            assertEquals(newBatch, bean.getBatch());
    
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesBean5Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesAutoRefreshRefreshFieldDisable  {
        private static SimpleConfig mockedConfig;

        @ApolloConfig
        private SimpleConfig        config;

        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));

            ConfigRepository configRepository = mock(ConfigRepository.class);

            when(configRepository.getConfig()).thenReturn(properties);

            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);

            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);

            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;

        @Autowired(required = false)
        private TestConfigurationPropertiesBean5 bean;

        @Test
        public void testAutoRefreshRefreshFieldDisable() throws Exception {

            int newTimeout = 1001;
            int newBatch = 2001;

            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());

            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));

            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

            TimeUnit.MILLISECONDS.sleep(100);

            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());

        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesBean6Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesAutoRefreshRefreshFieldEnabledAndDisable  {
        private static SimpleConfig mockedConfig;

        @ApolloConfig
        private SimpleConfig        config;

        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));

            ConfigRepository configRepository = mock(ConfigRepository.class);

            when(configRepository.getConfig()).thenReturn(properties);

            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);

            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);

            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;

        @Autowired(required = false)
        private TestConfigurationPropertiesBean6 bean;

        @Test
        public void testAutoRefreshRefreshFieldEnabledAndDisable() throws Exception {

            int newTimeout = 1001;
            int newBatch = 2001;

            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());

            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));

            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);

            TimeUnit.MILLISECONDS.sleep(100);

            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(newBatch, bean.getBatch());

        }
    }
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesBean7Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesAutoRefreshDisableRefreshFieldEnabled  {
        private static SimpleConfig mockedConfig;
        
        @ApolloConfig
        private SimpleConfig        config;
        
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
            
            ConfigRepository configRepository = mock(ConfigRepository.class);
            
            when(configRepository.getConfig()).thenReturn(properties);
            
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
            
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
            
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
        
        @Autowired(required = false)
        private TestConfigurationPropertiesBean7 bean;
        
        @Test
        public void testAutoRefreshDisableRefreshFieldEnabled() throws Exception {
            
            int newTimeout = 1001;
            int newBatch = 2001;
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
            
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
            
            TimeUnit.MILLISECONDS.sleep(100);
            
            assertEquals(newTimeout, bean.getTimeout());
            assertEquals(newBatch, bean.getBatch());
            
        }
    }
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesConfigApplication.class)
    @DirtiesContext
    public static class testConfigurationPropertiesConfigEnabled  {
        private static SimpleConfig mockedConfig;
        
        @ApolloConfig
        private SimpleConfig        config;
        
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            System.setProperty(PropertySourcesConstants.APOLLO_AUTO_REFRESH_CONFIGURATION_PROPERTIE, "com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean8");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
            
            ConfigRepository configRepository = mock(ConfigRepository.class);
            
            when(configRepository.getConfig()).thenReturn(properties);
            
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
            
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
            
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
        
        @Autowired(required = false)
        private TestConfigurationPropertiesBean8 bean;
        
        @Test
        public void testAutoRefreshDisableRefreshFieldEnabled() throws Exception {
            
            int newTimeout = 1001;
            int newBatch = 2001;
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
            
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
            
            TimeUnit.MILLISECONDS.sleep(100);
            
            assertEquals(newTimeout, bean.getTimeout());
            assertEquals(newBatch, bean.getBatch());
            
        }
    }
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesConfig2Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesConfigDisable  {
        private static SimpleConfig mockedConfig;
        
        @ApolloConfig
        private SimpleConfig        config;
        
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
            
            ConfigRepository configRepository = mock(ConfigRepository.class);
            
            when(configRepository.getConfig()).thenReturn(properties);
            
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
            
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
            
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
        
        @Autowired(required = false)
        private TestConfigurationPropertiesBean9 bean;
        
        @Test
        public void testAutoRefreshDisableRefreshFieldEnabled() throws Exception {
            
            int newTimeout = 1001;
            int newBatch = 2001;
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
            
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
            
            TimeUnit.MILLISECONDS.sleep(100);
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
        }
    }
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesConfig3Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesConfigDisableRefreshDisabled  {
        private static SimpleConfig mockedConfig;
        
        @ApolloConfig
        private SimpleConfig        config;
        
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            System.setProperty(PropertySourcesConstants.APOLLO_AUTO_REFRESH_CONFIGURATION_PROPERTIE, "com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean10");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
            
            ConfigRepository configRepository = mock(ConfigRepository.class);
            
            when(configRepository.getConfig()).thenReturn(properties);
            
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
            
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
            
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
        
        @Autowired(required = false)
        private TestConfigurationPropertiesBean10 bean;
        
        @Test
        public void testAutoRefreshDisableRefreshFieldEnabled() throws Exception {
            
            int newTimeout = 1001;
            int newBatch = 2001;
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
            
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
            
            TimeUnit.MILLISECONDS.sleep(100);
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
        }
    }
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = TestConfigurationPropertiesConfig4Application.class)
    @DirtiesContext
    public static class testConfigurationPropertiesConfigDisableRefreshEnabled  {
        private static SimpleConfig mockedConfig;
        
        @ApolloConfig
        private SimpleConfig        config;
        
        @BeforeClass
        public static void beforeClass() throws Exception {
            doSetUp();
            System.setProperty(PropertySourcesConstants.APOLLO_AUTO_REFRESH_CONFIGURATION_PROPERTIE, "com.ctrip.framework.apollo.bean.TestConfigurationPropertiesBean11");
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "true");
            Properties properties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(initialTimeout), BATCH_PROPERTY,
                    String.valueOf(initialBatch));
            
            ConfigRepository configRepository = mock(ConfigRepository.class);
            
            when(configRepository.getConfig()).thenReturn(properties);
            
            SimpleConfig config = new SimpleConfig(ConfigConsts.NAMESPACE_APPLICATION, configRepository);
            
            mockConfig(ConfigConsts.NAMESPACE_APPLICATION, config);
            
            mockedConfig = config;
        }
        @AfterClass
        public static void afterClass() throws Exception {
          System.clearProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED);

          doTearDown();
        }
        @Autowired(required = false)
        private ApplicationContext              context;
        
        @Autowired(required = false)
        private TestConfigurationPropertiesBean11 bean;
        
        @Test
        public void testAutoRefreshDisableRefreshFieldEnabled() throws Exception {
            
            int newTimeout = 1001;
            int newBatch = 2001;
            
            assertEquals(initialTimeout, bean.getTimeout());
            assertEquals(initialBatch, bean.getBatch());
            
            Properties newProperties = assembleProperties(TIMEOUT_PROPERTY, String.valueOf(newTimeout), BATCH_PROPERTY,
                    String.valueOf(newBatch));
            
            config.onRepositoryChange(ConfigConsts.NAMESPACE_APPLICATION, newProperties);
            
            TimeUnit.MILLISECONDS.sleep(100);
            
            assertEquals(newTimeout, bean.getTimeout());
            assertEquals(newBatch, bean.getBatch());
            
        }
    }
}

@SpringBootApplication
@EnableConfigurationProperties(TestConfigurationPropertiesBean.class)
class TestConfigurationPropertiesApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesApplication.class, args);
    }
}

@SpringBootApplication
@EnableConfigurationProperties(TestConfigurationPropertiesBean1.class)
class TestConfigurationProperties1Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationProperties1Application.class, args);
    }
}

@SpringBootApplication(scanBasePackageClasses = TestConfigurationPropertiesBean2.class)
@EnableConfigurationProperties(TestConfigurationPropertiesBean2.class)
class TestConfigurationPropertiesBean2Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesBean2Application.class, args);
    }
}

@SpringBootApplication(scanBasePackageClasses = TestConfigurationPropertiesBean3.class)
@EnableConfigurationProperties(TestConfigurationPropertiesBean3.class)
class TestConfigurationPropertiesBean3Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesBean3Application.class, args);
    }
}

@SpringBootApplication(scanBasePackageClasses = TestConfigurationPropertiesBean4.class)
@EnableConfigurationProperties(TestConfigurationPropertiesBean4.class)
class TestConfigurationPropertiesBean4Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesBean4Application.class, args);
    }
}

@SpringBootApplication(scanBasePackageClasses = TestConfigurationPropertiesBean5.class)
@EnableConfigurationProperties(TestConfigurationPropertiesBean5.class)
class TestConfigurationPropertiesBean5Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesBean5Application.class, args);
    }
}

@SpringBootApplication(scanBasePackageClasses = TestConfigurationPropertiesBean6.class)
@EnableConfigurationProperties(TestConfigurationPropertiesBean6.class)
class TestConfigurationPropertiesBean6Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesBean6Application.class, args);
    }
}

@SpringBootApplication(scanBasePackageClasses = TestConfigurationPropertiesBean7.class)
@EnableConfigurationProperties(TestConfigurationPropertiesBean7.class)
class TestConfigurationPropertiesBean7Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesBean7.class, args);
    }
}

@SpringBootApplication
@EnableConfigurationProperties(TestConfigurationPropertiesBean8.class)
class TestConfigurationPropertiesConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesApplication.class, args);
    }
}

@SpringBootApplication
@EnableConfigurationProperties(TestConfigurationPropertiesBean9.class)
class TestConfigurationPropertiesConfig2Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesConfig2Application.class, args);
    }
}

@SpringBootApplication
@EnableConfigurationProperties(TestConfigurationPropertiesBean10.class)
class TestConfigurationPropertiesConfig3Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesConfig3Application.class, args);
    }
}

@SpringBootApplication
@EnableConfigurationProperties(TestConfigurationPropertiesBean11.class)
class TestConfigurationPropertiesConfig4Application {
    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationPropertiesConfig4Application.class, args);
    }
}