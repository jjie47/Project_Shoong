package com.example.demo.domain;

import lombok.Data;

@Data
public class PlanDTO {
	private long planId;
	private int isShared;
	private String startDate;
	private String endDate;
}
