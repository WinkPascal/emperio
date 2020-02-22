package com.swinkels.emperio.objects.behandeling;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.providers.AfspraakBehandeling.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandeling.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.Behandeling.BehandelingDao;
import com.swinkels.emperio.providers.Behandeling.BehandelingDaoImpl;

public class Behandeling {
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();

	
	private Bedrijf bedrijf;

	private int id;
	private String status;
	private String naam;
	private double prijs;
	private String beschrijving;
	private Date lengte;
	private String geslacht;



	public Behandeling(Bedrijf bedrijf, int id, String naam, double prijs, String beschrijving, Date lengte,
			String geslacht, int afspraken, Double inkomsten, int count) {
		this.bedrijf = bedrijf;
		this.id = id;
		this.naam = naam;
		this.prijs = prijs;
		this.beschrijving = beschrijving;
		this.lengte = lengte;
		this.geslacht = geslacht;
	}

	public ArrayList<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();
		return errors;
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
		if (behandelingDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}

	public void getInfo() {
		behandelingDao.getBehandeling(this);
	}

	public void saveAfspraakBehandeling(Afspraak afspraak) {
		afspraakBehandelingDao.saveAfspraakBehandeling(this, afspraak);
	}

	public boolean delete() {
		if (behandelingDao.delete(this)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update() {
		if (behandelingDao.update(this)) {
			return true;
		} else {
			return false;
		}
	}

}
