package com.iktpreobuka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import com.iktpreobuka.controllers.AuthController;
import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.Razred_Predmet;
import com.iktpreobuka.entites.dto.PripadaRazredPredmetDTO;
import com.iktpreobuka.repositories.PredmetRepository;
import com.iktpreobuka.repositories.RazredPredmetRepository;
import com.iktpreobuka.repositories.RazredRepository;

@Service
public class PredmetDaoImpl implements PredmetDao {

	@Autowired
	PredmetRepository predmetRepo;

	@Autowired
	RazredRepository razredRepo;

	
	@Autowired
	RazredPredmetRepository razredPredmetRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseEntity<?> addRazred(Integer pred, PripadaRazredPredmetDTO razPred) {
		try {
			if (predmetRepo.existsById(pred)) {
				Predmet predmet = predmetRepo.findById(pred).get();
				if (razredRepo.existsById(razPred.getRazredId())) {
					if (razredPredmetRepo.findByRazred_idAndPredmet_id(razPred.getRazredId(), pred).isPresent()
							&& razredPredmetRepo.findByRazred_idAndPredmet_id(razPred.getRazredId(), pred).get().getActive()) {
						return new ResponseEntity<String>("Postoji u bazi.", HttpStatus.NOT_ACCEPTABLE);
					} else {
						Razred_Predmet rp = new Razred_Predmet();
						rp.setRazred(razredRepo.findById(razPred.getRazredId()).get());
						rp.setPredmet(predmet);
						rp.setCasovaNedeljno(razPred.getCasovaNedeljno());
						rp.setKnjiga(razPred.getKnjiga());
						razredPredmetRepo.save(rp);
						logger.error("Greska prilikom dodavanja novog predmeta u razred");
					//	logger.info("Admin (email: " + AuthController.getEmail() + ") dodat novi razred predmet " + rp);
						return new ResponseEntity<Razred_Predmet>(rp, HttpStatus.OK);
					}
				} else
					return new ResponseEntity<RestError>(new RestError(10, "Ne postoji razred sa zadatim ID"),
							HttpStatus.NOT_FOUND);

			} else
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> addOcena(Integer ocena, PripadaRazredPredmetDTO ocenaPredmet) {
		// TODO Auto-generated method stub
		return null;
	}

}
