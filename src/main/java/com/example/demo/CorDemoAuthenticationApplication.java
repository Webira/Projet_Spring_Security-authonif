package com.example.demo;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.services.AccountService;

@SpringBootApplication
public class CorDemoAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorDemoAuthenticationApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(AccountService accountService) {
		return args -> {
			accountService.addNewRole(new AppRole(null, "USER"));
			accountService.addNewRole(new AppRole(null, "ADMIN"));
			accountService.addNewRole(new AppRole(null, "HR_MANAGER"));
			
			accountService.addNewUser(new AppUser(null, "user1", "1234", new ArrayList<>()));
			accountService.addNewUser(new AppUser(null, "user2", "5678", new ArrayList<>()));
			accountService.addNewUser(new AppUser(null, "admin", "9101", new ArrayList<>()));
			
			accountService.addRoleToUser("user1", "USER");
			accountService.addRoleToUser("user1", "HR_MANAGER");
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
		};
	}

}

