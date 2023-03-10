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

@Entity
@Table(name = "odeljenje")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Odeljenje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@Column
	private Integer label;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razred")
	protected Razred razred;

	@JsonIgnore
	@OneToMany(mappedBy = "odeljenje", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Ucenik> ucenik = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "odeljenje", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Odeljenje_Nastavnik_Razred_Predmet> onrp = new ArrayList<>();

	public Odeljenje() {
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

	public Razred getRazred() {
		return razred;
	}

	public void setRazred(Razred razred) {
		this.razred = razred;
	}

	public List<Ucenik> getUcenik() {
		return ucenik;
	}

	public void setUcenik(List<Ucenik> ucenik) {
		this.ucenik = ucenik;
	}

	public List<Odeljenje_Nastavnik_Razred_Predmet> getOnrp() {
		return onrp;
	}

	public void setOnrp(List<Odeljenje_Nastavnik_Razred_Predmet> onrp) {
		this.onrp = onrp;
	}

	@Override
	public String toString() {
		return "{ id:" + this.getId() + ", oznaka:" + this.getLabel() + ", razred_id:" + this.getRazred().getId();
	}

}
