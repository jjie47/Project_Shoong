package com.example.demo.domain;

import lombok.Data;

@Data
public class PageDTO {
	private int startPage;
	private int endPage;
	private int realEnd;
	private long total;
	private boolean prev,next;
	private int pagenum;
	private CriteriaJ criJ;
	
	public PageDTO(long total,int pagenum) {
		this.pagenum = pagenum;
		this.total = total;
		
		this.endPage = (int)Math.ceil(pagenum/10.0)*10;
		this.startPage = this.endPage-9;
		this.realEnd = (int)Math.ceil(total/10.0);
		
		this.realEnd = this.realEnd == 0 ? 1 : this.realEnd;
		
		this.endPage = this.endPage > this.realEnd ? this.realEnd : this.endPage;
		
		this.prev = this.startPage != 1;
		this.next = this.endPage < this.realEnd;
	}
	
	public PageDTO(long total, CriteriaJ criJ) {
		this.criJ = criJ;
		this.total = total;
		
		this.endPage = (int)Math.ceil(criJ.getPageNum()/10.0)*10;
		this.startPage = this.endPage - 9;
		this.realEnd = (int)Math.ceil(total/(double)criJ.getAmount());
		//this.realEnd가 0일때 시작페이지 1로 변경
		this.realEnd = this.realEnd == 0 ? 1 : this.realEnd;
		this.endPage = this.endPage > this.realEnd ? this.realEnd : this.endPage;
		
		this.prev = this.startPage != 1;
		this.next = this.endPage != this.realEnd;
		
	}
}
