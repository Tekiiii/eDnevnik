/*package com.iktpreobuka.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.entites.UserEntity;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.UserRepository;
import com.iktpreobuka.util.Encryption;

import io.jsonwebtoken.Jwts;

@RestController
public class UserController {

	@Autowired
	private SecretKey secretKey;
	@Value("${spring.security.token-duration}")
	private Integer tokenDuration;
	
	@Autowired
	private UserRepository userRepository;
	
	
	private String getJWTToken(UserEntity userEntity) {
		List<GrantedAuthority> grantedAuthorities= AuthorityUtils
		.commaSeparatedStringToAuthorityList(userEntity.getRole().name());
		String token= Jwts.builder().setId("softtekJWT").setSubject(userEntity.getEmail())
		.claim("authorities", grantedAuthorities.stream()
		.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + this.tokenDuration))
		.signWith(this.secretKey).compact();
		return"Bearer "+ token;
	}
	
	@RequestMapping(path = "/ednevnik/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam("user") String email, @RequestParam("password") String pwd) {
	UserEntity userEntity= userRepository.findByEmail(email);
	if(userEntity!= null&& Encryption.checkPassword(pwd, userEntity.getPassword())) {
	String token= getJWTToken(userEntity);
	UserDTO user= new UserDTO();
	user.setUser(email);
	user.setToken(token);
	return new ResponseEntity<>(user, HttpStatus.OK);
	}
	return new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED);
	}
	//@Secured("USER")
	@RequestMapping(path = "/ednevnik/users", method = RequestMethod.GET)
	public ResponseEntity<?> listUsers() {
	return new ResponseEntity<List<UserEntity>>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}

}*/