package com.swinkels.emperio.objects.security;

import com.swinkels.emperio.providers.ContactPersoon.ContactPersoonDao;
import com.swinkels.emperio.providers.ContactPersoon.ContactPersoonDaoImpl;

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
	
}
