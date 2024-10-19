package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.ReviewDTO;
import com.example.demo.service.CommentService;
import com.example.demo.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/review/*")
public class ReviewController {
	

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CommentService commentService;
   
   @GetMapping("list")
   public String list(Model model, @RequestParam(defaultValue = "1") int pagenum) {
	   List<ReviewDTO> rl = reviewService.getReviewList(pagenum, 8); //8개 리뷰 요청
	   System.out.println(rl.size());
	   
	 //코멘트 갯수를 담기 위한 해시맵
	 		Map<Long, Long> comcount = new HashMap<>();
	 		
	 		//for문 써서 reviewId에 따른 comment갯수 뽑아오기
	 		for (ReviewDTO rl1 : rl) {
	 			long count = commentService.getCount(rl1.getReviewId());
	 			comcount.put(rl1.getReviewId(), count);
	 		}
	   
	   model.addAttribute("reviews",rl);
	   model.addAttribute("count", comcount);
	  return "/review/list";
   }
   
   
   @ResponseBody
   @GetMapping("loadMore/{pagenum}")
   //페이지, 
   public List<ReviewDTO> getReviews(@PathVariable("pagenum") int pagenum){
	   List<ReviewDTO> reviews = reviewService.getReviews(pagenum);
	   
	   for(ReviewDTO review : reviews) {
		   long count = commentService.getCount(review.getReviewId());
		   review.setCommentCount(count);
	   }
	      
	   return reviews;
//	   return reviewService.getReviews((pagenum-1)*8);
   }
   
   @GetMapping("write")
   public void write() {
   }
   
   // 클라이언트가 /review/list 에서 "글쓰기" 버튼을 클릭
   // 
   @PostMapping("write")
   public String write(@RequestParam long planId,
		   				@RequestParam String review_title,
		   				@RequestParam String review_write_box, HttpServletRequest req,
		   				MultipartFile[] photos, HttpServletResponse resp) {
	   System.out.println("@!!");
	   String userId = "apple";
	   HttpSession session = req.getSession();
	   session.setAttribute("loginUser", userId);
	   
	  
	   
	   reviewService.writeReview(userId, planId, review_title, review_write_box);
       return "redirect:/review/list";
   }
   
   @GetMapping("write/myplan")
   @ResponseBody
   public List<Map<String, Object>> myPlan(HttpServletRequest req) {
	      //임시 로그인 처리
	      String userId = "apple";
	      HttpSession session = req.getSession();
	      session.setAttribute("loginUser", userId);
	         
	      List<Map<String, Object>> result = reviewService.getMyPlans(userId);
	      
	      System.out.println(result);
	            
	      return result;
	   }

//   @GetMapping("write/uploadFile")
//   public void file(String system_name, String origin_name, MultipartFile file, Model model, HttpServletRequest req) {
//	   //임시 로그인 처리
//	      String userId = "apple";
//	      HttpSession session = req.getSession();
//	      session.setAttribute("loginUser", userId);
//	      
//	  Resource resource = reviewService.uploadFile(system_name,origin_name,file);
//   };
   
   
   @GetMapping("read")
   // 클라이언트가 특정 review 를 클릭
   // /review/read?특정 review_id 를 db에 가져가서 해당하는 review의 데이터들을 가지고 와야
   public String read(@RequestParam("reviewId") long reviewId, Model model,HttpServletRequest req) {
//	   서비스.getView(reviewId);
	   ReviewDTO rdto = reviewService.getReadReview(reviewId);
	   
	   
	   System.out.println(rdto);
	   
	   String userId = "apple";
	   HttpSession session = req.getSession();
	   session.setAttribute("loginUser", userId);
	   
	   model.addAttribute("loginUser", userId);
	   model.addAttribute("review", rdto);
	   System.out.println(userId);
       
//       url : /review/get?review
//       Id=1
	   return "/review/read";
   }
   
   @GetMapping("list/read/delete/{reviewId}")
   public String deleteReview(@PathVariable long reviewId){
	   
	   reviewService.deleteReview(reviewId);
	   System.out.println("삭제된 리뷰아이디: "+reviewId);
      
       return "redirect:/review/list";
   }
   
		   @GetMapping("list/read/rlist/{reviewId}")
		   public String read(@PathVariable long reviewId) {
		     
		            
		      return "redirect:/review/list";
		   }
}
   
   
   
   
   
   