package com.core.fmservice.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.*;
@Configuration
@ConfigurationProperties(prefix = "core")
@Getter
@Setter
public class PropertiesConfig {
    private String DIR;
}