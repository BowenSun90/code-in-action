package com.alex.space.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alex Created by Alex on 2019/6/20.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoteServer {

  /**
   * Grpc server unique id
   */
  private String id;

  /**
   * Grpc server host ip
   */
  private String host;

  /**
   * Grpc server listen port
   */
  private int port;

}
