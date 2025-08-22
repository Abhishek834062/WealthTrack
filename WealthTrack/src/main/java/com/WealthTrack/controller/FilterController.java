package com.WealthTrack.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WealthTrack.dto.ExpenseDTO;
import com.WealthTrack.dto.FilterDTO;
import com.WealthTrack.dto.IncomeDTO;
import com.WealthTrack.service.ExpenseService;
import com.WealthTrack.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	
	@PostMapping
	public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter)
	{
		LocalDate startDate=filter.getStartDate()!=null?filter.getStartDate():LocalDate.MIN;
		LocalDate endDate=filter.getEndDate()!=null?filter.getEndDate():LocalDate.now();
		String keyword=filter.getKeyword()!=null?filter.getKeyword():"";
		String sortField=filter.getSortfield()!=null?filter.getSortfield():"date";
		Sort.Direction direction="desc".equalsIgnoreCase(filter.getSortOrder())? Sort.Direction.DESC:Sort.Direction.ASC;
		Sort sort =Sort.by(direction,sortField);
		if("income".equals(filter.getType()))
		{
			List<IncomeDTO> filterIncome = incomeService.filterIncome(startDate, endDate, keyword, sort);
			return ResponseEntity.ok(filterIncome);
		}
		else if("expense".equals(filter.getType()))
		{
			List<ExpenseDTO> filterExpense = expenseService.filterExpense(startDate, endDate, keyword, sort);
			return ResponseEntity.ok(filterExpense);
		}
		else
		{
			return ResponseEntity.badRequest().body("Invalid type Must be income or expense");
		}
	}
}
