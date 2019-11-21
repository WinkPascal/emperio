package com.swinkels.emperio.objects;

import java.util.ArrayList;

import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;

public class Bedrijf {
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	private String bedrijfsNaam;
	private String email;
	private String naam;
	private String tel;
	private String adres;
	private String wachtwoord;

	private String verplichtContactVeld;
	private boolean invoerveldEmail;
	private boolean invoerveldTelefoon;
	private boolean invoerveldAdres;

	public Bedrijf(String email) {
		this.email = email;
	}

	public Bedrijf(String bedrijfsNaam, String naam, String email, String tel, String adres, String wachtwoord) {
		this.bedrijfsNaam = bedrijfsNaam;
		this.naam = naam;
		this.email = email;
		this.tel = tel;
		this.adres = adres;
		this.wachtwoord = wachtwoord;
	}

	public String getVerplichtContactVeld() {
		return verplichtContactVeld;
	}

	public String getBedrijfsNaam() {
		return bedrijfsNaam;
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

	public boolean saveBedrijf() {
		if (bedrijfDao.saveBedrijf(this)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Behandeling> getBehandelingen() {
		System.out.println("sssssss2");

		return behandelingDao.getAllBehandelingen(this);
	}
}
