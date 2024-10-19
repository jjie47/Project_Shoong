package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.LikedPlanDTO;
import com.example.demo.domain.LikedReviewDTO;
import com.example.demo.domain.PhotoDTO;

@Mapper
public interface LikedMapper {
	List<LikedPlanDTO> getLikedPlans(String userId);
	void deleteLikedPlanByUserIdAndPlanId(String userId, Long planId);
	List<String> getCitiesByPlanId(Long planId);
	boolean existByUserIdAndPlanId(String userId, Long planId);
	
	List<LikedReviewDTO> getLikedReivews(String userId);
	boolean existByUserIdAndReviewId(String userId, Long reviewId);
	void deleteLikedReviewByUserIdAndReviewId(String userId, Long reviewId);
	List<PhotoDTO> getPhotosByReviewId(Long reviewId);
}
