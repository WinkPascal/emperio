package com.swinkels.emperio.objects;

import java.util.ArrayList;

import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;

public class Bedrijf {
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	private String bedrijfsNaam;
	private String email;
	private String naam;
	private String tel;
	private String adres;
	private String wachtwoord;



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

	public String getBedrijfsNaam() {
		return bedrijfsNaam;
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

	public ArrayList<Klant> getKlantenWithByPage(int pageNumber) {
		KlantDao klantDao = new KlantDaoImpl();
		int top = pageNumber * 20;
		int low = top - 20;
		return klantDao.getKlantenWithLimit(this, low, top);		
	}
}
