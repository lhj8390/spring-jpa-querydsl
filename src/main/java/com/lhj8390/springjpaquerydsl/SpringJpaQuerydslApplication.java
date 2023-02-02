package com.lhj8390.springjpaquerydsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringJpaQuerydslApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaQuerydslApplication.class, args);
    }

}
