package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.providers.DagDao;
import com.swinkels.emperio.providers.DagDaoImpl;
import com.swinkels.emperio.providers.InstellingenDao;
import com.swinkels.emperio.providers.InstellingenDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;

public class Bedrijf {
	private ArrayList<Dag> dagen = new ArrayList<Dag>();
	private ArrayList<Behandeling> behandelingen;
	private Instellingen instellingen;
	
	private String bedrijfsNaam;
	private Date inschrijfdatum;
	private String email;
	private String telefoon;
	private String woonplaats;
	private String postcode;
	private String adres;
	private String wachtwoord;

	public boolean save() {
		BedrijfDao bedrijfDao = new BedrijfDaoImpl();
		if (bedrijfDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Klant> getKlantenWithByPage(int pageNumber) {
		KlantDao klantDao = new KlantDaoImpl();
		int top = pageNumber * 20;
		int low = top - 20;
		return klantDao.getKlantenWithLimit(this, low, top);
	}

	public void retrieveBehandelingen() {
		BehandelingDao behandelingDao = new BehandelingDaoImpl();
		this.behandelingen =  behandelingDao.getAllBehandelingen(this);
	}

	public void saveDagen() {
		DagDao dagDao = new DagDaoImpl();
		for (Dag dag : dagen) {
			dagDao.saveDag(dag);
		}
	}
	
	public void retrieveDagen() {
		DagDao dagDao = new DagDaoImpl();
		dagDao.getWeekRooster(this);		
	}
	
	public void getKlantPaginaSettings(){
		BedrijfDao bedrijfDao = new BedrijfDaoImpl();
		bedrijfDao.getKlantPaginaSettings(this);
	}

	public void retrieveBehandelingenByGeslacht(String geslacht) {
		BehandelingDao behandelingDao = new BehandelingDaoImpl();
		behandelingDao.behandelingenByGeslacht(geslacht, this);
	}
	
	public Bedrijf(String bedrijfsNaam) {
		this.bedrijfsNaam = bedrijfsNaam;
	}
	
	public Bedrijf(String bedrijfsNaam, String wachtwoord, String email, String telefoon, String adres, String woonplaats, String postcode) {
		this.bedrijfsNaam = bedrijfsNaam;
		this.wachtwoord = wachtwoord;
		this.email = email;
		this.telefoon = telefoon;
		this.adres = adres;
		this.woonplaats = woonplaats;
		this.postcode = postcode;
	}

	public ArrayList<Dag> getDagen() {
		return this.dagen;
	}
	
	public ArrayList<Behandeling> getBehandelingen() {
		return this.behandelingen;
	}

	public void setDagen(ArrayList<Dag> dagen) {
		this.dagen = dagen;
	}

	public void addDag(Dag dag) {
		System.out.println(dag);
		this.dagen.add(dag);
	}

	public Date getInschrijfdatum() {
		return inschrijfdatum;
	}

	public Instellingen getInstellingen() {
		return instellingen;
	}

	public void setInstellingen(Instellingen instellingen) {
		this.instellingen = instellingen;
	}

	public void setBehandelingen(ArrayList<Behandeling> behandelingen) {
		this.behandelingen = behandelingen;
	}
	
	public void addBehandeling(Behandeling behandeling) {
		System.out.println(behandeling.getId());
		this.behandelingen.add(behandeling);
	}

	public void setInschrijfdatum(Date inschrijfdatum) {
		this.inschrijfdatum = inschrijfdatum;
	}

	public String getWoonplaats() {
		return woonplaats;
	}

	public void setWoonplaats(String woonplaats) {
		this.woonplaats = woonplaats;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setBedrijfsNaam(String bedrijfsNaam) {
		this.bedrijfsNaam = bedrijfsNaam;
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

	public String getTelefoon() {
		return telefoon;
	}

	public void setTelefoon(String tel) {
		this.telefoon = tel;
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
