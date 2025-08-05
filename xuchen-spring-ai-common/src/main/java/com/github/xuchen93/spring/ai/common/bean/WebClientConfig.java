package com.github.xuchen93.spring.ai.common.bean;

import cn.hutool.core.text.UnicodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * @author xuchen.wang
 * @date 2025/7/16
 */
@Slf4j
@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		log.info("【注册】默认的[WebClient]");
		return WebClient.builder()
				.filter((request, next) -> next.exchange(request)
						.flatMap(response -> response.bodyToMono(String.class)
								.map(body -> {
									// 解码 Unicode
									String decodedBody = UnicodeUtil.toString(body);
									// 重新包装为 ClientResponse
									return ClientResponse.from(response)
											.body(decodedBody)
											.build();
								})
						)
				)
				.clientConnector(new ReactorClientHttpConnector(
						HttpClient.create(ConnectionProvider.builder("webclient-pool")
										.maxConnections(32)          // 最大连接数
										.pendingAcquireMaxCount(-1)   // 等待获取连接的最大请求数（-1表示无限制）
										.pendingAcquireTimeout(Duration.ofSeconds(60))  // 等待连接超时时间
										.maxIdleTime(java.time.Duration.ofSeconds(60))  // 连接最大空闲时间
										.build()
								)
								.responseTimeout(java.time.Duration.ofMinutes(5))))
				.build();
	}
}
