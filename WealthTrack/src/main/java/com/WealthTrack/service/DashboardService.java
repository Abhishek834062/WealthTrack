package com.WealthTrack.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.WealthTrack.dto.ExpenseDTO;
import com.WealthTrack.dto.IncomeDTO;
import com.WealthTrack.dto.RecentTransactionDTO;
import com.WealthTrack.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

   
	
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	private final ProfileService profileService;

 
	
	public Map<String, Object> getDashboardData()
	{
		ProfileEntity currentProfile = profileService.getCurrentProfile();
	    Map<String, Object> returnValue = new LinkedHashMap<>();
	    List<IncomeDTO> latestIncome = incomeService.getLatest5IncomeForCurrentUser();
	    List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpenseForCurrentUser();
	    
	     List<RecentTransactionDTO> recentTransaction=Stream.concat(latestIncome.stream().map(income->
	      RecentTransactionDTO.builder()
	                            .id(income.getId())
	                            .name(income.getName())
	                            .profileId(currentProfile.getId())
	                            .icon(income.getIcon())
	                            .amount(income.getAmount())
	                            .date(income.getDate())
	                            .createAt(income.getCreatedAt())
	                            .updateAt(income.getUpdatedAt())
	                            .type("income")
	                            .build()),
	      
	      latestExpense.stream().map(expense->
	      RecentTransactionDTO.builder()
	                            .id(expense.getId())
	                            .name(expense.getName())
	                            .profileId(currentProfile.getId())
	                            .icon(expense.getIcon())
	                            .amount(expense.getAmount())
	                            .date(expense.getDate())
	                            .createAt(expense.getCreatedAt())
	                            .updateAt(expense.getUpdatedAt())
	                            .type("expense")
	                            .build()))
	      .sorted((a,b)->
	      {
	    	  int cmp=b.getDate().compareTo(a.getDate());
	    	  if(cmp==0 && a.getCreateAt()!=null && b.getCreateAt()!=null)
	    	  {
	    		  return b.getCreateAt().compareTo(a.getCreateAt());
	    	  }
	    	  return cmp;
	      }).collect(Collectors.toList());
	  
	     returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
	     returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
	     returnValue.put("totalBalance",incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()));
	     
	    returnValue.put("recent5Income", latestIncome);
	    returnValue.put("recent5Expense", latestExpense);
	    returnValue.put("recentTransaction", recentTransaction);
	    
	    return returnValue;
	    
	    
	}

}
