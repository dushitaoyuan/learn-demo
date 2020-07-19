package com.taoyuanx.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.taoyuanx.demo")
public class DemoBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoBootApplication.class, args);
	}


}
