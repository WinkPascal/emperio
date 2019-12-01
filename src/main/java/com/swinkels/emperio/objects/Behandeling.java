package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.providers.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;

public class Behandeling {
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

	public ArrayList<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();
		return errors;
	}

	//
	// builder
	//
	public Behandeling(Bedrijf bedrijf, int id, String naam, double prijs, String beschrijving, Date lengte,
			String geslacht) {
		super();
		this.bedrijf = bedrijf;
		this.id = id;
		this.naam = naam;
		this.prijs = prijs;
		this.beschrijving = beschrijving;
		this.lengte = lengte;
		this.geslacht = geslacht;
	}
	//
	//
	//

	public Behandeling(String behandelingsNaam) {
		this.naam = behandelingsNaam;
	}

	public Behandeling(int id, String behandelingsNaam, String beschrijving, Date lengte, double prijs,
			String geslacht) {
		this.id = id;
		this.naam = behandelingsNaam;
		this.beschrijving = beschrijving;
		this.lengte = lengte;
		this.prijs = prijs;
		this.geslacht = geslacht;
	}

	public Behandeling(String naam, Date lengte, double prijs) {
		this.naam = naam;
		this.lengte = lengte;
		this.prijs = prijs;
	}

	public Behandeling(int id, String naam, Date lengte) {
		this.naam = naam;
		this.lengte = lengte;
		this.id = id;
	}

	public Behandeling(Bedrijf bedrijf, String naam, String beschrijving, double prijs, Date lengte, String geslacht) {
		this.bedrijf = bedrijf;
		this.naam = naam;
		this.beschrijving = beschrijving;
		this.prijs = prijs;
		this.lengte = lengte;
		this.geslacht = geslacht;
	}

	public Behandeling(int id, String naam, int count) {
		this.id = id;
		this.naam = naam;
		this.count = count;
	}

	public int getAfspraken() {
		return afspraken;
	}

	public void setAfspraken(int afspraken) {
		this.afspraken = afspraken;
	}

	public Double getInkomsten() {
		return inkomsten;
	}

	public void setInkomsten(Double inkomsten) {
		this.inkomsten = inkomsten;
	}

	public void setGeslacht(String geslacht) {
		this.geslacht = geslacht;
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

	public boolean save() {
		BehandelingDao behandelingDao = new BehandelingDaoImpl();
		if (behandelingDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}

	public void getInfo() {
		BehandelingDao behandelingDao = new BehandelingDaoImpl();
		behandelingDao.getBehandeling(this);
	}

	public void saveAfspraakBehandeling(Afspraak afspraak) {
		AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
		afspraakBehandelingDao.saveAfspraakBehandeling(this, afspraak);
	}

	public boolean delete() {
		BehandelingDao behandelingDao = new BehandelingDaoImpl();
		if (behandelingDao.delete(this)) {
			return true;
		} else {
			return false;
		}
	}

}
