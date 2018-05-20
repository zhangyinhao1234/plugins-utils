package com.github.zhangyinhao1234.example.hibernate_in_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@ComponentScan(value = { "com.binpo.plugin", "com.binpo.web" })
public class WebMain {
	public static void main(String[] args) {
		SpringApplication.run(WebMain.class, args);
	}
}
