package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.entites.dto.OcenaDTO;

public interface OcenaDao {

	public ResponseEntity<?> createOcena (OcenaDTO newOcena);

	public ResponseEntity<?> izracunajZakljucnu (Integer ucenikId, Integer predmetId);

	public ResponseEntity<?> zakljuciOcenu(Integer ucenikId, Integer predmetId, Integer suggestion);

	ResponseEntity<?> updateOcena(Integer id, OcenaDTO updateOcena);
}
