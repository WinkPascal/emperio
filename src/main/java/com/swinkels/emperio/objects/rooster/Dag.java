package com.swinkels.emperio.objects.rooster;

import java.util.ArrayList; 
import java.util.Date;

import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.providers.Afspraak.AfspraakDao;
import com.swinkels.emperio.providers.Afspraak.AfspraakDaoImpl;
import com.swinkels.emperio.support.JavascriptDateAdapter;

public class Dag {
	private ArrayList<Afspraak> afspraken;
	private Bedrijf bedrijf;
	private int id;
	private int dag;
	private Date openingsTijd;
	private Date sluitingsTijd;
	
	
	
	public Dag(ArrayList<Afspraak> afspraken, int id, int dagNummer, Date openingsTijd,
			Date sluitingsTijd) {
		this.afspraken = afspraken;
		this.id = id;
		this.dag = dagNummer;
		this.openingsTijd = openingsTijd;
		this.sluitingsTijd = sluitingsTijd;
	}

	public Dag(int id, String openingsTijd,
			   String sluitingsTijd){
		this.id = id;
		this.openingsTijd =JavascriptDateAdapter.StringToDate(openingsTijd, "HH:mm");
		this.sluitingsTijd = JavascriptDateAdapter.StringToDate(sluitingsTijd, "HH:mm");;
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
