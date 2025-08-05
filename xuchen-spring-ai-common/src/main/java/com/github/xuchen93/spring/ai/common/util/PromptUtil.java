package com.github.xuchen93.spring.ai.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

/**
 * @author xuchen.wang
 * @date 2025/7/17
 */
public class PromptUtil {

	public static String defaultPrompt() {
		return "你是一个组手，擅长查询分析数据";
	}

	public static String toolPrompt(String method, String description) {
		return StrUtil.format("你是一个助手，通过 {} 函数回答我 {}", method, description);
	}

	public static String toolPrompt(String method, String description, Map<String, Object> userParams) {
		return StrUtil.format("你是一个助手，通过 {} 函数回答我 {}。以下是用户提供的参数：{}", method, description, JSONUtil.toJsonStr(userParams));
	}

	public static String mcpPrompt(String description, Map<String, Object> userParams) {
		return StrUtil.format("你是一个助手，回答我 {}。以下是用户提供的参数：{}", description, JSONUtil.toJsonStr(userParams));
	}
}
