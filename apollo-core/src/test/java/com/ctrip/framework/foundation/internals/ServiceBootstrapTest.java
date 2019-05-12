package com.ctrip.framework.foundation.internals;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ServiceConfigurationError;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.framework.apollo.core.spi.MetaServerProvider;
import com.ctrip.framework.apollo.tracer.spi.MessageProducerManager;
import com.ctrip.framework.foundation.spi.ProviderManager;
import com.ctrip.framework.foundation.spi.provider.ApplicationProvider;
import com.ctrip.framework.foundation.spi.provider.NetworkProvider;
import com.ctrip.framework.foundation.spi.provider.Provider;
import com.ctrip.framework.foundation.spi.provider.ServerProvider;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ServiceBootstrapTest {
  @Test
  public void loadFirstSuccessfully() throws Exception {
    Interface1 service = ServiceBootstrap.loadFirst(Interface1.class);
    assertTrue(service instanceof Interface1Impl);
  }

  @Test(expected = IllegalStateException.class)
  public void loadFirstWithNoServiceFileDefined() throws Exception {
    ServiceBootstrap.loadFirst(Interface2.class);
  }

  @Test(expected = IllegalStateException.class)
  public void loadFirstWithServiceFileButNoServiceImpl() throws Exception {
    ServiceBootstrap.loadFirst(Interface3.class);
  }

  @Test(expected = ServiceConfigurationError.class)
  public void loadFirstWithWrongServiceImpl() throws Exception {
    ServiceBootstrap.loadFirst(Interface4.class);
  }

  @Test(expected = ServiceConfigurationError.class)
  public void loadFirstWithServiceImplNotExists() throws Exception {
    ServiceBootstrap.loadFirst(Interface5.class);
  }

  @Test
  public void loadAllWithServiceImplSortASC() throws Exception {
    List<MetaServerProvider> metaServerProviders = ServiceBootstrap.loadAll(MetaServerProvider.class, ServiceBootstrap.ASC);
    MetaServerProvider lastMetaServerProvider = null;
    for (MetaServerProvider metaServerProvider : metaServerProviders) {
      if(lastMetaServerProvider == null) {
        lastMetaServerProvider  = metaServerProvider;
        continue;
      }
      Assert.assertTrue(lastMetaServerProvider.getOrder() < metaServerProvider.getOrder());
      
      lastMetaServerProvider  = metaServerProvider;
    }
  }
  
  @Test
  public void loadAllWithServiceImplSortDESC() throws Exception {
    List<MetaServerProvider> metaServerProviders = ServiceBootstrap.loadAll(MetaServerProvider.class, ServiceBootstrap.DESC);
    MetaServerProvider lastMetaServerProvider = null;
    for (MetaServerProvider metaServerProvider : metaServerProviders) {
      if(lastMetaServerProvider == null) {
        lastMetaServerProvider  = metaServerProvider;
        continue;
      }
      Assert.assertTrue(lastMetaServerProvider.getOrder() > metaServerProvider.getOrder());
      
      lastMetaServerProvider  = metaServerProvider;
    }
  }
  
  @Test
  public void loadFirstWithServiceImplSort() throws Exception {
    Provider applicationProvider = ServiceBootstrap.loadFirst(ApplicationProvider.class, ServiceBootstrap.ASC);
    
    Assert.assertTrue(applicationProvider instanceof ApplicationProvider);
    
    Provider networkProvider = ServiceBootstrap.loadFirst(NetworkProvider.class, ServiceBootstrap.ASC);
    
    Assert.assertTrue(networkProvider instanceof NetworkProvider);
    
    Provider serverProvider = ServiceBootstrap.loadFirst(ServerProvider.class, ServiceBootstrap.ASC);
    
    Assert.assertTrue(serverProvider instanceof ServerProvider);
    
    ProviderManager providerManager = ServiceBootstrap.loadFirst(ProviderManager.class, ServiceBootstrap.ASC);
    
    Assert.assertTrue(providerManager instanceof ProviderManager);
    
    MessageProducerManager messageProducerManager = ServiceBootstrap.loadFirst(MessageProducerManager.class, ServiceBootstrap.ASC);
    
    Assert.assertTrue(messageProducerManager instanceof MessageProducerManager);
  }
  
  private interface Interface1 {
  }

  public static class Interface1Impl implements Interface1 {
  }

  private interface Interface2 {
  }

  private interface Interface3 {
  }

  private interface Interface4 {
  }

  private interface Interface5 {
  }
}
