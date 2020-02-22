package com.swinkels.emperio.providers.Afspraak;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.AfspraakInterface;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.statestieken.Statestieken;

public interface AfspraakDao {
	public List<Afspraak> getAfsprakenFromKlantId(int klantId);

	public List<Afspraak> getAfsprakenBetweenDates(Date beginDate, Date eindDate);

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

	public HashMap<String, String> getGeslachtenVanAfspraken(Statestieken bedrijf, Date date);

	public List<Double> getPrijzenVanAfspraak(Statestieken statestieken, Date date);

	public List<Date> getLengtesVanAfspraak(Statestieken statestieken, Date date);

	public List<Afspraak> getInkomstenForStatistics(Statestieken statestieken, Date date);

}
