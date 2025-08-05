package com.github.xuchen93.spring.ai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 聊天记忆默认配置
 *
 * @author xuchen.wang
 * @date 2025/7/15
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ChatMemory.class)
public class MemoryConfig {

	@ConditionalOnMissingBean(ChatMemoryRepository.class)
	@Bean
	public ChatMemoryRepository chatMemoryRepository() {
		log.info("【注册】默认的[chatMemoryRepository]");
		return new InMemoryChatMemoryRepository();
	}


	@Bean
	@ConditionalOnMissingBean(ChatMemory.class)
	public ChatMemory chatMemory(@Autowired ChatMemoryRepository chatMemoryRepository) {
		log.info("【注册】默认的[chatMemory]");
		return MessageWindowChatMemory
				.builder()
				.chatMemoryRepository(chatMemoryRepository)
				.maxMessages(20)
				.build();
	}
}
