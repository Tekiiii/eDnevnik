package com.iktpreobuka.entites.dto;

import com.iktpreobuka.entites.Predmet;

public class PredmetDTO {
	protected Integer id;		
	protected String name; 
	protected Integer fond;
	protected String razred;
	
	

	public PredmetDTO(Predmet p) {
		super();
		this.id = p.getId();
		this.name = p.getName();
		this.fond = p.getFond();
		this.razred = p.getRazred();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
