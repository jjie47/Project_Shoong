package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.PlaceDTO;
@Mapper
public interface PlaceMapper {

	List<Long> getMyPlaceByDestIds(List<Long> destIds,List<Long> planIds);

	int insertPlace(Set<PlaceDTO> places);
	int insertPlaces(PlaceDTO place);
	
	List<PlaceDTO> getPlaceByPlanId(Long planId);
	List<PlaceDTO> getPlacesByDestinationId(Long planId, Long destinationId);
}
