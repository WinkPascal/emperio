package com.swinkels.emperio.objects;

import com.swinkels.emperio.providers.InstellingenDao;
import com.swinkels.emperio.providers.InstellingenDaoImpl;

public class Instellingen extends Bedrijf {
	InstellingenDao instellingenDao = new InstellingenDaoImpl();

	private String kleurKlasse1;
	private double maximumPrijsVanKlasse1;
	private String kleurKlasse2;
	private double maximumPrijsVanKlasse2;
	private String kleurKlasse3;

	private String bedrijfsEmailString;
	private String bedrijfsTelefoonString;
	private String bedrijfsAdresString;

	private boolean emailKlantInvoer;
	private boolean telefoonKlantInvoer;
	private boolean adresKlantInvoer;

	public Instellingen(String bedrijf) {
		super(bedrijf);
	}
	
	public Instellingen(String bedrijf, boolean emailKlantInvoer, boolean telefoonKlantInvoer,
			boolean adresKlantInvoer, String kleurKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2,
			double maximumPrijsVanKlasse2, String kleurKlasse3, String bedrijfsEmail, String bedrijfsTelefoon,
			String bedrijfsAdres) {
		super(bedrijf);

		this.emailKlantInvoer = emailKlantInvoer;
		this.telefoonKlantInvoer = telefoonKlantInvoer;
		this.adresKlantInvoer = adresKlantInvoer;

		this.kleurKlasse1 = kleurKlasse1;
		this.maximumPrijsVanKlasse1 = maximumPrijsVanKlasse1;
		this.kleurKlasse2 = kleurKlasse2;
		this.maximumPrijsVanKlasse2 = maximumPrijsVanKlasse2;
		this.kleurKlasse3 = kleurKlasse3;

		this.bedrijfsEmailString = bedrijfsEmail;
		this.bedrijfsTelefoonString = bedrijfsTelefoon;
		this.bedrijfsAdresString = bedrijfsAdres;
	}

	public Instellingen(String bedrijf, String kleurKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2,
			double maximumPrijsVanKlasse2, String kleurKlasse3) {
		super(bedrijf);
		this.kleurKlasse1 = kleurKlasse1;
		this.maximumPrijsVanKlasse1 = maximumPrijsVanKlasse1;
		this.kleurKlasse2 = kleurKlasse2;
		this.maximumPrijsVanKlasse2 = maximumPrijsVanKlasse2;
		this.kleurKlasse3 = kleurKlasse3;
	}

	public Instellingen(String bedrijf2, boolean telefoonKlantInvoer, boolean emailKlantInvoer, boolean adresKlantInvoer,
			String bedrijfsEmail2, String bedrijfsTelefoon2, String bedrijfsAdres2) {
		super(bedrijf2);
		this.telefoonKlantInvoer = telefoonKlantInvoer;
		this.emailKlantInvoer = emailKlantInvoer;
		this.adresKlantInvoer = adresKlantInvoer;

		this.bedrijfsEmailString = bedrijfsEmail2;
		this.bedrijfsTelefoonString = bedrijfsTelefoon2;
		this.bedrijfsAdresString = bedrijfsAdres2;
	}

	public boolean save() {
		if (instellingenDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}

	public void getInplanSettings() {
		instellingenDao.getInplanSettings(this);
	}
	
	public void updateInplanSettings() {
		instellingenDao.updateInplanSettings(this);
	}

	public void updateColors() {
		instellingenDao.update(this);
	}

	public void retrieveInstellingen() {
		instellingenDao.getInstellingen(this);
	}

	public double getMaximumPrijsVanKlasse1() {
		return maximumPrijsVanKlasse1;
	}

	public String getBedrijfsEmailString() {
		return bedrijfsEmailString;
	}

	public void setBedrijfsEmailString(String bedrijfsEmailString) {
		this.bedrijfsEmailString = bedrijfsEmailString;
	}

	public String getBedrijfsTelefoonString() {
		return bedrijfsTelefoonString;
	}

	public void setBedrijfsTelefoonString(String bedrijfsTelefoonString) {
		this.bedrijfsTelefoonString = bedrijfsTelefoonString;
	}

	public String getBedrijfsAdresString() {
		return bedrijfsAdresString;
	}

	public void setBedrijfsAdresString(String bedrijfsAdresString) {
		this.bedrijfsAdresString = bedrijfsAdresString;
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



}
