package com.github.xuchen93.spring.ai.controller.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author xuchen.wang
 * @date 2025/7/4
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnClass(ChatClient.class)
public class ChatController {

	private final ChatClient chatClient;

	@GetMapping("chat")
	public Flux<String> chat(@RequestParam(defaultValue = "一句话介绍你自己") String msg) {
		return this.chatClient
				.prompt()
				.user(msg)
				.stream()
				.content();
	}
}
