package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.GroupMemberDTO;
import com.example.demo.mapper.DestinationMapper;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.UserMapper;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	GroupMapper gmapper;
	
	@Autowired
	UserMapper umapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<GroupMemberDTO> getList(long planId) {
		List<GroupMemberDTO> list = gmapper.getMemberListByPlanId(planId);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getInviteList(String userId) {
		GroupMapper gMapper = sqlSession.getMapper(GroupMapper.class);
		List<Map<String, Object>> list = gMapper.getInvitedListByUserId(userId);
		if(list!=null) {
			for(Map<String, Object> request: list) {
				long planId = (long)request.get("plan_id");
				String leader = gmapper.getLeaderByPlanId(planId);
				if(leader!=null) {
					request.put("leader", leader);
				}
			}
		}
		return list;
	}
	
	@Override
	public int request(long planId, String userId) {
		GroupDTO group = new GroupDTO();
		group.setPlanId(planId);
		group.setUserId(userId);
		group.setRule("요청됨");
		if(gmapper.insertGroup(group)==1) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public int addMember(long planId, String userId) {
		if(gmapper.updateMember(planId, userId)==1) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public int removeMember(long planId, String userId) {
		if(gmapper.deleteMember(planId, userId)==1) {
			return 1;
		}
		return 0;
	}

	@Override
	public List<String> getMembersId(long planId) {
		GroupMapper gMapper = sqlSession.getMapper(GroupMapper.class);
		List<String> membersId = gMapper.getMembersId(planId);
		return membersId;
	}
}
