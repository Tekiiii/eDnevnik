package com.iktpreobuka.controllers;

import java.util.Optional;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.dto.NazivPredmetaDTO;
import com.iktpreobuka.entites.dto.PredmetDTO;
import com.iktpreobuka.entites.dto.PripadaRazredPredmetDTO;
import com.iktpreobuka.repositories.PredmetRepository;
import com.iktpreobuka.services.PredmetDao;

@RestController
@RequestMapping(path = "ednevnik/predmet")
@CrossOrigin(origins = "http://localhost:3000")
public class PredmetController {

	@Autowired
	PredmetRepository predmetRepo;

	@Autowired
	PredmetDao predmetDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Secured({"admin", "NASTAVNIK"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Predmet> result = predmetRepo.findAll();
			return new ResponseEntity<Iterable<Predmet>>(result, HttpStatus.OK);

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
			if (predmetRepo.existsById(id)) {
				return new ResponseEntity<Predmet>(predmetRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public PredmetDTO addPredmet(@Valid @RequestBody PredmetDTO dto) {
		
		Predmet predmet = new Predmet();
		predmet.setName(dto.getName());
		predmet.setFond(dto.getFond());
		predmet.setRazred(dto.getRazred());
	
	predmetRepo.save(predmet);
	return new PredmetDTO(predmet);

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/{ids}")
	public ResponseEntity<?> updatePredmet(@PathVariable Integer id, @Valid @RequestBody PredmetDTO dto) {
		Optional<Predmet> p = predmetRepo.findById(id);
		if(p.isPresent()) {
			Predmet p1 = p.get();
			p1.setName(dto.getName());
			p1.setFond(dto.getFond());
			p1.setRazred(dto.getRazred());
			predmetRepo.save(p1);
			return new ResponseEntity<PredmetDTO>(new PredmetDTO(p1), HttpStatus.OK);
		}else{
			return new ResponseEntity<PredmetDTO>(HttpStatus.NOT_FOUND);
		}
	}
	//	}
		
	//	if (result.hasErrors()) {
	//		return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
	//	}

	//	try {
	//		Integer id = Integer.valueOf(ids);
	//		if (predmetRepo.existsById(id)) {
	//			Predmet predmet = predmetRepo.findById(id).get();
	//			Predmet oldPredmet = predmetRepo.findById(id).get();
	//			predmet.setName(newPredmet.getNaziv());
	//			predmetRepo.save(predmet);
	//			logger.error("Greska prilikom update predmeta " + predmet);
	//			logger.info("Admin (email: " + AuthController.getEmail() + ") updated predmet from:" + oldPredmet
	//					+ " to: " + predmet);
	//			return new ResponseEntity<Predmet>(predmet, HttpStatus.OK);

		//	} else
	//			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
	//					HttpStatus.NOT_FOUND);

	//	} catch (Exception e) {
	//		return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
	//				HttpStatus.INTERNAL_SERVER_ERROR);
	//	}

	//}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{ids}")
	public ResponseEntity<?> removePredmet(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (predmetRepo.existsById(id)) {
				Predmet predmet = predmetRepo.findById(id).get();
				if (predmet.getRazredPredmet().isEmpty()) {
					predmetRepo.deleteById(id);
					logger.error("Greska prilikom brisanja predmeta.");
					logger.info("Admin (email: " + AuthController.getEmail() + ") deleted predmet " + predmet);
					return new ResponseEntity<Predmet>(predmet, HttpStatus.OK);
				} else {
					return new ResponseEntity<RestError>(
							new RestError(10, "Nemoguce obrisati, postoji veza sa odeljenjem."),
							HttpStatus.BAD_REQUEST);
				}

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/ocena/{ids}")
	public ResponseEntity<?> createGradeSubject(@PathVariable String ids, @RequestBody PripadaRazredPredmetDTO ocenaPredmet) {
		Integer id = Integer.valueOf(ids);
		return predmetDao.addOcena(id, ocenaPredmet);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
