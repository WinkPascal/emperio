package com.swinkels.emperio.objects;

import javax.json.JsonValue;

public class Klant {
	private int id;
	private String naam;
	private String email;
	private String tel;
	private String geslacht;
	private String adres;
	private Bedrijf bedrijf;
	private int hoeveeleheidAfspraken;
	private double hoeveelheidInkomsten;

	public Klant(int id, String naam, String email, String tel, String geslacht, String klantAdres) {
		this.id = id;
		this.naam = naam;
		this.email = email;
		this.tel = tel;
		this.geslacht = geslacht;
		this.adres = klantAdres;
	}

	public Klant(String naam, String email, String tel, String geslacht) {
		this.naam = naam;
		this.email = email;
		this.tel = tel;
		this.geslacht = geslacht;
	}
	
	public Klant(String naam, String email, String tel, String geslacht, Bedrijf bedrijf) {
		this.naam = naam;
		this.email = email;
		this.tel = tel;
		this.geslacht = geslacht;
		this.bedrijf = bedrijf;
	}

	
	public Klant(String klantNaam) {
		this.naam = klantNaam;
	}

	// getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getGeslacht() {
		return geslacht;
	}

	public void setGeslacht(String geslacht) {
		this.geslacht = geslacht;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getNaam() {
		return naam;
	}

	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public int getAantalAfspraken() {
		return hoeveeleheidAfspraken;
	}

	public String getAdres() {
		return adres;
	}

	public double getHoeveelheidInkomsten() {
		return hoeveelheidInkomsten;
	}
}
