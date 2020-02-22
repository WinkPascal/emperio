package com.swinkels.emperio.objects.klant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.AfspraakInterface;
import com.swinkels.emperio.providers.Klant.KlantDao;
import com.swinkels.emperio.providers.Klant.KlantDaoImpl;

public class Klant implements KlantInterface {
	private static KlantDao klantDao = new KlantDaoImpl();

	private KlantStatestieken statestieken;
	private List<Afspraak> afspraken = new ArrayList<Afspraak>();

	private int id;
	private String naam;
	private String email;
	private String telefoon;
	private String geslacht;
	private String adres;

	public Klant(int id, String naam, String email, String telefoon, String geslacht, String adres) {
		this.id = id;
		this.naam = naam;
		this.email = email;
		this.telefoon = telefoon;
		this.geslacht = geslacht;
		this.adres = adres;
	}

	public static Klant getKlantById(int id) {
		Klant klant = klantDao.getKlantById(id);
		List<Afspraak> afspraken = AfspraakInterface.getLaatste5AfsprakenFromKlant(id);
		klant.setAfspraken(afspraken);
		return klant;
	}

	public HashMap<String, String> toDTO() {
		HashMap<String, String> dto = new HashMap<>();
		dto.put("id", Integer.toString(id));
		dto.put("naam", naam);
		dto.put("email", email);
		dto.put("telefoon", telefoon);
		dto.put("geslacht", geslacht);
		dto.put("adres", adres);
		return dto;
	}

	public String getEmail(){
		return email;
	}

	// de benodigde getters en setters
	public List<Afspraak> getAfspraken() {
		return afspraken;
	}

	public void addAfspraak(Afspraak afspraak) {
		this.afspraken.add(afspraak);
	}
	
	
	public void setAfspraken(List<Afspraak> afspraak) {
		afspraken = afspraak;
	}

	public KlantStatestieken getStatestieken() {
		return statestieken;
	}

	public void setStatestieken(KlantStatestieken statestieken) {
		this.statestieken = statestieken;
	}
}
