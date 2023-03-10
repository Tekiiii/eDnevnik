package com.iktpreobuka.entites;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends UserEntity {

	public Admin() {
		super();
	}
}
