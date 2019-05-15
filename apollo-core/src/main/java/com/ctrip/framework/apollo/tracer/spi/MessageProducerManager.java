package com.ctrip.framework.apollo.tracer.spi;

import com.ctrip.framework.apollo.core.spi.Ordered;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface MessageProducerManager  extends Ordered{
  /**
   * @return the message producer
   */
  MessageProducer getProducer();
}
