package com.alex.space.client;

import com.alex.space.common.GrpcClient;
import com.alex.space.common.RemoteServer;
import com.alex.space.proto.Average;
import com.alex.space.proto.GreeterGrpc;
import com.alex.space.proto.HelloReply;
import com.alex.space.proto.HelloRequest;
import com.alex.space.proto.Metric;
import com.alex.space.proto.MetricsGrpc;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
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
          .id("RpcServer-1")
          .host("127.0.0.1")
          .port(9001)
          .build();

      GrpcClient client = new GrpcClient(remoteServer);
      client.init();

      log.info("Rpc client started.");

      Thread mainThread = Thread.currentThread();

      // Make a call
//      calc();
//      greet();

      Thread t = new Thread(() -> {
        try {
          calc();
          mainThread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      t.start();

      Thread.sleep(10000);
      log.info("....");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void greet() {
    Channel channel = GrpcClient.connect("RpcServer-1").getChannel();
    GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
    HelloRequest request = HelloRequest.newBuilder()
        .setName("Admin")
        .build();
    HelloReply reply = blockingStub.sayHello(request);
    log.info("Response: {}", reply);
  }

  private static void calc() throws InterruptedException {
    Channel channel = GrpcClient.connect("RpcServer-1").getChannel();
    MetricsGrpc.MetricsStub stub = MetricsGrpc.newStub(channel);

    StreamObserver<Metric> collect = stub.collect(new StreamObserver<Average>() {
      @Override
      public void onNext(Average value) {
        log.info("Average: " + value.getVal());
      }

      @Override
      public void onError(Throwable t) {
      }

      @Override
      public void onCompleted() {
      }
    });

    collect.onNext(Metric.newBuilder().setMetric(1).build());
    collect.onNext(Metric.newBuilder().setMetric(2).build());
    collect.onNext(Metric.newBuilder().setMetric(3).build());
    collect.onNext(Metric.newBuilder().setMetric(4).build());
    collect.onNext(Metric.newBuilder().setMetric(5).build());
    collect.onNext(Metric.newBuilder().setMetric(6).build());
    collect.onNext(Metric.newBuilder().setMetric(7).build());

    Thread.sleep(5000);
    collect.onCompleted();
  }
}
