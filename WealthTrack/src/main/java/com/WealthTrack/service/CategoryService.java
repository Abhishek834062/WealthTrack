package com.WealthTrack.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.WealthTrack.dto.CategoryDTO;
import com.WealthTrack.entity.CategoryEntity;
import com.WealthTrack.entity.ProfileEntity;
import com.WealthTrack.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final ProfileService profileService;
	private final CategoryRepository categoryRepository;

	public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
		ProfileEntity profile = profileService.getCurrentProfile();
		if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exist");
		}

		CategoryEntity newCategory = toEntity(categoryDTO, profile);
		newCategory = categoryRepository.save(newCategory);

		return toDTO(newCategory);
	}
	
	public List<CategoryDTO> getCategoriesForCurrentUser()
	{
		ProfileEntity currentProfile = profileService.getCurrentProfile();
		List<CategoryEntity> categories = categoryRepository.findByProfileId(currentProfile.getId());
		
		return categories.stream().map(this::toDTO).toList();
	}
	
	public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type)
	{
		ProfileEntity currentProfile = profileService.getCurrentProfile();
		List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type, currentProfile.getId());
		
		return categories.stream().map(this::toDTO).toList();
	}
	
	public CategoryDTO updateCategories(Long categoryId,CategoryDTO dto)
	{
		ProfileEntity currentProfile = profileService.getCurrentProfile();
		CategoryEntity existingCategories = categoryRepository.findByIdAndProfileId(categoryId, currentProfile.getId())
				.orElseThrow(()->new RuntimeException("Categories not found"));
		existingCategories.setName(dto.getName());
		existingCategories.setType(dto.getType());
		existingCategories.setIcon(dto.getIcon());
		existingCategories = categoryRepository.save(existingCategories);
		return toDTO(existingCategories);
	}
	
	

	// helper method
	private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {
		return CategoryEntity.builder().name(categoryDTO.getName()).icon(categoryDTO.getIcon()).profile(profile)
				.type(categoryDTO.getType()).build();
	}

	private CategoryDTO toDTO(CategoryEntity categoryEntity) {
		return CategoryDTO.builder().id(categoryEntity.getId())
				.profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
				.name(categoryEntity.getName()).icon(categoryEntity.getIcon()).type(categoryEntity.getType())
				.createdAt(categoryEntity.getCreatedAt()).updatedAt(categoryEntity.getUpdatedAt()).build();
	}

}
