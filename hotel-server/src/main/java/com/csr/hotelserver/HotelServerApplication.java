package com.csr.hotelserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HotelServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelServerApplication.class, args);
    }

    /**
     * 开启Cors校验, 解决跨域访问问题
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .exposedHeaders("Content-Disposition");
            }
        };
    }

}
