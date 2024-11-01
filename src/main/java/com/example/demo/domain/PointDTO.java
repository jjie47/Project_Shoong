package com.example.demo.domain;

import lombok.Data;

@Data
public class PointDTO {
	private String pointId;
	private String createdDate;
	private String content;
	private int point;
	private String userId;
}
