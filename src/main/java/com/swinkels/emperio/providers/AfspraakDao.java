package com.swinkels.emperio.providers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;

public interface AfspraakDao {
	
	public ArrayList<Afspraak> getAfsprakenVandaag(Date date, Bedrijf bedrijf) throws ParseException;

	public boolean setAfspraak(Afspraak afspraak);

	public Afspraak getAfspraakId(Afspraak afspraak);

	public ArrayList<Afspraak> getOpenPlekken(Date date, String behandelingen);
}
