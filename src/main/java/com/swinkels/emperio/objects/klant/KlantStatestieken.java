package com.swinkels.emperio.objects.klant;

import java.util.HashMap;

public class KlantStatestieken {

	private int aantalAfspraken;
	private double hoeveelheidInkomsten;

	public KlantStatestieken(Double hoeveelheidInkomsten, int aantalAfspraken) {
		this.aantalAfspraken = aantalAfspraken;
		this.hoeveelheidInkomsten = hoeveelheidInkomsten;
	}

	
	public HashMap<String, String> toDTO() {
		HashMap<String, String> dto = new HashMap<>();		
		dto.put("aantalAfspraken", Integer.toString(aantalAfspraken));
		dto.put("hoeveelheidInkomsten", Double.toString(hoeveelheidInkomsten));
		return dto;
	}
}
