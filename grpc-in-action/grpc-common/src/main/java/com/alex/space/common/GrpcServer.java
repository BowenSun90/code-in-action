package com.alex.space.common;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/6/19.
 */
@Slf4j
public class GrpcServer {

  private final int port;
  private final List<BindableService> serviceList;
  private final Class serverInterceptor;

  private Server server;

  public GrpcServer(GrpcProperties grpcProperties, BindableService... services) {
    this.port = grpcProperties.getPort();
    this.serverInterceptor = grpcProperties.getServerInterceptor();
    this.serviceList = new ArrayList<>();
    this.serviceList.addAll(Arrays.asList(services));
    log.info("Grpc server: {}, port: {}", grpcProperties.getName(), grpcProperties.getPort());
  }

  public void start() throws Exception {

    ServerBuilder builder = ServerBuilder.forPort(port);
    if (serverInterceptor == null) {
      for (BindableService service : serviceList) {
        builder.addService(service);
      }
    } else {
      for (BindableService service : serviceList) {
        builder.addService(ServerInterceptors
            .intercept(service, (ServerInterceptor) serverInterceptor.newInstance()));
      }
    }
    server = builder.build().start();

    startDaemonAwaitThread();
  }

  public void destroy() {
    Optional.ofNullable(server).ifPresent(Server::shutdown);
    log.info("gRPC server stopped.");
  }

  private void startDaemonAwaitThread() {
    Thread awaitThread = new Thread(() -> {
      try {
        GrpcServer.this.server.awaitTermination();
      } catch (InterruptedException e) {
        log.warn("gRPC server stopped." + e.getMessage());
      }
    });
    awaitThread.setDaemon(false);
    awaitThread.start();
  }

}
