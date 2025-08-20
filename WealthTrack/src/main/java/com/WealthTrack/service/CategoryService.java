package com.WealthTrack.service;

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
