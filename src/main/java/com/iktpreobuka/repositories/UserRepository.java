package com.iktpreobuka.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	UserEntity findByEmail(String email);

}
