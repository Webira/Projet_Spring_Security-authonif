package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.auth.CustomPasswordEncoder;
import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.repository.AppRoleRepository;
import com.example.demo.repository.AppUserRepository;



@Service
@Transactional

public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AppRoleRepository appRoleRepository;
	
	@Autowired
	private AppUserRepository appUserRepository;
	
		//injecter @Bean
	@Autowired
	private CustomPasswordEncoder passwordEncoder;  //pour match()
	//private PasswordEncoder passwordEncoder;

	//recuperer le mdp saisié par user, creer new mdp crypté et salté avec CustomPasswordEncoder, remplaser
	@Override
	public AppUser addNewUser(AppUser appUser) {
		String pwd = appUser.getPwd();
		String pwdEncrypted = passwordEncoder.encode(pwd); 
		appUser.setPwd(pwdEncrypted);
		return appUserRepository.save(appUser);
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser foundUser = appUserRepository.findByUsername(username);
		AppRole foundRole = appRoleRepository.findByRoleName(roleName);
		foundUser.getAppRoles().add(foundRole);
	}

	@Override
	public boolean checkPassword(String username, String pwd) {
		AppUser foundUser = appUserRepository.findByUsername(username);
		if(foundUser == null) {
			return false;
		}
		String encodedPassword = foundUser.getPwd();
		return passwordEncoder.matches(pwd, encodedPassword);
	}
	
	

}
