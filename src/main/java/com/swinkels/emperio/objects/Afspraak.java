package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.providers.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;

public class Afspraak {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();

	private Klant klant;
	private Bedrijf bedrijf;
	private ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

	private int id;
	private Date timestamp;
	private Double prijs;

	public Afspraak(Klant klant, Bedrijf bedrijf, ArrayList<Behandeling> behandelingen, int id, Date timestamp,
			Double prijs) {
		this.klant = klant;
		this.bedrijf = bedrijf;
		this.behandelingen = behandelingen;
		this.id = id;
		this.timestamp = timestamp;
		this.prijs = prijs;
	}

	public ArrayList<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();
		if (timestamp.before(new Date())) {
			errors.add("Een afspraak kan niet in het verleden worden gepland.");
		}
		return errors;
	}

	public void save() {
		afspraakDao.setAfspraak(this);
		afspraakDao.getAfspraakId(this);
		for (Behandeling behandeling : behandelingen) {
			behandeling.setBedrijf(bedrijf);
			behandeling.getInfo();
			afspraakBehandelingDao.saveAfspraakBehandeling(behandeling, this);
		}
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
		if (afspraakDao.deleteAfspraak(this)) {
			return true;
		} else {
			return false;
		}
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


}
