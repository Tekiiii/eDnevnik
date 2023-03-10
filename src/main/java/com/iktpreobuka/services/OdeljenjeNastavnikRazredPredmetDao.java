package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.entites.dto.OdeljenjeNastavnikRazredPredmetDTO;

public interface OdeljenjeNastavnikRazredPredmetDao {

	public ResponseEntity<?> putOnrp(Integer onrpId, OdeljenjeNastavnikRazredPredmetDTO onrp);

	public ResponseEntity<?> deleteOnrp(Integer id);

}
