package com.swinkels.emperio.objects;

public class Behandeling {
	int id;
	Bedrijf bedrijf;
	String naam;
	String beschrijving;
	String tijd;
	double prijs;
	
	public Behandeling(String behandelingsNaam) {
		this.naam = behandelingsNaam;
	}
	
	public Behandeling(int id, String behandelingsNaam, String beschrijving, String lengte, double prijs) {
		this.id=id;
		this.naam = behandelingsNaam;
		this.beschrijving = beschrijving;
		this.tijd = lengte;
		this.prijs = prijs;
	}

	public Behandeling(int afspraakBehandeling) {
		this.id = afspraakBehandeling;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public String getBeschrijving() {
		return beschrijving;
	}

	public void setBeschrijving(String beschrijving) {
		this.beschrijving = beschrijving;
	}

	public String getTijd() {
		return tijd;
	}

	public void setTijd(String tijd) {
		this.tijd = tijd;
	}

	public double getPrijs() {
		return prijs;
	}

	public void setPrijs(double prijs) {
		this.prijs = prijs;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getNaam() {
		return naam;
	}
}
