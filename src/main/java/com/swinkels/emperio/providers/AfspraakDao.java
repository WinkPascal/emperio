package com.swinkels.emperio.providers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.objects.Statestieken;

public interface AfspraakDao {

	public void getAfsprakenBetweenDates(Dag dag, Date beginDate, Date eindDate, Bedrijf bedrijf);

	public boolean setAfspraak(Afspraak afspraak);

	public void getAfspraakId(Afspraak afspraak);

	public Afspraak getAfspraak(Bedrijf bedrijf, int id);

	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date date);

	public void getInkomsten(Statestieken bedrijf, Date vanDate);

	public void getAantalAfsprakenPerDag(Bedrijf bedrijf, Date endDate);

	public void getAantalAfsprakenEnInkomstenByklant(Bedrijf bedrijf, Klant klant);

	public void getLaatste3Afspraken(Bedrijf bedrijf, Klant klant);

	public int getMinutesOfAfspraak(Afspraak afspraak);

	public Klant getKlant(Afspraak afspraak);

	public ArrayList<Behandeling> getBehandelingen(Afspraak afspraak);

	public boolean deleteAfspraak(Afspraak afspraak);

	public HashMap<String, String> getGeslachtenVanAfspraken(Statestieken bedrijf,Date date);

	public List<Double> getPrijzenVanAfspraak(Statestieken statestieken, Date date);

	public List<Date> getLengtesVanAfspraak(Statestieken statestieken, Date date);

	public List<Afspraak> getInkomstenForStatistics(Statestieken statestieken, Date date);
	
}
