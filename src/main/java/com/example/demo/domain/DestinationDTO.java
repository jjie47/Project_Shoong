package com.example.demo.domain;

import lombok.Data;

@Data
public class DestinationDTO {
	private long destinationId;
	private String cityKor;
	private String cityEng;
	private String countryKor;
	private String countryEng;
	private String countryCode;
	private String continent;
	private long planId;
}
