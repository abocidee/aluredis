package com.alu.itoken.service.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication()
@EnableEurekaClient
public class ServiceRedisApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceRedisApplication.class, args);
	}

}
