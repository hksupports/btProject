package com.xiudun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.xiudun.dao")
@SpringBootApplication
public class BtApplication {
	public static void main(String[] args) {
		SpringApplication.run(BtApplication.class, args);
	}
}

