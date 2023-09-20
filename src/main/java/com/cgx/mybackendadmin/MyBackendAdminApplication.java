package com.cgx.mybackendadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cgx.mybackendadmin.Mapper")
public class MyBackendAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBackendAdminApplication.class, args);
	}

}