package com.swinkels.emperio.objects;

import java.util.Date;

public class BehandelingBuilder {
	private Bedrijf bedrijf;

	private int id;
	private String naam;
	private double prijs;
	private String beschrijving;
	private Date lengte;
	private String geslacht;

	private int afspraken;
	private Double inkomsten;
	private int count;

	public BehandelingBuilder() {

	}

	public BehandelingBuilder setAfspraken(int afspraken) {
		this.afspraken = afspraken;
		return this;
	}

	public BehandelingBuilder setInkomsten(Double inkomsten) {
		this.inkomsten = inkomsten;
		return this;
	}

	public BehandelingBuilder setCount(int count) {
		this.count = count;
		return this;
	}

	public BehandelingBuilder setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
		return this;
	}

	public BehandelingBuilder setId(int id) {
		this.id = id;
		return this;
	}

	public BehandelingBuilder setNaam(String naam) {
		this.naam = naam;
		return this;
	}

	public BehandelingBuilder setPrijs(double prijs) {
		this.prijs = prijs;
		return this;
	}

	public BehandelingBuilder setBeschrijving(String beschrijving) {
		this.beschrijving = beschrijving;
		return this;
	}

	public BehandelingBuilder setLengte(Date lengte) {
		this.lengte = lengte;
		return this;
	}

	public BehandelingBuilder setGeslacht(String geslacht) {
		this.geslacht = geslacht;
		return this;
	}

	public Behandeling make() {
		Behandeling behandeling = new Behandeling(bedrijf, id, naam, prijs, beschrijving, lengte,
				geslacht, afspraken, inkomsten, count);
		return behandeling;
	}
}
