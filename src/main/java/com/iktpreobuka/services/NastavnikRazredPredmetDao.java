package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

public interface NastavnikRazredPredmetDao {

	ResponseEntity<?> updateNrp(Integer id, Integer nastavnikId, Integer razredPredmetId);
}
