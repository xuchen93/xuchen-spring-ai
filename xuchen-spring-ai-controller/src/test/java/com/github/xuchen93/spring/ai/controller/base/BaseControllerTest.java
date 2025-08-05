package com.github.xuchen93.spring.ai.controller.base;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Slf4j
public class BaseControllerTest {


	private static final Semaphore SEMAPHORE = new Semaphore(1);
	protected final WebClient webClient = WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector(
					HttpClient.create(ConnectionProvider.builder("webclient-pool")
									.maxConnections(100)          // 最大连接数
									.pendingAcquireMaxCount(-1)   // 等待获取连接的最大请求数（-1表示无限制）
									.pendingAcquireTimeout(Duration.ofSeconds(60))  // 等待连接超时时间
									.maxIdleTime(java.time.Duration.ofSeconds(60))  // 连接最大空闲时间
									.build()
							)
							.responseTimeout(java.time.Duration.ofMinutes(5))))
			.baseUrl(getServerUrl())
			.build();

	@SneakyThrows
	@AfterAll
	public static void afterAll() {
		SEMAPHORE.acquire();
	}

	protected String getServerUrl() {
		return "http://localhost:8080";
	}

	public Disposable get(Map<String, String> param) {
		return get(param, "");
	}

	public Disposable get(Map<String, String> param, String path) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		if (param != null) {
			map.setAll(param);
		}
		return doGet(map, path);
	}

	// 流式获取字符串响应
	@SneakyThrows
	private Disposable doGet(MultiValueMap<String, String> param, String path) {
		SEMAPHORE.acquire();
		log.info("请求路径：{}{}", getServerUrl(), path);
		log.info("请求参数：{}", param);
		StringBuilder sb = new StringBuilder();
		long startTime = System.currentTimeMillis();
		return webClient.get()
				.uri(uriBuilder ->
						uriBuilder.path(path)
								.queryParams(param)
								.build()
				)
				.attributes((attributeBuilder -> attributeBuilder.putAll(param)))
				.accept(MediaType.TEXT_EVENT_STREAM)
				.retrieve()
				.bodyToFlux(String.class)
				.doFinally(signalType -> {
					log.info("请求总耗时：{} ms", System.currentTimeMillis() - startTime);
					SEMAPHORE.release();
					log.info("完整的响应：\n{}", sb);
				})
				.subscribe(msg -> {
					log.info(msg);
					sb.append(msg);
				})
				;

	}
}
