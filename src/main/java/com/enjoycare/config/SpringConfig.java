package com.enjoycare.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.enjoycare.controller", "com.enjoycare.service", "com.enjoycare.dao"})
public class SpringConfig { }
