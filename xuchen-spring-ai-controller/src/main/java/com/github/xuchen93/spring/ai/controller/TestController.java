package com.github.xuchen93.spring.ai.controller;

import cn.hutool.core.date.DateUtil;
import com.github.xuchen93.spring.ai.common.entity.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("test")
	public R<String> test() {
		return R.success(DateUtil.now());
	}

}
