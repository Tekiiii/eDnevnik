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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Ocena;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.dto.OcenaDTO;
import com.iktpreobuka.repositories.OcenaRepository;
import com.iktpreobuka.repositories.PredmetRepository;
import com.iktpreobuka.repositories.UcenikRepository;
import com.iktpreobuka.services.OcenaDao;

@RestController
@RequestMapping(path = "/ednevnik/ocena")
public class OcenaController {

	private OcenaRepository ocenaRepo;

	private OcenaDao ocenaDao;

	private UcenikRepository ucenikRepo;
	private PredmetRepository predmetRepo;

	@Autowired
	public OcenaController(UcenikRepository ucenikRepository, PredmetRepository predmetRepo, OcenaRepository ocenaRepo,
			OcenaDao ocenaDao) {
		this.ucenikRepo = ucenikRepository;
		this.predmetRepo = predmetRepo;
		this.ocenaDao = ocenaDao;
		this.ocenaRepo = ocenaRepo;
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Ocena> result = ocenaRepo.findAll();
			return new ResponseEntity<Iterable<Ocena>>(result, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "NASTAVNIK" })
	@RequestMapping(method = RequestMethod.GET, value = "/by_id/{ids}")
	public ResponseEntity<?> getById(@PathVariable String ids) {

		try {
			Integer id = Integer.valueOf(ids);
			if (ocenaRepo.existsById(id)) {
				return new ResponseEntity<Ocena>(ocenaRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ocena sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({ "admin", "NASTAVNIK" })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addOcena(@Valid @RequestBody OcenaDTO newOcena, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			return ocenaDao.createOcena(newOcena);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{ids}")
	public ResponseEntity<?> updateOcena(@PathVariable String ids, @Valid @RequestBody OcenaDTO updateOcena,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {

			Integer id = Integer.valueOf(ids);
			if (ocenaRepo.existsById(id)) {
				return ocenaDao.updateOcena(id, updateOcena);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ocena sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeOcena(@PathVariable String ids) {

		try {
			Integer id = Integer.valueOf(ids);
			if (ocenaRepo.existsById(id)) {
				Ocena ocena = ocenaRepo.findById(id).get();
				ocenaRepo.deleteById(id);
				return new ResponseEntity<Ocena>(ocena, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ocena sa zadatim  ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	private Ucenik getUcenik() {
		Integer id = 17;
		return ucenikRepo.findById(id).get();
	}

	private Iterable<Ucenik> getSveUcenike() {
		return ucenikRepo.findAll();
	}
	
	
	
	
	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.GET, value = "/izracunajzakljucnu/{ucenikId}/predmet/{predmetId}")
	private ResponseEntity<?> izracunajZakljucnu(@PathVariable String ucenikId, @PathVariable Long predmetId) {

		try {

			Iterable<Ucenik> ucenici = getSveUcenike();
			Ucenik ucenik = getUcenik();
			Predmet predmet = predmetRepo.findById(predmetId).get();

			if (!ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_PredmetAndOznaka_Oznaka(ucenik, predmet, "zakljucna").isPresent()) {
			List<Ocena> ocene = ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(ucenik, predmet);
				Double sum = 0.0;
					for (Ocena ocena : ocene) {
						sum += ocena.getOcena();
					}
			
				Integer zakljucnaOcena = (int) Math.round(sum / ocene.size());
					return new ResponseEntity<Integer>(zakljucnaOcena, HttpStatus.OK);
						} else {
					return new ResponseEntity<RestError>(new RestError(9, "Postoji zakljucna ocena!!"),	HttpStatus.BAD_REQUEST);
			 }
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.POST, value = "zakljuciocenu/{ucenikId}/predmet/{predmetId}")
	private ResponseEntity<?> zakljuciOcenu(@PathVariable String ucenikIds, @PathVariable String predmetIds,
			@RequestParam String sugg) {

		try {
			Integer ucenikId = Integer.valueOf(ucenikIds);
			Integer predmetId = Integer.valueOf(predmetIds);
			Integer suggestion = Integer.parseInt(sugg);
			return ocenaDao.zakljuciOcenu(ucenikId, predmetId, suggestion);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
