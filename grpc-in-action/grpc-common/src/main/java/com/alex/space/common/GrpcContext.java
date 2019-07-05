package com.alex.space.common;

import io.grpc.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/6/20.
 */
@Slf4j
@Data
@AllArgsConstructor
public class GrpcContext {

  private Channel channel;

}
