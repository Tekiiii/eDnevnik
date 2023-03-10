package com.iktpreobuka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.controllers.AuthController;
import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Nastavnik;
import com.iktpreobuka.entites.Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.PozicijaNastavnika;
import com.iktpreobuka.entites.Razred_Predmet;
import com.iktpreobuka.repositories.NastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.NastavnikRepository;
import com.iktpreobuka.repositories.RazredPredmetRepository;

@Service
public class NastavnikRazredPredmetDaoImpl implements NastavnikRazredPredmetDao {

	@Autowired
	NastavnikRepository nastavnikRepo;

	@Autowired
	RazredPredmetRepository razredPredmetRepo;

	@Autowired
	NastavnikRazredPredmetRepository nrpRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseEntity<?> updateNrp(Integer id, Integer nastavnikId, Integer razredPredmetId) {
		if (nrpRepo.existsById(id)) {
			if (nastavnikRepo.existsById(nastavnikId)) {
				if (razredPredmetRepo.existsById(razredPredmetId)) {
					Nastavnik nastavnik = nastavnikRepo.findById(nastavnikId).get();
					Razred_Predmet razredPredmet = razredPredmetRepo.findById(razredPredmetId).get();
					Integer label = razredPredmet.getRazred().getLabel();

					if (nrpRepo.findByNastavnik_idAndRazredPredmet_id(nastavnikId, razredPredmetId).isPresent()) {
						return new ResponseEntity<String>("Postoji u bazi.", HttpStatus.NOT_ACCEPTABLE);
					} else {
						if ((nastavnik.getPozicijaNastavnika() == PozicijaNastavnika.predmetni_nastavnik && label > 4)
								|| (nastavnik.getPozicijaNastavnika() == PozicijaNastavnika.ucitelj && label < 5)) {
							Nastavnik_Razred_Predmet nrp = nrpRepo.findById(id).get();
							Nastavnik_Razred_Predmet starijinrp = nrpRepo.findById(id).get();
							nrp.setNastavnik(nastavnik);
							nrp.setRazredPredmet(razredPredmet);
							nrpRepo.save(nrp);
							logger.error("Greska prilikom izmene nrp " + nrp);
							logger.info("Admin (email: " + AuthController.getEmail() + ") updated nrp from:" + starijinrp
									+ " to: " + nrp);
							return new ResponseEntity<Nastavnik_Razred_Predmet>(nrp, HttpStatus.OK);
						} else {
							return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
						}
					}
				} else {
					return new ResponseEntity<RestError>(new RestError(10, "Ne postoji veza  ID"),
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nrp sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}
	}
}
