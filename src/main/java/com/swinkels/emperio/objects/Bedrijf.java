package com.swinkels.emperio.objects;

public class Bedrijf {
	private String email;
	private String naam;
	private String tel;
	private String adres;
	private String wachtwoord;
	
	public Bedrijf(String bedrijfsNaam) {
		this.email = bedrijfsNaam;
	}
	
	public Bedrijf(String email, String naam, String tel, String adres) {
		this.email = email;
		this.naam = naam;
		this.tel = tel;
		this.adres = adres;
	}
	
	public Bedrijf(String email, String naam, String tel, String adres, String wachtwoord) {
		this.email = email;
		this.naam = naam;
		this.tel = tel;
		this.adres = adres;
		this.wachtwoord = wachtwoord;
	}
	
	public String getWachtwoord() {
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
