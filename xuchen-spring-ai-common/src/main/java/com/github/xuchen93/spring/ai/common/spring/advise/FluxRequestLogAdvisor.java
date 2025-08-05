package com.github.xuchen93.spring.ai.common.spring.advise;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xuchen.wang
 * @date 2025/7/15
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class FluxRequestLogAdvisor {


	private final ObjectMapper objectMapper = new ObjectMapper();

	private final int consumeTime = 2000;

	@PostConstruct
	public void init() {
		log.info("【注册】{}", this.getClass().getSimpleName());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}


	// 定义切点
	@Pointcut("execution(* *..controller..*.*(..))")
	public void controllerPointCut() {

	}

	// Around增强处理
	@Around("controllerPointCut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取方法信息

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		String baseLog = String.format("[%s-%s]-[%s]", System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(100, 1000), method.getName());

		// 记录请求开始时间
		long startTime = System.currentTimeMillis();
		int argLength = joinPoint.getArgs().length;
		Object arg;
		switch (argLength) {
			case 0 -> arg = null;
			case 1 -> arg = joinPoint.getArgs()[0];
			default -> arg = joinPoint.getArgs();
		}
		log.info("{}入参:[{}]方法", baseLog, objectMapper.writeValueAsString(arg));

		// 执行目标方法
		Object result = joinPoint.proceed();
		// 处理响应
		if (result instanceof Mono) {
			return ((Mono<?>) result)
					.doOnSuccess(response -> log.info("{}耗时[{}]ms出参:[{}]", baseLog, System.currentTimeMillis() - startTime, formatObject(((Mono<?>) result).block())))
					.doOnError(error -> log.error("{}失败：{}", baseLog, error.getMessage()));
		} else if (result instanceof Flux<?>) {
			StringBuilder sb = new StringBuilder();
			final AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());
			return ((Flux<?>) result)
					.doOnComplete(() -> {
						if (!sb.isEmpty()) {
							Arrays.stream(sb.toString().split("\n")).forEach(i -> log.info("{}出参:[{}]", baseLog, i));
						}
						log.info("{}耗时[{}] ms", baseLog, System.currentTimeMillis() - startTime);
					})
					.doOnNext(subscription -> {
						if (System.currentTimeMillis() - lastTime.get() > consumeTime) {
							Arrays.stream(sb.toString().split("\n")).forEach(i -> log.info("{}出参:[{}]", baseLog, i));
							lastTime.set(System.currentTimeMillis());
							sb.setLength(0);
						} else {
							sb.append(subscription);
						}
					})
					.doOnError(error -> log.error("{}耗时[{}]ms 失败:[{}]", baseLog, System.currentTimeMillis() - startTime, error.getMessage()));
		} else {
			log.info("{}耗时[{}]ms出参:[{}]", baseLog, System.currentTimeMillis() - startTime, formatObject(result));
			return result;
		}
	}

	private String formatObject(Object obj) {
		if (obj == null) {
			return "";
		}
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("格式化失败：{}", e.getMessage());
		}
		return "";
	}
}
