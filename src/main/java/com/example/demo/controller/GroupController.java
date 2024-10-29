package com.example.demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.GroupMemberDTO;
import com.example.demo.domain.UserDTO;
import com.example.demo.service.GroupService;


@Controller
@RequestMapping("/group/*")
public class GroupController {

	@Autowired
	GroupService service;
	
	@GetMapping("list")
	public String list(String planId, Model model) {
		List<GroupMemberDTO> list = service.getList(Long.parseLong(planId));
		model.addAttribute("groupMemberList", list);
		return "/plan/write :: #groupMemberList";
	}
	
	@GetMapping("list/invite")
	@ResponseBody
	public List<Map<String, Object>> getInviteList(String userId) {
		List<Map<String, Object>> list = service.getInviteList(userId);
		System.out.println(list);
		Collections.reverse(list);
		return list;
	}
	
	@PostMapping("invite")
	public ResponseEntity<String> invite(@RequestBody Map<String, Object> data) {
		long planId = ((Integer)data.get("planId")).longValue();
		String userId = (String)data.get("userId");
		if(service.request(planId, userId)==1) {
			return new ResponseEntity<String>("invite success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("invite failed", HttpStatus.OK);
	}
	
	@GetMapping("members")
	@ResponseBody
	public ResponseEntity<List<String>> members(String responsedPlanId) {
		System.out.println(responsedPlanId);
		long planId = Long.parseLong(responsedPlanId);
		System.out.println(planId);
		List<String> membersId = service.getMembersId(planId);
		System.out.println(membersId);
		if(membersId!=null) {
			return new ResponseEntity<List<String>>(membersId, HttpStatus.OK);
		}
		return new ResponseEntity<List<String>>(HttpStatus.OK);
	}
	
	@PostMapping("invite/accept")
	public ResponseEntity<String> accept(@RequestBody Map<String, Object> data) {
		long planId = Long.parseLong((String)data.get("acceptedPlanId"));
		String userId = (String)data.get("userId");
		if(service.addMember(planId, userId)==1) {
			return new ResponseEntity<String>("add member success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("add member failed", HttpStatus.OK);
	}
	
	@PostMapping("invite/refuse")
	public ResponseEntity<String> refuse(@RequestBody Map<String, Object> data) {
		long planId = Long.parseLong((String)data.get("refusedPlanId"));
		String userId = (String)data.get("userId");
		if(service.removeMember(planId, userId)==1) {
			return new ResponseEntity<String>("remove member success", HttpStatus.OK);
		}
		return new ResponseEntity<String>("remove member failed", HttpStatus.OK);
	}
}
