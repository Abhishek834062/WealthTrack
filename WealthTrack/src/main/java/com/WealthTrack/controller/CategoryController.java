package com.WealthTrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WealthTrack.dto.CategoryDTO;
import com.WealthTrack.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<CategoryDTO>  saveCategorg( @RequestBody CategoryDTO categoryDTO)
	{
		CategoryDTO saveCategory = categoryService.saveCategory(categoryDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveCategory);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>>  getCategories()
	{
		List<CategoryDTO> categories = categoryService.getCategoriesForCurrentUser();
		
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/{type}")
	public ResponseEntity<List<CategoryDTO>>  getCategoriesByType(@PathVariable String type)
	{
		List<CategoryDTO> categories = categoryService.getCategoriesByTypeForCurrentUser(type);
		
		return ResponseEntity.ok(categories);
	}
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO)
	{
		CategoryDTO updateCategories = categoryService.updateCategories(categoryId, categoryDTO);
		return ResponseEntity.ok(updateCategories);
	}
}
