package com.example.demo.domain;

import lombok.Data;

@Data
public class UserDTO {
	private String userId;
	private String password;
	private String nickname;
	private String email;
	private String phoneNumber;
	private int point;
	private String systemName;
	private String originName;
}
