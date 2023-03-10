package com.iktpreobuka.entites.dto;

import javax.validation.constraints.NotNull;

public class OdeljenjeNastavnikRazredPredmetDTO {

	@NotNull
	private Boolean active;
	
	@NotNull
	private Integer nrpId;
	
	@NotNull
	private Integer odeljenjeId;

	public OdeljenjeNastavnikRazredPredmetDTO() {
		super();
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getNrpId() {
		return nrpId;
	}

	public void setNrpId(Integer nrpId) {
		this.nrpId = nrpId;
	}

	public Integer getOdeljenjeId() {
		return odeljenjeId;
	}

	public void setOdeljenjeId(Integer odeljenjeId) {
		this.odeljenjeId = odeljenjeId;
	}

}
