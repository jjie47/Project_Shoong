package com.example.demo.domain;

import lombok.Data;

@Data
public class GroupMemberDTO {
	private String rule;
	private long planId;
	private String userId;
	private String nickname;
}
