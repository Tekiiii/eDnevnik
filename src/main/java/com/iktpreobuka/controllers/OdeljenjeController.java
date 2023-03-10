package com.iktpreobuka.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.iktpreobuka.entites.Odeljenje;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.repositories.OdeljenjeRepository;
import com.iktpreobuka.services.OdeljenjeDao;


@RestController
@RequestMapping(path = "ednevnik/odeljenje")
public class OdeljenjeController {

	@Autowired
	OdeljenjeRepository odeljenjeRepo;

	@Autowired
	OdeljenjeDao odeljenjeDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Secured("admin")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			Iterable<Odeljenje> result = odeljenjeRepo.findAll();
			return new ResponseEntity<Iterable<Odeljenje>>(result, HttpStatus.OK);

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
			if (odeljenjeRepo.existsById(id)) {
				return new ResponseEntity<Odeljenje>(odeljenjeRepo.findById(id).get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addOdeljenje(@RequestParam String labelS) {
		try {
			Integer label = Integer.valueOf(labelS);
			Odeljenje odeljenje = new Odeljenje();
			odeljenje.setLabel(label);;
			odeljenjeRepo.save(odeljenje);
			logger.error("Greska prilikom kreiranja novog odeljenja.");
			logger.info("Admin (email:" + AuthController.getEmail() + ") added new  odeljenje " + odeljenje);
			return new ResponseEntity<Odeljenje>(odeljenje, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "update/{ids}")
	public ResponseEntity<?> updateOdeljenje(@PathVariable String ids, @RequestParam String oznakaS) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer oznaka = Integer.valueOf(oznakaS);
			if (odeljenjeRepo.existsById(id)) {
				Odeljenje odeljenje = odeljenjeRepo.findById(id).get();
				Odeljenje staroOdeljenje = odeljenjeRepo.findById(id).get();
				odeljenje.setLabel(oznaka);
				odeljenjeRepo.save(odeljenje);
				logger.error("Greska prilikom izmene odeljenja sa #id:" + odeljenje.getId());
				logger.info("Admin (email:" + AuthController.getEmail() + ") updated  odeljenje from" + odeljenje
						+ " to: " + staroOdeljenje);
				return new ResponseEntity<Odeljenje>(odeljenje, HttpStatus.OK);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
						HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{ids}")
	public ResponseEntity<?> removeOdeljenje(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (odeljenjeRepo.existsById(id)) {
				Odeljenje odeljenje = odeljenjeRepo.findById(id).get();
				if (odeljenje.getOnrp().isEmpty()) {
					if (odeljenje.getUcenik().isEmpty()) {
						odeljenjeRepo.deleteById(id);
						logger.error("Greska prilikom brisanja odeljenja sa #id:" + odeljenje.getId());
						logger.info(
								"Admin (email:" + AuthController.getEmail() + ") deleted  odeljenje " + odeljenje);
						return new ResponseEntity<Odeljenje>(odeljenje, HttpStatus.OK);
					} else
						return new ResponseEntity<RestError>(
								new RestError(12, "Odeljenje ne moze da se izbrise dok su u njemu ucenici."),
								HttpStatus.NOT_FOUND);
				} else
					return new ResponseEntity<RestError>(
							new RestError(10, "Odeljenje ne moze da se izbrise, postoji veza sa nastavnikom, razredom, predmetom."),
							HttpStatus.NOT_FOUND);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (

		Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/razred/{ids}")
	public ResponseEntity<?> addRazred(@PathVariable String ids, @RequestParam String razredIds) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer razredId = Integer.valueOf(razredIds);
			return odeljenjeDao.addRazred(id, razredId);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/ucenik/{ids}")
	public ResponseEntity<?> addUcenik(@PathVariable String ids, @RequestParam String ucenikIds) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer ucenikId = Integer.valueOf(ucenikIds);
			return odeljenjeDao.addUcenik(id, ucenikId);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured("admin")
	@RequestMapping(method = RequestMethod.PUT, value = "/nrp/{ids}")
	public ResponseEntity<?> createNastavnikRazredPredmet(@PathVariable String ids, @RequestParam String nrpIds) {
		try {
			Integer id = Integer.valueOf(ids);
			Integer nrpId = Integer.valueOf(nrpIds);
			return odeljenjeDao.addNastavnikRazredPredmet(id, nrpId);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.GET, value = "/listaUcenika/{ids}")
	public ResponseEntity<?> returnListaUCenika(@PathVariable String ids) {
		try {
			Integer id = Integer.valueOf(ids);
			if (odeljenjeRepo.existsById(id)) {
				List<Ucenik> ucenici = odeljenjeRepo.findById(id).get().getUcenik();
				return new ResponseEntity<List<Ucenik>>(ucenici, HttpStatus.OK);
			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
