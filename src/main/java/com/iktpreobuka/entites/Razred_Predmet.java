package com.iktpreobuka.entites;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "razred_predmet")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Razred_Predmet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@Column
	private Boolean active;

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predmet")
	protected Predmet predmet;

	@Column(name = "casova_nedeljno")
	private Integer casovaNedeljno;


	@Column(name = "knjiga")
	private String knjiga;


	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razred")
	protected Razred razred;

	@JsonIgnore
	@OneToMany(mappedBy = "razredPredmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Nastavnik_Razred_Predmet> nrp = new ArrayList<>();

	public Razred_Predmet() {
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Predmet getPredmet() {
		return predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	public Integer getCasovaNedeljno() {
		return casovaNedeljno;
	}

	public void setCasovaNedeljno(Integer casovaNedeljno) {
		this.casovaNedeljno = casovaNedeljno;
	}


	public String getKnjiga() {
		return knjiga;
	}

	public void setKnjiga(String knjiga) {
		this.knjiga = knjiga;
	}

	public Razred getRazred() {
		return razred;
	}

	public void setRazred(Razred razred) {
		this.razred = razred;
	}

	public List<Nastavnik_Razred_Predmet> getNrp() {
		return nrp;
	}

	public void setNrp(List<Nastavnik_Razred_Predmet> nrp) {
		this.nrp = nrp;
	}

	@Override
	public String toString() {
		return " id: " + this.getId() + ", knjiga:" + this.getKnjiga()
				+", casovaNedeljno:" + this.getCasovaNedeljno()
				+ ", razred_id:" + this.getRazred().getId() + ", subject_id:"
				+ this.getPredmet().getId() + "}";
	}

}
