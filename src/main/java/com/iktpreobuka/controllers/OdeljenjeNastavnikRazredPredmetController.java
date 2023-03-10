package com.iktpreobuka.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.dto.OdeljenjeNastavnikRazredPredmetDTO;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.services.OdeljenjeNastavnikRazredPredmetDao;


@RestController
@RequestMapping(path = "ednevnik/onrp")
public class OdeljenjeNastavnikRazredPredmetController {

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository odeljenjeNastavnikRazredPredmetRepo;

	@Autowired
	OdeljenjeNastavnikRazredPredmetDao odeljenjeNastavnikRazredPredmetDao;

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Odeljenje_Nastavnik_Razred_Predmet> rezultat = odeljenjeNastavnikRazredPredmetRepo.findAll();
			return new ResponseEntity<Iterable<Odeljenje_Nastavnik_Razred_Predmet>>(rezultat, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET, value = "/by_id/{ids}")
	public ResponseEntity<?> getById(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (odeljenjeNastavnikRazredPredmetRepo.existsById(id)) {
				return new ResponseEntity<Odeljenje_Nastavnik_Razred_Predmet>(odeljenjeNastavnikRazredPredmetRepo.findById(id).get(),
						HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(
						new RestError(10, "Ne postoji veza ONRP sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "update/{ids}")
	public ResponseEntity<?> updateOdeljenjeNastavnikRazredPredmet(@PathVariable String ids, @Valid @RequestBody OdeljenjeNastavnikRazredPredmetDTO onrp,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			Integer id = Integer.valueOf(ids);
			return odeljenjeNastavnikRazredPredmetDao.putOnrp(id, onrp);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeOdeljenjeNastavnikRazredPredmet(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			return odeljenjeNastavnikRazredPredmetDao.deleteOnrp(id);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET, value = "/uceniciuodeljenju/{ids}")
	public ResponseEntity<?> returnPupilsConnected(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (odeljenjeNastavnikRazredPredmetRepo.existsById(id)) {
				Odeljenje_Nastavnik_Razred_Predmet onrp = odeljenjeNastavnikRazredPredmetRepo.findById(id).get();
				List<Ucenik> ucenik = onrp.getOdeljenje().getUcenik();
				return new ResponseEntity<List<Ucenik>>(ucenik, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(
						new RestError(10, "Ne postoji veza ONRP sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(""));
	}

}
