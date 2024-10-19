package com.example.demo.service;

public interface LikedPlanService {
	boolean insert(long planId, String userId);
	boolean delete(long planId, String userId);
}
