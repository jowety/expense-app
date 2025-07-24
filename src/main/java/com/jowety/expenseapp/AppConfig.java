package com.jowety.expenseapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@EnableScheduling
public class AppConfig implements WebMvcConfigurer{

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Define the path pattern
//            .allowedOrigins("http://localhost:4200") // Allow specific origins
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific methods
            .allowedHeaders("*") // Allow all headers
//            .allowCredentials(true) // Enable sending credentials (cookies, authorization headers)
            .maxAge(3600); // Set max age for pre-flight requests
    }

}
