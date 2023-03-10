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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Nastavnik;
import com.iktpreobuka.entites.UserRole;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.NastavnikRepository;
import com.iktpreobuka.services.NastavnikDao;
import com.iktpreobuka.services.UserDao;



@RestController
@RequestMapping(path = "/ednevnik/nastavnik")
public class NastavnikController {

	@Autowired
	NastavnikRepository nastavnikRepo;

	@Autowired
	NastavnikDao nastavnikDao;

	@Autowired
	UserDao userDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {

		try {
			Iterable<Nastavnik> result = nastavnikRepo.findAll();
			return new ResponseEntity<Iterable<Nastavnik>>(result, HttpStatus.OK);

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
			if (userDao.getLoggedInUser().getRole().equals(UserRole.NASTAVNIK)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			if (nastavnikRepo.existsById(id)) {
				return new ResponseEntity<Nastavnik>(nastavnikRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa trazenim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNastavnik(@Valid @RequestBody UserDTO newNastavnik, BindingResult result) {
		Nastavnik nastavnik = new Nastavnik();
		nastavnik.setRole(UserRole.NASTAVNIK);
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			return nastavnikDao.postDodajNastavnika(nastavnik, newNastavnik);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{ids}")
	public ResponseEntity<?> updateNastavnik(@PathVariable String ids, @Valid @RequestBody UserDTO updateNastavnik,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			Integer id = Integer.valueOf(ids);
			if (nastavnikRepo.existsById(id)) {
				Nastavnik nastavnik = nastavnikRepo.findById(id).get();
				return nastavnikDao.postDodajNastavnika(nastavnik, updateNastavnik);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeNastavnik(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (nastavnikRepo.existsById(id)) {
				Nastavnik nastavnik = nastavnikRepo.findById(id).get();
				if (nastavnik.getNrp().isEmpty()) {
					nastavnikRepo.deleteById(id);
					logger.error("Greska prilikom brisanja nastavnika #id" + nastavnik.getId());
					logger.info("Admin (email: " + AuthController.getEmail() + ") deleted prdmet " + nastavnik);
					return new ResponseEntity<Nastavnik>(nastavnik, HttpStatus.OK);
				} else {
					nastavnik.setRole(UserRole.NOT_ACTIVE);
					nastavnikDao.setNotActiveOnrp(nastavnik);
					return new ResponseEntity<Nastavnik>(nastavnik, HttpStatus.OK);
				}

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/razredPredmet/{ids}")
	public ResponseEntity<?> createNastavnikRazredPredmet(@PathVariable String ids, @RequestParam String razredPredmetIds) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer razredPredmetId = Integer.valueOf(razredPredmetIds);
			return nastavnikDao.addRazredPredmet(id, id);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/ucitelj/{ids}")
	public ResponseEntity<?> createUcitelj(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);

			return nastavnikDao.ucitelj(id);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/predmetniNastavnik/{ids}")
	public ResponseEntity<?> createPredmetniNastavnik(@PathVariable String ids,
			@RequestParam String sIds) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer sId = Integer.valueOf(sIds);
			return nastavnikDao.predmetNastavnik(id, sId);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "NASTAVNIK" })
	@RequestMapping(method = RequestMethod.PUT, value = "/password/{ids}")
	public ResponseEntity<?> updatePassword(@PathVariable String ids, @RequestParam String oldP,
			@RequestParam String newP) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.NASTAVNIK)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			return userDao.updatePasword(id, oldP, newP);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({ "admin", "NASTAVNIK" })
	@RequestMapping(method = RequestMethod.GET, value = "/getONRP/{ids}")
	public ResponseEntity<?> getOnrpForNastavnik(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.NASTAVNIK)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}

			return nastavnikDao.getONRP(id);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({ "NASTAVNIK", "admin" })
	@RequestMapping(method = RequestMethod.GET, value = "/ocene/{nIds}/{onrpIds}")
	public ResponseEntity<?> getStudentsMarks(@PathVariable String nIds, @PathVariable String ctgsIds) {
		try {
			Integer nId = Integer.valueOf(nIds);
			Integer onrpId = Integer.valueOf(ctgsIds);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.NASTAVNIK)
					&& !userDao.getLoggedInId().equals(nId)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			return nastavnikDao.getUcenikOcena(nId, onrpId);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
