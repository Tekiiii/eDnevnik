package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.entites.UserEntity;


public interface UserDao {

	public ResponseEntity<?> updatePasword(Integer id, String oldP, String newP);

	public Integer getLoggedInId();

	public UserEntity getLoggedInUser();
}
