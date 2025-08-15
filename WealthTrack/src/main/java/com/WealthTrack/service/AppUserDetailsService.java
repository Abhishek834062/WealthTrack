package com.WealthTrack.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.WealthTrack.entity.ProfileEntity;
import com.WealthTrack.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

	private final ProfileRepository profileRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		ProfileEntity  exitsProfileEntity=profileRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Profile not found with Email:-"+email));
		
		
		 
		return User.builder()
				.username(exitsProfileEntity.getEmail())
				.password(exitsProfileEntity.getPassword())
				.authorities(Collections.emptyList())
				.build();
	}

}
