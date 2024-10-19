package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.LikedPlanMapper;

@Service
public class LikedPlanServiceImpl implements LikedPlanService {

	@Autowired
	private LikedPlanMapper likedPlanMapper;
	
	@Override
	public boolean insert(long planId, String userId) {
		return likedPlanMapper.insertLikedPlan(planId, userId) == 1;
	}
	
	@Override
	public boolean delete(long planId, String userId) {
		return likedPlanMapper.deleteLikedPlan(planId, userId) == 1;
	}
	
}
