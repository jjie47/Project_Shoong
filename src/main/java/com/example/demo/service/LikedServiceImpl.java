package com.example.demo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.domain.LikedPlanDTO;
import com.example.demo.domain.LikedReviewDTO;
import com.example.demo.domain.PhotoDTO;
import com.example.demo.mapper.LikedMapper;
import com.example.demo.mapper.LikedPlanMapper;

@Service
public class LikedServiceImpl implements LikedService{
	
	@Autowired
	private LikedMapper likedMapper;
	
	@Value("${file.dir}")
	private String saveFolder;
	
	private static final String DEFAULT_IMAGE_PATH = "/images/review_background_img.png";
	
	@Override
	public List<LikedPlanDTO> getLikedPlansList(String userId) {
		List<LikedPlanDTO> likedPlans = likedMapper.getLikedPlans(userId);
		
		// planId 별로 그룹화
		for(LikedPlanDTO lp : likedPlans) {
			List<String> cities = likedMapper.getCitiesByPlanId(lp.getPlanId());
			lp.setCities(cities);
			
			// 첫번째 도시 설정
			String cityKorFirst = cities.size() > 0 ? cities.get(0) : "";
			lp.setCityKorFirst(cityKorFirst);

			// "외 N" 로직
			if(cities.size() > 1) {
				lp.setOtherCitiesCnt(cities.size() - 1);
			}
			else {
				lp.setOtherCitiesCnt(0);
			}
		}
		return likedPlans;
	}

	@Override
	public void deleteLikedPlan(String userId, Long planId) {
		// userId와 planId로 DB에 있는지 확인
		if(!likedMapper.existByUserIdAndPlanId(userId, planId)){
			// 해당 게시글이 없습니다. 예외 발생 
		}
		likedMapper.deleteLikedPlanByUserIdAndPlanId(userId, planId);
	}

	//관심후기게시글 - 사용자가 등록한 이미지로 바탕화면 설정
	//이미지파일을 pc에 저장된 경로에서 불러오는 방식
	//PhotoDTO의 systemName을 사용해서 경로에 접근할 수 있다. 
	//실제 경로를 프론트에 직접 노출 시키는건 보안상 적절하지 않기 때문에, 파일을 제공하는 컨트롤러를 통해 이미지 url을 반화하도록 처리한다.
	//LikedReviewDTO의 backgroundImage 필드에 서버에서 제공하는 url을 넣어준다.
	@Override
	public List<LikedReviewDTO> getLikedReviewsList(String userId) {
		//userId로 좋아요한 후기 목록 가져옴
		List<LikedReviewDTO> likedReviews = likedMapper.getLikedReivews(userId);
		
		//각 후기에서 첫번째 사진으로 배경 설정
		for(LikedReviewDTO review : likedReviews) {
			//기본 배경 이미지 경로로 초기화
			String fullPath = DEFAULT_IMAGE_PATH;
			
			List<PhotoDTO> photos = likedMapper.getPhotosByReviewId(review.getReviewId());
			//사진이 있다면
			if(photos != null && !photos.isEmpty()) { 
				PhotoDTO firstPhoto = photos.get(0);
				String systemName = firstPhoto.getSystemName(); 
				
				//사진이 존재하는지 파일에 확인
				File file = new File(saveFolder, systemName);
				if(file.exists()) {
					//서버에서 파일을 제공하는 url을 설정
					String filename = file.getName(); //파일명만 추출
					fullPath =  "/file/" + filename;
				}
			}
			//배경 이미지 경로 설정
			review.setBackgroundImage(fullPath);
		}
		
		return likedReviews;
	}

	@Override
	public void deleteLikedReview(String userId, Long reviewId) {
		// userId와 reviewId로 DB에 있는지 확인
		if(!likedMapper.existByUserIdAndReviewId(userId, reviewId)) {
			// 해당 게시글이 없습니다. 예외 발생
		}
		likedMapper.deleteLikedReviewByUserIdAndReviewId(userId, reviewId);
	}
	
}
