package com.core.fmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.core.fmservice.configs.PropertiesConfig;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableEurekaClient
@EnableConfigurationProperties(PropertiesConfig.class)
public class FMServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FMServiceApplication.class, args);
	}
}
