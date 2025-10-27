package com.upskilling.experiment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync // Enables asynchronous method execution (Crucial for non-blocking emails)
public class ApplicationConfig {}