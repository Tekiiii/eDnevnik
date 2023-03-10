package com.iktpreobuka.entites;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "nadleznost")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Nastavnik_Razred_Predmet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razred_Predmet")
	protected Razred_Predmet razredPredmet;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nastavnik")
	protected Nastavnik nastavnik;

	@JsonIgnore
	@OneToMany(mappedBy = "nrp", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Odeljenje_Nastavnik_Razred_Predmet> onrp = new ArrayList<>();

	public Nastavnik_Razred_Predmet() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Razred_Predmet getRazredPredmet() {
		return razredPredmet;
	}

	public void setRazredPredmet(Razred_Predmet razredPredmet) {
		this.razredPredmet = razredPredmet;
	}

	public Nastavnik getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(Nastavnik nastavnik) {
		this.nastavnik = nastavnik;
	}

	public List<Odeljenje_Nastavnik_Razred_Predmet> getCtgs() {
		return onrp;
	}

	public void setOnrp(List<Odeljenje_Nastavnik_Razred_Predmet> onrp) {
		this.onrp = onrp;
	}

	@Override
	public String toString() {
		return "{ id:" + this.getId() + ", nastavnik_id: " + this.getNastavnik().getId() + ", nrp_id: "
				+ this.getRazredPredmet().getId() + " }";
	}

}
