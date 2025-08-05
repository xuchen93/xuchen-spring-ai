package com.github.xuchen93.spring.ai.controller;

import com.github.xuchen93.spring.ai.controller.base.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class ChatControllerTest extends BaseControllerTest {


	@Test
	public void chat() {
		get(Map.of("msg", "一句话介绍一下天安门"), "/chat/chat");
	}
}
