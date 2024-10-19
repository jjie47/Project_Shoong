package com.example.demo.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.domain.DestinationDTO;
import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.PhotoDTO;
import com.example.demo.domain.PlanDTO;
import com.example.demo.domain.ReviewDTO;
import com.example.demo.mapper.DestinationMapper;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.LikedMapper;
import com.example.demo.mapper.PlaceMapper;
import com.example.demo.mapper.PlanMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.mapper.UserMapper;

@Service
public class MainServiceImpl implements MainService{
	
	@Autowired
	private DestinationMapper destinationMapper;
	@Autowired
	private PlanMapper planMapper;
	@Autowired
	private PlaceMapper placeMapper;
	@Autowired
	private ReviewMapper reviewMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private LikedMapper likedMapper;
	
	@Value("${file.dir}")
	private String saveFolder;

	private static final String DEFAULT_MAIN_IMAGE_PATH = "/images/sample_img1.png";
	
	@Override
	public List<Map<String, Object>> getMain() {
		//반환할 값
				List<Map<String, Object>> result = new ArrayList<>();
				
				//조회수 순으로 리뷰 긁어오기
				List<ReviewDTO> rdt = reviewMapper.getTopEight();
				
				//리뷰안에서 planId들 꺼내오기
				List<Long> planIds = new ArrayList<>();
				for (ReviewDTO rdt1 : rdt) {
					planIds.add(rdt1.getPlanId());
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
				
				//유저 수 꺼내오기
				long auc = userMapper.getAllUserCnt();
				//리뷰 수 꺼내오기
				long arc = reviewMapper.getAllReviewCnt();
				//계획 수 꺼내오기
				long apc = planMapper.getAllPlanCnt();
				

			    // 각 계획에 대해 해시맵에 데이터 담기
			    for (int i = 0; i < planIds.size(); i++) {
			        Map<String, Object> planInfo = new HashMap<>();
			        
			        planInfo.put("userCount",auc);
			        planInfo.put("reviewCount",arc);
			        planInfo.put("planCount",apc);
			        
			        // 목적지 정보
			        DestinationDTO destination = first.get(i);
			        planInfo.put("cityKor", destination.getCityKor());
			        planInfo.put("countryKor", destination.getCountryKor());
			        
			        // 리뷰 정보
			        ReviewDTO revi = rdt.get(i);
			        planInfo.put("reviewId", revi.getReviewId());
			        			        
			        // 계획 정보
			        PlanDTO plan = pld.get(i);
			        String startDateString = plan.getStartDate().substring(0, 10);
			        String endDateString = plan.getEndDate().substring(0, 10);
			        
			        planInfo.put("startDate", startDateString);
			        planInfo.put("endDate", endDateString);
			        
			     // 날짜 계산
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			        LocalDate startDate = LocalDate.parse(startDateString, formatter);
			        LocalDate endDate = LocalDate.parse(endDateString, formatter);
			        
			        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
			        int nights = (int) daysBetween; // 몇 박
			        int days = nights + 1; // 몇 일 (체류일은 1일 추가)

			        planInfo.put("nights", nights);
			        planInfo.put("days", days);
			        
			        //reviewid 기준 사진 꺼내오기
			        String fullPath = DEFAULT_MAIN_IMAGE_PATH;
			        
			        List<PhotoDTO> photos = likedMapper.getPhotosByReviewId(revi.getReviewId());
			        
			        if(photos != null && !photos.isEmpty()) {
			        	PhotoDTO firstPhoto = photos.get(0);
			        	String systemName = firstPhoto.getSystemName();

			        	File file = new File(saveFolder, systemName);
			        	if(file.exists()) {
			        		String filename = file.getName();
			        		fullPath = "/file/" + filename;
			        	}
			        }
			        planInfo.put("reviewImage", fullPath);

			        result.add(planInfo);
			    }
			    return result;
	}

}
