package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;

public class Afspraak {

	private int id;
	private Klant klant;
	private Bedrijf bedrijf;
	private Date timestamp;
	private Double prijs;
	private ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

	public Afspraak() {

	}

	public Afspraak(int id, Date timestamp, Klant klant) {
		this.id = id;
		this.timestamp = timestamp;
		this.klant = klant;
	}

	public Afspraak(Date afspraakTimestamp, Bedrijf bedrijf, Klant klant) {
		this.timestamp = afspraakTimestamp;
		this.bedrijf = bedrijf;
		this.klant = klant;
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

	public ArrayList<Behandeling> getBehandelingen() {
		return behandelingen;
	}

	public void setBehandelingen(ArrayList<Behandeling> behandelingen) {
		this.behandelingen = behandelingen;
	}

	
	public void setPrijs(double prijs) {
		this.prijs = prijs;
	}

	public Double getPrijs() {
		return prijs;
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
	
	public void retrieveBehandelingen() {
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		behandelingen = afspraakDao.getBehandelingen(this);
	}
	public void retrieveKlant() {
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		klant = afspraakDao.getKlant(this);
	}

	public boolean delete() {
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		if(afspraakDao.deleteAfspraak(this)) {
			return true;
		} else {
			return false;
		}
	}
}
