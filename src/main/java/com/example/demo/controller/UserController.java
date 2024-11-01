package com.example.demo.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.CommentDTO;
import com.example.demo.domain.LikedPlanDTO;
import com.example.demo.domain.LikedReviewDTO;
import com.example.demo.domain.PageDTO;
import com.example.demo.domain.PointDTO;
import com.example.demo.domain.ReviewDTO;
import com.example.demo.domain.UserDTO;
import com.example.demo.service.CommentService;
import com.example.demo.service.LikedService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/*")
public class UserController {
	
	@Autowired
	private UserService service;
	@Autowired
	private LikedService likedService;
	
	
	
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CommentService commentService;
	
	
	
	
	
	
//	회원가입 - 페이지로 이동
	@GetMapping("join")
	public void replace() {}
	
//	회원가입 - 데이터 전송 - 성공시 로그인 페이지로 이동(js에서)
	@PostMapping("join")
	@ResponseBody
	public Map<String, String> join(@ModelAttribute UserDTO user, HttpServletResponse resp) {		
		// 캐시 비활성화 헤더에 넣기 - 추후 개발예정
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0"); //응답이 캐시되지 않도록
		resp.setHeader("Pragma", "no-cache"); //HTTP/1.0 호환성을 위해
		resp.setDateHeader("Expries", 0); //캐시 만료 시간을 0
		
		Map<String, String> response = new HashMap<>();
		if(service.join(user)) {
			Cookie cookie = new Cookie("joinId", user.getUserId());
			cookie.setPath("/");
			cookie.setMaxAge(180);
			resp.addCookie(cookie);
			response.put("status", "success");
		}
		else {
			response.put("status", "fail");
		}
		return response;
	}
	
//	아이디 중복체크
	@GetMapping("checkId")
	@ResponseBody
	public String checkId(String userId) {
		if(service.checkId(userId)) {
			// 중복된 아이디가 없다는 뜻. 이 아아디 사용 가능
			return "O";
		}
		else {
			return "X";
		}
	}
	
//	로그인 - 페이지로 이동
	@GetMapping("login")
	public String login(Model model, HttpSession session) {
		String userId = (String)session.getAttribute("loginUser");
		
		if(userId != null) {
			return "redirect:/";
		}
		return "/user/login";
	}
	
//	로그인 - 데이터 전송 - 성공시 홈으로 이동(js에서)
	@PostMapping("login")
	@ResponseBody
	public Map<String, String> login(@RequestBody Map<String, String> body, HttpServletRequest req) {
		String userId = body.get("userId");
		String password = body.get("password");
		HttpSession session = req.getSession(); //세션이 없다면 새로 생성 후 반환
		
		Map<String, String> response = new HashMap<>();
		if(service.login(userId, password)) {
			session.setAttribute("loginUser", userId);
			response.put("status", "success");
		}
		else {
			response.put("status", "fail");
		}
		return response;
	}
	
//	로그아웃 - 성공시 홈으로 이동
	@GetMapping("logout")
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession(false); //세션이 없다면 null 반환
		if(session != null) {
			session.invalidate();
		}
		return "redirect:/";
	}
	
//	마이페이지 - 페이지로 이동
	@GetMapping("myaccount")
	public String myaccount(HttpSession session, Model model) {
		String userId = (String)session.getAttribute("loginUser");
		UserDTO user = service.getUserByUserId(userId);
		
		model.addAttribute("user", user);
		
		if(userId == null) {
			return "redirect:/user/login";
		}
		
		return "user/myaccount";
	}
	
//	마이페이지 - 회원정보 수정 페이지로 이동
	@GetMapping("myaccountEdit")
	public String editAccount(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
		UserDTO user = service.getUserByUserId(userId);
		
		model.addAttribute("user", user);
		return "user/myaccountEdit";
	}
	
//	마이페이지 - 회원정보 수정 - 데이터 전송  + 파일 업로드
	@PostMapping("myaccountEdit")
	public String editAccount(
			HttpSession session, 
			@ModelAttribute UserDTO user, 
			@RequestParam(value = "profileInput", required = false) MultipartFile profileImage
			) {
		String userId = (String)session.getAttribute("loginUser");
		user.setUserId(userId);
		
		if(service.updateUser(user, profileImage)){
			return "redirect:/user/myaccount";
		}
		else {
			//에러 추후 구현 예정
			return "redirect:/user/myaccount?error";
		}
	}
	
//	회원탈퇴 - 데이터 전송 
	@PostMapping("deleteAccount")
	public String deleteAccount(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if(session != null) {
			String userId = (String)session.getAttribute("loginUser");
			if(service.deleteUser(userId)) {
				session.invalidate();
				return "redirect:/";
			}
			else {
				//에러 추후 구현 예정
				return "redirect:/user/myaccount?error";
			}
		}
		//세션이 없으면 로그인 페이지로 이동
		return "redirect:/user/login";
	}
	
//	마이페이지 - 관심 계획 목록 페이지로 이동
	@GetMapping("/mypage/likedplanlist")
	public String likedplanlist(HttpServletRequest req, HttpServletResponse resp, Model model) {
		HttpSession session = req.getSession(false);
		String userId = (session != null) ? (String) session.getAttribute("loginUser") : null;	
		
		model.addAttribute("user", userId);
		
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "user/mypage/likedplanlist";
	}
	
//	마이페이지 - 관심 계획 목록 - JSON 데이터 반환
	@GetMapping("/mypage/likedPlans")
	@ResponseBody
	public List<LikedPlanDTO> likedPlanList(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
	
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return likedService.getLikedPlansList(userId);
	}
	
//	마이페이지 - 관심 계획 목록 - 게시글 삭제
	@DeleteMapping("/mypage/likedPlans/{planId}")
	public String deleteLikedPlan(@PathVariable Long planId, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
	
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		likedService.deleteLikedPlan(userId, planId);
		return "user/mypage/likedplanlist";
	}
	
//	마이페이지 - 관심 후기 목록 페이지로 이동
	@GetMapping("/mypage/likedreviewlist")
	public String likedreviewlist(HttpServletRequest req, HttpServletResponse resp, Model model) {
		HttpSession session = req.getSession(false);
		String userId = (session != null) ? (String) session.getAttribute("loginUser") : null;
		
		model.addAttribute("user", userId);
		
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "user/mypage/likedreviewlist";
	}
	
//	마이페이지 - 관심 후기 목록 - JSON 데이터 반환
	@GetMapping("/mypage/likedReviews")
	@ResponseBody
	public List<LikedReviewDTO> likedReveiwList(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
	
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		return likedService.getLikedReviewsList(userId);
	}
	
//	마이페이지 - 관심 후기 목록 - 게시글 삭제
	@DeleteMapping("/mypage/likedReviews/{reviewId}")
	public String deleteLikedReivew(@PathVariable Long reviewId, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
	
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		likedService.deleteLikedReview(userId, reviewId);
		return "user/mypage/likedreviewlist";
	}
	
	@GetMapping("mySchedule")
	public String myschedule() {
		return "user/mySchedule";
	}
	
	@GetMapping("myPlan")
	public String myPlan(HttpServletRequest req, Model model) {
		
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
			
		List<Map<String, Object>> result = service.getMyPlan(userId);
		model.addAttribute("rt", result);
		
		System.out.println(result);
				
		return "user/myPlan";
	}
	
	@GetMapping("myPlan/exit/{userId}/{planId}")
	public String exitMyplan(@PathVariable String userId, @PathVariable long planId){
		
		service.exitMyPlan(userId,planId);
		
	    return "redirect:/user/myPlan";
	}
	
	@GetMapping("myPlan/delete/{planId}")
	public String deleteMyplan(@PathVariable long planId){
		
		service.deleteMyPlan(planId);
		
	    return "redirect:/user/myPlan";
	}
	
	@GetMapping("myReview")
	public String myReview(HttpServletRequest req, Model model,@RequestParam(defaultValue = "1") Integer pagenum) {
		
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
		
		
		if (pagenum == null) {
	        pagenum = 1;
	    }
		List<List<Object>> rvcom = service.getMyReview(userId,pagenum);
		
		model.addAttribute("review", rvcom);
		model.addAttribute("pageMaker", new PageDTO(service.getMyReviewTotal(userId),pagenum));
		
		
		return "user/myReview";
	}
	
	@GetMapping("myReview/delete/{reviewId}")
	public String deleteMyReview(@PathVariable long reviewId){
		
		service.deleteMyReview(reviewId);
		
	    return "redirect:/user/myReview";
	}
	
	@GetMapping("myComment")
	public String myComment(HttpServletRequest req, Model model,@RequestParam(defaultValue = "1") Integer pagenum) {
		
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
		
		//게시글 불러오고 html에 넣는 model 만들기
		if (pagenum == null) {
	        pagenum = 1;
	    }
		List<CommentDTO> cm = service.getMyComment(userId,pagenum);
		
		model.addAttribute("comment", cm);
		model.addAttribute("pageMaker", new PageDTO(service.getMyCommentTotal(userId),pagenum));
		
		
		return "user/myComment";
	}
	
	@GetMapping("myComment/delete/{commentId}")
	public String deleteMycomment(@PathVariable long commentId){
		service.deleteMycomment(commentId);
		
	    return "redirect:/user/myComment";
	}
	
	
	
	@GetMapping("search")
	@ResponseBody
	public List<UserDTO> search(String keyword, String planId) {
		List<UserDTO> list = service.getUsersByKeyword(keyword, Long.parseLong(planId));
//		System.out.println(list);
//		model.addAttribute("userList", list);
//		return "/plan/write :: #userList";
		return list;
	}


//	마이페이지 - 내 계정 - 포인트 관리
	@GetMapping("mypoint")
	public String mypoint(HttpServletRequest req, HttpServletResponse resp, Model model) {
		HttpSession session = req.getSession(false);
		String userId = (session != null) ? (String) session.getAttribute("loginUser") : null;
		
		model.addAttribute("user", userId);
		
		List<PointDTO> result = service.getPointByUserId(userId);
		model.addAttribute("pt", result);
		
		if(userId == null) {
			try {
				resp.sendRedirect("/user/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return "user/mypoint";
	}
	
//	@GetMapping("/")
//	public String index(HttpServletRequest req) {
////      임시아이디 입력
//      String userid = "apple";
////      session 부여
//      HttpSession session = req.getSession();
//      session.setAttribute("loginUser", userid);
////      원하는 페이지 띄우기
//		return "index";
//	}
	
	@GetMapping("myplan/owo1")
	public String owo1() {
		return "user/myplan/owo1";
//		mypage
	}
	
	@GetMapping("myplan/owo2")
	public String owo2() {
		return "user/myplan/owo2";
//		mypage/myplan
	}
	
	@GetMapping("myplan/owo3")
	public String owo3(HttpServletRequest req, Model model) {
		
		//임시 로그인 처리
		String userId = "apple";
		HttpSession session = req.getSession();
		session.setAttribute("loginUser", userId);
		
		//게시글 불러오고 html에 넣는 model 만들기
		List<ReviewDTO> rv = reviewService.getMyReview(userId);
		
		//코멘트 갯수를 담기 위한 해시맵
		Map<Long, Long> comcount = new HashMap<>();
		
		//for문 써서 reviewId에 따른 comment갯수 뽑아오기
		for (ReviewDTO rv1 : rv) {
			long count = commentService.getCount(rv1.getReviewId());
			comcount.put(rv1.getReviewId(), count);
		}
				
		model.addAttribute("review", rv);
		model.addAttribute("count", comcount);
		
		return "user/myplan/owo3";
	}
	
	@GetMapping("myplan/owo4")
	public String owo4() {
		return "user/myplan/owo4";
	}
	
}
