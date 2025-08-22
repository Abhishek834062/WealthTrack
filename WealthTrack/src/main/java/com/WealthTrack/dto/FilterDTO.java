package com.WealthTrack.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FilterDTO {

	private String type;
	private LocalDate startDate;
	private LocalDate endDate;
	private String keyword;
	private String sortfield; //date amount name
	private String sortOrder; //asc and desc
}
