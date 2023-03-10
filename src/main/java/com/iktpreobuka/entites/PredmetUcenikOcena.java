package com.iktpreobuka.entites;

import java.util.ArrayList;
import java.util.List;


public class PredmetUcenikOcena {

	private Ucenik ucenik;

	private List<Ocena> ocena = new ArrayList<>();

	public PredmetUcenikOcena() {
		super();
	}

	public Ucenik getUcenik() {
		return ucenik;
	}

	public void setUcenik(Ucenik ucenik) {
		this.ucenik = ucenik;
	}

	public List<Ocena> getOcena() {
		return ocena;
	}

	public void setOcena(List<Ocena> ocena) {
		this.ocena = ocena;
	}

}
