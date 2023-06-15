package com.iktpreobuka.entites;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "nastavnici")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Nastavnik extends UserEntity {

	@Column(name = "no_of_licence")
	private String noOfLicence;

	@Enumerated(EnumType.STRING)
	@Column(name = "ime_pozicije")
	private PozicijaNastavnika pn;

	@JsonIgnore
	@OneToMany(mappedBy = "nastavnik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Nastavnik_Razred_Predmet> nrp = new ArrayList<>();

	public Nastavnik() {
		super();
	}

	public String getNoOfLicence() {
		return noOfLicence;
	}

	public void setNoOfLicence(String noOfLicence) {
		this.noOfLicence = noOfLicence;
	}

	public PozicijaNastavnika getPozicijaNastavnika() {
		return pn;
	}

	public void setPozicijaNastavnika(PozicijaNastavnika pn) {
		this.pn = pn;
	}

	public List<Nastavnik_Razred_Predmet> getNrp() {
		return nrp;
	}

	public void setNrp(List<Nastavnik_Razred_Predmet> nrp) {
		this.nrp = nrp;
	}

	@Override
	public String toString() {
		return super.toString() + ", noOfLicence:" + this.getNoOfLicence() + ", pn:" + this.getPozicijaNastavnika() + "}";
	}

}
