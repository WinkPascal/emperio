package com.swinkels.emperio.objects;

public class Afspraak {

	int id;
	Klant klant;
	Bedrijf bedrijf;
	String tijd;
	String lengte;

	public Afspraak(int id, String tijd, String lengte, Klant klant) {
		this.id = id;
		this.tijd = tijd;
		this.lengte = lengte;
		this.klant = klant;
	}

	public Afspraak(String afspraakTijd, String afspraakDatum, Bedrijf bedrijf,
			Klant klant) {
		this.lengte= afspraakTijd;
		this.tijd= afspraakDatum;
		this.bedrijf= bedrijf;
		this.klant= klant;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Klant getKlant() {
		return klant;
	}

	public void setKlant(Klant klant) {
		this.klant = klant;
	}

	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public String getTijd() {
		return tijd;
	}

	public void setTijd(String tijd) {
		this.tijd = tijd;
	}

	public String getLengte() {
		return lengte;
	}

	public void setLengte(String lengte) {
		this.lengte = lengte;
	}

}
