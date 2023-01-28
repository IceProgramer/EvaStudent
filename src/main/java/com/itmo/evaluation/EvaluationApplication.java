package com.itmo.evaluation;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.itmo.evaluation.mapper")
public class EvaluationApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EvaluationApplication.class, args);
    }

}
