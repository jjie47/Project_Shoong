package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.CommentDTO;

@Mapper
public interface CommentMapper {

	long getCount(long reviewId);

	void deleteCommentsByReviewId(long reviewId);

	List<CommentDTO> getMyComment(String userId, int startRow);
	
	long getMyCommentTotal(String userId);

	void deleteMycomment(long commentId);

}
