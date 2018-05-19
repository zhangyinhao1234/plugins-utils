package com.binpo.plugin.hibernate.query.page.generic;

import java.io.PrintStream;

public class CanotRemoveObjectException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public void printStackTrace() {
		System.out.println("删除对象错误!");
		super.printStackTrace();
	}
}