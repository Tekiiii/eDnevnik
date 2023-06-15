package com.iktpreobuka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import com.iktpreobuka.controllers.AuthController;
import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.Odeljenje;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.dto.OdeljenjeNastavnikRazredPredmetDTO;
import com.iktpreobuka.repositories.NastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OdeljenjeRepository;


@Service
public class OdeljenjeNastavnikRazredPredmetDaoImpl implements OdeljenjeNastavnikRazredPredmetDao {

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository onrpRepo;

	@Autowired
	OdeljenjeRepository odeljenjeRepo;

	@Autowired
	NastavnikRazredPredmetRepository nrpRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseEntity<?> putOnrp(Integer id, OdeljenjeNastavnikRazredPredmetDTO onrp) {
		if (onrpRepo.existsById(id)) {
			if (odeljenjeRepo.existsById(onrp.getOdeljenjeId())) {
				if (nrpRepo.existsById(onrp.getNrpId())) {
					Odeljenje odeljenje = odeljenjeRepo.findById(onrp.getOdeljenjeId()).get();
					Nastavnik_Razred_Predmet nrp = nrpRepo.findById(onrp.getNrpId()).get();
					if (odeljenje.getRazred().equals(nrp.getRazredPredmet().getRazred())) {
						Odeljenje_Nastavnik_Razred_Predmet onrp2b = onrpRepo.findById(id).get();
						Odeljenje_Nastavnik_Razred_Predmet exonrp = onrpRepo.findById(id).get();
						onrp2b.setActive(onrp.getActive());
						onrp2b.setOdeljenje(odeljenje);
						onrp2b.setNrp(nrp);
						onrpRepo.save(onrp2b);
						logger.error("Greska prilikom update onrp");
						//logger.info("Admin (email:" + AuthController.getEmail() + ") updated  onrp from:{ id:"
								//+ exonrp.getId() + ", odeljenje_id: " + exonrp.getOdeljenje().getId()
								//+ ", nrp_id:	 " + exonrp.getNrp().getId() + " } to:{ id:" + onrp2b.getId()
								//+ ", odeljenje_id: " + onrp2b.getOdeljenje().getId() + ", nrp_id:	 "
								//+ onrp2b.getNrp().getId() + " }");
						return new ResponseEntity<Odeljenje_Nastavnik_Razred_Predmet>(onrp2b, HttpStatus.OK);
					} else {
						return new ResponseEntity<RestError>(new RestError(5,
								"Nema podudaranja."),
								HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<RestError>(new RestError(10, "Ne postoji trazena veza sa ID"),
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa trazenim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Nema podudaranja izmedju nastavnika, odeljenja, razreda i predmeta sa trazenim ID."),
					HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<?> deleteOnrp(Integer id) {
		if (onrpRepo.existsById(id)) {
			Odeljenje_Nastavnik_Razred_Predmet onrp = onrpRepo.findById(id).get();
			if (onrp.getOcena().isEmpty()) {
				onrpRepo.deleteById(id);
				logger.error("Error occurred");
				//logger.info("Admin (email:" + AuthController.getEmail() + ") deleted  onrp:{ id:" + onrp.getId()
						//+ ", odeljenje_id: " + onrp.getOdeljenje().getId() + ", nrp_id:	 " + onrp.getNrp().getId()
						//+ " }");
				return new ResponseEntity<Odeljenje_Nastavnik_Razred_Predmet>(onrp, HttpStatus.OK);
			} else {
				onrp.setActive(false);
				logger.error("Greska prilikom brisanja veze onrp");
				//logger.info("Admin (email:" + AuthController.getEmail() + ") updated onrp with id:" + onrp.getId()
						//+ " active = false ");
				return new ResponseEntity<RestError>(
						new RestError(10, "Nemoguce obrisati, postoji veza sa ocenom."),
						HttpStatus.OK);
			}

		} else
			return new ResponseEntity<RestError>(
					new RestError(10, "Nema podudaranja izmedju nastavnika, odeljenja, razreda i predmeta sa trazenim ID."), HttpStatus.NOT_FOUND);

	}

}
