package com.example.demo.domain;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class PlanDetailsDTO {
	private PlanDTO plan;
    private List<DestinationDTO> destinations;
    private List<PlaceDTO> places;
    private List<CostDTO> costs;
    private List<GroupDTO> groups;
    private List<UserDTO> users;
    private List<ReviewDTO> reviews;
    private int daysCount;
    private int likedCount;
    private int likedCheck;
    private String leaderNick; //그룹장 닉네임
    private List<List<HashMap<String, Object>>> planDataList;
}
