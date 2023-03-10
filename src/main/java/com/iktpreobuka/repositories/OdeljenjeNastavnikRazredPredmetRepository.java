package com.iktpreobuka.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.Nastavnik;
import com.iktpreobuka.entites.Odeljenje;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.Predmet;

public interface OdeljenjeNastavnikRazredPredmetRepository extends CrudRepository<Odeljenje_Nastavnik_Razred_Predmet, Integer> {

	Optional<Odeljenje_Nastavnik_Razred_Predmet> findByOdeljenjeAndNrp_RazredPredmet_Predmet(Odeljenje odeljenje,
			Predmet predmet);

	List<Odeljenje_Nastavnik_Razred_Predmet> findByOdeljenjeAndNrp_RazredPredmet_Predmet_id(Odeljenje odeljenje,
			Integer predmetId);;

	List<Odeljenje_Nastavnik_Razred_Predmet> findByNrp_Nastavnik(Nastavnik nastavnik);

	List<Odeljenje_Nastavnik_Razred_Predmet> findByNrp_NastavnikIdAndActive(Integer nastavnikId, Boolean ac);

	List<Odeljenje_Nastavnik_Razred_Predmet> findByOdeljenjeAndActive(Odeljenje odeljenje, Boolean ac);

	List<Odeljenje_Nastavnik_Razred_Predmet> findByOdeljenje(Odeljenje odeljenje);

	Optional<Odeljenje_Nastavnik_Razred_Predmet> findByOdeljenje_IdAndNrp_idAndActive(Integer odeljenjeId, Integer nrpId, Boolean ac);
}
