package com.swinkels.emperio.objects.klant;

public class KlantBuilder {
	private int id;

	private String naam;
	private String email;
	private String telefoon;
	private String geslacht;
	private String adres;

	public KlantBuilder() {

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

	public KlantBuilder setTel(String telefoon) {
		this.telefoon = telefoon;
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

	public Klant build() {
		Klant klant = new Klant(id, naam, email, telefoon, geslacht, adres);
		return klant;
	}
}
