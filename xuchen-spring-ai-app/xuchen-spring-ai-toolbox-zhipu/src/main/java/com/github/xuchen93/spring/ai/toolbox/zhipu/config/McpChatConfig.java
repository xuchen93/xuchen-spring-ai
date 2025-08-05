package com.github.xuchen93.spring.ai.toolbox.zhipu.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author xuchen.wang
 * @date 2025/7/8
 */
@Slf4j
@Configuration
@ConditionalOnClass(ChatClient.class)
@RequiredArgsConstructor
public class McpChatConfig {

	private final ChatMemory chatMemory;
	private final ToolCallbackProvider toolCallbackProvider;

	@Bean("mcpChatClient")
	public ChatClient mcpChatClient(ChatModel chatModel) {
		log.info("【注册】McpChatClient with toolCallbackProvider:{}, ", Arrays.stream(toolCallbackProvider.getToolCallbacks())
				.map(t -> t.getToolDefinition().name())
				.collect(Collectors.joining(",")));
		ChatClient chatClient = ChatClient
				.builder(chatModel)
				.defaultToolCallbacks(toolCallbackProvider)
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
				.build();
		return chatClient;
	}
}
