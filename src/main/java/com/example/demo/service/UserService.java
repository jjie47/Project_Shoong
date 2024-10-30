package com.example.demo.service;

import java.util.List;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.CommentDTO;
import com.example.demo.domain.UserDTO;
import com.example.demo.domain.UserDTO;

public interface UserService {
	boolean join(UserDTO user);
	boolean login(String userId, String password);
	boolean checkId(String userId);
	UserDTO getUserByUserId(String userId);
	boolean deleteUser(String userId);
	boolean updateUser(UserDTO user, MultipartFile profileImage);
	
	List<List<Object>> getMyReview(String userId,Integer pagenum);
	long getMyReviewTotal(String userId);	
	void deleteMyReview(long reviewId);
	List<CommentDTO> getMyComment(String userId, Integer pagenum);
	long getMyCommentTotal(String userId);
	void deleteMycomment(long commentId);
	List<Map<String, Object>> getMyPlan(String userId);
	void deleteMyPlan(long planId);
	void exitMyPlan(String userId, long planId);

	List<UserDTO> getUsersByKeyword(String keyword, long planId);
	
	void socialJoin(UserDTO userDTO);
	UserDTO getSocialUserByUserId(String userId);

}
 