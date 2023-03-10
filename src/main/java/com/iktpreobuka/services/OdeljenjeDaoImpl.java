package com.iktpreobuka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.controllers.AuthController;
import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.Odeljenje;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.Razred;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.repositories.NastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OdeljenjeRepository;
import com.iktpreobuka.repositories.RazredRepository;
import com.iktpreobuka.repositories.UcenikRepository;


@Service
public class OdeljenjeDaoImpl implements OdeljenjeDao {

	@Autowired
	OdeljenjeRepository odeljenjeRepo;

	@Autowired
	NastavnikRazredPredmetRepository nastavnikRazredPredmetRepo;

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository odeljenjeNastavnikRazredPredmetRepo;

	@Autowired
	UcenikRepository ucenikRepo;

	@Autowired
	RazredRepository razredRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseEntity<?> addNastavnikRazredPredmet(Integer odeljenjeId, Integer nrpId) {
		if (odeljenjeRepo.existsById(odeljenjeId)) {
			if (nastavnikRazredPredmetRepo.existsById(nrpId)) {
				Odeljenje odeljenje = odeljenjeRepo.findById(odeljenjeId).get();
				Nastavnik_Razred_Predmet nrp = nastavnikRazredPredmetRepo.findById(nrpId).get();

				if (odeljenje.getRazred().equals(nrp.getRazredPredmet().getRazred())) {
					if (!odeljenjeNastavnikRazredPredmetRepo.findByOdeljenje_IdAndNrp_idAndActive(odeljenjeId, nrpId, true)
							.isPresent()) {
						Odeljenje_Nastavnik_Razred_Predmet onrp = new Odeljenje_Nastavnik_Razred_Predmet();
						onrp.setOdeljenje(odeljenje);
						onrp.setNrp(nrp);
						onrp.setActive(true);
						odeljenjeNastavnikRazredPredmetRepo.save(onrp);
						return new ResponseEntity<Odeljenje_Nastavnik_Razred_Predmet>(onrp, HttpStatus.OK);
					} else {
						return new ResponseEntity<RestError>(new RestError(5, "Postoji u bazi."),
								HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<RestError>(new RestError(5, "Greska. Ne postoji trazeni ID."),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji podudaranje sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenjesa trazenim ID"),
					HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<?> addUcenik(Integer odeljenjeId, Integer ucenikId) {
		if (odeljenjeRepo.existsById(odeljenjeId)) {
			if (ucenikRepo.existsById(ucenikId)) {
				Ucenik ucenik = ucenikRepo.findById(ucenikId).get();
				Odeljenje odeljenje = odeljenjeRepo.findById(odeljenjeId).get();
				ucenik.setOdeljenje(odeljenje);
				ucenikRepo.save(ucenik);
				logger.error("Greska u dodavanju ucenika u odeljenje.");
				logger.info(
						"Admin (email:" + AuthController.getEmail() + ") dodat ucenik u odeljenje " + odeljenje);
				return new ResponseEntity<Ucenik>(ucenik, HttpStatus.OK);
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa trazenim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<?> addRazred(Integer odeljenjeId, Integer razredId) {
		if (odeljenjeRepo.existsById(odeljenjeId)) {
			if (razredRepo.existsById(razredId)) {
				Razred razred = razredRepo.findById(razredId).get();
				Odeljenje odeljenje = odeljenjeRepo.findById(odeljenjeId).get();
				odeljenje.setRazred(razred);
				odeljenjeRepo.save(odeljenje);
				logger.error("Greska prilikom dodavanja razreda odeljenju");
				logger.info(
						"Admin (email:" + AuthController.getEmail() + ") added razred odeljenju " + odeljenje);
				return new ResponseEntity<Odeljenje>(odeljenje, HttpStatus.OK);
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "NE postoji razred sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}
	}

}
