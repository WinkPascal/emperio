package com.swinkels.emperio.objects;

import com.swinkels.emperio.providers.InstellingenDao;
import com.swinkels.emperio.providers.InstellingenDaoImpl;

public class Instellingen{
	Bedrijf bedrijf;
	
	private String kleurKlasse1;
	private double maximumPrijsVanKlasse1;
	private String kleurKlasse2;
	private double maximumPrijsVanKlasse2;
	private String kleurKlasse3;
	
	private boolean emailKlantInvoer;
	private boolean telefoonKlantInvoer;
	private boolean adresKlantInvoer;
	
	private boolean bedrijfsEmail;
	private boolean bedrijfsTelefoon;
	private boolean bedrijfsAdres;
	
	public Instellingen(Bedrijf bedrijf,boolean emailKlantInvoer, boolean telefoonKlantInvoer, boolean adresKlantInvoer, 
			String kleurKlasse1, double maximumPrijsVanKlasse1, 
			String kleurKlasse2, double maximumPrijsVanKlasse2, 
			String kleurKlasse3, boolean bedrijfsEmail, boolean bedrijfsTelefoon, boolean bedrijfsAdres) {
		this.bedrijf = bedrijf;
		this.emailKlantInvoer = emailKlantInvoer;
		this.telefoonKlantInvoer = telefoonKlantInvoer;
		this.adresKlantInvoer = adresKlantInvoer;
		this.kleurKlasse1 = kleurKlasse1;
		this.maximumPrijsVanKlasse1 = maximumPrijsVanKlasse1;
		this.kleurKlasse2 = kleurKlasse2;
		this.maximumPrijsVanKlasse2 = maximumPrijsVanKlasse2;
		this.kleurKlasse3 = kleurKlasse3;
		this.bedrijfsEmail = bedrijfsEmail;
		this.bedrijfsTelefoon = bedrijfsTelefoon;
		this.bedrijfsAdres = bedrijfsAdres;
	}


	public boolean isBedrijfsEmail() {
		return bedrijfsEmail;
	}


	public void setBedrijfsEmail(boolean bedrijfsEmail) {
		this.bedrijfsEmail = bedrijfsEmail;
	}


	public boolean isBedrijfsTelefoon() {
		return bedrijfsTelefoon;
	}


	public void setBedrijfsTelefoon(boolean bedrijfsTelefoon) {
		this.bedrijfsTelefoon = bedrijfsTelefoon;
	}


	public boolean isBedrijfsAdres() {
		return bedrijfsAdres;
	}


	public void setBedrijfsAdres(boolean bedrijfsAdres) {
		this.bedrijfsAdres = bedrijfsAdres;
	}


	public Instellingen(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}


	public boolean isEmailKlantInvoer() {
		return emailKlantInvoer;
	}

	public void setEmailKlantInvoer(boolean emailKlantInvoer) {
		this.emailKlantInvoer = emailKlantInvoer;
	}

	public boolean isTelefoonKlantInvoer() {
		return telefoonKlantInvoer;
	}

	public void setTelefoonKlantInvoer(boolean telefoonKlantInvoer) {
		this.telefoonKlantInvoer = telefoonKlantInvoer;
	}

	public boolean isAdresKlantInvoer() {
		return adresKlantInvoer;
	}

	public void setAdresKlantInvoer(boolean adresKlantInvoer) {
		this.adresKlantInvoer = adresKlantInvoer;
	}

	public String getKleurKlasse1() {
		return kleurKlasse1;
	}

	public void setKleurKlasse1(String kleurKlasse1) {
		this.kleurKlasse1 = kleurKlasse1;
	}

	public double getMaximumPrijsVanKlasse1() {
		return maximumPrijsVanKlasse1;
	}

	public void setMaximumPrijsVanKlasse1(double maximumPrijsVanKlasse1) {
		this.maximumPrijsVanKlasse1 = maximumPrijsVanKlasse1;
	}

	public String getKleurKlasse2() {
		return kleurKlasse2;
	}

	public void setKleurKlasse2(String kleurKlasse2) {
		this.kleurKlasse2 = kleurKlasse2;
	}

	public double getMaximumPrijsVanKlasse2() {
		return maximumPrijsVanKlasse2;
	}

	public void setMaximumPrijsVanKlasse2(double maximumPrijsVanKlasse2) {
		this.maximumPrijsVanKlasse2 = maximumPrijsVanKlasse2;
	}

	public String getKleurKlasse3() {
		return kleurKlasse3;
	}

	public void setKleurKlasse3(String kleurKlasse3) {
		this.kleurKlasse3 = kleurKlasse3;
	}
	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}
	public Bedrijf getBedrijf() {
		return this.bedrijf;
	}

	public boolean save() {
		InstellingenDao instellingen = new InstellingenDaoImpl();
		if(instellingen.save(this)) {
			return true;
		} else {
			return false;	
		}
	}


	public void update() {
		InstellingenDao instellingen = new InstellingenDaoImpl();
		instellingen.update(this);
	}
	
	public void retrieveInstellingen() {
		InstellingenDao instellingenDao = new InstellingenDaoImpl();
		instellingenDao.getInstellingen(this);
	}




	
}
