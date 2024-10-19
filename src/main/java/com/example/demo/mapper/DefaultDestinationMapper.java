package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.DefaultDestinationDTO;

@Mapper
public interface DefaultDestinationMapper {
	DefaultDestinationDTO getDestinationById(int destinationId);
	List<DefaultDestinationDTO> getDestinationsByKeyword(String keyword);
	List<DefaultDestinationDTO> getDestinationsByKeyAndCont(String keyword, String continent);
}
