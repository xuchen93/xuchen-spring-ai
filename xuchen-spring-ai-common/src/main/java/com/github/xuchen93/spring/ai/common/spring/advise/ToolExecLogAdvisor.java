package com.github.xuchen93.spring.ai.common.spring.advise;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author xuchen.wang
 * @date 2025/7/17
 */
@Aspect
@Slf4j
@Component
public class ToolExecLogAdvisor {

	private ObjectMapper objectMapper = new ObjectMapper();

	@PostConstruct
	public void init() {
		log.info("【注册】tool执行日志切面：{}", this.getClass().getSimpleName());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	// 条件2：拦截带有@Tool注解的方法
	@Pointcut("@annotation(org.springframework.ai.tool.annotation.Tool)")
	public void hasToolAnnotation() {
	}

	@Around("hasToolAnnotation()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		log.info("【tool】【{}】 方法 入参：{}", joinPoint.getSignature().getName(), JSONUtil.toJsonStr(args));
		Object result = joinPoint.proceed();
		log.info("【tool】【{}】 方法 返回：{}", joinPoint.getSignature().getName(), JSONUtil.toJsonStr(result));
		return result;
	}
}
