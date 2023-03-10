package com.iktpreobuka.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.OznakaOcene;

public interface OznakaOceneRepository extends CrudRepository<OznakaOcene, Integer> {

	Optional<OznakaOcene> findByOznaka(String oznaka);
}
