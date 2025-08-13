package com.WealthTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.WealthTrack.entity.ProfileEntity;
import java.util.Optional;



public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
	
	 Optional<ProfileEntity> findByEmail(String email);
	 
	 Optional<ProfileEntity> findByActivationToken(String activationToken);

}
