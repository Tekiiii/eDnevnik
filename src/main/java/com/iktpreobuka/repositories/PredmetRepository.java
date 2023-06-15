package com.iktpreobuka.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.Predmet;

public interface PredmetRepository extends CrudRepository<Predmet, Long> {

	Optional<Predmet> findByNameIgnoreCase(String name);
}
