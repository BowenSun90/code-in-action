package com.alex.space.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
@ComponentScan(basePackages = "com.alex.space.springboot")
@SpringBootApplication
@MapperScan(basePackages = "com.alex.space.springboot.dao")
public class SecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityDemoApplication.class, args);
	}
}
