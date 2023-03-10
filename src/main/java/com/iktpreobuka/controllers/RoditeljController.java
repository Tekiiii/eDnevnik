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
import com.iktpreobuka.entites.Roditelj;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.UserRole;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.RoditeljRepository;
import com.iktpreobuka.services.UserDao;


@RestController
@RequestMapping(path = "ednevnik/roditelj")
public class RoditeljController {

	@Autowired
	RoditeljRepository roditeljRepo;

	@Autowired
	UserDao userDao;

	
	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Roditelj> result = roditeljRepo.findAll();
			return new ResponseEntity<Iterable<Roditelj>>(result, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "RODITELJ" })
	@RequestMapping(method = RequestMethod.GET, value = "/by_id/{ids}")
	public ResponseEntity<?> getById(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.RODITELJ)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			if (roditeljRepo.existsById(id)) {
				return new ResponseEntity<Roditelj>(roditeljRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "NE postoji roditelj sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addRoditelj(@Valid @RequestBody UserDTO newRoditelj, BindingResult result) {
		Roditelj roditelj = new Roditelj();
		roditelj.setRole(UserRole.RODITELJ);
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			roditelj.setName(newRoditelj.getName());
			roditelj.setLastName(newRoditelj.getLastName());
			roditelj.setEmail(newRoditelj.getEmail());
			roditeljRepo.save(roditelj);
			return new ResponseEntity<Roditelj>(roditelj, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{ids}")
	public ResponseEntity<?> updateRoditelj(@PathVariable String ids, @Valid @RequestBody UserDTO updateRoditelj,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			Integer id = Integer.valueOf(ids);
			if (roditeljRepo.existsById(id)) {
				Roditelj roditelj = roditeljRepo.findById(id).get();
				roditelj.setName(updateRoditelj.getName());
				roditelj.setLastName(updateRoditelj.getLastName());
				roditelj.setEmail(updateRoditelj.getEmail());
				roditeljRepo.save(roditelj);
				return new ResponseEntity<Roditelj>(roditelj, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji roditelj sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeRoditelj(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (roditeljRepo.existsById(id)) {
				Roditelj roditelj = roditeljRepo.findById(id).get();
				roditeljRepo.deleteById(id);
				return new ResponseEntity<Roditelj>(roditelj, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji roditelj sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Secured({ "admin", "RODITELJ" })
	@RequestMapping(method = RequestMethod.GET, value = "/dete/{ids}")
	public ResponseEntity<?> getDete(@PathVariable String ids, Roditelj roditelj) {
		try {
			Integer id = Integer.valueOf(ids);
			if (roditeljRepo.existsById(id)) {
				roditelj  = roditeljRepo.findById(id).get();
				return new ResponseEntity<List<Ucenik>>(roditelj.getUcenik(),HttpStatus.OK);
		} else
			if (userDao.getLoggedInUser().getRole().equals(UserRole.RODITELJ)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<List<Roditelj>>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@Secured({ "admin", "RODITELJ"})
	@RequestMapping(method = RequestMethod.PUT, value = "/password/{ids}")
	public ResponseEntity<?> updatePassword(@PathVariable String ids, @RequestParam String oldP,
			@RequestParam String newP) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInUser().getRole().equals(UserRole.RODITELJ)
					&& !userDao.getLoggedInId().equals(id)) {
				return new ResponseEntity<RestError>(new RestError(10, "Not authorized"), HttpStatus.BAD_REQUEST);
			}
			return userDao.updatePasword(id, oldP, newP);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
