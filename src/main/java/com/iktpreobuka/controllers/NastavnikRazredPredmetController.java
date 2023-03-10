package com.iktpreobuka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Nastavnik_Razred_Predmet;
import com.iktpreobuka.repositories.NastavnikRazredPredmetRepository;
import com.iktpreobuka.services.NastavnikRazredPredmetDao;


@RestController
@RequestMapping(path = "/ednevnik/nrp")
public class NastavnikRazredPredmetController {

	@Autowired
	NastavnikRazredPredmetRepository nastavnikRazredPredmetRepo;

	@Autowired
	NastavnikRazredPredmetDao nastavnikRazredPredmetDao;

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Nastavnik_Razred_Predmet> rezultat = nastavnikRazredPredmetRepo.findAll();
			return new ResponseEntity<Iterable<Nastavnik_Razred_Predmet>>(rezultat, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET, value = "/by_id/{ids}")
	public ResponseEntity<?> getById(@PathVariable String ids) {
		Integer id = Integer.valueOf(ids);

		try {
			if (nastavnikRazredPredmetRepo.existsById(id)) {
				return new ResponseEntity<Nastavnik_Razred_Predmet>(nastavnikRazredPredmetRepo.findById(id).get(),
						HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(
						new RestError(10, "Ne postoji veza NRP sa zadatim ID."), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "update/{ids}")
	public ResponseEntity<?> updateNastavnikRazredPredmet(@PathVariable String ids, @RequestParam String nastavnikIds,
			@RequestParam String razPredIds) {

		try {
			Integer id = Integer.valueOf(ids);
			Integer nastavnikId = Integer.valueOf(nastavnikIds);
			Integer razPredId = Integer.valueOf(razPredIds);

			return nastavnikRazredPredmetDao.updateNrp(id, nastavnikId, razPredId);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeNastavnikRazredPredmet(@PathVariable String ids) {

		try {
			Integer id = Integer.valueOf(ids);
			if (nastavnikRazredPredmetRepo.existsById(id)) {
				Nastavnik_Razred_Predmet np = nastavnikRazredPredmetRepo.findById(id).get();
				nastavnikRazredPredmetRepo.deleteById(id);
				return new ResponseEntity<Nastavnik_Razred_Predmet>(np, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(
						new RestError(10, "Ne postoji veza NRP sa zadatim ID."), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
