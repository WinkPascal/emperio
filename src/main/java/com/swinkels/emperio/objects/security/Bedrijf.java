package com.swinkels.emperio.objects.security;

import java.util.ArrayList;  
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.email.Email;
import com.swinkels.emperio.objects.instellingen.InstellingenFacade;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.support.JavascriptDateAdapter;

public class Bedrijf {
	private String bedrijfsNaam;
	private Date inschrijfdatum;
	private String wachtwoord;

	public ArrayList<Email> getEmails() {
		return emails;
	}

	public ArrayList<Klant> getKlantenByRequest(int page, String sort, String search) {
		klantDao.getKlanten(this, page, sort, search);
		return null;
	}
	
	public void setEmails(ArrayList<Email> emails) {
		this.emails = emails;
	}
	public void getInfo() {
		bedrijfDao.getInfo(this);
	}

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
	
	public ArrayList<Email> getEmailsByRequest(int hoeveelheid) {
		return emailDao.getEmails(this, hoeveelheid);
	}


	public Date getVroegsteOpeningsTijd() {
		Date vroegsteOpeningsTijd = JavascriptDateAdapter.StringToDate("23:59", "HH:mm");
		for (Dag dag : dagen) {
			if(dag.getOpeningsTijd() != null && vroegsteOpeningsTijd.compareTo(dag.getOpeningsTijd()) > 0) {
				vroegsteOpeningsTijd = dag.getOpeningsTijd();
			}
		}
		return vroegsteOpeningsTijd;
	}
	
	public ArrayList<Klant> getKlanten() {
		return klanten;
	}

	public void setKlanten(ArrayList<Klant> klanten) {
		this.klanten = klanten;
	}

	public Date getLaatsteSluitingsTijd() {
		Date laatsteSluitingsTijd = JavascriptDateAdapter.StringToDate("00:00", "HH:mm");
		for (Dag dag : dagen) {
			if (dag.getSluitingsTijd() != null && laatsteSluitingsTijd.compareTo(dag.getSluitingsTijd()) < 0) {
				laatsteSluitingsTijd = dag.getSluitingsTijd();
			}
		}
		return laatsteSluitingsTijd;
	}
	
	public boolean save() {
		if (bedrijfDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}
	public void zoekKlant(String request) {
		klantDao.zoekKlant(this, request);
	}

	public void zoekBehandelingen(int pageNumber, String geslacht, String sort, String zoek) {
		behandelingDao.getBehandelingen(this, pageNumber, geslacht, sort, zoek);
	}
	
	public ArrayList<Klant> getKlantenWithByPage(int pageNumber) {
		int top = pageNumber * 20;
		int low = top - 20;
		return klantDao.getKlantenWithLimit(this, low, top);
	}

	public void saveDagen() {
		if(bedrijfDao.needsSetup(bedrijfsNaam)) {
			for (Dag dag : dagen) {
				dagDao.saveDag(dag);
			}
		} else {
			for (Dag dag : dagen) {
				dagDao.updateDag(dag);
			}
		}
	}
	

	public void updateDagen() {

	}
	
	public void retrieveDagen() {
		dagDao.getWeekRooster(this);		
	}
	
	public void getKlantPaginaSettings(){
		bedrijfDao.getKlantPaginaSettings(this);
	}

	public void retrieveBehandelingenByGeslacht(String geslacht) {
		behandelingDao.behandelingenByGeslacht(geslacht, this);
	}
	
	public void getOpeningsTijden(Date vandaag) {
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
		this.dagen.add(dag);
	}

	public Date getInschrijfdatum() {
		return inschrijfdatum;
	}

	public InstellingenFacade getInstellingen() {
		return instellingen;
	}

	public void setInstellingen(InstellingenFacade instellingen) {
		this.instellingen = instellingen;
	}

	public void setBehandelingen(ArrayList<Behandeling> behandelingen) {
		this.behandelingen = behandelingen;
	}
	
	public void addBehandeling(Behandeling behandeling) {
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
