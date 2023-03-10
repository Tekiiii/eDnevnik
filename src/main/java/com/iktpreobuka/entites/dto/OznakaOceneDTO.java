package com.iktpreobuka.entites.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OznakaOceneDTO {
	
	@NotNull(message = "Oznaka ocene must be provided.")
	@Size(min = 2, max = 30, message = "Oznaka ocene must be between {min} and {max} characters long.")
	private String oznaka;
	
	@NotNull(message = "Opis ocene  must be provided.")
	@Size(min = 2, max = 300, message = "Opis ocene must be between {min} and {max} characters long.")
	private String opis;

	public OznakaOceneDTO() {
		super();
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	
}
