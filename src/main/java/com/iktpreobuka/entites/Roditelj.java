package com.iktpreobuka.entites;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "roditelj")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Roditelj extends UserEntity {

	@JsonBackReference
	@OneToMany(mappedBy = "roditelj", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	protected List<Ucenik> ucenik = new ArrayList<>();

	public Roditelj() {
		super();
	}

	public List<Ucenik> getUcenik() {
		return ucenik;
	}

	public void setUcenik(List<Ucenik> ucenik) {
		this.ucenik = ucenik;
	}


}
