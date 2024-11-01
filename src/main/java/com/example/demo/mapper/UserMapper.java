package com.example.demo.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.PointDTO;
import com.example.demo.domain.UserDTO;

@Mapper
public interface UserMapper {
	int insertUser(UserDTO user);
	int deleteUser(String userId);
	UserDTO getUserByUserId(String userId);
	int updateUser(UserDTO user);
	
	long getAllUserCnt();
	List<UserDTO> getList(String keyword);
	String getNicknameByUserId(String userId);
	
	List<UserDTO> getUserByPlanId(Long planId);
	String getLeaderNickByPlanId(Long planId);
	
	int insertSocialUser(UserDTO user);
	
	List<PointDTO> getPointByUserId(String userId);
}
