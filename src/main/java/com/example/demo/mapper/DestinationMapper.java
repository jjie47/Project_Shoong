package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.DefaultDestinationDTO;
import com.example.demo.domain.DestinationDTO;

@Mapper
public interface DestinationMapper {

	List<DestinationDTO> getMyDestinationByPlanIds(List<Long> planIds);

	DestinationDTO getMyfirst(long planId);

	List<Long> getMyDestCountByPlanIds(List<Long> planIds);

	int insertDestination(Set<DestinationDTO> destinations, long planId);
	List<Map<String, Object>> getDestinationsByPlanId(long planId);
	
	List<DestinationDTO> getDestinationByPlanId(Long planId);
	DestinationDTO getDestinationById(Long destinationId);
}
