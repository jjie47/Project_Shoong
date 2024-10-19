package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.DefaultDestinationDTO;
import com.example.demo.mapper.DefaultDestinationMapper;

@Service
public class DefaultDestinationServiceImpl implements DefaultDestinationService {

	@Autowired
	private DefaultDestinationMapper ddmapper;
	
	@Override
	public List<DefaultDestinationDTO> getList(String keyword, String continent) {
		List<DefaultDestinationDTO> list = new ArrayList<>();
		
		if(continent.equals("전체")) {
			list = ddmapper.getDestinationsByKeyword(keyword);
//			System.out.println(list);
		}
		else {
			list = ddmapper.getDestinationsByKeyAndCont(keyword, continent);
		}
		return list;
	}
	
	@Override
	public DefaultDestinationDTO getDestination(int destinationId) {
		return ddmapper.getDestinationById(destinationId);
	}
}
