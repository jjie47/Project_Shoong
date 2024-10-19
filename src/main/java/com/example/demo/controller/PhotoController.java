package com.example.demo.controller;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.PhotoService;



@Controller
@RequestMapping("/file/*")
public class PhotoController {

	@Autowired
	private PhotoService photoService;
	
	@GetMapping("{systemName}")
	public ResponseEntity<Resource> serveResource(@PathVariable String systemName) throws Exception {
		HashMap<String, Object> datas = photoService.getServeResource(systemName);
		String contentType = (String)datas.get("contentType");
		Resource resource = (Resource)datas.get("resource");
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
	}
}
