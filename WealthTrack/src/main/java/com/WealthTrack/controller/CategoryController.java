package com.WealthTrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
