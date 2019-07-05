package com.alex.space.common;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author Alex Created by Alex on 2019/7/5.
 */
@Data
@Builder
public class GrpcProperties {

  /**
   * Server unique id
   */
  private String name;

  /**
   * server listen port
   */
  private Integer port;

  /**
   * client config
   */
  private List<RemoteServer> remoteServers;

  /**
   * client interceptor
   */
  private Class clientInterceptor;

  /**
   * server interceptor
   */
  private Class serverInterceptor;

}
