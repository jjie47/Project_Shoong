package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.GroupMemberDTO;

public interface GroupService {
	List<GroupMemberDTO> getList(long planId);
}
