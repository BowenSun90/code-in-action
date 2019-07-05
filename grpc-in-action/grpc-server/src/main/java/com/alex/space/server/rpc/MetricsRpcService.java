package com.alex.space.server.rpc;

import com.alex.space.proto.Average;
import com.alex.space.proto.Metric;
import com.alex.space.proto.MetricsGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/7/5.
 */
@Slf4j
public class MetricsRpcService extends MetricsGrpc.MetricsImplBase {

  @Override
  public StreamObserver<Metric> collect(final StreamObserver<Average> responseObserver) {

    return new StreamObserver<Metric>() {
      private long sum = 0;
      private long count = 0;

      @Override
      public void onNext(Metric value) {
        log.info("collect value: " + value);
        sum += value.getMetric();
        count++;
      }

      @Override
      public void onError(Throwable t) {
        responseObserver.onError(t);
      }

      @Override
      public void onCompleted() {
        Average average = Average.newBuilder()
            .setVal(sum / count)
            .build();
        responseObserver.onNext(average);
      }

    };
  }
}
