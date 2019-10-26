package com.swinkels.emperio.objects;

import java.util.Date;

public class Behandeling {
	private int id;
	private Bedrijf bedrijf;
	private String naam;
	private String beschrijving;
	private Date lengte;
	private double prijs;
	private String geslacht;
	private int count;
	
	public Behandeling(String behandelingsNaam) {
		this.naam = behandelingsNaam;
	}
	
	public Behandeling(int id, String behandelingsNaam, String beschrijving, Date lengte, double prijs) {
		this.id=id;
		this.naam = behandelingsNaam;
		this.beschrijving = beschrijving;
		this.lengte = lengte;
		this.prijs = prijs;
	}

	public Behandeling(int afspraakBehandeling) {
		this.id = afspraakBehandeling;
	}

	public Behandeling(String naam, Date lengte, double prijs) {
		this.naam = naam;
		this.lengte = lengte;
		this.prijs = prijs;
	}
	
	public Behandeling(Bedrijf bedrijf, String naam, String beschrijving, double prijs, Date lengte,
			String geslacht) {
		this.bedrijf = bedrijf;
		this.naam = naam;
		this.beschrijving=beschrijving;
		this.prijs = prijs;
		this.lengte = lengte;
		this.geslacht = geslacht;
	}

	
	public Behandeling(int id, String naam, int count) {
		this.id = id;
		this.naam = naam;
		this.count = count;
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
	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
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

	public Date getLengte() {
		return lengte;
	}

	public void setLengte(Date lengte) {
		this.lengte = lengte;
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
	
	public String getGeslacht() {
		return geslacht;
	}
}
