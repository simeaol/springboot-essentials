package com.springboot2.essentials.springboot2essentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.springboot2")
public class Springboot2EssentialsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot2EssentialsApplication.class, args);
    }

}
