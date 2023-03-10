package com.iktpreobuka.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.Nastavnik_Razred_Predmet;

public interface NastavnikRazredPredmetRepository extends CrudRepository<Nastavnik_Razred_Predmet, Integer> {

	Optional<Nastavnik_Razred_Predmet> findByNastavnik_idAndRazredPredmet_id(Integer nastavnikId, Integer rpId);

	List<Nastavnik_Razred_Predmet> findByNastavnik_id(Integer id);
}
