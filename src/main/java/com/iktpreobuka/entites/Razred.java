
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "razred")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Razred {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@Column(name = "label")
	private Integer label;

	@JsonBackReference
	@OneToMany(mappedBy = "razred", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Razred_Predmet> razredPredmet = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "razred", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Odeljenje> odeljenje = new ArrayList<>();

	public Razred() {
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

	public Integer getLabel() {
		return label;
	}

	public void setLabel(Integer label) {
		this.label = label;
	}

	public List<Razred_Predmet> getRazredPredmet() {
		return razredPredmet;
	}

	public void setRazredPredmet(List<Razred_Predmet> razredPredmet) {
		this.razredPredmet = razredPredmet;
	}

	public List<Odeljenje> getOdeljenje() {
		return odeljenje;
	}

	public void setOdeljenje(List<Odeljenje> odeljenje) {
		this.odeljenje = odeljenje;
	}

}
