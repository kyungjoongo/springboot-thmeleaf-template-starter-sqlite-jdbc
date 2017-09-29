package com.kyungjoon.springbootjsp.bootjsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.kyungjoon.springbootjsp.*"})
@SpringBootApplication
public class BootjspApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootjspApplication.class, args);
	}
}
