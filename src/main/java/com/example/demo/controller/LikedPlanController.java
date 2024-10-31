package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.LikedPlanService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LikedPlanController {
	
	@Autowired
	private LikedPlanService service;
	
	
	@PostMapping("/plan/likedPlan")
	@ResponseBody
	public void likedPlan(@RequestParam("planId") long planId, @RequestParam("likedCheck") int likedCheck, Model model, HttpServletRequest req) {
//      임시아이디 입력
	    String userId = "apple";
		
//      session 부여
//	    HttpSession session = req.getSession();
//	    String userId = (String)session.getAttribute("loginUser");
	    
	    //빈 하트를 클릭했을 때 (좋아요 생성)
	    if(likedCheck == 1) {
	    	//좋아요 목록에 넣기
		    service.insert(planId, userId);
	    }
	    //채워진 하트를 클릭했을 때 (좋아요 취소)
	    else if(likedCheck == 0){
	    	//좋아요 목록에서 빼기
	    	service.delete(planId, userId);
	    }
	}

}
