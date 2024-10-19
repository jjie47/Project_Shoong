package com.example.demo.domain;

import lombok.Data;

@Data
public class ReviewDTO {
	private long reviewId;
	private String title;
	private String content;
	private String createdDate;
	private String updatedDate;
	private int likeCount;
	private int viewCount;
	private String userId;
	private long planId;
	
	private long commentCount;
}