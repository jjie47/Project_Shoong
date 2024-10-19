package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.LikedPlanDTO;
import com.example.demo.domain.LikedReviewDTO;

public interface LikedService {
	List<LikedPlanDTO> getLikedPlansList(String userId);
	void deleteLikedPlan(String userId, Long planId);
	List<LikedReviewDTO> getLikedReviewsList(String userId);
	void deleteLikedReview(String userId, Long reviewId);
	
}
