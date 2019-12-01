package com.swinkels.emperio.objects;

import com.swinkels.emperio.providers.ContactPersoonDao;
import com.swinkels.emperio.providers.ContactPersoonDaoImpl;

public class ContactPersoon extends Bedrijf{
	private Bedrijf bedrijf;
	private String voornaam;
	private String achternaam;
	private String rekeningnummer;
	private String telefoonnummer;
	private String emailadres;
	
	public ContactPersoon(Bedrijf bedrijf, String voornaam, String achternaam, String rekeningnummer, String telefoonnummer,
			String emailadres) {
		super();
		this.bedrijf = bedrijf;
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.rekeningnummer = rekeningnummer;
		this.telefoonnummer = telefoonnummer;
		this.emailadres = emailadres;
	}
	
	public boolean save() {
		ContactPersoonDao contactpersoonDao = new ContactPersoonDaoImpl();
		if(contactpersoonDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public String getRekeningnummer() {
		return rekeningnummer;
	}

	public void setRekeningnummer(String rekeningnummer) {
		this.rekeningnummer = rekeningnummer;
	}

	public String getTelefoonnummer() {
		return telefoonnummer;
	}

	public void setTelefoonnummer(String telefoonnummer) {
		this.telefoonnummer = telefoonnummer;
	}

	public String getEmailadres() {
		return emailadres;
	}

	public void setEmailadres(String emailadres) {
		this.emailadres = emailadres;
	}


	
	
}
