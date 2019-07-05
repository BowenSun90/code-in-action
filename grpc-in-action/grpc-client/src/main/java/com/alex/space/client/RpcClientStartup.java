package com.alex.space.client;

import com.alex.space.common.GrpcClient;
import com.alex.space.common.RemoteServer;
import com.alex.space.proto.GreeterGrpc;
import com.alex.space.proto.HelloReply;
import com.alex.space.proto.HelloRequest;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/7/5.
 */
@Slf4j
public class RpcClientStartup {

  public static void main(String[] args) {

    try {

      // Start Rpc Client
      RemoteServer remoteServer = RemoteServer.builder()
          .id("GreeterServer-1")
          .host("127.0.0.1")
          .port(9001)
          .build();

      GrpcClient client = new GrpcClient(remoteServer);
      client.init();

      log.info("Rpc client started.");

      // Make a call
      Channel channel = GrpcClient.connect("GreeterServer-1").getChannel();
      GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
      HelloRequest request = HelloRequest.newBuilder()
          .setName("Admin")
          .build();
      HelloReply reply = blockingStub.sayHello(request);
      log.info("Response: {}", reply);

    } catch (Exception e) {
      log.error(e.getMessage());
    }

  }

}
