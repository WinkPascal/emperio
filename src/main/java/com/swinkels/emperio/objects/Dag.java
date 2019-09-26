package com.swinkels.emperio.objects;

import java.util.Date;

public class Dag {
	int id;
	Bedrijf bedrijf;
	Date openingsTijd;
	Date sluitingsTijd;
	int dag;
	
	public Dag(Date openingsTijd, Date sluitingsTijd) {
		this.openingsTijd = openingsTijd;
		this.sluitingsTijd = sluitingsTijd;
	}

	public Dag(int dagNummer, Date openingsTijdDate, Date sluitingsTijdDate) {
		this.openingsTijd = openingsTijdDate;
		this.sluitingsTijd = sluitingsTijdDate;
		this.dag = dagNummer; 
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

	public Date getOpeningsTijd() {
		return openingsTijd;
	}

	public void setOpeningsTijd(Date openingsTijd) {
		this.openingsTijd = openingsTijd;
	}

	public Date getSluitingsTijd() {
		return sluitingsTijd;
	}

	public void setSluitingsTijd(Date sluitingsTijd) {
		this.sluitingsTijd = sluitingsTijd;
	}

	public int getDag() {
		return dag;
	}

	public void setDag(int dag) {
		this.dag = dag;
	}
}
