package com.github.xuchen93.spring.ai.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuchen.wang
 * @date 2025/7/8
 */
@Slf4j
@Configuration
@ConditionalOnClass(ChatClient.class)
@RequiredArgsConstructor
public class SimpleChatConfig {

	private final ChatMemory chatMemory;
//
//	/**
//	 * ChatClient
//	 */
//	@Bean("chatClient")
//	public ChatClient chatClient(ChatModel chatModel) throws Exception {
//		log.info("【注册】ChatClient：{}", chatModel.getClass().getSimpleName());
//		ChatClient chatClient = ChatClient
//				.builder(chatModel)
//				.defaultAdvisors(new SimpleLoggerAdvisor())
//				.build();
//		return chatClient;
//	}

	/**
	 * ChatClient
	 */
	@Bean("memoryChatClient")
	@ConditionalOnMissingBean(ChatClient.class)
	public ChatClient memoryChatClient(ChatModel chatModel) throws Exception {
		log.info("【注册】memoryChatClient：{}", chatModel.getClass().getSimpleName());
		ChatClient chatClient = ChatClient
				.builder(chatModel)
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
				.build();
		return chatClient;
	}
}
