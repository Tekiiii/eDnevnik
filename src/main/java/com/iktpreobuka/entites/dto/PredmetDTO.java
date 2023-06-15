package com.iktpreobuka.entites.dto;

import com.iktpreobuka.entites.Predmet;

public class PredmetDTO {
	protected Long id;		
	protected Integer version;
	protected String name; 
	protected Integer fond;
	protected String razred;
	
	

	public PredmetDTO(Predmet p) {
		super();
		this.id = p.getId();
		this.name = p.getName();
		this.fond = p.getFond();
		this.razred = p.getRazred();
		this.version = p.getVersion();
	}
	
	public PredmetDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFond() {
		return fond;
	}
	public void setFond(Integer fond) {
		this.fond = fond;
	}
	public String getRazred() {
		return razred;
	}
	public void setRazred(String razred) {
		this.razred = razred;
	}
	
	
}
