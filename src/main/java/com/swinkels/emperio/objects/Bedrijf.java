package com.swinkels.emperio.objects;

public class Bedrijf {
	private String email;
	private String naam;
	private String tel;
	private String adres;
	private String wachtwoord;
	private String verplichtContactVeld;
	private boolean invoerveldEmail;
	private boolean invoerveldTelefoon;
	private boolean invoerveldAdres;
	
	public Bedrijf(String bedrijfsNaam) {
		this.email = bedrijfsNaam;
	}
	
	public Bedrijf(String email, String naam, String tel, String adres) {
		this.email = email;
		this.naam = naam;
		this.tel = tel;
		this.adres = adres;
	}
	
	public String getVerplichtContactVeld() {
		return verplichtContactVeld;
	}

	public void setVerplichtContactVeld(String verplichtContactVeld) {
		this.verplichtContactVeld = verplichtContactVeld;
	}

	public boolean getInvoerveldEmail() {
		return invoerveldEmail;
	}

	public void setInvoerveldEmail(boolean invoerveldEmail) {
		this.invoerveldEmail = invoerveldEmail;
	}

	public boolean getInvoerveldTelefoon() {
		return invoerveldTelefoon;
	}

	public void setInvoerveldTelefoon(boolean invoerveldTelefoon) {
		this.invoerveldTelefoon = invoerveldTelefoon;
	}

	public boolean getInvoerveldAdres() {
		return invoerveldAdres;
	}

	public void setInvoerveldAdres(boolean invoerveldAdres) {
		this.invoerveldAdres = invoerveldAdres;
	}

	public Bedrijf(String email, String naam, String tel, String adres, String wachtwoord) {
		this.email = email;
		this.naam = naam;
		this.tel = tel;
		this.adres = adres;
		this.wachtwoord = wachtwoord;
	}
	
	public Bedrijf() {
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
