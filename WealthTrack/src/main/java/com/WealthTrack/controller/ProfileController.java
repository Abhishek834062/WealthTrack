package com.WealthTrack.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.WealthTrack.dto.AuthDTO;
import com.WealthTrack.dto.ProfileDto;
import com.WealthTrack.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {

   
	
	private final ProfileService profileService;
	
	
	@PostMapping("/register")
	public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto)
	{
		ProfileDto registerProfile = profileService.registerProfile(profileDto);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
	}
	
	@GetMapping("/activate")
	public ResponseEntity<String> activateProfile(@RequestParam String token)
	{
		boolean activateProfile = profileService.activateProfile(token);
		if(activateProfile)
		{
			return ResponseEntity.ok("Profile activated successfully");
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already in used");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO)
	{
		try
		{
			if(!profileService.isAccountActive(authDTO.getEmail()))
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Acount is not active plese active your account first"));
			}
			
			Map<String, Object> response=profileService.authenticationAndGenerateToken(authDTO);
			
			return ResponseEntity.ok(response);
		}
		catch(Exception e)
		{
		   return	ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
		}
		
	}

}
