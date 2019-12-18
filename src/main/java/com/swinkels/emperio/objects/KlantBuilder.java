package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.List;

public class KlantBuilder {
	private List<Afspraak> afspraken = new ArrayList<Afspraak>();

	private int id;

	private String naam;
	private String email;
	private String tel;
	private String geslacht;
	private String adres;
	private Bedrijf bedrijf;

	private int hoeveeleheidAfspraken;
	private double hoeveelheidInkomsten;

	public KlantBuilder() {

	}

	public KlantBuilder setAfspraken(List<Afspraak> afspraken) {
		this.afspraken = afspraken;
		return this;
	}

	public KlantBuilder setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
		return this;
	}

	public KlantBuilder setHoeveeleheidAfspraken(int hoeveeleheidAfspraken) {
		this.hoeveeleheidAfspraken = hoeveeleheidAfspraken;
		return this;
	}

	public KlantBuilder setHoeveelheidInkomsten(double hoeveelheidInkomsten) {
		this.hoeveelheidInkomsten = hoeveelheidInkomsten;
		return this;
	}

	public KlantBuilder setId(int id) {
		this.id = id;
		return this;
	}

	public KlantBuilder setNaam(String naam) {
		this.naam = naam;
		return this;
	}

	public KlantBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public KlantBuilder setTel(String tel) {
		this.tel = tel;
		return this;
	}

	public KlantBuilder setGeslacht(String geslacht) {
		this.geslacht = geslacht;
		return this;
	}

	public KlantBuilder setAdres(String adres) {
		this.adres = adres;
		return this;
	}

	public Klant make() {
		Klant klant =new Klant(afspraken, id, naam,
				 email, tel, geslacht,  adres,  bedrijf,  hoeveeleheidAfspraken,
				 hoeveelheidInkomsten);
		return klant;
	}
}
