package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.DefaultDestinationDTO;
import com.example.demo.service.DefaultDestinationService;

@Controller
@RequestMapping("/defaultdestination/*")
public class DefaultDestinationController {
	
	@Autowired
	DefaultDestinationService service;

	@GetMapping("list")
	public String list(String keyword, String continent, Model model) {
		List<DefaultDestinationDTO> list = service.getList(keyword, continent);
		model.addAttribute("defaultDestinationList", list);
		return "/plan/write :: #defaultDestinationList";
	}
	
	@GetMapping("get")
	@ResponseBody
	public DefaultDestinationDTO get(int destinationId) {
		return service.getDestination(destinationId);	
	}
}
