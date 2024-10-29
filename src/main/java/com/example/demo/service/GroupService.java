package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.GroupMemberDTO;
import com.example.demo.domain.UserDTO;

public interface GroupService {
	List<GroupMemberDTO> getList(long planId);
	List<Map<String, Object>> getInviteList(String userId);
	int request(long planId, String userId);
	int addMember(long planId, String userId);
	int removeMember(long planId, String userId);
	List<String> getMembersId(long planId);
}
