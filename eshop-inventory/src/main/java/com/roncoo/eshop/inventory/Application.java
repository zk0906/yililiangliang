package com.roncoo.eshop.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.roncoo.eshop.mapper")
public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
