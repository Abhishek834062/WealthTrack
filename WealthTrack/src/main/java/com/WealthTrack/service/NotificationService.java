package com.WealthTrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.WealthTrack.entity.ProfileEntity;
import com.WealthTrack.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final ProfileRepository profileRepository;
	private final IncomeService incomeService;
	private final ExpenseService expenseService;
	private final EmailService emailService;
	
	@Value("${wealth.track.fronted.url}")
	private String frontedUrl;
	
	@Scheduled(cron ="0 * * * * *",zone = "IST")
	//@Scheduled(cron ="0 0 22 * * *",zone = "IST")
	public void sendDailyIncomeExpenseReminder()
	{
		log.info("Job started: sendDailyIncomeExpenseReminder()");
		List<ProfileEntity> profiles = profileRepository.findAll();
		for(ProfileEntity profile:profiles)
		{
			String body = "Hi " + profile.getFullName() + ",<br><br>"
			        + "This is a friendly reminder to add your income and expenses for today in Wealth Track Application,<br><br> "
			        + "<a href='" + frontedUrl + "' style='display:inline-block; padding:10px 20px; background-color:#4CAF50; color:#fff; text-decoration:none; border-radius:5px; font-weight:bold;'>Go to WealthTrack</a>"
			        + "<br><br> Best regards,<br> WealthTrack Team";

		emailService.sendEmail(profile.getEmail(),"Daily reminder: Add your income and expenses", body);
		}
		log.info("Job Completed: sednDailyIncomeExpenseReminder()");
	}
	
	
}
