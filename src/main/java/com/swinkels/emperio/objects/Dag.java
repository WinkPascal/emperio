package com.swinkels.emperio.objects;

import java.util.ArrayList; 
import java.util.Date;

import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.JavascriptDateAdapter;

public class Dag {
	private Bedrijf bedrijf;
	private ArrayList<Afspraak> afspraken;
	
	private int id;
	private Date openingsTijd;
	private Date sluitingsTijd;
	private int dag;
	
	private int aantalAfspraken;
	private Date datum;
	
	
	public Dag(Bedrijf bedrijf, ArrayList<Afspraak> afspraken, int id, int dagNummer, Date openingsTijd,
			Date sluitingsTijd) {
		super();
		this.bedrijf = bedrijf;
		this.afspraken = afspraken;
		this.id = id;
		this.dag = dagNummer;
		this.openingsTijd = openingsTijd;
		this.sluitingsTijd = sluitingsTijd;
	}

	
	public Dag(Date openingsTijd, Date sluitingsTijd, Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
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
			Date openingsTijd = JavascriptDateAdapter.StringToDate(openingsTijdString, "HH:mm");
			Date sluitingsTijd = JavascriptDateAdapter.StringToDate(sluitingsTijdString, "HH:mm");
			
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

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public ArrayList<Afspraak> getAfspraken() {
		return afspraken;
	}

	public void setAfspraken(ArrayList<Afspraak> afspraken) {
		this.afspraken = afspraken;
	}
	
	public void getAfsprakenBetweenDates(Date vandaag, Date morgen) {
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		afspraakDao.getAfsprakenBetweenDates(this, vandaag, morgen, bedrijf);
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
