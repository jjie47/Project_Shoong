package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.aspect.CoolsmsException;
import com.example.demo.service.SmsService;

import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

@Controller
@RequestMapping("/sms/*")
public class SmsController {
	
	@Autowired
	private SmsService smsService;
	
//	인증번호 발송
	@PostMapping("send")
	@ResponseBody //JSON 형식으로 응답
	public Map<String, String> sendSms(@RequestBody Map<String, String> body, HttpSession session) {
		String phoneNumber = body.get("phoneNumber");
		Map<String, String> response = new HashMap<>();
		try {
			String verificationCode = smsService.sendSms(phoneNumber);
			//인증번호 세션에 저장
			session.setAttribute("verificationCode", verificationCode);
			
			response.put("status", "success");
			response.put("message", "SMS 전송에 성공했습니다.");
		} 
		catch(CoolsmsException e ) {
			response.put("status", "error");
			response.put("message", "SMS 전송에 실패했습니다.");
		}
		return response; 
	}
	
//	인증번호 확인
	@PostMapping("verify")
	@ResponseBody
	public Map<String, String> verifySms(@RequestBody Map<String, String> body, HttpSession session) {
		String inputCode = body.get("authCode");
		Map<String, String> response = new HashMap<>();
		
		//세션에서 인증번호 가져오기
		String veriCode = (String)session.getAttribute("verificationCode");
		
		if(smsService.verifySms(inputCode, veriCode)) {
			response.put("status", "success");
			response.put("message", "SMS 인증에 성공했습니다.");
		}
		else {
			response.put("status", "error");
			response.put("message", "SMS 인증에 실패했습니다.");
		}
		return response;
	}
}
