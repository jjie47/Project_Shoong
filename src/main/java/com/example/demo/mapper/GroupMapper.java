package com.example.demo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.GroupDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.GroupMemberDTO;

@Mapper
public interface GroupMapper {

	List<GroupDTO> getMyGroup(String userId);

	List<Long> getGroupCount(List<Long> planIds);

	void exitMyPlan(String userId, long planId);
	
	int insertGroup(GroupDTO group);
	List<GroupMemberDTO> getMemberListByPlanId(long planId);
	
	List<GroupDTO> getGroupByPlanId(Long planId);
}
