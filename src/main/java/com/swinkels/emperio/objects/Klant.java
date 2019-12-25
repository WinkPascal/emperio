package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.List;

import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;

public class Klant {
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakDao afspraakDao = new AfspraakDaoImpl();

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



	public Klant(List<Afspraak> afspraken, int id, String naam,
			String email, String tel, String geslacht, String adres, Bedrijf bedrijf, int hoeveeleheidAfspraken,
			double hoeveelheidInkomsten) {
		this.afspraken = afspraken;
		this.id = id;
		this.naam = naam;
		this.email = email;
		this.tel = tel;
		this.geslacht = geslacht;
		this.adres = adres;
		this.bedrijf = bedrijf;
		this.hoeveeleheidAfspraken = hoeveeleheidAfspraken;
		this.hoeveelheidInkomsten = hoeveelheidInkomsten;
	}

	public void getInfo() {
		klantDao.getKlant(bedrijf, this);
		afspraakDao.getLaatste3Afspraken(bedrijf, this);
	}

	public void saveOrFindAndGetId() {
		if (!klantDao.getKlantIdByEmail(this)) {
			if (!klantDao.getKlantIdByPhone(this)) {
				klantDao.setKlant(this);
				klantDao.getKlantId(this);
			}
		}
	}
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
	public List<Afspraak> getAfspraken() {
		return afspraken;
	}

	public void setAfspraken(List<Afspraak> afspraken) {
		this.afspraken = afspraken;
	}
	
	public void addAfspraak(Afspraak afspraak) {
		afspraken.add(afspraak);
	}
	
	public String getNaam() {
		return naam;
	}

	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public int getHoeveeleheidAfspraken() {
		return hoeveeleheidAfspraken;
	}

	public void setHoeveeleheidAfspraken(int hoeveeleheidAfspraken) {
		this.hoeveeleheidAfspraken = hoeveeleheidAfspraken;
	}

	public void setHoeveelheidInkomsten(double hoeveelheidInkomsten) {
		this.hoeveelheidInkomsten = hoeveelheidInkomsten;
	}

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public int getAantalAfspraken() {
		return hoeveeleheidAfspraken;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public double getHoeveelheidInkomsten() {
		return hoeveelheidInkomsten;
	}



}
