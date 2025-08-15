package com.WealthTrack.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;
	
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
				.password(passwordEncoder.encode(profileDto.getPassword()))
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
	
	public boolean isAccountActive(String email)
	{
		return profileRepository.findByEmail(email)
				.map(ProfileEntity::getIsActive)
				.orElse(false);
	}
	
	public ProfileEntity getCurrentProfile()
	{
	  Authentication authentication=	SecurityContextHolder.getContext().getAuthentication();
	  return profileRepository.findByEmail(authentication.getName())
			  .orElseThrow(()-> new UsernameNotFoundException("Profile not found with email:-"+ authentication.getName()));
	
	}
	
	public ProfileDto getPublicProfile(String email)
	{
		ProfileEntity currentUser=null;
		if(email==null)
		{
			currentUser=getCurrentProfile();
		}
		else {
			currentUser=profileRepository.findByEmail(email)
					.orElseThrow(()-> new UsernameNotFoundException("Profile not found with email"+email));
		}
		
		return ProfileDto.builder()
				.id(currentUser.getId())
				.fullName(currentUser.getFullName())
				.email(currentUser.getEmail())
				.profileImageUrl(currentUser.getProfileImageUrl())
				.createdAt(currentUser.getCreatedAt())
				.updatedAt(currentUser.getUpdatedAt())
				.build();
	}
	
	


}
