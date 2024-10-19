package com.example.demo.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PhotoServiceImple implements PhotoService{

	@Value("${file.dir}")
	private String saveFolder;

	@Override
	public HashMap<String, Object> getServeResource(String systemName) throws Exception {
		Path path = Paths.get(saveFolder+systemName);
		String contentType = Files.probeContentType(path);
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		HashMap<String, Object> datas = new HashMap<>();
		datas.put("contentType", contentType);
		datas.put("resource", resource);
		return datas;
	}
	
}
