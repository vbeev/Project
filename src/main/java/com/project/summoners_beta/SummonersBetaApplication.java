package com.project.summoners_beta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SummonersBetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummonersBetaApplication.class, args);
    }

}
