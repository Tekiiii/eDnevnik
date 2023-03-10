package com.iktpreobuka.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.iktpreobuka.entites.Razred_Predmet;
import com.iktpreobuka.entites.dto.RazredPredmetDTO;
import com.iktpreobuka.repositories.RazredPredmetRepository;

@RestController
@RequestMapping(path = "/ednevnik/razredPredmet")
public class RazredPredmetController {

	@Autowired
	RazredPredmetRepository razredPredmetRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Razred_Predmet> rezultat = razredPredmetRepo.findAll();
			return new ResponseEntity<Iterable<Razred_Predmet>>(rezultat, HttpStatus.OK);

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
			if (razredPredmetRepo.existsById(id)) {
				return new ResponseEntity<Razred_Predmet>(razredPredmetRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji razred povezan sa predmetom sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "update/{ids}")
	public ResponseEntity<?> updateRazredPredmet(@PathVariable String ids,
			@Valid @RequestBody RazredPredmetDTO updateRazredPredmet, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			Integer id = Integer.valueOf(ids);
			if (razredPredmetRepo.existsById(id)) {
				Razred_Predmet rp = razredPredmetRepo.findById(id).get();
				Razred_Predmet oldrp = razredPredmetRepo.findById(id).get();
				rp.setActive(updateRazredPredmet.getActive());
				rp.setCasovaNedeljno(updateRazredPredmet.getCasovaNedeljno());
				rp.setKnjiga(updateRazredPredmet.getKnjiga());
				razredPredmetRepo.save(rp);
				logger.error("Error occurred");
				logger.info("Admin (email: " + AuthController.getEmail() + ") updated rp from " + oldrp + " to " + rp);
				return new ResponseEntity<Razred_Predmet>(rp, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "There is no grade_subject with such ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeRazredPredmet(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (razredPredmetRepo.existsById(id)) {
				Razred_Predmet rp = razredPredmetRepo.findById(id).get();
				if (rp.getNrp().isEmpty()) {
					razredPredmetRepo.deleteById(id);
					logger.error("Error occurred");
					logger.info("Admin (email: " + AuthController.getEmail() + ") deleted rp: " + rp);
					return new ResponseEntity<Razred_Predmet>(rp, HttpStatus.OK);
				} else {
					rp.setActive(false);
					razredPredmetRepo.save(rp);
					logger.error("Error occurred");
					logger.info(
							"Admin (email: " + AuthController.getEmail() + ") rp, id " + rp.getId() + "to not activeF");
					return new ResponseEntity<RestError>(
							new RestError(10, "Nije moguce obrisati."),	HttpStatus.OK);
				}
			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji razred u vezi sa predmetom sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
