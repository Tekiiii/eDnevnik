package com.iktpreobuka.entites;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ucenik")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Ucenik extends UserEntity {

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "roditelj_id")
	protected Roditelj roditelj;

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenje")
	protected Odeljenje odeljenje;

	@JsonIgnore
	@OneToMany(mappedBy = "ucenik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Ocena> ocena = new ArrayList<>();

	public Ucenik() {
		super();
	}

	public Roditelj getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(Roditelj roditelj) {
		this.roditelj = roditelj;
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
