package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages ="org.example.mapper",sqlSessionFactoryRef = "sqlSessionFactory") // 扫描mapper类
public class SqliteApplication {

	public static void main(String[] args) {
		System.setProperty("javax.xml.accessExternalDTD", "all");
		SpringApplication.run(SqliteApplication.class, args);
	}


}

