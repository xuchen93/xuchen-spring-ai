package com.github.xuchen93.spring.ai.common.spring.advise;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * appName，端口号，访问根路径输出
 *
 * @author xuchen.wang
 * @date 2025/7/16
 */
@Slf4j
@Component
public class AppWebInfoAwareAdvisor implements ApplicationContextAware, ApplicationListener<ApplicationReadyEvent> {

	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		log.info("【注册】{}", this.getClass().getSimpleName());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@SneakyThrows
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Environment env = applicationContext.getEnvironment();
		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\thttp://localhost:{}{}\n\t" +
						"External: \thttp://{}:{}{}\n" +
						"----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port", "8080"),
				env.getProperty("spring.webflux.base-path", ""),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port", "8080"),
				env.getProperty("spring.webflux.base-path", ""));
	}
}
