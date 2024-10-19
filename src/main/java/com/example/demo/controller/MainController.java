package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.MainService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	private MainService service;
	
	@GetMapping("/")
	public String mainPage(HttpServletRequest req, Model model) {
		
		List<Map<String, Object>> result = service.getMain();
		model.addAttribute("rt", result);
		System.out.println(result);
		
		return "index";
	}
	
}
