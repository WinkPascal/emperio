package com.swinkels.emperio.objects;

import com.swinkels.emperio.providers.BedrijfsInstellingenDao;
import com.swinkels.emperio.providers.BedrijfsInstellingenDaoImpl;

public class BedrijfsInstellingen{
	Bedrijf bedrijf;
	private String telefoonBedrijf;
	private String emailBedrijf;
	private String adresBedrijf;
	
	private boolean emailKlantInvoer;
	private boolean telefoonKlantInvoer;
	private boolean adresKlantInvoer;
	
	private String kleurKlasse1;
	private double minimumPrijsVanKlasse1;
	private double maximumPrijsVanKlasse1;
	private String kleurKlasse2;
	private double maximumPrijsVanKlasse2;
	private String kleurKlasse3;
	
	public BedrijfsInstellingen(String telefoonBedrijf, String emailBedrijf, String adresBedrijf,
			boolean emailKlantInvoer, boolean telefoonKlantInvoer, boolean adresKlantInvoer, String kleurKlasse1,
			double minimumPrijsVanKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2,
			double maximumPrijsVanKlasse2, String kleurKlasse3) {
		this.telefoonBedrijf = telefoonBedrijf;
		this.emailBedrijf = emailBedrijf;
		this.adresBedrijf = adresBedrijf;
		this.emailKlantInvoer = emailKlantInvoer;
		this.telefoonKlantInvoer = telefoonKlantInvoer;
		this.adresKlantInvoer = adresKlantInvoer;
		this.kleurKlasse1 = kleurKlasse1;
		this.minimumPrijsVanKlasse1 = minimumPrijsVanKlasse1;
		this.maximumPrijsVanKlasse1 = maximumPrijsVanKlasse1;
		this.kleurKlasse2 = kleurKlasse2;
		this.maximumPrijsVanKlasse2 = maximumPrijsVanKlasse2;
		this.kleurKlasse3 = kleurKlasse3;
	}
	
	public String getTelefoonBedrijf() {
		return telefoonBedrijf;
	}

	public void setTelefoonBedrijf(String telefoonBedrijf) {
		this.telefoonBedrijf = telefoonBedrijf;
	}

	public String getEmailBedrijf() {
		return emailBedrijf;
	}

	public void setEmailBedrijf(String emailBedrijf) {
		this.emailBedrijf = emailBedrijf;
	}

	public String getAdresBedrijf() {
		return adresBedrijf;
	}

	public void setAdresBedrijf(String adresBedrijf) {
		this.adresBedrijf = adresBedrijf;
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

	public double getMinimumPrijsVanKlasse1() {
		return minimumPrijsVanKlasse1;
	}

	public void setMinimumPrijsVanKlasse1(double minimumPrijsVanKlasse1) {
		this.minimumPrijsVanKlasse1 = minimumPrijsVanKlasse1;
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

	public boolean saveBedrijfInstellingen() {
		BedrijfsInstellingenDao bedrijfsIntellingenDao = new BedrijfsInstellingenDaoImpl();
		if(bedrijfsIntellingenDao.save(this)) {
			return true;
		} else {
			return false;	
		}		
	}


	
}
