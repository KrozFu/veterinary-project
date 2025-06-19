package com.veterinaria.api_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer archivos desde la carpeta /uploads como /imagenes/**
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:uploads/");
    }
}
