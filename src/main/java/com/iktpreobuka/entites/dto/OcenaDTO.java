package com.iktpreobuka.entites.dto;

import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OcenaDTO {

	@NotNull(message = "Ocena must be provided.")
	@Min(value = 1)
	@Max(value = 5)
	private Integer ocena;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  
	@PastOrPresent
	@NotNull(message = "Datum davanja ocene must be provided.")
	private LocalDate dataOcena;

	@NotNull(message = "Ucenik id must be provided.")
	@Digits(fraction = 0, integer = 10)
	private Integer ucenikId;

	@NotNull(message = "OdeljenjeNastavnikRazredPredmet id must be provided.")
	@Digits(fraction = 0, integer = 10)
	private Integer onrpId;

	@NotNull(message = "Oznaka ocene id must be provided.")
	@Digits(fraction = 0, integer = 10)
	private Integer oznakaId;

	public OcenaDTO() {
		super();
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

	public Integer getucenikId() {
		return ucenikId;
	}

	public void setucenikId(Integer ucenikId) {
		this.ucenikId = ucenikId;
	}

	public Integer getOnrpId() {
		return onrpId;
	}

	public void setOnrpId(Integer onrpId) {
		this.onrpId = onrpId;
	}

	public Integer getOznakaId() {
		return oznakaId;
	}

	public void setOznakaId(Integer oznakaId) {
		this.oznakaId = oznakaId;
	}


}
