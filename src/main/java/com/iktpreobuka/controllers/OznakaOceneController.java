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
import com.iktpreobuka.entites.OznakaOcene;
import com.iktpreobuka.entites.dto.OznakaOceneDTO;
import com.iktpreobuka.repositories.OznakaOceneRepository;


@RestController
@RequestMapping(path = "/ednevnik/oznakaocene")
public class OznakaOceneController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	OznakaOceneRepository oznakaOceneRepo;

	
	@Secured({ "admin", "NASTAVNIK" })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<OznakaOcene> result = oznakaOceneRepo.findAll();
			return new ResponseEntity<Iterable<OznakaOcene>>(result, HttpStatus.OK);

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
			if (oznakaOceneRepo.existsById(id)) {
				return new ResponseEntity<OznakaOcene>(oznakaOceneRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji oznaka ocene sa trazenimID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodajOznakuOcene(@Valid @RequestBody OznakaOceneDTO newOznakaOcene, BindingResult result) {
		OznakaOcene oznakaOcene = new OznakaOcene();

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			oznakaOcene.setOznaka(newOznakaOcene.getOznaka());
			oznakaOcene.setDescription(newOznakaOcene.getOpis());
			oznakaOceneRepo.save(oznakaOcene);
			logger.error("Greska prilikom kreiranja oznake ocene.");
			logger.info("Admin (email: " + AuthController.getEmail() + ") created new oznaka ocene " + oznakaOcene);
			return new ResponseEntity<OznakaOcene>(oznakaOcene, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{ids}")
	public ResponseEntity<?> updateOznakaOcena(@PathVariable String ids, @Valid @RequestBody OznakaOceneDTO updateMarkType,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		Integer id = Integer.valueOf(ids);
		try {

			if (oznakaOceneRepo.existsById(id)) {
				OznakaOcene oznakaOcene = oznakaOceneRepo.findById(id).get();
				OznakaOcene oldmarkType = oznakaOceneRepo.findById(id).get();
				oznakaOcene.setOznaka(oznakaOcene.getOznaka());
				oznakaOcene.setDescription(oznakaOcene.getDescription());
				oznakaOceneRepo.save(oznakaOcene);
				logger.error("Greska prilikom izmene oznake ocene");
				logger.info("Admin (email: " + AuthController.getEmail() + ") updated oznaka ocene from:" + oldmarkType
						+ " to: " + oznakaOcene);
				return new ResponseEntity<OznakaOcene>(oznakaOcene, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji oznaka ocene sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeMarkType(@PathVariable String ids) {
		Integer id = Integer.valueOf(ids);

		try {
			if (oznakaOceneRepo.existsById(id)) {
				OznakaOcene oznakaOcene = oznakaOceneRepo.findById(id).get();
				if (oznakaOcene.getOcena().isEmpty()) {
					oznakaOceneRepo.deleteById(id);
					logger.error("Greska prilikom brisanja oznake ocene.");
					logger.info("Admin (email: " + AuthController.getEmail() + ") deleted oznaka ocene " + oznakaOcene);
					return new ResponseEntity<OznakaOcene>(oznakaOcene, HttpStatus.OK);
				} else {
					return new ResponseEntity<RestError>(new RestError(10, "Nemoguce obrisati oznaku ocene. "),
							HttpStatus.NOT_FOUND);
				}

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji oznaka ocene sa trazenim ID"),
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
