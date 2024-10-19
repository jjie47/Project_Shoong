package com.example.demo.service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.aspect.CoolsmsException;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
public class SmsServiceImpl implements SmsService{

	@Value("${coolsms.api.key}")
	private String apiKey;
	
	@Value("${coolsms.api.secret}")
	private String apiSecret;

	@Value("${petharmony.phone.number}")
	private String fromPhoneNumber;
	
//	sms 전송하기 위한 서비스 객체
	private DefaultMessageService messageService;
	
	@PostConstruct
	public void init() {
		this.messageService = NurigoApp.INSTANCE.initialize(
				apiKey, 
				apiSecret, 
				"https://api.coolsms.co.kr"
		);
	}

	@Override
	public String sendSms(String to) {
		// 랜덤한 4자리 인증번호
		String verificationCode = generateRandomNumber();
		
		// coolSMS API 객체 생성
		Message message = new Message();
		message.setFrom(fromPhoneNumber);
		message.setTo(to);
		message.setText("[Shoong]인증번호는 [" + verificationCode + "] 입니다.");
				
		// 메세지 전송
		SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
		return verificationCode;
	}

	// 인증번호 난수 생성 메서드
	private String generateRandomNumber() {
		Random rd = new Random();
		StringBuilder numStr = new StringBuilder();
		for(int i = 0; i < 4; i++) {
			numStr.append(rd.nextInt(10)); // 0~9사이 숫자
		}
		return numStr.toString();
	}

	@Override
	public boolean verifySms(String inputCode, String veriCode) {
		if(inputCode != null && veriCode.equals(inputCode)) {
			return true;
		}
		return false;
	}
}
