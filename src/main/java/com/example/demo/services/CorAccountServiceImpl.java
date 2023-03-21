package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.auth.CustomPasswordEncoder;
import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.repository.AppRoleRepository;
import com.example.demo.repository.AppUserRepository;


@Service
@Transactional
public class CorAccountServiceImpl implements AccountService,UserDetailsService {
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private AppRoleRepository appRoleRepository;
	
	@Autowired
	private CustomPasswordEncoder passwordEncoder;

	@Override
	public AppUser addNewUser(AppUser appUser) {
		/*on profite du moment ou il s'enregistre pour rÃ©cuperer son mot de passe
		 * pour le crypter avec le composant CustomPasswordEncoder qui fait cryptage + salage
		 */
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser foundUser = appUserRepository.findByUsername(username);
		if(foundUser == null) {
			throw new UsernameNotFoundException(username);
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		foundUser.getAppRoles().forEach(r -> {
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		return new User(foundUser.getUsername(), foundUser.getPwd(), authorities);
	}
	
	
}

