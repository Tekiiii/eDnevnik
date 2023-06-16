package com.iktpreobuka.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.Razred_Predmet;

//onrp ODeljenjeNastavnikPredmet
//nrp NastavnikRazredPredmet


public interface RazredPredmetRepository extends CrudRepository<Razred_Predmet, Integer> {

	Optional<Razred_Predmet> findByRazred_idAndPredmet_id(Integer razredIs, Long pred);

	Razred_Predmet findByRazred_label(Integer label);

	List<Razred_Predmet> findByRazred_LabelIsLessThan(Integer label);

	List<Razred_Predmet> findByPredmet_idAndRazred_LabelIsGreaterThan(Integer id, Integer label);
	
}
