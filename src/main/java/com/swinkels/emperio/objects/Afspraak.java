package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

public class Afspraak {

	int id;
	Klant klant;
	Bedrijf bedrijf;
	Date timestamp;
	ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

	public Afspraak(int id, Date timestamp, Klant klant) {
		this.id = id;
		this.timestamp = timestamp;
		this.klant = klant;
	}

	public Afspraak(Date afspraakTimestamp, Bedrijf bedrijf, Klant klant) {
		this.timestamp= afspraakTimestamp;
		this.bedrijf= bedrijf;
		this.klant= klant;
	}
	
	public Afspraak(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}
	
	public void addBehandeling(Behandeling behandeling) {
		behandelingen.add(behandeling);
	}
	
	public ArrayList<Behandeling> getBehandelingen(){
		return behandelingen;
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

	public Date getTimeStamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
