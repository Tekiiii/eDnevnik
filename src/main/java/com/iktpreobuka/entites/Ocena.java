package com.iktpreobuka.entites;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ocena")
public class Ocena {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	private Integer version;

	@Column
	private Integer ocena;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column
	private LocalDate dataOcena;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column
	private LocalDate upisanaOcena;

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "oznaka")
	protected OznakaOcene oznaka;

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "given_by")
	protected Odeljenje_Nastavnik_Razred_Predmet onrp;

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "given_to")
	protected Ucenik ucenik;

	public Ocena() {
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

	public Integer getOcena() {
		return ocena;
	}

	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}

	public LocalDate getDataOcena() {
		return dataOcena;
	}

	public void setDataOcena(LocalDate dataOcena) {
		this.dataOcena = dataOcena;
	}

	public LocalDate getUpisanaOcena() {
		return upisanaOcena;
	}

	public void setUpisanaOcena(LocalDate upisanaOcena) {
		this.upisanaOcena = upisanaOcena;
	}

	public OznakaOcene getOznaka() {
		return oznaka;
	}

	public void setOznaka(OznakaOcene oznaka) {
		this.oznaka = oznaka;
	}

	public Odeljenje_Nastavnik_Razred_Predmet getOnrp() {
		return onrp;
	}

	public void setOnrp(Odeljenje_Nastavnik_Razred_Predmet onrp) {
		this.onrp = onrp;
	}

	public Ucenik getUcenik() {
		return ucenik;
	}

	public void setUcenik(Ucenik ucenik) {
		this.ucenik = ucenik;
	}

	@Override
	public String toString() {
		return "{ id: " + this.getId() + ", ocena:" + this.getOcena() + ", oznaka_id:" + this.getOznaka().getId() + ", data:"
				+ this.getDataOcena() + ", upisana:" + this.getUpisanaOcena() + ", onrp_id:" + this.getOnrp().getId()
				+ ", ucenik_id:" + this.getUcenik().getId() + " }";
	}

}
