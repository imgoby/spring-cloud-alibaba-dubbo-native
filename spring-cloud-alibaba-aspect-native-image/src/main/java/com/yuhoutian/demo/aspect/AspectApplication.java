package com.yuhoutian.demo.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AspectApplication implements CommandLineRunner {

	@Autowired
	private Task task;

	public static void main(String[] args) {
		SpringApplication.run(AspectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		task.exec("hello");
	}
}
