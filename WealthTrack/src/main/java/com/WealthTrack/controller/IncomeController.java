package com.WealthTrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WealthTrack.dto.IncomeDTO;
import com.WealthTrack.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

	private final IncomeService incomeService;
	
	@PostMapping
	public ResponseEntity<IncomeDTO> addIncome( @RequestBody IncomeDTO dto)
	{
		IncomeDTO income = incomeService.addIncome(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(income);
	}
}
