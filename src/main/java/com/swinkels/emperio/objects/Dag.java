package com.swinkels.emperio.objects;

import java.util.Date;

import com.swinkels.emperio.service.ServiceFilter;

public class Dag {
	private int id;
	private Bedrijf bedrijf;
	private Date openingsTijd;
	private Date sluitingsTijd;
	private int aantalAfspraken;
	private int dag;
	
	
	public Dag(Date openingsTijd, Date sluitingsTijd) {
		this.openingsTijd = openingsTijd;
		this.sluitingsTijd = sluitingsTijd;
	}
	
	public Dag(Bedrijf bedrijf, int dag, String openingsTijdString, String sluitingsTijdString) {
		this.dag = dag;
		this.bedrijf = bedrijf;
		
		if(openingsTijdString == null || sluitingsTijdString == null) {
			this.openingsTijd = null;
			this.sluitingsTijd = null;
		} else {
			Date openingsTijd = ServiceFilter.StringToDateFormatter(openingsTijdString, "HH:mm");
			Date sluitingsTijd = ServiceFilter.StringToDateFormatter(sluitingsTijdString, "HH:mm");
			
			this.openingsTijd= openingsTijd;
			this.sluitingsTijd = sluitingsTijd;
		}
	}

	public Dag(int dagNummer, Date openingsTijdDate, Date sluitingsTijdDate) {
		this.openingsTijd = openingsTijdDate;
		this.sluitingsTijd = sluitingsTijdDate;
		this.dag = dagNummer; 
	}
	
	public Dag(int dagNummer, int aantalAfspraken) {
		this.dag = dagNummer;
		this.aantalAfspraken = aantalAfspraken;
	}

	public int getAantalAfspraken() {
		return aantalAfspraken;
	}

	public void setAantalAfspraken(int aantalAfspraken) {
		this.aantalAfspraken = aantalAfspraken;
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
	public boolean validateTijden() {
		if(openingsTijd == null || sluitingsTijd == null) {
			return false;
		} else {
			if(openingsTijd.after(sluitingsTijd)) {
				System.out.println(openingsTijd +" fout "+sluitingsTijd);
				return true;
			} else if(openingsTijd.before(sluitingsTijd)) {
				System.out.println(openingsTijd +" goed "+sluitingsTijd);
				return false;
			} else {
				System.out.println(openingsTijd +" tegelijk "+sluitingsTijd);
				return true;	
			}
		}
	}
}
