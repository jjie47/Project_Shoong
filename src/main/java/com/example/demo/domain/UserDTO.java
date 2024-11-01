package com.example.demo.domain;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
	private String userId;
	private String password;
	private String nickname;
	private String email;
	private String phoneNumber;
	private String systemName;
	private String originName;
	private List<PointDTO> points;
	private int totalPoints;
}
