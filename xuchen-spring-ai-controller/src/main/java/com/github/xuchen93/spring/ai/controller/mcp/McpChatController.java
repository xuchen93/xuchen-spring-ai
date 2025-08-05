package com.github.xuchen93.spring.ai.controller.mcp;

import com.github.xuchen93.spring.ai.common.util.PromptUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

/**
 * @author xuchen.wang
 * @date 2025/7/4
 */
@Slf4j
@RestController
@RequestMapping("/mcp/chat")
@RequiredArgsConstructor
public class McpChatController {

	private final ChatClient mcpChatClient;
	@Value("classpath:prompt.txt")
	Resource promptResource;

	@SneakyThrows
	@GetMapping
	public Flux<String> chat(String msg, @RequestParam(defaultValue = "1") String userId) {
		return this.mcpChatClient
				.prompt(promptResource.exists() ? promptResource.getContentAsString(StandardCharsets.UTF_8) : PromptUtil.defaultPrompt())
				.user(msg)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, userId))
				.stream()
				.content();
	}

	@SneakyThrows
	@GetMapping("/tools")
	public Flux<String> tools() {
		return this.mcpChatClient
				.prompt(PromptUtil.defaultPrompt())
				.user("你的工具有哪些")
				.stream()
				.content();
	}
}
