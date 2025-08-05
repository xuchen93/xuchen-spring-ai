package com.github.xuchen93.spring.ai.toolbox.zhipu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.github.xuchen93"})
public class ChatZhipuApp {

	public static void main(String[] args) {
		SpringApplication.run(ChatZhipuApp.class, args);
	}
}
