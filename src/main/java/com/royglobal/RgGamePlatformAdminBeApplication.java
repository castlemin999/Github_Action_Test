package com.royglobal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class RgGamePlatformAdminBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RgGamePlatformAdminBeApplication.class, args);
	}

}
