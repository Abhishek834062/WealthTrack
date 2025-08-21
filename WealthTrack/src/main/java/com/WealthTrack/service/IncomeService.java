package com.WealthTrack.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.WealthTrack.dto.IncomeDTO;
import com.WealthTrack.entity.CategoryEntity;

import com.WealthTrack.entity.IncomeEntity;
import com.WealthTrack.entity.ProfileEntity;
import com.WealthTrack.repository.CategoryRepository;
import com.WealthTrack.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {
	
	private final IncomeRepository incomeRepository;
	private final ProfileService profileService;
	private final CategoryRepository categoryRepository;
	
	public IncomeDTO addIncome(IncomeDTO dto)
	{
	ProfileEntity currentProfile = profileService.getCurrentProfile();
	CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
			.orElseThrow(()-> new RuntimeException("Category not found"));
	IncomeEntity newExpense =toEntity(dto, currentProfile, category);
	newExpense=incomeRepository.save(newExpense);
	
	return toDto(newExpense);
}
	//retrive Income
		public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser()
		{
			ProfileEntity currentProfile = profileService.getCurrentProfile();
			
			LocalDate now=LocalDate.now();
			LocalDate startDate=now.withDayOfMonth(1);
			LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
			List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(currentProfile.getId(), startDate, endDate);
			
			return list.stream().map(this::toDto).toList();
		}
		
		//delete income
		public void deleteIncome(Long incomeId)
		{
			ProfileEntity currentProfile = profileService.getCurrentProfile();
		    IncomeEntity entity = incomeRepository.findById(incomeId)
		    		.orElseThrow(()-> new RuntimeException("Expense Not found"));
		    
		    if (!entity.getProfile().getId().equals(currentProfile.getId())) {
		        throw new RuntimeException("Unauthorized to delete this income");
		    }
		   
		    incomeRepository.delete(entity);
		}

//helper methods
public IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity)
{
	return IncomeEntity.builder()
			.name(incomeDTO.getName())
			.icon(incomeDTO.getIcon())
			.amount(incomeDTO.getAmount())
			.date(incomeDTO.getDate())
			.category(categoryEntity)
			.profile(profileEntity)
			.build();
}

public IncomeDTO toDto(IncomeEntity incomeEntity)
{
	return IncomeDTO.builder()
			.id(incomeEntity.getId())
			.name(incomeEntity.getName())
			.icon(incomeEntity.getIcon())
			.amount(incomeEntity.getAmount())
			.date(incomeEntity.getDate())
			.createdAt(incomeEntity.getCreatedAt())
			.updatedAt(incomeEntity.getUpdatedAt())
			.categoryId(incomeEntity.getCategory() !=null? incomeEntity.getCategory().getId():null)
			.categoryName(incomeEntity.getName() !=null? incomeEntity.getName():null)
			.build();
}

	

}
