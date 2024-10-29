package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

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
	
	int insertRequest(GroupDTO group);
	
	List<Map<String, Object>> getInvitedListByUserId(String userId);
	
	String getLeaderByPlanId(long planId);
	
	int updateMember(long planId, String userId);
	
	int deleteMember(long planId, String userId);
	
	List<String> getMembersId(long planId);
}
