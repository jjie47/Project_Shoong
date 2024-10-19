package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikedPlanMapper {
	int insertLikedPlan(long planId, String userId);
	int deleteLikedPlan(long planId, String userId);
	
	int getLikedCountByPlanId(Long planId);
	int getLikedCheck(Long planId, String userId);
}
