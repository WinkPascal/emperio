package com.swinkels.emperio.objects;

public class Afspraak {

	int id;
	Klant klant;
	Bedrijf bedrijf;
	Behandeling behandeling;
	String tijd;
	String lengte;

	public Afspraak(int id, String tijd, String lengte, Klant klant, Behandeling behandeling) {
		this.id = id;
		this.tijd = tijd;
		this.lengte = lengte;
		this.klant = klant;
		this.behandeling = behandeling;
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

	public Behandeling getBehandeling() {
		return behandeling;
	}

	public void setBehandeling(Behandeling behandeling) {
		this.behandeling = behandeling;
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
