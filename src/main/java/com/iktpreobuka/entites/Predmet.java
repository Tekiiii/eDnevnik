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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "predmet")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Predmet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private Integer version;

	@Column
	private String name;
	
	@Column
	private String razred;
	
	@Column
	private Integer fond;

	@JsonBackReference
	@OneToMany(mappedBy = "predmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Razred_Predmet> razredPredmet = new ArrayList<>();

	public Predmet() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Razred_Predmet> getRazredPredmet() {
		return razredPredmet;
	}

	public void setRazredPredmet(List<Razred_Predmet> razredPredmet) {
		this.razredPredmet = razredPredmet;
	}
	

	public Integer getFond() {
		return fond;
	}

	public void setFond(Integer fond) {
		this.fond = fond;
	}
	
	

	public String getRazred() {
		return razred;
	}

	public void setRazred(String razred) {
		this.razred = razred;
	}

	@Override
	public String toString() {
		return "{ id: " + this.getId() + ", name:" + this.getName() + "}";
	}

}
