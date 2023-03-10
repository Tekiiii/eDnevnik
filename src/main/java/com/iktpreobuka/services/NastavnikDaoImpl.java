package com.iktpreobuka.services;

import java.util.ArrayList;
import java.util.List;

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
import com.iktpreobuka.entites.Ocena;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.PozicijaNastavnika;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.PredmetUcenikOcena;
import com.iktpreobuka.entites.Razred_Predmet;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.NastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.NastavnikRepository;
import com.iktpreobuka.repositories.OcenaRepository;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.PredmetRepository;
import com.iktpreobuka.repositories.RazredPredmetRepository;



@Service
public class NastavnikDaoImpl implements NastavnikDao {

	@Autowired
	NastavnikRepository nastavnikRepo;

	@Autowired
	RazredPredmetRepository razredPredmetRepo;

	@Autowired
	NastavnikRazredPredmetRepository nastavnikRazredPredmetRepo;

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository onrpRepo;

	@Autowired
	PredmetRepository predmetRepo;

	@Autowired
	OcenaRepository ocenaRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Override
	public ResponseEntity<?> addRazredPredmet(Integer nastavnikId, Integer razredPredmetId) {
		if (nastavnikRepo.existsById(nastavnikId)) {
			if (razredPredmetRepo.existsById(razredPredmetId)) {
				Nastavnik nastavnik = nastavnikRepo.findById(nastavnikId).get();
				Razred_Predmet razredPredmet = razredPredmetRepo.findById(razredPredmetId).get();
				Integer label = razredPredmet.getRazred().getLabel();

				if (nastavnikRazredPredmetRepo.findByNastavnik_idAndRazredPredmet_id(nastavnikId, razredPredmetId).isPresent()) {
					return new ResponseEntity<String>("Postoji u bazi.", HttpStatus.NOT_ACCEPTABLE);
				} else {
					if ((nastavnik.getPozicijaNastavnika() == PozicijaNastavnika.predmetni_nastavnik && label > 4)
							|| (nastavnik.getPozicijaNastavnika() == PozicijaNastavnika.ucitelj && label < 5)) {
						Nastavnik_Razred_Predmet nrp = new Nastavnik_Razred_Predmet();
						nrp.setNastavnik(nastavnik);;
						nrp.setRazredPredmet(razredPredmet);;
						nastavnikRazredPredmetRepo.save(nrp);
						logger.error("Greska prilikom stvaranja veze nrp");
						logger.info("Admin (email: " + AuthController.getEmail() + ")  dodata nova veza nrp " + nrp);
						return new ResponseEntity<Nastavnik_Razred_Predmet>(nrp, HttpStatus.OK);
					} else {
						return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
					}
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji razred predmet veza sa trazenim  ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<?> ucitelj(Integer nastavnikId) {
		if (nastavnikRepo.existsById(nastavnikId)) {
			for (Razred_Predmet rp : razredPredmetRepo.findByRazred_LabelIsLessThan(5)) {
				addRazredPredmet(nastavnikId, rp.getId());
				logger.error("Greska prilikom kreiranja veze nrp");
				logger.info("Admin (email: " + AuthController.getEmail()
						+ ") dodata nova veza razred predmet ucitelju #id " + nastavnikId);
			}
			return new ResponseEntity<List<Nastavnik_Razred_Predmet>>(nastavnikRazredPredmetRepo.findByNastavnik_id(nastavnikId),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> predmetNastavnik(Integer nastavnikId, Integer predmetId) {
		if (nastavnikRepo.existsById(nastavnikId)) {
			for (Razred_Predmet rp : razredPredmetRepo.findByPredmet_idAndRazred_LabelIsGreaterThan(predmetId, 4)){
				addRazredPredmet(nastavnikId, rp.getId());
				logger.error("Greska prilikom kreiranja veze nrp");
				logger.info("Admin (email: " + AuthController.getEmail()
						+ ")  dodata nova veza razred predmet nastavniku #id " + nastavnikId);
			}
			return new ResponseEntity<List<Nastavnik_Razred_Predmet>>(nastavnikRazredPredmetRepo.findByNastavnik_id(nastavnikId),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> postDodajNastavnika(Nastavnik nastavnik, UserDTO newNastavnik) {
		if (newNastavnik.getPozicijaNastavnika() == null) {
			return new ResponseEntity<RestError>(new RestError(2, "Pozicija mora da postoji"), HttpStatus.BAD_REQUEST);
		}
		if (newNastavnik.getTeacherNoOfLicence() == null) {
			return new ResponseEntity<RestError>(new RestError(2, "Licenca nastavnika mora da postoji"),
					HttpStatus.BAD_REQUEST);
		}
		nastavnik.setName(newNastavnik.getName());
		nastavnik.setLastName(newNastavnik.getLastName());
		nastavnik.setEmail(newNastavnik.getEmail());
		nastavnik.setPozicijaNastavnika(newNastavnik.getPozicijaNastavnika());
		nastavnik.setNoOfLicence(newNastavnik.getTeacherNoOfLicence());
		nastavnikRepo.save(nastavnik);
		logger.error("Greska prilikom kreiranja veze tgs");
		logger.info("Admin (email: " + AuthController.getEmail() + ")  dodat novi nastavnik " + nastavnik);

		return new ResponseEntity<Nastavnik>(nastavnik, HttpStatus.OK);
	}

	@Override
	public void setNotActiveOnrp(Nastavnik nastavnik) {
		List<Odeljenje_Nastavnik_Razred_Predmet> onrpList = onrpRepo.findByNrp_Nastavnik(nastavnik);
		for (Odeljenje_Nastavnik_Razred_Predmet onrp : onrpList) {
			onrp.setActive(false);
		}

	}

	@Override
	public ResponseEntity<?> getONRP(Integer nastavnikId) {
		if (nastavnikRepo.existsById(nastavnikId)) {
			return new ResponseEntity<List<Odeljenje_Nastavnik_Razred_Predmet>>(
					onrpRepo.findByNrp_NastavnikIdAndActive(nastavnikId, true), HttpStatus.OK);
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> getUcenikOcena(Integer nastavnikId, Integer onrpId) {
		List<PredmetUcenikOcena> predmetUcenikOcena = new ArrayList<>();
		if (nastavnikRepo.existsById(nastavnikId)) {
			if (onrpRepo.existsById(onrpId)) {
				Odeljenje_Nastavnik_Razred_Predmet onrp = onrpRepo.findById(onrpId).get();
				Nastavnik nastavnik = nastavnikRepo.findById(nastavnikId).get();
				if (onrp.getNrp().getNastavnik().equals(nastavnik)) {
					List<Ucenik> ucenik = onrp.getOdeljenje().getUcenik();
					Predmet predmet = onrp.getNrp().getRazredPredmet().getPredmet();
					for (Ucenik u : ucenik) {
						PredmetUcenikOcena puo = new PredmetUcenikOcena();
						List<Ocena> ocena = ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(u, predmet);
						puo.setUcenik(u);
						puo.setOcena(ocena);
						predmetUcenikOcena.add(puo);
					}
					return new ResponseEntity<List<PredmetUcenikOcena>>(predmetUcenikOcena, HttpStatus.OK);
				} else {
					return new ResponseEntity<RestError>(
							new RestError(10, "Greska."), HttpStatus.NOT_FOUND);
				}

			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji veza onrp sa zadatimId."),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji nastavnik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}

	}



}
