package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public ArrayList<String> validate(){
		ArrayList<String> errors = new ArrayList<String>();
		if(this.email != null) {
			Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
			Matcher mat = pattern.matcher(email);
			if (!mat.matches()) {
				errors.add("Voer een geldig email adres in.");
			}
		}
		if(this.telefoon != null) {
			String regex = "^(((\\\\+31|0|0031)6){1}[1-9]{1}[0-9]{7})$";
			Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			Matcher mat = pattern.matcher(this.telefoon);
			if (!mat.matches()) {
				errors.add("Voer een geldig mobiel nummer in.");
			}
		}
		if(this.postcode != null) {
			String regex = "^[1-9][0-9]{3}[ ]?([A-RT-Za-rt-z][A-Za-z]|[sS][BCbcE-Re-rT-Zt-z])$";
			Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			Matcher mat = pattern.matcher(this.postcode);
			if (!mat.matches()) {
				errors.add("Voer een geldig postcode in");
			}
		}
		if(this.wachtwoord != null) {
			String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
			Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			Matcher mat = pattern.matcher(this.wachtwoord);
			if (!mat.matches()) {
				errors.add("Uw wachtword moet minimaal 8 karakters, 1 nummer, een kleine en grote letter en een speciaal teken bevatten.");
			}
		}
		return errors;
	}
	
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
	
	public void getOpeningsTijden(Date vandaag) {
		DagDao dagDao = new DagDaoImpl();
		dagDao.getOpeningsTijden(this, vandaag);
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
