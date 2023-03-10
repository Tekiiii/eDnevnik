package com.iktpreobuka.entites;

import java.util.ArrayList;
import java.util.List;


public class PredmetOcena {

	String predmet;

	List<Ocena> ocena = new ArrayList<>();

	public PredmetOcena() {
		super();
	}

	public String getPredmet() {
		return predmet;
	}

	public void setPredmet(String predmet) {
		this.predmet = predmet;
	}

	public List<Ocena> getOcena() {
		return ocena;
	}

	public void setOcena(List<Ocena> ocena) {
		this.ocena = ocena;
	}

}
