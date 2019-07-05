package com.alex.space.server.rpc;

import com.alex.space.proto.GreeterGrpc;
import com.alex.space.proto.HelloReply;
import com.alex.space.proto.HelloRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/7/5.
 */
@Slf4j
public class GreeterRpcService extends GreeterGrpc.GreeterImplBase {

  @Override
  public void sayHello(final HelloRequest request,
      final StreamObserver<HelloReply> responseObserver) {

    log.info("Receive hello from {}", request.getName());
    HelloReply reply = HelloReply.newBuilder()
        .setMessage("Reply to " + request.getName())
        .build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

}
