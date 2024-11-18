package com.gm.javaeaseframe.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.gm.javaeaseframe.core.boot.BaseApplication;

//@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"com.gm"})
@SpringBootApplication(scanBasePackages = {"com.gm.javaeaseframe.demo"})
//@ServletComponentScan(basePackages = {"com.gm"})
@EnableFeignClients
public class DemoServerBootstrap extends BaseApplication
{
    
    public static void main(String[] args) {
        SpringApplication.run(DemoServerBootstrap.class, args);
    }
}
