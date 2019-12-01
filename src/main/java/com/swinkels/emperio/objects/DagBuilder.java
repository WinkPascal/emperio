package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

public class DagBuilder{
	private Bedrijf bedrijf;
	private ArrayList<Afspraak> afspraken;
	
	private int id;
	private int dagNummer;
	private Date openingsTijd;
	private Date sluitingsTijd;


	public DagBuilder() {
		
	}
	
	public DagBuilder setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
		return this;
	}
	public DagBuilder setAfspraken(ArrayList<Afspraak> afspraken) {
		this.afspraken = afspraken;
		return this;
	}
	public DagBuilder setId(int id) {
		this.id = id;
		return this;
	}
	public DagBuilder setDag(int dag) {
		this.dagNummer = dag;
		return this;
	}
	public DagBuilder setOpeningsTijd(Date openingsTijd) {
		this.openingsTijd = openingsTijd;
		return this;
	}
	public DagBuilder setSluitingsTijd(Date sluitingsTijd) {
		this.sluitingsTijd = sluitingsTijd;
		return this;
	}
	
	public Dag make() {
		Dag dag = new Dag(bedrijf, afspraken, id, dagNummer, openingsTijd, sluitingsTijd);
		return dag;
	}
}
