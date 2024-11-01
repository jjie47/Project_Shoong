package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.domain.UserDTO;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kotlinx.serialization.json.JsonObject;

@Controller
@RequestMapping("/oauth2/*")
public class OauthController {
	
	@Value("${kakao.client.id}")
	private String kakaoClientId;
	
	@Value("${kakao.redirect.uri}")
	private String kakaoRedirectUri;
	
	@Value("${google.client.id}")
	private String googleClientId;
	
	@Value("${google.redirect.uri}")
	private String googleRedirectUri;
	
	@Value("${google.client.secret}")
	private String googleClientSecret;
	
	@Value("${naver.client.id}")
	private String naverClientId;
	
	@Value("${naver.redirect.uri}")
	private String naverRedirectUri;
	
	@Value("${naver.client.secret}")
	private String naverClientSecret;

	@Autowired
	private UserService userService;

//	인가코드 요청
	@GetMapping("kakao")
	public String kakaoLogin() {
		String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoClientId + 
				"&redirect_uri=" + kakaoRedirectUri + 
				"&response_type=code";
		
		return "redirect:" + kakaoLoginUrl;
	}
	
//	인가코드 수신/ 엑세스 토큰 요청
	@GetMapping("kakao/callback")
	public String callback(@RequestParam("code") String code, HttpServletRequest req) {
		//1. 인가코드를 기반으로 엑세스 토큰 발급
		String accessToken = generateToken(code);
		
		//2. 엑세스 토큰 기반으로 사용자 정보 조회
		JsonNode user = findUserInfo(accessToken);
		
		//3. 사용자 정보 추출
		String userId = user.path("id").asText();
		String nickname = user.path("properties").path("nickname").asText();
		String systemName = user.path("properties").path("profile_image").asText();

		//4. 만약 처음 로그인한다면 DB에 사용자 정보 저장
		UserDTO userDTO = userService.getSocialUserByUserId(userId);
		
		if(userDTO == null) {
			userDTO = new UserDTO();
			userDTO.setUserId(userId);
			userDTO.setNickname(nickname);
			userDTO.setSystemName(systemName);
			
			userService.socialJoin(userDTO);
		}
		
		//5. 세션에 사용자 정보 저장
		HttpSession session = req.getSession();
		session.setAttribute("loginUser", userId);
		
		return "redirect:/";
	}

	
//	인가코드를 통해 엑세스 토큰 발급 메서드
	private String generateToken(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		params.add("grant_type", "authorization_code"); 
		params.add("client_id", kakaoClientId);
		params.add("redirect_uri", kakaoRedirectUri);
		params.add("code", code);
		
		//WebClient 이용하여 토큰 발급
		JsonNode response = WebClient.builder()
				.baseUrl("https://kauth.kakao.com/oauth/token")
				.build()
				.post()
				.header("Content-type", "application/x-www-form-urlencoded;charset-utf-8")
				.body(BodyInserters.fromFormData(params)) //MultiValueMap 형식의 폼 데이터를 사용하여 전송할 수 있도록 함
				.retrieve() //응답처리
				.bodyToMono(JsonNode.class)
				.block();
		
		return response.path("access_token").asText();
	}
	
//	엑세스토큰을 통해 회원 정보 조회 메서드
	private JsonNode findUserInfo(String accessToken) {
		return WebClient.builder()
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build()
                .post()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
	}

//	google
	@GetMapping("google")
	public String googleLogin() {
		String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId + 
				"&redirect_uri=" + googleRedirectUri +
				"&response_type=code" +
				"&scope=email profile";
		
		return "redirect:" + googleLoginUrl;
	}
	
	@GetMapping("google/callback")
	public String googleCallback(@RequestParam("code") String code, HttpServletRequest req) {
		String accessToken = generateGoogleToken(code);
		
		JsonNode user = findUserInfoOfGoogle(accessToken);
		
		String userId = user.path("id").asText();
		String nickname = user.path("name").asText();
		String email = user.path("email").asText();
		String systemName = user.path("picture").asText();
		
		UserDTO userDTO = userService.getSocialUserByUserId(userId);
		
		if(userDTO == null) {
			userDTO = new UserDTO();
			userDTO.setUserId(userId);
			userDTO.setNickname(nickname);
			userDTO.setEmail(email);
			userDTO.setSystemName(systemName);
			
			userService.socialJoin(userDTO);
		}
		
		HttpSession session = req.getSession();
		session.setAttribute("loginUser", userId);
		
		return "redirect:/";
	}
	
	private String generateGoogleToken(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		params.add("grant_type", "authorization_code"); 
		params.add("client_id", googleClientId);
		params.add("client_secret", googleClientSecret);
		params.add("redirect_uri", googleRedirectUri);
		params.add("code", code);	
		
		JsonNode response = WebClient.builder()
				.baseUrl("https://oauth2.googleapis.com/token")
				.build()
				.post()
				.header("Content-type", "application/x-www-form-urlencoded;charset-utf-8")
				.body(BodyInserters.fromFormData(params)) 
				.retrieve() 
				.bodyToMono(JsonNode.class)
				.block();
		
		return response.path("access_token").asText();
	}
	
	private JsonNode findUserInfoOfGoogle(String accessToken) {
		return WebClient.builder()
                .baseUrl("https://www.googleapis.com/userinfo/v2/me")
                .build()
                .get()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
	}
	
//	네이버
	@GetMapping("naver")
	public String naverLogin() {
		String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?client_id=" + naverClientId +
				"&redirect_uri=" + naverRedirectUri +
				"&response_type=code";
		
		return "redirect:" + naverLoginUrl;
	}
	
	@GetMapping("naver/callback")
	public String naverCallback(@RequestParam("code") String code, HttpServletRequest req) {
		String accessToken = generateNaverToken(code);
		
		JsonNode user = findUserInfoOfNaver(accessToken);

		String userId = user.path("response").path("id").asText();
		String nickname = user.path("response").path("nickname").asText();
		String email = user.path("response").path("email").asText();
		String systemName = user.path("response").path("profile_image").asText();
		String phoneNumber = user.path("response").path("mobile").asText();
		
		UserDTO userDTO = userService.getSocialUserByUserId(userId);
		
		if(userDTO == null) {
			userDTO = new UserDTO();
			userDTO.setUserId(userId);
			userDTO.setNickname(nickname);
			userDTO.setEmail(email);
			userDTO.setSystemName(systemName);
			userDTO.setPhoneNumber(phoneNumber);
			
			userService.socialJoin(userDTO);
		}
		
		HttpSession session = req.getSession();
		session.setAttribute("loginUser", userId);
		
		return "redirect:/";
	}

	private String generateNaverToken(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		params.add("grant_type", "authorization_code"); 
		params.add("client_id", naverClientId);
		params.add("client_secret", naverClientSecret);
		params.add("redirect_uri", naverRedirectUri);
		params.add("code", code);	
		
		JsonNode response = WebClient.builder()
				.baseUrl("https://nid.naver.com/oauth2.0/token")
				.build()
				.post()
				.header("Content-type", "application/x-www-form-urlencoded;charset-utf-8")
				.body(BodyInserters.fromFormData(params)) 
				.retrieve() 
				.bodyToMono(JsonNode.class)
				.block();
		
		return response.path("access_token").asText();
	}
	
	private JsonNode findUserInfoOfNaver(String accessToken) {
		return WebClient.builder()
                .baseUrl("https://openapi.naver.com/v1/nid/me")
                .build()
                .get()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
	}
}

