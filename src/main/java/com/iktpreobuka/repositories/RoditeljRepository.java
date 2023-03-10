package com.iktpreobuka.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.Roditelj;

public interface RoditeljRepository extends CrudRepository<Roditelj, Integer> {

	Optional<Roditelj> findByEmail(String email);
}
