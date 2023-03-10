package com.iktpreobuka.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Ocena;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.OznakaOcene;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.repositories.OcenaRepository;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OznakaOceneRepository;
import com.iktpreobuka.repositories.PredmetRepository;
import com.iktpreobuka.repositories.UcenikRepository;


@RestController
@RequestMapping(path = "ednevnik/zakljucivanje")
public class ZakljucnaOcenaController {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	UcenikRepository ucenikRepo;

	@Autowired
	PredmetRepository predmetRepo;

	@Autowired
	OcenaRepository ocenaRepo;

	@Autowired
	OznakaOceneRepository oznakaOceneRepo;

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository onrpRepo;

	@GetMapping
	private Ucenik getUcenik() {
		Integer id = 17;
		return ucenikRepo.findById(id).get();
	}

	public ResponseEntity<?> izracunavanjeZakljucneOcene (Integer ucenikId, Integer predmetId) {
		if (ucenikRepo.existsById(ucenikId)) {
			if (predmetRepo.existsById(predmetId)) {
				Ucenik ucenik = ucenikRepo.findById(ucenikId).get();
				Predmet predmet = predmetRepo.findById(predmetId).get();
				if (!ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_PredmetAndOznaka_Oznaka(ucenik, predmet, "zakljucna")
						.isPresent()) {
					List<Ocena> ocene = ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(ucenik, predmet);
					Double sum = 0.0;
					for (Ocena ocena : ocene) {
						sum += ocena.getOcena();
					}
					Integer zakljucnaOcena = (int) Math.round(sum / ocene.size());
					return new ResponseEntity<Integer>(zakljucnaOcena, HttpStatus.OK);
				} else {
					return new ResponseEntity<RestError>(new RestError(9, "Postoji zakljucna ocena!"),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
					HttpStatus.NOT_FOUND);
		}

	}

	public ResponseEntity<?> zakljucivanjeOcene(Integer ucenikId, Integer predmetId, Integer suggestion) {
		if (ucenikRepo.existsById(ucenikId)) {
			if (predmetRepo.existsById(predmetId)) {
				Ucenik ucenik = ucenikRepo.findById(ucenikId).get();
				Predmet predmet = predmetRepo.findById(predmetId).get();
				if (!ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_PredmetAndOznaka_Oznaka(ucenik, predmet, "FINAL")
						.isPresent()) {
					List<Ocena> ocene = ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(ucenik, predmet);
					Double sum = 0.0;
					for (Ocena ocena : ocene) {
						sum += ocena.getOcena();
					}
					Integer zakljucnaOcena = (int) Math.round(sum / ocene.size());
					if (suggestion >= zakljucnaOcena) {
						String finalOznaka = "FINAL";
						OznakaOcene o = oznakaOceneRepo.findByOznaka(finalOznaka).get();
						Odeljenje_Nastavnik_Razred_Predmet onrp = onrpRepo.findByOdeljenjeAndNrp_RazredPredmet_Predmet(ucenik.getOdeljenje(), predmet).get();
						Ocena dataKonacna = new Ocena();
						dataKonacna.setOcena(suggestion);
						dataKonacna.setDataOcena(LocalDate.now());
						dataKonacna.setUpisanaOcena(LocalDate.now());
						dataKonacna.setUcenik(ucenik);
						dataKonacna.setOnrp(onrp);
						dataKonacna.setOznaka(o);
						ocenaRepo.save(dataKonacna);
						try {
							sendTemplateMessage(dataKonacna);

						} catch (Exception e) {
							return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
									HttpStatus.INTERNAL_SERVER_ERROR);
						}

						return new ResponseEntity<Ocena>(dataKonacna, HttpStatus.OK);
					} else {
						return new ResponseEntity<RestError>(
								new RestError(99, "Ne moze da se zakljuci manja ocena od izracunate!"),
								HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<RestError>(new RestError(9, "Postoji zakljucna ocena!"),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatimID"),
					HttpStatus.NOT_FOUND);
		}
	}

	protected void sendTemplateMessage(Ocena ocena) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(ocena.getUcenik().getRoditelj().getEmail());
		helper.setSubject("Nova ocena");
		String text = "<html>" + "<body>" + "<table style='border:2px solid black'" + "<th>" + "<td>pupil</td>"
				+ "<td>mark</td>" + "<td>subject</td>" + "<td>teacher</td>" + "<td>date</td>" + "</th>" + "<tr>"
				+ "<td>" + ocena.getUcenik().getName() + " " + ocena.getUcenik().getLastName() + "</td>" + "<td>"
				+ ocena.getOcena() + "</td>" + "<td>" + ocena.getOnrp().getNrp().getRazredPredmet().getPredmet().getName()
				+ "</td>" + "<td>" + ocena.getOnrp().getNrp().getNastavnik().getName() + " "
				+ ocena.getOnrp().getNrp().getNastavnik().getLastName() + "</td>" + "<td>" + ocena.getDataOcena() + "</td>"
				+ "</tr>" + "<table>" + "</body>" + "</html>";
		helper.setText(text, true);
		emailSender.send(mail);

	}

	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.GET, value = "/izracunajzakljucnu/{ucenikIds}/predmet/{predmetIds}")
	private ResponseEntity<?> izracunajZakljucnuOcenu(@PathVariable String ucenikIds, @PathVariable String predmetIds) {

		try {
			Integer ucenikId = Integer.valueOf(ucenikIds);
			Integer predmetId = Integer.valueOf(predmetIds);
			return izracunajZakljucnuOcenu(ucenikIds, predmetIds);

		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@Secured({ "admin", "predmetni_nastavnik" })
	@RequestMapping(method = RequestMethod.POST, value = "zakljuciocenu/{ucenikIds}/{predmetIds}/sugg")
	private ResponseEntity<?> zakljuciOcenu(@PathVariable String ucenikIds, @PathVariable String predmetIds,
			@RequestParam String sugg) {

		try {
			Integer ucenikId = Integer.valueOf(ucenikIds);
			Integer predmetId = Integer.valueOf(predmetIds);
			Integer suggestion = Integer.parseInt(sugg);

			if (ucenikRepo.existsById(ucenikId)) {
				if (predmetRepo.existsById(predmetId)) {
					Ucenik ucenik = ucenikRepo.findById(ucenikId).get();
					Predmet predmet = predmetRepo.findById(predmetId).get();
					if (!ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_PredmetAndOznaka_Oznaka(ucenik, predmet, "FINAL")
							.isPresent()) {
						List<Ocena> ocene = ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_Predmet(ucenik, predmet);
						Double sum = 0.0;
						for (Ocena ocena : ocene) {
							sum += ocena.getOcena();
						}
						Integer zakljucnaOcena = (int) Math.round(sum / ocene.size());
						if (suggestion >= zakljucnaOcena) {
							String finalOznaka = "FINAL";
							OznakaOcene o = oznakaOceneRepo.findByOznaka(finalOznaka).get();
							Odeljenje_Nastavnik_Razred_Predmet onrp = onrpRepo.findByOdeljenjeAndNrp_RazredPredmet_Predmet(ucenik.getOdeljenje(), predmet)
									.get();
							Ocena zakljucena = new Ocena();
							zakljucena.setOcena(suggestion);
							zakljucena.setDataOcena(LocalDate.now());
							zakljucena.setUpisanaOcena(LocalDate.now());
							zakljucena.setUcenik(ucenik);
							zakljucena.setOnrp(onrp);
							zakljucena.setOznaka(o);
							ocenaRepo.save(zakljucena);
							try {
								sendTemplateMessage(zakljucena);

							} catch (Exception e) {
								return new ResponseEntity<RestError>(
										new RestError(1, "Error ocured: " + e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
							}

							return new ResponseEntity<Ocena>(zakljucena, HttpStatus.OK);
						} else {
							return new ResponseEntity<RestError>(
									new RestError(99, "Ne moze da se zakljuci manja ocena od izracunate!"),
									HttpStatus.BAD_REQUEST);
						}
					} else {
						return new ResponseEntity<RestError>(new RestError(9, "Postoji zakljucena ocena!"),
								HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<RestError>(new RestError(10, "Ne postoji predmet sa zadatim ID"),
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
