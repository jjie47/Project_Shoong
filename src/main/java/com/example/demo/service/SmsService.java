package com.example.demo.service;

import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface SmsService {
	String sendSms(String phoneNumber);
	boolean verifySms(String phoneNumber, String authCode);
	
}
