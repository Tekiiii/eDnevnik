package com.iktpreobuka.entites.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NazivPredmetaDTO {

	@NotNull(message = "Naziv predmeta must be provided.")
	@Size(min = 2, max = 30, message = "Naziv predmeta must be between {min} and {max} characters long.")
	private String naziv;

	public NazivPredmetaDTO() {
		super();
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

}
