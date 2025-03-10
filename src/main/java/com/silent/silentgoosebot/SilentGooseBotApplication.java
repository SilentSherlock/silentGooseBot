package com.silent.silentgoosebot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.silent.silentgoosebot.dao")
@EnableScheduling
public class SilentGooseBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SilentGooseBotApplication.class, args);
    }

}
