package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.ReviewDTO;

import jakarta.annotation.Resource;

@Service
public interface ReviewService {

	List<ReviewDTO> getMyReview(String userId);
	List<ReviewDTO> getList();
	List<ReviewDTO> getReviewList(int pagenum, int amount);
	List<ReviewDTO> insertReview();
	void writeReview(String userId, long planId, String review_title, String review_write_box);
	List<Map<String, Object>> getMyPlans(String userId);
	Resource uploadFile(String system_name, String origin_name, MultipartFile file);
	ReviewDTO getReadReview(long reviewId);
	List<ReviewDTO> getReviews(int pagenum);
	void deleteReview(long reviewId);
	
}
