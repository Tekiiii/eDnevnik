package com.iktpreobuka.entites.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.iktpreobuka.entites.Nastavnik;
import com.iktpreobuka.entites.PozicijaNastavnika;

public class NastavnikDTO {

	protected Integer id;		
	protected String lastName;
	protected String name; 
	protected String noOfLicence;
	protected String email;
	//@Enumerated(EnumType.STRING)
	protected PozicijaNastavnika pn;
	
	
	public NastavnikDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public NastavnikDTO(Nastavnik n) {
		super();
		this.id = n.id;
		this.lastName = n.lastName;
		this.name =n.name;
		this.noOfLicence = n.noOfLicence;
		this.email = n.email;
		this.pn = n.pn;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNoOfLicence() {
		return noOfLicence;
	}


	public void setNoOfLicence(String noOfLicence) {
		this.noOfLicence = noOfLicence;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public PozicijaNastavnika getPn() {
		return pn;
	}


	public void setPn(PozicijaNastavnika pn) {
		this.pn = pn;
	}
	
	
}
