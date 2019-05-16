package com.ctrip.framework.apollo.demo.spring.springBootDemo.autorefresh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.ctrip.framework.apollo.ClientConfigConsts;
import com.ctrip.framework.apollo.spring.boot.RefreshConfigurationProperties;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@SpringBootApplication
public class SpringBootRefreshSampleApplication2 {

  public static void main(String[] args) throws IOException {
    //
    System.setProperty(ClientConfigConsts.APOLLO_AUTO_UPDATE_INJECTED_SPRING_PROPERTIE, "false");
    ApplicationContext context = new SpringApplicationBuilder(SpringBootRefreshSampleApplication2.class).run(args);
    AutoRefreshConfigurationPropertiesRedisConfig refreshEnabledRedisConfig = context.getBean(AutoRefreshConfigurationPropertiesRedisConfig.class);
    
    RefreshConfigurationProperties refreshConfigurationProperties = context.getBean(RefreshConfigurationProperties.class);
System.out.println(refreshConfigurationProperties);
    System.out.println("SpringBootSampleApplication Demo. Input any key except quit to print the values. Input quit to exit.");
    while (true) {
      System.out.print("> ");
      String input = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
      if (!Strings.isNullOrEmpty(input) && input.trim().equalsIgnoreCase("quit")) {
        System.exit(0);
      }

      System.out.println(refreshEnabledRedisConfig);
      System.out.println(context.getEnvironment().getProperty("redis.cache.expireSeconds"));
    }
  }
  
}
