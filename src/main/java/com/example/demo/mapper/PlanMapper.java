package com.example.demo.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.CriteriaJ;
import com.example.demo.domain.PlanDTO;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.PlanDTO;

@Mapper
public interface PlanMapper {
	List<PlanDTO> getUserPlans(String userId);

	List<PlanDTO> getMyPlanByPlanIds(List<Long> planIds);
	
	void deleteMyPlan(long planId);

	List<Long> getAllPlan();

	long getAllPlanCnt();
	
	int insert(PlanDTO plan);

	int delete(PlanDTO plan);
	
	int insertPlan(PlanDTO plan);
	
	int updatePlanDate(PlanDTO plan);
	
	List<PlanDTO> getPlans(CriteriaJ criJ); //전체 계획 불러오기
	List<PlanDTO> getSharedPlans(CriteriaJ criJ); //공유된 계획 불러오기
	long getTotalCount(CriteriaJ criJ); //공유된 계획 총 갯수
	int getDaysCountByPlanId(Long planId); //여행일수
	
	PlanDTO getPlanByPlanId(long planId); //계획 하나 불러오기
}
