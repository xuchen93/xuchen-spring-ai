package com.github.xuchen93.spring.ai.controller;

import com.github.xuchen93.spring.ai.controller.base.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class ToolControllerTest extends BaseControllerTest {


	@Test
	public void getNextHour() {
		get(Map.of(), "/tool/time/getNextHour");
	}

}
