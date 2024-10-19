package com.example.demo.domain;

import lombok.Data;

@Data
public class CostDTO {
	private long costId;
	private String content;
	private int expectedCost;
	private int totalCost;
	private String payer;
	private long placeId;
}
