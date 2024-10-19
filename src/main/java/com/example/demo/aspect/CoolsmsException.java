package com.example.demo.aspect;

public class CoolsmsException extends RuntimeException{
	public CoolsmsException(String message) {
		super(message);
	}
	
	public CoolsmsException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
