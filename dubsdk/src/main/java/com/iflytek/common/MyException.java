package com.iflytek.common;

public class MyException extends Exception{
	public MyException(String className, String method, String kind) {
		// TODO Auto-generated constructor stub
		super(className + " " + method + " " + kind);
	}
}
