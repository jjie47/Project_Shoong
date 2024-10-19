package com.example.demo.service;

import org.springframework.stereotype.Service;
@Service
public interface CommentService {

	long getCount(long reviewId);
	
}
