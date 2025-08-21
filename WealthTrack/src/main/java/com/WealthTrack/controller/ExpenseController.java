package com.WealthTrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WealthTrack.dto.ExpenseDTO;
import com.WealthTrack.dto.IncomeDTO;
import com.WealthTrack.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

	private final ExpenseService expenseService;
	
	@PostMapping
	public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO dto)
	{
		
		ExpenseDTO expense = expenseService.addExpense(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(expense);
	}
	@GetMapping
	public ResponseEntity<List<ExpenseDTO>> getExpense()
	{
		List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpenseForCurrentUser();
		
		return ResponseEntity.ok(expenses);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void>  deleteExpense(@PathVariable Long id)
	{
		expenseService.deleteIncome(id);
		return ResponseEntity.noContent().build();
	}
	
}
