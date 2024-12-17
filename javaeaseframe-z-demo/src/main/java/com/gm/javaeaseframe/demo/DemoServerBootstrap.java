package com.gm.javaeaseframe.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.gm.javaeaseframe.core.boot.BaseApplication;

@SpringBootApplication
@EnableFeignClients
public class DemoServerBootstrap extends BaseApplication
{
    
    public static void main(String[] args) {
        SpringApplication.run(DemoServerBootstrap.class, args);
    }
}
