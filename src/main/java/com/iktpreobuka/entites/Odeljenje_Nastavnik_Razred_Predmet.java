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
@Table(name = "raspored")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Odeljenje_Nastavnik_Razred_Predmet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@Column(name = "active")
	private Boolean active;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nadleznost")
	protected Nastavnik_Razred_Predmet nrp;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenje")
	protected Odeljenje odeljenje;

	@JsonIgnore
	@OneToMany(mappedBy = "onrp", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Ocena> ocena = new ArrayList<>();

	public Odeljenje_Nastavnik_Razred_Predmet() {
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

	public Nastavnik_Razred_Predmet getNrp() {
		return nrp;
	}

	public void setNrp(Nastavnik_Razred_Predmet nrp) {
		this.nrp = nrp;
	}

	public Odeljenje getOdeljenje() {
		return odeljenje;
	}

	public void setOdeljenje(Odeljenje odeljenje) {
		this.odeljenje = odeljenje;
	}

	public List<Ocena> getOcena() {
		return ocena;
	}

	public void setOcena(List<Ocena> ocena) {
		this.ocena = ocena;
	}

}
