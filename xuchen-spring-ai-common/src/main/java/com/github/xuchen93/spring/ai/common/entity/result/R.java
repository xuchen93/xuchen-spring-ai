package com.github.xuchen93.spring.ai.common.entity.result;

import lombok.Data;

import java.io.Serializable;


@Data
public class R<T> implements Serializable {

	private int code;

	private String msg;

	private T data;


	public R() {

	}

	private R(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <T> R<T> success() {
		return new R<>(200, "success", null);
	}

	public static <T> R<T> success(T t) {
		return new R<>(200, "success", t);
	}

}
