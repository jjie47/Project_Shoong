package com.example.demo.domain;

import lombok.Data;

@Data
public class DefaultDestinationDTO {
	private int destinationId;
	private String cityKor;
	private String cityEng;
	private String countryKor;
	private String countryEng;
	private String countryCode;
	private String continent;
	private String description;
	private String systemName;
	private String originName;
}
