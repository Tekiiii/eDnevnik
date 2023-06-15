package com.iktpreobuka.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.iktpreobuka.util.Encryption;


@Entity
@Table(name = "korisnici")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer"})
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Version
	private Integer version;

	@Column
	public String name;

	@Column(name = "last_name")
	public String lastName;

	@Column(name = "email", nullable = false, unique = true)
	public String email;

	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Transient
	private Boolean firstLog;

	public UserEntity() {
		super();
		firstLog = true;
		password = Encryption.getPassEncoded("Pass");
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Boolean getFirstLog() {
		return firstLog;
	}

	public void setFirstLog(Boolean firstLog) {
		this.firstLog = firstLog;
	}

	@Override
	public String toString() {
		return "{ id:" + this.getId() + ", name:" + this.getName() + ", lastName:" + this.getLastName() + ", email:"
				+ this.getEmail() + ", role:" + this.getRole() + "}";
	}
}
