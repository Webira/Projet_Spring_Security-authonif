package com.example.demo.auth;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final SecureRandom secureRandom;
	
	public CustomPasswordEncoder() {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
		this.secureRandom = new SecureRandom();
	}
	
	//definir methode salt
	@Override
	public String encode(CharSequence rawPassword) {
		String salt = generateSalt();
		String saltedPassword = rawPassword + salt;
		return bCryptPasswordEncoder.encode(saltedPassword);
	}
		//creer une methode generateSalt() pour crrer une chaine de character alleatoire
	private String generateSalt() {
		byte[] saltBytes = new byte[16];
		secureRandom.nextBytes(saltBytes);
		return new String(saltBytes);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		//declare salt et le recuperrer dans mdp encodé:
		//on extrait le sel du mdp encrypté dans la bd. Le salt correspond au 16 premier caracteres
		String salt = encodedPassword.substring(0,16);
		//on concatene le "mdp en clair" avec le sel extrait juste avant pour obtenir le mdp salé
		String saltedPassword = rawPassword + salt;
		//on vient extraire le hachage stocké en bdd avec le substring à partir du 16eme caractere
		String hashedPassword = encodedPassword.substring(16);
		//on matches pour comparer les 2 empruntes et verifier si elles correspondent
		return bCryptPasswordEncoder.matches(saltedPassword, hashedPassword);
	}

}
