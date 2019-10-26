package com.swinkels.emperio.providers;


import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;

public interface AfspraakDao {
	
	public ArrayList<Afspraak> getAfsprakenBetweenDates(Date beginDate, Date eindDate, Bedrijf bedrijf);

	public boolean setAfspraak(Afspraak afspraak);

	public Afspraak getAfspraakId(Afspraak afspraak);
	
	public Afspraak getAfspraak(Bedrijf bedrijf, int id);
	
	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date date);

	public ArrayList<Double> getInkomsten(Bedrijf bedrijf, Date vanDate);

	public ArrayList<Dag> getAantalAfsprakenPerDag(Bedrijf bedrijf, Date endDate);
}
