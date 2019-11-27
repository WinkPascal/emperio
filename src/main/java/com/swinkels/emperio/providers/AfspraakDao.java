package com.swinkels.emperio.providers;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Klant;

public interface AfspraakDao {

	public void getAfsprakenBetweenDates(Dag dag, Date beginDate, Date eindDate, Bedrijf bedrijf);

	public boolean setAfspraak(Afspraak afspraak);

	public void getAfspraakId(Afspraak afspraak);

	public Afspraak getAfspraak(Bedrijf bedrijf, int id);

	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date date);

	public ArrayList<Double> getInkomsten(Bedrijf bedrijf, Date vanDate);

	public ArrayList<Dag> getAantalAfsprakenPerDag(Bedrijf bedrijf, Date endDate);

	public ArrayList<Double> getAantalAfsprakenEnInkomstenByklant(Bedrijf bedrijf, Klant klant);

	public ArrayList<Afspraak> getLaatste3Afspraken(Bedrijf bedrijf, Klant klant);

	public int getMinutesOfAfspraak(Afspraak afspraak);

	public Klant getKlant(Afspraak afspraak);

	public ArrayList<Behandeling> getBehandelingen(Afspraak afspraak);

	public boolean deleteAfspraak(Afspraak afspraak);
}
