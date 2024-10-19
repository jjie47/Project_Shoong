package com.example.demo.domain;

import java.util.List;

import lombok.Data;

@Data
public class LikedPlanDTO {
	private String userId;
	private Long planId; 
	private String nickname;
	private String countryKor;
	private String cityKor;
	private String startDate;
	private String endDate; 
	private int ruleCnt;
	private int placeCnt;
	private List<String> cities;
	private int otherCitiesCnt;
	private String cityKorFirst;
}
