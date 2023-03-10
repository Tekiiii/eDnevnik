package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.entites.dto.PripadaRazredPredmetDTO;

public interface PredmetDao {

	public ResponseEntity<?> addRazred(Integer pred, PripadaRazredPredmetDTO razPred);

	public ResponseEntity<?> addOcena(Integer ocena, PripadaRazredPredmetDTO ocenaPredmet);
}
