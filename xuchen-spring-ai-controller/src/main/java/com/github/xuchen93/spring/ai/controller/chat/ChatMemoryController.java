package com.github.xuchen93.spring.ai.controller.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author xuchen.wang
 * @date 2025/7/4
 */
@Slf4j
@RestController
@RequestMapping("memory")
@RequiredArgsConstructor
public class ChatMemoryController {

	private final ChatClient memoryChatClient;

	@GetMapping("chat")
	public Flux<String> chat(@RequestParam(defaultValue = "你是谁？") String msg, @RequestParam(defaultValue = "1") String userId) {
		return this.memoryChatClient
				.prompt()
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, userId))
				.user(msg)
				.stream()
				.content();
	}
}
