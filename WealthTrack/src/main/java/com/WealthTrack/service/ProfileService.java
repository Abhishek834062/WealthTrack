package com.WealthTrack.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.WealthTrack.dto.ProfileDto;
import com.WealthTrack.entity.ProfileEntity;
import com.WealthTrack.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	
	private final ProfileRepository profileRepository;
	private final EmailService emailService;
	
	public ProfileDto registerProfile(ProfileDto profiledoDto)
	{
		ProfileEntity newProfile = toEntity(profiledoDto);
		newProfile.setActivationToken(UUID.randomUUID().toString());
		newProfile = profileRepository.save(newProfile);
	
		//send activation email
		String activationLink="http://localhost:8080/api/v1.0/activate?token="+newProfile.getActivationToken();
		String subject="Activate your WeatlhTrack account";
		String body="Click on the following link to activate your account:-"+activationLink;
		emailService.sendEmail(newProfile.getEmail(), subject, body);
		
		
		
		return toDto(newProfile);
	}
	
	public ProfileEntity toEntity( ProfileDto profileDto)
	{
		return ProfileEntity.builder()
				.id(profileDto.getId())
				.fullName(profileDto.getFullName())
				.email(profileDto.getEmail())
				.password(profileDto.getPassword())
				.profileImageUrl(profileDto.getProfileImageUrl())
				.createdAt(profileDto.getCreatedAt())
				.updatedAt(profileDto.getUpdatedAt())
				.build();
	}
	
	public ProfileDto toDto(ProfileEntity profileEntity)
	{
		return ProfileDto.builder()
				.id(profileEntity.getId())
				.fullName(profileEntity.getFullName())
				.email(profileEntity.getEmail())
				.profileImageUrl(profileEntity.getProfileImageUrl())
				.createdAt(profileEntity.getCreatedAt())
				.updatedAt(profileEntity.getUpdatedAt())
				
				.build();
	}
	
	public boolean activateProfile(String activationToken) {
	    return profileRepository.findByActivationToken(activationToken)
	            .map(profile -> {
	                profile.setIsActive(true);  // Activate the profile
	                profileRepository.save(profile);
	                return true;
	            })
	            .orElse(false);  
	}


}
