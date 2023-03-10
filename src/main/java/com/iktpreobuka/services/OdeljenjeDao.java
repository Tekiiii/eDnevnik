package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

public interface OdeljenjeDao {

	public ResponseEntity<?> addNastavnikRazredPredmet(Integer odeljenjeId, Integer nrpId);

	public ResponseEntity<?> addUcenik(Integer odeljenjeId, Integer ucenikId);
	
	public ResponseEntity<?> addRazred(Integer odeljenjeId, Integer razredId);
}
