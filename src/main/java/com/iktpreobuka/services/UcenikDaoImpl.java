package com.iktpreobuka.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Ocena;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.PredmetOcena;
import com.iktpreobuka.entites.Roditelj;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.UserEntity;
import com.iktpreobuka.entites.UserRole;
import com.iktpreobuka.entites.dto.UserDTO;
import com.iktpreobuka.repositories.OcenaRepository;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OdeljenjeRepository;
import com.iktpreobuka.repositories.RoditeljRepository;
import com.iktpreobuka.repositories.UcenikRepository;


@Service
public class UcenikDaoImpl implements UcenikDao {

	@Autowired
	UcenikRepository ucenikRepo;

	@Autowired
	RoditeljRepository roditeljRepo;

	@Autowired
	OdeljenjeRepository odeljenjeRepo;

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository onrpRepo;

	@Autowired
	OcenaRepository ocenaRepo;

	@Override
	public ResponseEntity<?> postDodajUcenika(Ucenik ucenik, UserDTO newUcenik) {
		ucenik.setName(newUcenik.getName());
		ucenik.setLastName(newUcenik.getLastName());
		ucenik.setEmail(newUcenik.getEmail());
		ucenikRepo.save(ucenik);
		return new ResponseEntity<Ucenik>(ucenik, HttpStatus.OK);
	}

	@Override
	public void addRoditelj(Ucenik ucenik, UserDTO roditelj) {
		if (roditeljRepo.findByEmail(roditelj.getEmail()).isPresent()) {
			Roditelj roditeljFromDB = roditeljRepo.findByEmail(roditelj.getEmail()).get();
			if (((UserEntity) roditeljFromDB).getRole() == UserRole.NOT_ACTIVE) {
				roditeljFromDB.setRole(UserRole.RODITELJ);
				roditeljRepo.save(roditeljFromDB);
			}
			ucenik.setRoditelj(roditeljFromDB);
		} else {
			Roditelj newRoditelj = new Roditelj();
			newRoditelj.setName(roditelj.getName());
			newRoditelj.setLastName(roditelj.getLastName());
			newRoditelj.setEmail(roditelj.getEmail());
			newRoditelj.setRole(UserRole.RODITELJ);
			roditeljRepo.save(newRoditelj);

			ucenik.setRoditelj(newRoditelj);
		}
		ucenikRepo.save(ucenik);

	}

	@Override
	public void removeRoditelj(Ucenik ucenik) {
		if (ucenik.getRoditelj() != null) {
			Roditelj roditelj = ucenik.getRoditelj();
			if (roditelj.getUcenik().size() == 1) {
				roditelj.setRole(UserRole.NOT_ACTIVE);
				roditeljRepo.save(roditelj);
			}
		}
	}

	@Override
	public ResponseEntity<?> addOdeljenje(Ucenik ucenik, Integer odeljenjeId) {
		if (odeljenjeRepo.existsById(odeljenjeId)) {
			ucenik.setOdeljenje(odeljenjeRepo.findById(odeljenjeId).get());
			ucenikRepo.save(ucenik);
			return new ResponseEntity<Ucenik>(ucenik, HttpStatus.OK);
		} else
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji odeljenje sa zadatim ID"),
					HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<?> getOcena(Integer ucenikId, Integer predmetId) {

		if (ucenikRepo.existsById(ucenikId)) {
			if (!onrpRepo.findByOdeljenjeAndNrp_RazredPredmet_Predmet_id(
					ucenikRepo.findById(ucenikId).get().getOdeljenje(), predmetId).isEmpty()) {
				List<Ocena> ocena = ocenaRepo.findByUcenik_idAndOnrp_Nrp_RazredPredmet_Predmet_id(ucenikId, predmetId);
				return new ResponseEntity<List<Ocena>>(ocena, HttpStatus.OK);

			} else {
				return new ResponseEntity<RestError>(
						new RestError(10, "Ucenik sa id ne slusa predmet sa id"), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> getOcenaPredmet(Integer ucenikId) {
		if (ucenikRepo.existsById(ucenikId)) {
			Ucenik ucenik = ucenikRepo.findById(ucenikId).get();
			List<PredmetOcena> konacno = new ArrayList<>();
			List<Odeljenje_Nastavnik_Razred_Predmet> onrp = onrpRepo.findByOdeljenje(ucenik.getOdeljenje());
			for (Odeljenje_Nastavnik_Razred_Predmet cc : onrp) {
				PredmetOcena temp = new PredmetOcena();
				temp.setPredmet(cc.getNrp().getRazredPredmet().getPredmet().getName());
				List<Ocena> ocene = new ArrayList<>();
				List<Ocena> ocena = ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(ucenik,
						cc.getNrp().getRazredPredmet().getPredmet());
				for (Ocena o : ocena) {
					ocene.add(o);
				}
				temp.setOcena(ocene);
				konacno.add(	 temp);
			}
			return new ResponseEntity<List<PredmetOcena>>(konacno, HttpStatus.OK);
		} else
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
					HttpStatus.NOT_FOUND);

	}

	@Override
	public Boolean isItARoditelj(Integer ucenik, Integer roditelj) {
		Boolean result = false;
		for (Ucenik u : roditeljRepo.findById(roditelj).get().getUcenik()) {
			if (ucenik.equals(u.getId())) {
				result = true;
			}
			break;
		}
		return result;
	}

}
