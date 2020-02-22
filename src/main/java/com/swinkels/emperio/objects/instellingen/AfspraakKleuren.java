package com.swinkels.emperio.objects.instellingen;

import java.util.HashMap;

public class AfspraakKleuren {
	private String kleurKlasse1;
	private double maximumPrijsVanKlasse1;
	private String kleurKlasse2;
	private double maximumPrijsVanKlasse2;
	private String kleurKlasse3;

	protected AfspraakKleuren(String kleurKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2, double maximumPrijsVanKlasse2, String kleurKlasse3) {
		this.kleurKlasse1 = kleurKlasse1;
		this.maximumPrijsVanKlasse1 = maximumPrijsVanKlasse1;
		this.kleurKlasse2 = kleurKlasse2;
		this.maximumPrijsVanKlasse2 = maximumPrijsVanKlasse2;
		this.kleurKlasse3 = kleurKlasse3;
	}

	protected HashMap<String, String> toDto(){
		HashMap<String, String> dto = new HashMap<String, String>();
		dto.put("kleurKlasse1", kleurKlasse1);
		dto.put("maximumPrijsVanKlasse1", Double.toString(maximumPrijsVanKlasse1));
		dto.put("kleurKlasse2", kleurKlasse2);
		dto.put("maximumPrijsVanKlasse2", Double.toString(maximumPrijsVanKlasse2));
		dto.put("kleurKlasse3", kleurKlasse3);
		return dto;
	}
	protected void save(){

	}
}
