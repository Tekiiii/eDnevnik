package com.iktpreobuka.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.entites.Ucenik;

@Repository
public interface UcenikRepository extends CrudRepository<Ucenik, Integer> {

}
