package com.alex.space.server;

import com.alex.space.common.GrpcProperties;
import com.alex.space.common.GrpcServer;
import com.alex.space.server.rpc.GreeterRpcService;
import com.alex.space.server.rpc.MetricsClientSideRpcService;
import com.alex.space.server.rpc.MetricsTwoWayRpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/7/5.
 */
@Slf4j
public class RpcServerStartup {

  public static void main(String[] args) {

    try {

      // Start Rpc Server
      GrpcProperties properties = GrpcProperties.builder()
          .name("RpcServer")
          .port(9001)
          .build();

      GrpcServer server = new GrpcServer(properties,
          new GreeterRpcService(),
          new MetricsClientSideRpcService(),
          new MetricsTwoWayRpcService());
      server.start();

      log.info("Rpc server started, listen port 9001.");

    } catch (Exception e) {
      log.error(e.getMessage());
    }

  }
}
