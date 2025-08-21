package com.WealthTrack.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.WealthTrack.dto.ExpenseDTO;
import com.WealthTrack.entity.CategoryEntity;
import com.WealthTrack.entity.ExpenseEntity;
import com.WealthTrack.entity.IncomeEntity;
import com.WealthTrack.entity.ProfileEntity;
import com.WealthTrack.repository.CategoryRepository;
import com.WealthTrack.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final ProfileService profileService;
	private final CategoryRepository categoryRepository;

	public ExpenseDTO addExpense(ExpenseDTO dto) {
		ProfileEntity currentProfile = profileService.getCurrentProfile();
		CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		ExpenseEntity newExpense = toEntity(dto, currentProfile, category);
		newExpense = expenseRepository.save(newExpense);

		return toDto(newExpense);
	}

	// retrive expenses
	public List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser() {
		ProfileEntity currentProfile = profileService.getCurrentProfile();

		LocalDate now = LocalDate.now();
		LocalDate startDate = now.withDayOfMonth(1);
		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
		List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(currentProfile.getId(), startDate,
				endDate);

		return list.stream().map(this::toDto).toList();
	}

	// delete income
	public void deleteIncome(Long expenseId) {
		ProfileEntity currentProfile = profileService.getCurrentProfile();
		ExpenseEntity entity = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new RuntimeException("Expense Not found"));

		if (!entity.getProfile().getId().equals(currentProfile.getId())) {
			throw new RuntimeException("Unauthorized to delete this income");
		}

		expenseRepository.delete(entity);
	}

	// helper methods
	public ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
		return ExpenseEntity.builder().name(expenseDTO.getName()).icon(expenseDTO.getIcon())
				.amount(expenseDTO.getAmount()).date(expenseDTO.getDate()).category(categoryEntity)
				.profile(profileEntity).build();
	}

	public ExpenseDTO toDto(ExpenseEntity expenseEntity) {
		return ExpenseDTO.builder().id(expenseEntity.getId()).name(expenseEntity.getName())
				.icon(expenseEntity.getIcon()).amount(expenseEntity.getAmount()).date(expenseEntity.getDate())
				.createdAt(expenseEntity.getCreatedAt()).updatedAt(expenseEntity.getUpdatedAt())
				.categoryId(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getId() : null)
				.categoryName(expenseEntity.getName() != null ? expenseEntity.getName() : null).build();
	}

}
