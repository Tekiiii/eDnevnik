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
@Table(name = "oznaka_ocene")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OznakaOcene {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@Column(unique = true)
	private String oznaka;

	@Column
	private String description;

	@JsonBackReference
	@OneToMany(mappedBy = "oznaka", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Ocena> ocena = new ArrayList<>();

	public OznakaOcene() {
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

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Ocena> getOcena() {
		return ocena;
	}

	public void setOcena(List<Ocena> ocena) {
		this.ocena = ocena;
	}

	@Override
	public String toString() {
		return "{ id: " + this.getId() + ", oznaka:" + this.getOznaka() + ", description:" + this.getDescription() + " }";
	}


}
