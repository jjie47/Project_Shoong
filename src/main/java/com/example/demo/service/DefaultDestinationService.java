package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.DefaultDestinationDTO;


public interface DefaultDestinationService {
	List<DefaultDestinationDTO> getList(String keyword, String continent);
	DefaultDestinationDTO getDestination(int destinationId);
}
