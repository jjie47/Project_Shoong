package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.ReviewDTO;

@Mapper
public interface ReviewMapper {

	List<ReviewDTO> getmyReviewWithStartRow(String userId,int startRow);
	
	long getMyReviewTotal(String userId);

	void deleteMyReview(long reviewId);

	List<ReviewDTO> getTopEight();

	long getAllReviewCnt();

	List<ReviewDTO> getmyReview(String userId);
	List<ReviewDTO> getList();
	List<ReviewDTO> getReviewList();
	int insertReview(ReviewDTO review);
	void writeReview(String userId, long planId, String review_title, String review_write_box);
	ReviewDTO getReadReview(long reviewId);
	List<ReviewDTO> getReviews(int pagenum);
//	ReviewDTO getReviews(int pagenum);
	void deleteReview(long reviewId);
	
	List<ReviewDTO> getReviewByPlanId(Long planId);
}
