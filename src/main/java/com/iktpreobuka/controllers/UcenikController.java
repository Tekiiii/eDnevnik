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
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.UserRole;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.UcenikRepository;
import com.iktpreobuka.services.UcenikDao;
import com.iktpreobuka.services.UserDao;


@RestController
@RequestMapping(path = "ednevnik/ucenik")
public class UcenikController {

	@Autowired
	UcenikRepository ucenikRepo;

	@Autowired
	UcenikDao ucenikDao;

	@Autowired
	UserDao userDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Ucenik> result = ucenikRepo.findAll();
			return new ResponseEntity<Iterable<Ucenik>>(result, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "UCENIK" })
	@RequestMapping(method = RequestMethod.GET, value = "/by_id/{ids}")
	public ResponseEntity<?> getById(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.UCENIK)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			if (ucenikRepo.existsById(id)) {
				return new ResponseEntity<Ucenik>(ucenikRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addUcenik(@Valid @RequestBody UserDTO newPupil, BindingResult result) {
		Ucenik ucenik = new Ucenik();
		ucenik.setRole(UserRole.UCENIK);
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			return ucenikDao.postDodajUcenika(ucenik, newPupil);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{ids}")
	public ResponseEntity<?> updateUcenik(@PathVariable String ids, @Valid @RequestBody UserDTO updateUcenik,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			Integer id = Integer.valueOf(ids);
			if (ucenikRepo.existsById(id)) {
				Ucenik ucenik = ucenikRepo.findById(id).get();
				return ucenikDao.postDodajUcenika(ucenik, updateUcenik);
			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeUcenik(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (ucenikRepo.existsById(id)) {
				Ucenik ucenik = ucenikRepo.findById(id).get();
				ucenikDao.removeRoditelj(ucenik);
				if (ucenik.getOcena().isEmpty()) {
					ucenikRepo.deleteById(id);
					logger.error("Error occurred while deleting ucenika: " + ucenik);
					logger.info("Admin (email: " + AuthController.getEmail() + ")  deleted ucenik " + ucenik);
					return new ResponseEntity<Ucenik>(ucenik, HttpStatus.OK);
				} else {
					logger.error("Error occurred while deleting pupil: " + ucenik);
					logger.info("Admin (email: " + AuthController.getEmail() + ")  set NOT_ACTIVE to: " + ucenik);
					ucenik.setRole(UserRole.NOT_ACTIVE);
					return new ResponseEntity<RestError>(
							new RestError(10, "Nije moguce obrisati ucenika koji ima ocene."),
							HttpStatus.NOT_FOUND);
				}
			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/addRoditelj/{ids}")
	public ResponseEntity<?> addRoditelj(@PathVariable String ids, @RequestBody UserDTO roditelj,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			Integer id = Integer.valueOf(ids);
			if (ucenikRepo.existsById(id)) {
				Ucenik ucenik= ucenikRepo.findById(id).get();
				ucenikDao.addRoditelj(ucenik, roditelj);
				return new ResponseEntity<Ucenik>(ucenik, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/addOdeljenje/{ids}/{scIds}")
	public ResponseEntity<?> addOdeljenje(@PathVariable String ids, @PathVariable String odeljenjeIds) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer odeljenjeId = Integer.valueOf(odeljenjeIds);
			if (ucenikRepo.existsById(id)) {
				Ucenik ucenik = ucenikRepo.findById(id).get();
				return ucenikDao.addOdeljenje(ucenik, odeljenjeId);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "UCENIK" })
	@RequestMapping(method = RequestMethod.PUT, value = "/password/{ids}")
	public ResponseEntity<?> updatePassword(@PathVariable String ids, @RequestParam String oldP,
			@RequestParam String newP) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.UCENIK)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			return userDao.updatePasword(id, oldP, newP);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "UCENIK", "RODITELJ" })
	@RequestMapping(method = RequestMethod.GET, value = "/getOcenaPredmet/{pIds}")
	public ResponseEntity<?> getOcenaPredmet(@PathVariable Integer ucenikId) {
		try {
			Integer id = Integer.valueOf(ucenikId);
			if ((userDao.getLoggedInUser().getRole().equals(UserRole.UCENIK) && userDao.getLoggedInId().equals(id))
					|| userDao.getLoggedInUser().getRole().equals(UserRole.RODITELJ)
							&& ucenikDao.isItARoditelj(id, userDao.getLoggedInUser().getId())) {

				return ucenikDao.getOcenaPredmet(id);
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
