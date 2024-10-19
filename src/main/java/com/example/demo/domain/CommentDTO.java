package com.example.demo.domain;

import lombok.Data;

@Data
public class CommentDTO {
	private long commentId;
	private String content;
	private String createdDate;
	private String updatedDate;
	private int likeCount;
	private long reviewId;
	private String userId;
}
