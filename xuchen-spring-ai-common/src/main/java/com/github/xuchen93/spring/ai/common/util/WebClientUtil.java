package com.github.xuchen93.spring.ai.common.util;

import cn.hutool.http.HttpUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author xuchen.wang
 * @date 2025/7/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientUtil {
	private static WebClient webClient;
	private final WebClient webClientBean;

	public static <T> T blockGet(String url, Map<String, Object> paramMap, ParameterizedTypeReference<T> elementTypeRef) {
		return webClient.get()
				.uri(HttpUtil.urlWithFormUrlEncoded(url, paramMap, Charset.defaultCharset()))
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(elementTypeRef)
				.block();
	}

	@PostConstruct
	public void init() {
		log.info("【注册】默认的[WebClientUtil]");
		WebClientUtil.webClient = webClientBean;
	}
}
