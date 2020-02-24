package com.shsxt;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shsxt.crm.dao")        //创建动态代理,告诉springboot,对于dao接口在哪儿
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}
