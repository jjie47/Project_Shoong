package com.example.demo.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.CostDTO;
import com.example.demo.domain.CriteriaJ;
import com.example.demo.domain.DestinationDTO;
import com.example.demo.domain.GroupDTO;
import com.example.demo.domain.PlaceDTO;
import com.example.demo.domain.PlanDTO;
import com.example.demo.domain.PlanDetailsDTO;
import com.example.demo.domain.ReviewDTO;
import com.example.demo.domain.UserDTO;
import com.example.demo.mapper.CostMapper;
import com.example.demo.mapper.DestinationMapper;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.LikedPlanMapper;
import com.example.demo.mapper.PlaceMapper;
import com.example.demo.mapper.PlanMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlanServiceImpl implements PlanService {
	
	@Autowired
	private PlanMapper pmapper;
	@Autowired
	private GroupMapper gmapper;
	@Autowired
	private DestinationMapper dmapper; 
	@Autowired
	private PlaceMapper pcmapper;
	@Autowired
	private CostMapper cmapper;
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public long create(String userId) {
		PlanDTO plan = new PlanDTO();
		if(pmapper.insert(plan)==1) {
			long planId = plan.getPlanId();
			GroupDTO group = new GroupDTO();
			group.setPlanId(planId);
			group.setUserId(userId);
			group.setRule("그룹장");
			if(gmapper.insertGroup(group)==1) {
				return planId;
			}
		}
		return -1;
//		if(pmapper.delete(plan)==1) {
//			System.out.println("계획 생성 실패 : 계획 삭제 완료");
//			return -1;
//		}
//		System.out.println("계획 생성 실패 : 계획 삭제 실패");
//		return -2;
	}
	
	@Override
	public long regist(long planId,
			Map<String, Object> selectedDefaultDestinations,
			List<String> selectedDestinations,
			Map<String, String> selectedDates,
			Map<String, Object> selectedPlaces,
			Map<String, Object> itineraries,
			Map<String, Object> costs,
			String userId) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		PlanDTO plan = new PlanDTO();
		
		
		// 계획 날짜 업데이트
		plan.setPlanId(planId);
		plan.setStartDate(selectedDates.get("startDate"));
		plan.setEndDate(selectedDates.get("endDate"));
		if(pmapper.updatePlanDate(plan)==1) {
			System.out.println("Service : 계획 날짜 수정 성공");
			
			// 목적지 추가
			Set<DestinationDTO> destinations = new HashSet<>();
			for(Map.Entry<String, Object> entry : selectedPlaces.entrySet()) {
				Map<String, Object> map = (Map<String, Object>)entry.getValue();
		
				DestinationDTO destination = new DestinationDTO();
				
				destination.setCityKor((String)map.get("city"));
				destination.setCountryKor((String)map.get("country"));
				destination.setCountryCode((String)map.get("countryCode"));
				destination.setContinent((String)map.get("continent"));
				destinations.add(destination);
			}
			if(destinations.size()>0) {
				if(dmapper.insertDestination(destinations, planId)>0) {
					System.out.println("Service : 목적지 추가 성공");
					
//					Map<String, Long> destinationIds = new HashMap<>();
//					for(DestinationDTO destination : destinations) {
//						destinationIds.put(destination.getCityKor(), destination.getDestinationId());
//						System.out.println(destination);
//					}
					
					DestinationMapper dMapper = sqlSession.getMapper(DestinationMapper.class);
					List<Map<String, Object>> destinationsByPlanId = dMapper.getDestinationsByPlanId(planId);
					Map<String, Long> destinationIds = new HashMap<>();
					Map<String, Long> placeIds = new HashMap<>();
					
					System.out.println(destinationsByPlanId);
					
					// destination Id 가져와서 모으기
					for(Map<String, Object> map : destinationsByPlanId) {
						String city = (String)map.get("city_kor");
						Object obj = map.get("destination_id");
						if(obj instanceof Long) {
							System.out.println("이거 Long 맞대");
							Long destinationId = (Long)obj;
							destinationIds.put(city, destinationId);
						}
						else {
							System.out.println("obj는 Long 타입이 아니래");
						}
					}
					
					Set<PlaceDTO> places = new HashSet<>();
					for(Map.Entry<String, Object> entry : itineraries.entrySet()) {
						Map<String, Object> map = (Map<String, Object>)entry.getValue();
						String placeId = (String)map.get("placeId");
						Map<String, Object> selectedPlace = (Map<String, Object>)selectedPlaces.get(placeId);
						
						PlaceDTO place = new PlaceDTO();
						
						String date = (String)map.get("date");
						String startTime = date + " " + (String)map.get("startTime");
						String endTime = date + " " + (String)map.get("endTime");
						
						place.setMarkerId((String)map.get("placeId"));
						place.setName((String)selectedPlace.get("name"));
						place.setAddr((String)selectedPlace.get("addr"));
						place.setLatitude(Double.parseDouble((String)selectedPlace.get("latitude")));  
						place.setLongitude(Double.parseDouble((String)selectedPlace.get("longitude"))); 
						place.setStartTime(startTime);
						place.setEndTime(endTime);
						place.setType((String)selectedPlace.get("type"));
						place.setSystemName((String)selectedPlace.get("photo"));
//						place.setDestinationId(destinationIds.get((String)map.get("city")));
						
						String city = (String)selectedPlace.get("city");
						System.out.println(city);
						Long destinationId = destinationIds.get(city);
						if(destinationId != null) {	
							place.setDestinationId(destinationId); 
						} else {
							System.out.println("destinationId를 찾을 수 없더. 현재 도시는 " + city);
						}
						
						
						if(pcmapper.insertPlaces(place)==1) {
							long pcId = place.getPlaceId();
							System.out.println("Service : 장소 추가 - placeId : "+pcId);
							
//							List<CostDTO> costList = new ArrayList<>();
//							
//							String key = (String)entry.getKey();
//							
//							for(Map.Entry<String, Object> centry : costs.entrySet()) {
//								Map<String, Object> cmap = (Map<String, Object>)centry.getValue();
//							
//								String itineraryId = (String)cmap.get("itineraryId");
//								
//								if(key.equals(itineraryId)) {
//									
//									CostDTO cost = new CostDTO();
//									cost.setContent((String)cmap.get("content"));
//									cost.setExpectedCost(Integer.parseInt((String)cmap.get("amount")));
//									cost.setPayer((String)cmap.get("payer"));
//									cost.setPlaceId(pcId);
//									
//									costList.add(cost);
//								}
//							}
//							if(cmapper.insertCostByPlaceId(costList)>0) {
//								System.out.println("Service : 비용 추가 - placeId : "+pcId);
//								
//								
//							} else {
//								System.err.println("Service 에러 : 비용 추가 실패 - placeId : "+pcId);
//							}
							System.out.println("Service : 계획 등록 모두 완료");
						}
						
//						places.add(place);
					}
					
//					if(places.size()>0) {
//						if(pcmapper.insertPlace(places)>0) {
//							System.out.println("Service : 장소 추가 성공");
//							
//							
//							
//							for(Map.Entry<String, Object> entry : itineraries.entrySet()) {
//							
//							
//							}
//							
//							if(cmapper.insertCost(costs, )>0) {
//								System.out.println("Service : 비용 추가 성공");
//							}
//							
//							
//						}
//						
//					}
					
//					for(Map.Entry<String, Object> entry : selectedPlaces.entrySet()) {
//					Map<String, Object> map = (Map<String, Object>)entry.getValue();
//					
//					PlaceDTO place = new PlaceDTO();
//					
//					place.setMarkerId((String)map.get("id"));
//					place.setName((String)map.get("name"));
//					place.setAddr((String)map.get("addr"));
//					place.setLatitude((Double)map.get("latitude"));
//					place.setLongitude((Double)map.get("longitude"));
//					place.setType((String)map.get("id"));
//					place.setSystemName((String)map.get("id"));
//					place.setOriginName((String)map.get("id"));
//					place.setDestinationId(destinationIds.get((String)map.get("city")));
//					places.add(place);
//				}
				}
			}
			
			GroupDTO group = new GroupDTO();
			group.setUserId(userId);
			group.setPlanId(planId);
			group.setRule("그룹장");
			if(gmapper.insertGroup(group)==1) {
				System.out.println("Service : 그룹 추가 성공");
				return planId;
			}
		}
		return -1;
		
	}
	
	
	
	
	
	
	
	
	
	@Autowired
    private PlanMapper pMapper;
	@Autowired
	private DestinationMapper dMapper;
    @Autowired
    private PlaceMapper plMapper;
    @Autowired
    private CostMapper cMapper;
    @Autowired
    private GroupMapper gMapper;
    @Autowired
    private LikedPlanMapper lMapper;
    @Autowired
    private UserMapper uMapper;
    @Autowired
    private ReviewMapper rMapper;

	
	@Override
	public List<PlanDetailsDTO> getPlans(CriteriaJ criJ, String userId) {
		// 모든 Plan 가져오기(한 페이지의 갯수만큼)
//        List<PlanDTO> allPlans = pMapper.getAllPlans(pageNum);
		// 공유된 Plan 가져오기(한 페이지의 갯수만큼)
        List<PlanDTO> allPlans = pMapper.getSharedPlans(criJ);

        // PlanDetailsDTO 리스트 생성
        List<PlanDetailsDTO> planList = new ArrayList<>();

        // 계획 갯수만큼 반복
        for (PlanDTO plan : allPlans) {
            Long planId = plan.getPlanId();

            // 각 planId에 해당하는 Destination, Place 등 불러오기
            List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId); 
            List<PlaceDTO> places = plMapper.getPlaceByPlanId(planId);
            List<CostDTO> costs = cMapper.getCostByPlanId(planId);
            List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
            List<UserDTO> users = uMapper.getUserByPlanId(planId);
            List<ReviewDTO> reviews = rMapper.getReviewByPlanId(planId);
            int daysCount = pMapper.getDaysCountByPlanId(planId); //여행일수
            int likedCount = lMapper.getLikedCountByPlanId(planId); //좋아요갯수
            int likedCheck = lMapper.getLikedCheck(planId,userId); //로그인 유저의 좋아요 여부
            String leaderNick = uMapper.getLeaderNickByPlanId(planId); //그룹장 닉네임

            // PlanDetailsDTO 객체 생성 및 데이터 세팅
            PlanDetailsDTO planDetailsDTO = new PlanDetailsDTO();
            planDetailsDTO.setPlan(plan); //DTO
            planDetailsDTO.setDestinations(destinations); //List
            planDetailsDTO.setPlaces(places); //List
            planDetailsDTO.setCosts(costs); //List
            planDetailsDTO.setGroups(groups); //List
            planDetailsDTO.setUsers(users); //List
            planDetailsDTO.setReviews(reviews); //List
            planDetailsDTO.setDaysCount(daysCount); //int
            planDetailsDTO.setLikedCount(likedCount); //int
            planDetailsDTO.setLikedCheck(likedCheck); //int
            planDetailsDTO.setLeaderNick(leaderNick); //String
            
            // 리스트에 추가
            planList.add(planDetailsDTO);
        }
        
        // PlanDetailsDTO 리스트 반환
        return planList;
    }
	
	
	@Override
	public long getTotal(CriteriaJ criJ) {
		return pMapper.getTotalCount(criJ);
	}
	
	
	@Override
	public PlanDetailsDTO getPlan(long planId, String userId) {
		
		PlanDetailsDTO planList = new PlanDetailsDTO();

		// 각 planId에 해당하는 Destination, Place 등 불러오기
	    PlanDTO plan = pMapper.getPlanByPlanId(planId);
	    List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId); 
	    List<PlaceDTO> places = plMapper.getPlaceByPlanId(planId);
	    List<CostDTO> costs = cMapper.getCostByPlanId(planId);
	    List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
	    List<UserDTO> users = uMapper.getUserByPlanId(planId);
	    List<ReviewDTO> reviews = rMapper.getReviewByPlanId(planId);
	    int daysCount = pMapper.getDaysCountByPlanId(planId); // 여행일수
	    int likedCount = lMapper.getLikedCountByPlanId(planId); // 좋아요 갯수
	    int likedCheck = lMapper.getLikedCheck(planId, userId); // 로그인 유저의 좋아요 여부
	    String leaderNick = uMapper.getLeaderNickByPlanId(planId); // 그룹장 닉네임
	    
	    planList.setPlan(plan);
	    planList.setDestinations(destinations);
	    planList.setPlaces(places);
	    planList.setCosts(costs);
	    planList.setGroups(groups);
	    planList.setUsers(users);
	    planList.setReviews(reviews);
	    planList.setDaysCount(daysCount);
	    planList.setLikedCount(likedCount);
	    planList.setLikedCheck(likedCheck);
	    planList.setLeaderNick(leaderNick);
	    
	    
	    
	    //★★계획 하나에 들어있는 모든것
	    List<List<HashMap<String, Object>>> planDataList = new ArrayList<>();
	    
	    // 현재의 목적지ID = 0
        long currentDestinationId = 0;

	    //여행일수만큼 반복문 실행
	    for (int i = 0; i < daysCount+1; i++) {

	    	//★★하루 일정 List
	        List<HashMap<String, Object>> dayData = new ArrayList<>();
	        //★★하루 일정 List 안에 들어갈 내용 HashMap
	        //["num" : 하루 카운트, "date" : 날짜 카운트 , "place" : List<날짜별 장소>, "destination" : 목적지DTO]
	        HashMap<String, Object> dayInfo = new HashMap<>();
	        
	        //★몇일차인지 카운트 (내용에 put)
	        dayInfo.put("num", i+1);

	    	
	        //★날짜 카운트
	        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
	        //계획 시작날짜
	        String startDay = plan.getStartDate().substring(0, 10);
	        //Date 타입으로 바꿔주기
	        Date planDate = null;
	        try {
	        	planDate = sdfYMD.parse(startDay);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
	        
	        //날짜 연산을 위한 Calendar객체 생성 후 date 대입
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(planDate);
	        //날짜 더하기 (0부터 시작)
	        cal1.add(Calendar.DATE, i); 
	        String fmPlanDate = sdfYMD.format(cal1.getTime());
	        //날짜 카운트 (내용에 put)
	        dayInfo.put("date", fmPlanDate);


	        //★하루 일정안에 장소들
	        List<HashMap<String, Object>> placeList = new ArrayList<>();
	        
	        
	        //place 갯수만큼 반복문 실행
	        for (int j = 0; j < places.size(); j++) {
	        	//place 날짜 가져오기
		        String placeDay = places.get(j).getStartTime().substring(0, 10);
		        //Date 타입으로 바꿔주기
		        Date placeDate = null;
		        try {
		        	placeDate = sdfYMD.parse(placeDay);
				} catch (ParseException e) {
					e.printStackTrace();
				} 
		        Calendar cal2 = Calendar.getInstance();
		        cal2.setTime(placeDate);
		        String fmPlaceDate = sdfYMD.format(cal2.getTime());
		        
		        //plan날짜(날짜 카운트)랑 place날짜랑 동일한지 비교
	        	int result = fmPlanDate.compareTo(fmPlaceDate);
	        	//날짜가 같으면 int 0 이라고 나옴
	        	if(result == 0){
	        		HashMap<String, Object> place = new HashMap<>();
	        		//날짜별 각 장소 (내용에 put)
		        	place.put("place", places.get(j));
		        	//하루 일정안에 장소들 (리스트에 add)
		        	placeList.add(place);

		        	
		        	//현재 place의 destinationId 가져오기
		            long newDestinationId = places.get(j).getDestinationId();
	            
		            //새로운 destinationId가 기존과 다르면 새로운 destinationId로 갱신
		            if (currentDestinationId != newDestinationId) {  
		                //destinationId 갱신
		                currentDestinationId = newDestinationId;
		                
		                //목적지DTO (내용에 put)
		                dayInfo.put("destination", dMapper.getDestinationById(currentDestinationId));
		            }		            
		        }
	        }
	        
	        //하루 일정안에 장소 리스트 (내용에 put)
	        dayInfo.put("placeList", placeList);

	        //하루 일정 내용(HashMap)을 하루 일정 List에 넣기
	        dayData.add(dayInfo);
	        //하루 일정 List를 계획 List에 넣기
	        planDataList.add(dayData);
	    }
	    
	    planList.setPlanDataList(planDataList);
	    
	    System.out.println(planList.getPlanDataList());
	    
		return planList;
	}
	
	
	//수정전 완성본
//	@Override
//	public PlanDetailsDTO getPlan(long planId, String userId) {
//		
//		PlanDetailsDTO planList = new PlanDetailsDTO();
//
//		// 각 planId에 해당하는 Destination, Place 등 불러오기
//	    PlanDTO plan = pMapper.getPlanByPlanId(planId);
//	    List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId); 
//	    List<PlaceDTO> places = plMapper.getPlaceByPlanId(planId);
//	    List<CostDTO> costs = cMapper.getCostByPlanId(planId);
//	    List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
//	    List<UserDTO> users = uMapper.getUserByPlanId(planId);
//	    int daysCount = pMapper.getDaysCountByPlanId(planId); // 여행일수
//	    int likedCount = lMapper.getLikedCountByPlanId(planId); // 좋아요 갯수
//	    int likedCheck = lMapper.getLikedCheck(planId, userId); // 로그인 유저의 좋아요 여부
//	    String leaderNick = uMapper.getLeaderNickByPlanId(planId); // 그룹장 닉네임
//	    
//	    planList.setPlan(plan);
//	    planList.setDestinations(destinations);
//	    planList.setPlaces(places);
//	    planList.setCosts(costs);
//	    planList.setGroups(groups);
//	    planList.setUsers(users);
//	    planList.setDaysCount(daysCount);
//	    planList.setLikedCount(likedCount);
//	    planList.setLikedCheck(likedCheck);
//	    planList.setLeaderNick(leaderNick);
//
//		return planList;
//	}
	
	
	
	
//	@Override
//	public PlanDetailsDTO getPlan(long planId, String userId) {
//		PlanDetailsDTO planList = new PlanDetailsDTO();
//		
//		// 각 planId에 해당하는 Destination, Place 등 불러오기
//		PlanDTO plan = pMapper.getPlanByPlanId(planId);
//        List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId); 
//        List<PlaceDTO> places = plMapper.getPlaceByPlanId(planId);
//        List<CostDTO> costs = cMapper.getCostByPlanId(planId);
//        List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
//        List<UserDTO> users = uMapper.getUserByPlanId(planId);
//        int daysCount = pMapper.getDaysCountByPlanId(planId); //여행일수
//        int likedCount = lMapper.getLikedCountByPlanId(planId); //좋아요갯수
//        int likedCheck = lMapper.getLikedCheck(planId,userId); //로그인 유저의 좋아요 여부
//        String leaderNick = uMapper.getLeaderNickByPlanId(planId); //그룹장 닉네임
//		
//        planList.setPlan(plan);
//        planList.setDestinations(destinations);
//        planList.setPlaces(places);
//        planList.setCosts(costs);
//        planList.setGroups(groups);
//        planList.setUsers(users);
//        planList.setDaysCount(daysCount);
//        planList.setLikedCount(likedCount);
//        planList.setLikedCheck(likedCheck);
//        planList.setLeaderNick(leaderNick);
//        
//		return planList;
//		
//	}
	
	
	
//	@Override
//	public List<List<HashMap<String, Object>>> getPlanDays(long planId, String userId) {
//		
//		PlanDetailsDTO planList = new PlanDetailsDTO();
//
//		// 각 planId에 해당하는 Destination, Place 등 불러오기
//		PlanDTO plan = pMapper.getPlanByPlanId(planId);
//        List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId); 
//        List<PlaceDTO> places = plMapper.getPlaceByPlanId(planId);
//        List<CostDTO> costs = cMapper.getCostByPlanId(planId);
//        List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
//        List<UserDTO> users = uMapper.getUserByPlanId(planId);
//        int daysCount = pMapper.getDaysCountByPlanId(planId); //여행일수
//        int likedCount = lMapper.getLikedCountByPlanId(planId); //좋아요갯수
//        int likedCheck = lMapper.getLikedCheck(planId,userId); //로그인 유저의 좋아요 여부
//        String leaderNick = uMapper.getLeaderNickByPlanId(planId); //그룹장 닉네임
//		
//        planList.setPlan(plan);
//        planList.setDestinations(destinations);
//        planList.setPlaces(places);
//        planList.setCosts(costs);
//        planList.setGroups(groups);
//        planList.setUsers(users);
//        planList.setDaysCount(daysCount);
//        planList.setLikedCount(likedCount);
//        planList.setLikedCheck(likedCheck);
//        planList.setLeaderNick(leaderNick);
//
//        
//		//////////////////////////////////////////
//        
//        SimpleDateFormat sdfYMDHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
//
//        String test = "2024-07-16";
//
//        //String을 날짜 연산을 위해 Date 객체로 변경
//        Date date = (Date) sdfYMD.parse(test, null); 
//
//        //날짜 연산을 위한 Calendar객체 생성 후 date 대입
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        
//        
//        cal.add(Calendar.DATE, 1); // 1일 더하기
//        System.out.println("1일 더하기: "+sdfYMD.format(cal.getTime(), null, null));
//        
//		//////////////////////////////////////////
//		        
//		DestinationDTO destination = new DestinationDTO();
//		List<PlaceDTO> placeList = new ArrayList<>();
//		
//		//HashMap에 일자, 목적지, 장소리스트 추가
//		HashMap<String, Object> planDay = new HashMap<>();
//		planDay.put("day", 1);
//		planDay.put("destination", destination);
//		planDay.put("placeList", placeList);
//		
//		// List<HashMap<String, Object>> 생성
////		List<HashMap<String, Object>> planDays = new ArrayList<>();
//		// planDay을 List에 추가
//		planDays.add(planDay);
//		
//
//        //여행일 구하기
//        int planDaysCount = daysCount + 1;       
//        //여행 시작날짜 가져오기
//        plan.getStartDate();
//		
//		//여행일만큼 반복문돌기
//        for(int i=0; i<planDaysCount; i++) {
//        	List<HashMap<String, Object>> planDays = new ArrayList<>();
//        }
//		
//		//planId에 해당하는 목적지 가져오기
//		//목적지 갯수 구하기
//		//목적지 갯수만큼 planList추가하기(첫 destination일때만)
//		
//		//placeList에 List<PlaceDTO> placeList 추가하기
//
//		
//		
//		//반복문 내부에서
//		//"day" -> i++ 로 증가시키기
//		//"destination" -> 
//		
//		return planDays;
//	}
//	
	
	
//	지피티꺼
//	@Override
//	public List<HashMap<String, Object>> getPlanDays(long planId, String userId) {
//
//	    PlanDetailsDTO planList = new PlanDetailsDTO();
//	    
//	    // 각 planId에 해당하는 Destination, Place 등 불러오기
//	    PlanDTO plan = pMapper.getPlanByPlanId(planId);
//	    List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId); 
//	    List<PlaceDTO> places = plMapper.getPlaceByPlanId(planId);
//	    List<CostDTO> costs = cMapper.getCostByPlanId(planId);
//	    List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
//	    List<UserDTO> users = uMapper.getUserByPlanId(planId);
//	    int daysCount = pMapper.getDaysCountByPlanId(planId); // 여행일수
//	    int likedCount = lMapper.getLikedCountByPlanId(planId); // 좋아요 갯수
//	    int likedCheck = lMapper.getLikedCheck(planId, userId); // 로그인 유저의 좋아요 여부
//	    String leaderNick = uMapper.getLeaderNickByPlanId(planId); // 그룹장 닉네임
//	    
//	    planList.setPlan(plan);
//	    planList.setDestinations(destinations);
//	    planList.setPlaces(places);
//	    planList.setCosts(costs);
//	    planList.setGroups(groups);
//	    planList.setUsers(users);
//	    planList.setDaysCount(daysCount);
//	    planList.setLikedCount(likedCount);
//	    planList.setLikedCheck(likedCheck);
//	    planList.setLeaderNick(leaderNick);
//
//	    // 여행일 만큼 반복문을 돌면서 HashMap을 생성하여 데이터를 추가
//	    List<HashMap<String, Object>> planDays = new ArrayList<>();
//
//	    // 여행 시작날짜 가져오기
//	    SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
//	    Date startDate;
//	    try {
//	        startDate = sdfYMD.parse(plan.getStartDate());
//	    } catch (ParseException e) {
//	        throw new RuntimeException("날짜 파싱 오류", e); // 또는 적절한 예외 처리
//	    }
//
//	    Calendar cal = Calendar.getInstance();
//	    cal.setTime(startDate);
//	    
//	    List<HashMap<String, Object>> planDays = new ArrayList<>();
//	    int placeIndex = 0; // PlaceDTO의 인덱스를 관리할 변수
//
//	    for (int i = 0; i < daysCount + 1; i++) {
//	        HashMap<String, Object> planDay = new HashMap<>();
//	        planDay.put("day", i + 1);
//
//	        // 목적지(destination) 설정
//	        DestinationDTO destination = null;
//	        if (placeIndex < places.size() && (i == 0 || places.get(placeIndex).getPlaceId() != places.get(placeIndex - 1).getPlaceId())) {
//	            destination = destinations.get(i);  
//	        }
//	        planDay.put("destination", destination);
//
//	        // 해당 날짜의 placeList 설정
//	        List<PlaceDTO> placeList = new ArrayList<>();
//	        while (placeIndex < places.size() && sdfYMD.format(places.get(placeIndex).getStartTime()).equals(sdfYMD.format(cal.getTime()))) {
//	            placeList.add(places.get(placeIndex));
//	            placeIndex++;
//	        }
//	        planDay.put("placeList", placeList);
//
//	        // 다음 날로 이동
//	        cal.add(Calendar.DATE, 1);
//
//	        planDays.add(planDay);
//	    }
//
//	    return planDays;
//	}


	
//	@Override
//	public PlanDetailsDTO getPlanDays(long planId, String userId) {
//		
//		PlanDetailsDTO planList = new PlanDetailsDTO();
//
//		// 각 planId에 해당하는 Plan, Destination 등 불러오기
//	    PlanDTO plan = pMapper.getPlanByPlanId(planId);
//	    List<DestinationDTO> destinations = dMapper.getDestinationByPlanId(planId);
//	    List<CostDTO> costs = cMapper.getCostByPlanId(planId);
//	    List<GroupDTO> groups = gMapper.getGroupByPlanId(planId);
//	    List<UserDTO> users = uMapper.getUserByPlanId(planId);
//	    int daysCount = pMapper.getDaysCountByPlanId(planId); // 여행일수
//	    int likedCount = lMapper.getLikedCountByPlanId(planId); // 좋아요 갯수
//	    int likedCheck = lMapper.getLikedCheck(planId, userId); // 로그인 유저의 좋아요 여부
//	    String leaderNick = uMapper.getLeaderNickByPlanId(planId); // 그룹장 닉네임
//
//	    // Destination에 맞는 Place 가져와서 로직 추가
//	    Map<DestinationDTO, List<PlaceDTO>> destinationPlacesMap = new HashMap<>();
//	    for (DestinationDTO destination : destinations) {
//	        List<PlaceDTO> places = plMapper.getPlacesByDestinationId(planId, destination.getDestinationId());
//	        destinationPlacesMap.put(destination, places);
//	    }
//	    
//	    planList.setPlan(plan);
//	    planList.setDestinations(destinations); // Destination 리스트 설정
//	    planList.setDestinationPlacesMap(destinationPlacesMap); // 각 Destination에 따른 Place 리스트 설정
//	    planList.setCosts(costs);
//	    planList.setGroups(groups);
//	    planList.setUsers(users);
//	    planList.setDaysCount(daysCount);
//	    planList.setLikedCount(likedCount);
//	    planList.setLikedCheck(likedCheck);
//	    planList.setLeaderNick(leaderNick);
//
//        
//		
//		return planList;
//	}
}
