package com.alex.space.common;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.DnsNameResolverProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/6/20.
 */
@Slf4j
public class GrpcClient {

  private static final Map<String, GrpcContext> contextMap = new HashMap<>();

  private final List<RemoteServer> remoteServers;
  private final Class clientInterceptor;

  public GrpcClient(GrpcProperties grpcProperties) {
    this.remoteServers = grpcProperties.getRemoteServers();
    this.clientInterceptor = grpcProperties.getClientInterceptor();
    log.info("Grpc client: {}, remote server: {} ",
        grpcProperties.getName(), grpcProperties.getRemoteServers());
  }

  public GrpcClient(Class clientInterceptor, List<RemoteServer> servers) {
    this.clientInterceptor = clientInterceptor;
    remoteServers = servers;
  }

  public GrpcClient(RemoteServer... servers) {
    this(null, servers);
  }

  public GrpcClient(Class clientInterceptor, RemoteServer... servers) {
    this.clientInterceptor = clientInterceptor;
    remoteServers = new ArrayList<>();
    remoteServers.addAll(Arrays.asList(servers));
  }

  public void init() {
    if (remoteServers == null || remoteServers.size() == 0) {
      log.warn("remote servers is empty.");
      return;
    }

    for (RemoteServer server : remoteServers) {

      ManagedChannel channel = ManagedChannelBuilder
          .forAddress(server.getHost(), server.getPort())
          .nameResolverFactory(DnsNameResolverProvider.asFactory())
          .idleTimeout(30, TimeUnit.SECONDS)
          .usePlaintext()
          .build();

      if (clientInterceptor == null) {
        contextMap.put(server.getId(), new GrpcContext(channel));
      } else {
        try {
          ClientInterceptor interceptor = (ClientInterceptor) clientInterceptor.newInstance();
          Channel newChannel = ClientInterceptors.intercept(channel, interceptor);
          contextMap.put(server.getId(), new GrpcContext(newChannel));
        } catch (InstantiationException | IllegalAccessException e) {
          log.warn("ClientInterceptor cannot use, ignoring...");
          contextMap.put(server.getId(), new GrpcContext(channel));
        }
      }

    }

  }

  public static GrpcContext connect(String serverName) {
    return contextMap.get(serverName);
  }

}
