package com.alex.space.server.rpc;

import com.alex.space.proto.Average;
import com.alex.space.proto.Metric;
import com.alex.space.proto.MetricsTwoWayGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/7/5.
 */
@Slf4j
public class MetricsTwoWayRpcService extends MetricsTwoWayGrpc.MetricsTwoWayImplBase {

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
        onCompleted();
      }

      @Override
      public void onError(Throwable t) {
        responseObserver.onError(t);
      }

      @Override
      public void onCompleted() {
        Average average = Average.newBuilder()
            .setVal(sum * 1.0 / count)
            .build();
        responseObserver.onNext(average);
      }

    };
  }
}
