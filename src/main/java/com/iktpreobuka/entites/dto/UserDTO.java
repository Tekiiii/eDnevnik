package com.iktpreobuka.entites.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.iktpreobuka.entites.PozicijaNastavnika;

public class UserDTO {

	private String user;
	private String token;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@NotNull(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	private String name;

	@NotNull(message = "Last name must be provided.")
	@Size(min = 2, max = 30, message = "Last name must be between {min} and {max} characters long.")
	private String lastName;

	@NotNull(message = "Email must be provided.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.")
	private String email;

	private PozicijaNastavnika pozicijaNastavnika;

	private String teacherNoOfLicence;

	public UserDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PozicijaNastavnika getPozicijaNastavnika() {
		return pozicijaNastavnika;
	}

	public void setPozicijaNastavnika( PozicijaNastavnika pozicijaNastavnika) {
		this.pozicijaNastavnika = pozicijaNastavnika;
	}

	public String getTeacherNoOfLicence() {
		return teacherNoOfLicence;
	}

	public void setTeacherNoOfLicence(String teacherNoOfLicence) {
		this.teacherNoOfLicence = teacherNoOfLicence;
	}

}
