package com.example.demo.domain;

import lombok.Data;

//페이지를 구성하는 데이터들의 기준
@Data
public class CriteriaJ {
	private int start = 1;
	private int pageNum;
	private int amount;
	private String keywordType;
	private String keyword;
	private int startRow;
	private String category = "";
	
	public CriteriaJ() {
		//this() : 현재 클래스의 생성자
		//Criteria(int pageNum, int amount) 이 생성자를 호출했다고 보면됨
		this(1, 20);
	}
	
	public CriteriaJ(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
		this.startRow = (this.pageNum - 1) * this.amount;
	}
	
	//pageNum이 바뀔때 마다 startRow 같이 바꿔주기
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
		this.startRow = (this.pageNum - 1) * this.amount;
	}
	
}
