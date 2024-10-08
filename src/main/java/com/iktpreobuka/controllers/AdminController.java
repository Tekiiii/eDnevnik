package com.iktpreobuka.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Admin;
import com.iktpreobuka.entites.UserRole;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.AdminRepository;
import com.iktpreobuka.services.UserDao;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping(path = "ednevnik/admins")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

	@Autowired
	AdminRepository adminRepo;

	@Autowired
	UserDao userDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	
	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Admin> result = adminRepo.findAll();
			return new ResponseEntity<Iterable<Admin>>(result, HttpStatus.OK);

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
			if (adminRepo.existsById(id)) {
				return new ResponseEntity<Admin>(adminRepo.findById(id).get(), HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji admin sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addAdmin(@Valid @RequestBody UserDTO newAdmin, BindingResult result) {
		Admin admin = new Admin();
		admin.setRole(UserRole.admin);
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			admin.setName(newAdmin.getName());
			admin.setLastName(newAdmin.getLastName());
			admin.setEmail(newAdmin.getEmail());
			adminRepo.save(admin);
			return new ResponseEntity<Admin>(admin, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{ids}")
	public ResponseEntity<?> updateAdmin(@PathVariable String ids, @Valid @RequestBody UserDTO updateAdmin,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		try {
			Integer id = Integer.valueOf(ids);
			if (adminRepo.existsById(id)) {
				Admin admin = adminRepo.findById(id).get();
				admin.setName(updateAdmin.getName());
				admin.setLastName(updateAdmin.getLastName());
				admin.setEmail(updateAdmin.getEmail());
				adminRepo.save(admin);
				return new ResponseEntity<Admin>(admin, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji admin sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeAdmin(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (adminRepo.existsById(id)) {
				Admin admin = adminRepo.findById(id).get();
				adminRepo.deleteById(id);
				return new ResponseEntity<Admin>(admin, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji admin sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/password/{ids}")
	public ResponseEntity<?> updatePassword(@PathVariable String ids, @RequestParam String oldP,
			@RequestParam String newP) {
		try {
			Integer id = Integer.valueOf(ids);
			if (userDao.getLoggedInId().equals(id)) {
				return userDao.updatePasword(id, oldP, newP);
			} else {
				return new ResponseEntity<RestError>(new RestError(101, "Nemoguce menjanje tudjeg passworda"),
						HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	
	@Secured("admin")
    @RequestMapping(method = RequestMethod.GET, value = "/download")
    public ResponseEntity<ByteArrayResource> downloadFile() throws IOException {

        logger.info("Pozvana je metoda download.");
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spring-boot-logging.log");

        Path path1 = Paths.get("logs/spring-boot-logging.log");

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path1));

        return ResponseEntity.ok().headers(header)
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM).body(resource);

    }

}
