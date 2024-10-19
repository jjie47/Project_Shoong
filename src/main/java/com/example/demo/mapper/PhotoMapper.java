package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.PhotoDTO;

@Mapper
public interface PhotoMapper {
	int insertPhoto(PhotoDTO photo);
	
	
}