package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

public class AfspraakBuilder {
	private Bedrijf bedrijf;
	private Klant klant;
	private ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

	private int id;
	private Date timestamp;
	private Double prijs;
	
	public AfspraakBuilder() {
		
	}
	
	public AfspraakBuilder setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
		return this;
	}
	public AfspraakBuilder setKlant(Klant klant) {
		this.klant = klant;
		return this;
	}
	public AfspraakBuilder setBehandelingen(ArrayList<Behandeling> behandelingen) {
		this.behandelingen = behandelingen;
		return this;
	}
	public AfspraakBuilder setId(int id) {
		this.id = id;
		return this;
	}
	public AfspraakBuilder setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	public AfspraakBuilder setPrijs(Double prijs) {
		this.prijs = prijs;
		return this;
	}
	
	public Afspraak makeAfspraak() {
		Afspraak afspraak = new Afspraak(bedrijf, klant, behandelingen, id, timestamp,
				prijs);
		
		return afspraak;
	}
}
