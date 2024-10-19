package com.example.demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.DestinationDTO;
import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.PlanDTO;
import com.example.demo.domain.ReviewDTO;
import com.example.demo.mapper.DestinationMapper;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.PlanMapper;
import com.example.demo.mapper.ReviewMapper;

import jakarta.annotation.Resource;

@Service
public class ReviewServiceImpl implements ReviewService{
	
	@Mapper
	@Autowired
	private ReviewMapper reviewMapper;
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private PlanMapper planMapper;
	@Autowired
	private DestinationMapper destinationMapper;

	@Override
	public List<ReviewDTO> getMyReview(String userId) {
		List<ReviewDTO> review = reviewMapper.getmyReview(userId);
		return review;
	}
	@Override
	public List<ReviewDTO> getReviewList(int pagenum, int amount){
		List<ReviewDTO> rl = reviewMapper.getReviewList();
		return rl;
	}
	@Override
	public List<ReviewDTO> getList() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ReviewDTO> insertReview() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void writeReview(String userId, long planId, String review_title, String review_write_box) {
		reviewMapper.writeReview(userId, planId, review_title, review_write_box );
		
	}
	@Override
	public List<Map<String, Object>> getMyPlans(String userId) {
		  //반환할 값
	      List<Map<String, Object>> result = new ArrayList<>();
	      
	      //userid 기반으로 그룹 DTO 불러오기
	      List<GroupDTO> gr = groupMapper.getMyGroup(userId);
	      
	      //gr이 빈값일 경우, 계획이 없을 경우 다시 돌아감
	      if (gr.isEmpty()) {
	          return result;
	      }
	      
	      //그룹안에서 planId들 꺼내오기
	      List<Long> planIds = new ArrayList<>();
	      for (GroupDTO gr1 : gr) {
	         planIds.add(gr1.getPlanId());
	      }
	      
	      //첫번째 나라 가져오기
	      List<DestinationDTO> first = new ArrayList<>();
	      for (Long planId : planIds) {
	         DestinationDTO ddt = destinationMapper.getMyfirst(planId);
	         first.add(ddt);
	      }
	     
	      
	      //planid 기준 계획 꺼내오기
	      List<PlanDTO> pld = planMapper.getMyPlanByPlanIds(planIds);
	      
	      //planid 기준 목적지 꺼내오기
	      List<DestinationDTO> dt = destinationMapper.getMyDestinationByPlanIds(planIds);
	      
	      
	      
	      
	     


	       // 각 계획에 대해 해시맵에 데이터 담기
	       for (int i = 0; i < planIds.size(); i++) {
	           Map<String, Object> planInfo = new HashMap<>();
	           planInfo.put("userId",userId);
	           
	           // 목적지 정보
	           DestinationDTO destination = first.get(i);
	           planInfo.put("cityKor", destination.getCityKor());
	           planInfo.put("countryKor", destination.getCountryKor());
	           
	           // 그룹 정보
	           GroupDTO group = gr.get(i);
	           planInfo.put("rule", group.getRule());
	           planInfo.put("planId", group.getPlanId());
	           
//	           // 계획 정보
//	           PlanDTO plan = pld.get(i);
//	           planInfo.put("startDate", plan.getStartDate().substring(0, 10));
//	           planInfo.put("endDate", plan.getEndDate().substring(0, 10));
	           
	           
	        // 계획 정보
	           PlanDTO plan = pld.get(i);
	           String startDateString = plan.getStartDate().substring(0, 10);
	           String endDateString = plan.getEndDate().substring(0, 10);
	           
	           planInfo.put("startDate", startDateString);
	           planInfo.put("endDate", endDateString);
	           
	           result.add(planInfo);
	       }
	       
	       return result;
	}
	
	@Value("${file.dir}")
	private String saveFolder;
	@Override
	public Resource uploadFile(String system_name, String origin_name, MultipartFile file) {
		Path path = Paths.get(saveFolder+system_name);
		Resource resource = null;
		return resource;
	}
	@Override
	public ReviewDTO getReadReview(long reviewId) {
		ReviewDTO rdto = reviewMapper.getReadReview(reviewId);
		System.out.println(rdto);
		return rdto;
	}
	@Override
	public List<ReviewDTO> getReviews(int pagenum) {
		List<ReviewDTO> rpdto = reviewMapper.getReviews(pagenum);
		System.out.println(rpdto);
		return rpdto;
	}
	@Override
	public void deleteReview(long reviewId) {
		reviewMapper.deleteReview(reviewId);
	}
	

	
}
