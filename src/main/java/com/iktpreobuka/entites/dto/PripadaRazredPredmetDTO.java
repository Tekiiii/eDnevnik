package com.iktpreobuka.entites.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PripadaRazredPredmetDTO {

	@NotNull(message = "Razred id must be provided.")
	@Digits(fraction = 0, integer = 10)
	private Integer razredId;

	@NotNull(message = "Active must be provided.")
	private Boolean active;

	@NotNull(message = "Casova nedeljno must be provided.")
	@Digits(fraction = 0, integer = 10)
	private Integer casovaNedeljno;


	@Size(min = 2, max = 30)
	private String knjiga;


	public PripadaRazredPredmetDTO() {
		super();
	}

	public Integer getRazredId() {
		return razredId;
	}

	public void setRazredId(Integer razredId) {
		this.razredId = razredId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

}
