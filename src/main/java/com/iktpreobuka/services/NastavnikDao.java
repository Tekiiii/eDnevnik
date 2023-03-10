package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.entites.Nastavnik;
import com.iktpreobuka.entites.dto.UserDTO;

public interface NastavnikDao {

	ResponseEntity<?> addRazredPredmet(Integer nastavnik, Integer razredPredmet);
	
	ResponseEntity<?> ucitelj(Integer nastavnikId);

	ResponseEntity<?> predmetNastavnik(Integer id, Integer predmetId);

	ResponseEntity<?> postDodajNastavnika(Nastavnik nastavnik, UserDTO newNastavnik);

	void setNotActiveOnrp(Nastavnik nastavnik);

	ResponseEntity<?> getONRP(Integer nastavnikId);

	ResponseEntity<?> getUcenikOcena(Integer nastavnikId, Integer onrpId);

}
