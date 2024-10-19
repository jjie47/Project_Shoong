package com.example.demo.domain;

import java.util.List;

import lombok.Data;

@Data
public class LikedReviewDTO {
	private String userId;
	private Long reviewId;
	private Long planId;
	private String title;
	private String content;
	private String nickname;
	private String createdDate;
	private String updatedDate;
	private int likeCount;
	private int viewCount;
	private String backgroundImage;
}
