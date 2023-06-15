package com.iktpreobuka.services;

import java.time.LocalDate;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//import com.iktpreobuka.controllers.AuthController;
import com.iktpreobuka.controllers.util.RestError;
import com.iktpreobuka.entites.Ocena;
import com.iktpreobuka.entites.Odeljenje_Nastavnik_Razred_Predmet;
import com.iktpreobuka.entites.OznakaOcene;
import com.iktpreobuka.entites.Predmet;
import com.iktpreobuka.entites.Ucenik;
import com.iktpreobuka.entites.dto.OcenaDTO;
import com.iktpreobuka.repositories.OcenaRepository;
import com.iktpreobuka.repositories.OdeljenjeNastavnikRazredPredmetRepository;
import com.iktpreobuka.repositories.OznakaOceneRepository;
import com.iktpreobuka.repositories.PredmetRepository;
import com.iktpreobuka.repositories.UcenikRepository;


@Service
public class OcenaDaoImpl implements OcenaDao {

	@Autowired
	OcenaRepository ocenaRepo;

	@Autowired
	OznakaOceneRepository oznakaOceneRepo;

	@Autowired
	UcenikRepository ucenikRepo;

	@Autowired
	OdeljenjeNastavnikRazredPredmetRepository onrpRepo;

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	PredmetRepository predmetRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseEntity<?> createOcena(OcenaDTO newOcena) {
		if (oznakaOceneRepo.existsById(newOcena.getOznakaId())) {
			if (ucenikRepo.existsById(newOcena.getucenikId())) {
				if (onrpRepo.existsById(newOcena.getOnrpId())) {
					OznakaOcene o = oznakaOceneRepo.findById(newOcena.getOznakaId()).get();
					Ucenik ucenik = ucenikRepo.findById(newOcena.getucenikId()).get();
					Odeljenje_Nastavnik_Razred_Predmet onrp = onrpRepo.findById(newOcena.getOnrpId()).get();
					if (ucenik.getOdeljenje().equals(onrp.getOdeljenje())) {
						if (!ocenaRepo.findByUcenikAndOnrp_Nrp_RazredPredmet_PredmetAndOznaka_Oznaka(ucenik,
								onrp.getNrp().getRazredPredmet().getPredmet(), "zakljucna").isPresent()) {
							Ocena ocena = new Ocena();
							ocena.setOcena(newOcena.getOcena());
							ocena.setDataOcena(newOcena.getDataOcena());
							ocena.setUpisanaOcena(LocalDate.now());
							ocena.setUcenik(ucenik);
							ocena.setOnrp(onrp);
							ocenaRepo.save(ocena);
							try {
								sendTemplateMessage(ocena);

							} catch (Exception e) {
								return new ResponseEntity<RestError>(
										new RestError(1, "Error ocured: " + e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
							}
							logger.error("Greska prilikom kreiranja nove ocene");
							//logger.info("User (email: " + AuthController.getEmail() + ") added new ocena " + ocena);
							return new ResponseEntity<Ocena>(ocena, HttpStatus.OK);
						} else {
							return new ResponseEntity<RestError>(
									new RestError(9,
											"Postoji zakljucna ocena! Nije dozvoljeno dodavanje novih osena!"),
									HttpStatus.BAD_REQUEST);

						}
					} else {
						return new ResponseEntity<RestError>(new RestError(7,
								"Ucenik ne moze dobiti ocenu od nastavnika koji mu ne predaje."
								+ ""
								+ ""),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RestError>(
							new RestError(10, "Ne postoji predmetni profesor koji predaje predmet odeljenju sa trazenim ID"),
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa trazenim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji oznaka ocene sa trazenim ID"),
					HttpStatus.NOT_FOUND);

		}
	}

	@Override
	public ResponseEntity<?> updateOcena(Integer id, OcenaDTO updateOcena) {
		if (oznakaOceneRepo.existsById(updateOcena.getOznakaId())) {
			if (ucenikRepo.existsById(updateOcena.getucenikId())) {
				if (onrpRepo.existsById(updateOcena.getOnrpId())) {
					OznakaOcene o = oznakaOceneRepo.findById(updateOcena.getOznakaId()).get();
					Ucenik ucenik = ucenikRepo.findById(updateOcena.getucenikId()).get();
					Odeljenje_Nastavnik_Razred_Predmet onrp = new Odeljenje_Nastavnik_Razred_Predmet();
					if (ucenik.getOdeljenje().equals(onrp.getOdeljenje())) {
						Ocena ocena = ocenaRepo.findById(id).get();
						Ocena staraOcena = ocenaRepo.findById(id).get();
						ocena.setOcena(updateOcena.getOcena());
						ocena.setDataOcena(updateOcena.getDataOcena());
						ocena.setUpisanaOcena(LocalDate.now());
						ocena.setOznaka(o);;
						ocena.setUcenik(ucenik);
						ocena.setOnrp(onrp);
						ocenaRepo.save(ocena);
						try {
							sendTemplateMessageForUpdatedMark(ocena, staraOcena);

						} catch (Exception e) {
							return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
									HttpStatus.INTERNAL_SERVER_ERROR);
						}
						logger.error("Greska.");
						//logger.info("User (email: " + AuthController.getEmail() + ") updated ocena from " + staraOcena
							//	+ " to: " + ocena);
						return new ResponseEntity<Ocena>(ocena, HttpStatus.OK);

					} else {
						return new ResponseEntity<RestError>(new RestError(7,
								"Pupil can not be given mark due there is no connestion between puppil and class_teacher_grade_subject "),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RestError>(
							new RestError(10, "Ne postoji veya onrp sa zadatim ID"),
							HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RestError>(new RestError(10, "Ne postoji ucenik sa zadatim ID"),
						HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RestError>(new RestError(10, "Ne postoji oznaka ocene sa zadatim ID"),
					HttpStatus.NOT_FOUND);

		}
	}

	protected void sendTemplateMessage(Ocena ocena) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(ocena.getUcenik().getRoditelj().getEmail());
		helper.setSubject("Nova ocena");
		String text = "<html>" + "<body>" + "<table style='border:2px solid black'" + "<th>" + "<td>ucenik</td>"
				+ "<td>ocena</td>" + "<td>predmet</td>" + "<td>nastavnik</td>" + "<td>date</td>" + "</th>" + "<tr>"
				+ "<td>" + ocena.getUcenik().getName() + " " + ocena.getUcenik().getLastName() + "</td>" + "<td>"
				+ ocena.getOcena() + "</td>" + "<td>" + ocena.getOnrp().getNrp().getRazredPredmet().getPredmet().getName()
				+ "</td>" + "<td>" + ocena.getOnrp().getNrp().getNastavnik().getName() + " "
				+ ocena.getOnrp().getNrp().getNastavnik().getLastName() + "</td>" + "<td>" + ocena.getDataOcena() + "</td>"
				+ "</tr>" + "<table>" + "</body>" + "</html>";
		helper.setText(text, true);
		emailSender.send(mail);

	}

	protected void sendTemplateMessageForUpdatedMark(Ocena ocena, Ocena staraOcena) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(ocena.getUcenik().getRoditelj().getEmail());
		helper.setSubject("Nova ocena");
		String text = "<html>" + "<body>" + "<h3>Ocena je promenjena</h3><table style='border:2px solid black'" + "<th>"
				+ "<td>ucenik</td>" + "<td>ocena</td>" + "<td>predmet</td>" + "<td>nastavnik</td>" + "<td>date</td>"
				+ "</th>" + "<tr>" + "<td>" + ocena.getUcenik().getName() + " " + ocena.getUcenik().getLastName() + "</td>"
				+ "<td>" + ocena.getOcena() + "</td>" + "<td>"
				+ ocena.getOnrp().getNrp().getRazredPredmet().getPredmet().getName() + "</td>" + "<td>"
				+ ocena.getOnrp().getNrp().getNastavnik().getName() + " "
				+ ocena.getOnrp().getNrp().getNastavnik().getLastName() + "</td>" + "<td>" + ocena.getDataOcena() + "</td>"
				+ "</tr>" + "<tr>" + "<td>" + staraOcena.getUcenik().getName() + " " + staraOcena.getUcenik().getLastName()
				+ "</td>" + "<td>" + staraOcena.getOcena() + "</td>" + "<td>"
				+ staraOcena.getOnrp().getNrp().getRazredPredmet().getPredmet().getName() + "</td>" + "<td>"
				+ staraOcena.getOnrp().getNrp().getNastavnik().getName() + " "
				+ staraOcena.getOnrp().getNrp().getNastavnik().getLastName() + "</td>" + "<td>" + staraOcena.getDataOcena()
				+ "</td>" + "</tr>" + "<table>" + "</body>" + "</html>";
		helper.setText(text, true);
		emailSender.send(mail);

	}	
	

	@Override
	public ResponseEntity<?> zakljuciOcenu(Integer ucenikId, Integer predmetId, Integer suggestion) {
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
						String finalType = "FINAL";
						OznakaOcene o = oznakaOceneRepo.findByOznaka(finalType).get();
						Odeljenje_Nastavnik_Razred_Predmet onrp = onrpRepo.findByOdeljenjeAndNrp_RazredPredmet_Predmet(ucenik.getOdeljenje(), predmet).get();
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
							return new ResponseEntity<RestError>(new RestError(1, "Error ocured: " + e.getMessage()),
									HttpStatus.INTERNAL_SERVER_ERROR);
						}
						logger.error("Greska prilikom zakljucivanja ocene.");
						//logger.info(
							//	"User (email: " + AuthController.getEmail() + ") added new zakljucena ocena " + zakljucena);
						return new ResponseEntity<Ocena>(zakljucena, HttpStatus.OK);
					} else {
						return new ResponseEntity<RestError>(
								new RestError(99, "Ne moze da se zakljuci ocena manja od zakljucene ocene!"),
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
	}

	@Override
	public ResponseEntity<?> izracunajZakljucnu(Integer ucenikId, Integer predmetId) {
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
			logger.error("Greska prilikom dobijanja zakljucne ocene");
			return new ResponseEntity<Integer>(zakljucnaOcena, HttpStatus.OK);
		} else {
			return new ResponseEntity<RestError>(new RestError(9, "Postoji zakljucna ocena!"),
					HttpStatus.BAD_REQUEST);
		}
	}
}
