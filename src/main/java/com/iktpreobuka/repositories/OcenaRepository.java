package com.iktpreobuka.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.entites.Ocena;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.Ucenik;

//onrp ODeljenjeNastavnikPredmet
//nrp NastavnikRazredPredmet

public interface OcenaRepository extends CrudRepository<Ocena, Integer> {


	List<Ocena> findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(Ucenik ucenik, Predmet predmet);

	
	List<Ocena> findByUcenik_idAndOnrp_Nrp_RazredPredmet_Predmet_id (Integer ucenikId, Integer predmetId);
		
	
	List<Ocena> findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet_name (Ucenik ucenik, String predmet);

	Optional<Ocena> findByUcenikAndOnrp_Nrp_RazredPredmet_PredmetAndOznaka_Oznaka(Ucenik ucenik, Predmet predmet,
			String string);

	

}
