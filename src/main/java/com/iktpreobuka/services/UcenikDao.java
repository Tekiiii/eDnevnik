package com.iktpreobuka.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.dto.UserDTO;


public interface UcenikDao {

	public ResponseEntity<?> postDodajUcenika(Ucenik ucenik, UserDTO newUcenik);

	public void removeRoditelj(Ucenik ucenik);

	public void addRoditelj(Ucenik ucenik, UserDTO roditelj);

	public ResponseEntity<?> addOdeljenje(Ucenik ucenik, Integer odeljenjeId);

	public ResponseEntity<?> getOcena(Integer ucenikId, Integer predmetId);

	public ResponseEntity<?> getOcenaPredmet(Integer ucenikId);

	public Boolean isItARoditelj(Integer ucenik, Integer roditelj);

	

}
