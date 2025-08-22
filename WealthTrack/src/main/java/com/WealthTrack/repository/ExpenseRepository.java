package com.WealthTrack.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WealthTrack.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{
	
	//select * from tbl_expense where profile_id=?1 order by date desc
		List<ExpenseEntity>  findByProfileIdOrderByDateDesc(Long profileId);
		
		//select * from tbl_expense where profile_id=?1 order by date desc limit 5 only
		List<ExpenseEntity>  findTop5ByProfileIdOrderByDateDesc(Long profileId);
		
		@Query("select sum(e.amount) from ExpenseEntity e where e.profile.Id=:profileId")
		BigDecimal findTotalExpenseByProfileId( @Param ("profileId") Long profileId);
		
		//select * from tbl_expense where profile_id=?1  and date between ?2 and ?3 and name like %?4 
		List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
				Long profileId,
				LocalDate startDate,
				LocalDate endDate,
				String keyword,
				Sort sort
				);
		
		List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate, LocalDate endDate);
		
		List<ExpenseEntity> findByProfileIdAndDate(Long profileId,LocalDate date);
}
