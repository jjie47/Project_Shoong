package com.example.demo.controller;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.CriteriaJ;
import com.example.demo.domain.DefaultDestinationDTO;
import com.example.demo.domain.PageDTO;
import com.example.demo.domain.PlanDetailsDTO;
import com.example.demo.service.PlanService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/plan/*")
public class PlanController {
	
	@Autowired
	private PlanService service;
	
	@GetMapping("write")
	public String write(HttpServletRequest req, HttpServletResponse resp, Model model) {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("loginUser");
		if(userId!=null) {
			long planId = service.create(userId);
			if(planId!=-1) {
				model.addAttribute("planId", planId);
				model.addAttribute("userId", userId);
				return "/plan/write";
			}
			else {
				Cookie cookie = new Cookie("isCreated", "false");
				cookie.setPath("/");
				cookie.setMaxAge(60);
				resp.addCookie(cookie);
			}
			return "redirect:/";			
		}
		else {
			Cookie cookie = new Cookie("isLogined", "false");
			cookie.setPath("/");
			cookie.setMaxAge(60);
			resp.addCookie(cookie);
		}
		return "/user/login";
	}
	
	@PostMapping("write")
	public void write(@RequestBody Map<String, Object> data, HttpServletRequest req, HttpServletResponse resp)
	{
		HttpSession session = req.getSession();
		
		long planId = ((Integer)data.get("planId")).longValue();
		System.out.println("계획 Id 확인 : " + planId);
		Map<String, Object> selectedDefaultDestinations = (Map<String, Object>) data.get("selectedDefaultDestinations");
		List<String> selectedDestinations = (List<String>)data.get("selectedDestinations");
		Map<String, String> selectedDates = (Map<String, String>) data.get("selectedDates");
		Map<String, Object> selectedPlaces = (Map<String, Object>) data.get("selectedPlaces");
		Map<String, Object> itineraries = (Map<String, Object>) data.get("itineraries");
		Map<String, Object> costs = (Map<String, Object>) data.get("costs");
		
		String userId = (String) session.getAttribute("loginUser");
		
		
		if(service.regist(planId,selectedDefaultDestinations,selectedDestinations, selectedDates, selectedPlaces, itineraries, costs, userId) != -1) {
			System.out.println("===========================");
			System.out.println("Controller : 게시글 등록 성공!");
			
			Cookie cookie = new Cookie("hasRegisted", "true");
			cookie.setPath("/");
			cookie.setMaxAge(5);
			resp.addCookie(cookie);
		}
		
		
		
		
		
		
		
		
		
		
		for (Map.Entry<String, Object> entry : selectedDefaultDestinations.entrySet()) {
			System.out.print("selectedDefaultDestinations : ");
		    System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		for (String entry : selectedDestinations) {
			System.out.print("selectedDestinations : ");
			System.out.println(entry);
		}
		for (Map.Entry<String, String> entry : selectedDates.entrySet()) {
			System.out.print("selectedDates : ");
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		for (Map.Entry<String, Object> entry : selectedPlaces.entrySet()) {
			System.out.print("selectedPlaces : ");
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		for (Map.Entry<String, Object> entry : itineraries.entrySet()) {
			System.out.print("itineraries : ");
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		for (Map.Entry<String, Object> entry : costs.entrySet()) {
			System.out.print("costs : ");
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}	
	
	
	
	
	
	
	

	    @GetMapping("list")
	    public String getPlanList(CriteriaJ criJ, Model model) {
	    	String userId = "abc123";
	    	List<PlanDetailsDTO> planLists = service.getPlans(criJ, userId);
	    	
	        model.addAttribute("planLists", planLists);
	        model.addAttribute("pageMaker",new PageDTO(service.getTotal(criJ), criJ));

	        return "plan/list";
	    }

	    
	    @GetMapping("get")
	    public String getPlanView(@RequestParam("planId") Long planId, Model model) {
	    	String userId = "abc123";
	    	System.out.println("planId : " + planId);
	    	
	    	PlanDetailsDTO planList = service.getPlan(planId, userId);
	    	model.addAttribute("planList", planList);
	    	
	        return "plan/get";
	    }
}
