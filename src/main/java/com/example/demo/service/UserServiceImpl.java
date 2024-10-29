package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.CommentDTO;
import com.example.demo.domain.DestinationDTO;
import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.GroupMemberDTO;
import com.example.demo.domain.PlanDTO;
import com.example.demo.domain.ReviewDTO;
import com.example.demo.domain.UserDTO;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.DestinationMapper;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.PlaceMapper;
import com.example.demo.mapper.PlanMapper;
import com.example.demo.mapper.ReviewMapper;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.UserDTO;
import com.example.demo.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ReviewMapper reviewMapper;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private PlanMapper planMapper;
	@Autowired
	private DestinationMapper destinationMapper;
	@Autowired
	private PlaceMapper placeMapper;

	@Value("${file.dir}")
	private String saveFolder;

	private static final String DEFAULT_FROFILE_IMAGE_PATH = "/images/default_profile.png";
	
	@Override
	public boolean join(UserDTO user) {
		return userMapper.insertUser(user) == 1;
	}

	@Override
	public boolean login(String userId, String password) {
		UserDTO user = userMapper.getUserByUserId(userId);
		if(user != null) {
			if(user.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkId(String userId) {
		UserDTO user = userMapper.getUserByUserId(userId);
		return user == null;
	}
	
	@Override
	public UserDTO getUserByUserId(String userId) {	
		UserDTO user = userMapper.getUserByUserId(userId);

		String fullPath = "";
		String systemName = user.getSystemName();
		
		if("default_profile.png".equals(systemName)) {
			fullPath = DEFAULT_FROFILE_IMAGE_PATH;
		}
		else {
			File file = new File(saveFolder, systemName);
			if(file.exists()) {
				String filename = file.getName();
				fullPath = "/file/" + filename;
			}
		}
		user.setSystemName(fullPath);
		
		return user;
	}
	
	@Override
	public boolean deleteUser(String userId) {
		UserDTO user = userMapper.getUserByUserId(userId);		
		if(user != null) {
			return userMapper.deleteUser(userId) == 1;
		}
		return false;
	}
	

	//DB에서 userDTO로 데이터를 받아와서 
	//현재 경로의 파일과 일치하는지 확인 후 
	//경로의 파일 삭제 후 새로운 파일 저장 로직이 필요해보임
	//즉 수정 시에 pc folder에 기존 이미지 파일 삭제하기 
	@Override
	public boolean updateUser(UserDTO user, MultipartFile profileImage) {
		
		//파일 저장 로직
		if(profileImage != null && !profileImage.isEmpty()) {
			String systemName = saveProfileImage(profileImage);
			user.setSystemName(systemName);
			user.setOriginName(profileImage.getOriginalFilename());
		}
		return userMapper.updateUser(user) == 1;
	}

	//사진 저장 메서드
	private String saveProfileImage(MultipartFile profileImage) {
		String originName = profileImage.getOriginalFilename();
		int lastIdx = originName.lastIndexOf(".");
		String ext = originName.substring(lastIdx); //.png
		LocalDateTime now = LocalDateTime.now();
		String time = now.format(DateTimeFormatter.ofPattern("yyyMMddHHmmss"));
		String systemName = time + UUID.randomUUID().toString()+ext;
		
		//실제 생성될 파일 경로
		String path = saveFolder + systemName;
		
		//실제 파일 저장
		try {
			profileImage.transferTo(new File(path));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return systemName;
	}
	
	//my 리뷰 불러오기
		public List<List<Object>> getMyReview(String userId,Integer pagenum) {
			int startRow = (pagenum-1)*10;
			List<List<Object>> rvcom = new ArrayList<>();
			List<ReviewDTO> rv = reviewMapper.getmyReviewWithStartRow(userId,startRow);

			List<Long> count = new ArrayList<>();
			for (ReviewDTO rv1 : rv) {
				long cnt = commentMapper.getCount(rv1.getReviewId());
				count.add(cnt);
			}
			
			rvcom.add(new ArrayList<Object>(rv)); 
		    rvcom.add(new ArrayList<Object>(count));
							
			return rvcom;
		}
		
		@Override
		public long getMyReviewTotal(String userId) {
			return reviewMapper.getMyReviewTotal(userId);
		}
		
		//my 리뷰 삭제하기
		@Override
		public void deleteMyReview(long reviewId) {
			reviewMapper.deleteMyReview(reviewId);
		}
		
		//my 커멘트 불러오기
		@Override
		public List<CommentDTO> getMyComment(String userId, Integer pagenum){
			int startRow = (pagenum-1)*10;
			List<CommentDTO> cm = commentMapper.getMyComment(userId, startRow);
			return cm;
		}
		
		@Override
		public long getMyCommentTotal(String userId) {
			return commentMapper.getMyCommentTotal(userId);
		}

		//my 커멘트 삭제하기
		@Override
		public void deleteMycomment(long commentId) {
			commentMapper.deleteMycomment(commentId);
		};
		
		//내 계획 불러오기
		@Override
		public List<Map<String, Object>> getMyPlan(String userId) {
			
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
			
			//planid 기준 사람 수 세기
			List<Long> pcnt = groupMapper.getGroupCount(planIds);
			
			//planid 기준 계획 꺼내오기
			List<PlanDTO> pld = planMapper.getMyPlanByPlanIds(planIds);
			
			//planid 기준 목적지 꺼내오기
			List<DestinationDTO> dt = destinationMapper.getMyDestinationByPlanIds(planIds);
			
			//목적지 개수 꺼내오기
			List<Long> dtc = destinationMapper.getMyDestCountByPlanIds(planIds);
			
			//destId로 순회 돌면서 받아오기
			List<Long> destIds = new ArrayList<>();
			for (DestinationDTO dt1 : dt) {
				destIds.add(dt1.getDestinationId());
			}
			
			//planid와 destid로 장소 수 꺼내오기
			List<Long> pc = placeMapper.getMyPlaceByDestIds(destIds, planIds);
			
			
			System.out.println(planIds);
		    // 각 계획에 대해 해시맵에 데이터 담기
		    for (int i = 0; i < planIds.size(); i++) {
		        Map<String, Object> planInfo = new HashMap<>();
		        
		        planInfo.put("userId",userId);
		        
		        // 목적지 정보
		        DestinationDTO destination = first.get(i);
		        if (first.isEmpty()) {
				    return result;
				}
		        System.out.println("log: "+destination);
		        planInfo.put("cityKor", destination.getCityKor());
		        planInfo.put("countryKor", destination.getCountryKor());
		        
		        // 그룹 정보
		        GroupDTO group = gr.get(i);
		        planInfo.put("rule", group.getRule());
		        planInfo.put("planId", group.getPlanId());
		        
//		        // 계획 정보
//		        PlanDTO plan = pld.get(i);
//		        planInfo.put("startDate", plan.getStartDate().substring(0, 10));
//		        planInfo.put("endDate", plan.getEndDate().substring(0, 10));
		        
		        
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
		        
		        
		        // 장소 수 및 인원 수
		        planInfo.put("placeCount", pc.get(i));
		        planInfo.put("personCount", pcnt.get(i));
		        planInfo.put("destCount", dtc.get(i));
		        
		        result.add(planInfo);
		    }
		    
		    return result;		
		}
		
		@Override
		public void exitMyPlan(String userId, long planId) {
			groupMapper.exitMyPlan(userId,planId);
		};
		
		
		@Override
		public void deleteMyPlan(long planId) {
			planMapper.deleteMyPlan(planId);
		};
	
	@Override
	public List<UserDTO> getUsersByKeyword(String keyword, long planId) {
		List<UserDTO> ulist = userMapper.getList(keyword);
		List<GroupDTO> glist = groupMapper.getGroupByPlanId(planId);
		List<String> gUserList = new ArrayList<>();
		for(GroupDTO group : glist) {
			gUserList.add(group.getUserId());
		}
		
		List<UserDTO> list = ulist.stream().filter(u ->
			!gUserList.contains(u.getUserId())
		).collect(Collectors.toList());
		
		return list;
	}
}
