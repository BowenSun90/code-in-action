package com.alex.space.client;

import com.alex.space.common.GrpcClient;
import com.alex.space.common.RemoteServer;
import com.alex.space.proto.Average;
import com.alex.space.proto.GreeterGrpc;
import com.alex.space.proto.HelloReply;
import com.alex.space.proto.HelloRequest;
import com.alex.space.proto.Metric;
import com.alex.space.proto.MetricsClientSideGrpc;
import com.alex.space.proto.MetricsTwoWayGrpc;
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

      // Make a call
//      greet();
//      clientSideStream();
//      twoWayStream();

      Thread t = new Thread(() -> {
        twoWayStreamKeepAlive();

        try {
          Thread.sleep(5000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      t.start();
      t.join();

      log.info("Wait...");
      Thread.sleep(10000);
      log.info("Finish...");

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

  private static void clientSideStream() {
    Channel channel = GrpcClient.connect("RpcServer-1").getChannel();
    MetricsClientSideGrpc.MetricsClientSideStub stub = MetricsClientSideGrpc.newStub(channel);

    StreamObserver<Metric> collect = stub.collect(new StreamObserver<Average>() {
      @Override
      public void onNext(Average value) {
        log.info("Average(client side stream): " + value.getVal());
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
    collect.onCompleted();
  }

  private static void twoWayStream() {
    Channel channel = GrpcClient.connect("RpcServer-1").getChannel();
    MetricsTwoWayGrpc.MetricsTwoWayStub stub = MetricsTwoWayGrpc.newStub(channel);

    StreamObserver<Metric> collect = stub.collect(new StreamObserver<Average>() {
      @Override
      public void onNext(Average value) {
        log.info("Average(two way stream): " + value.getVal());
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

    collect.onCompleted();
  }

  private static void twoWayStreamKeepAlive() {
    Channel channel = GrpcClient.connect("RpcServer-1").getChannel();
    MetricsTwoWayGrpc.MetricsTwoWayStub stub = MetricsTwoWayGrpc.newStub(channel);

    StreamObserver<Metric> collect = stub.collect(new StreamObserver<Average>() {
      @Override
      public void onNext(Average value) {
        log.info("Average(two way stream): " + value.getVal());
      }

      @Override
      public void onError(Throwable t) {
      }

      @Override
      public void onCompleted() {
      }
    });

    for (int i = 0; i < 1000; i++) {
      collect.onNext(Metric.newBuilder().setMetric(i).build());

      try {
        Thread.sleep(5000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
