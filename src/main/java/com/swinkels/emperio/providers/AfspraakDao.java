package com.swinkels.emperio.providers;


import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;

public interface AfspraakDao {
	
	public ArrayList<Afspraak> getAfsprakenBetweenDates(Date beginDate, Date eindDate, Bedrijf bedrijf);
	public boolean setAfspraak(Afspraak afspraak);

	public Afspraak getAfspraakId(Afspraak afspraak);

	public ArrayList<Afspraak> getOpenPlekken(Date date, String behandelingen);
	
	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date date);
}
