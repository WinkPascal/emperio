package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Afspraak;

public interface AfspraakDao {
	
	public ArrayList<Afspraak> getAfsprakenVandaag(String date, String bedrijf);

	public boolean setAfspraak(Afspraak afspraak);

	public int getAfspraakId(Afspraak afspraak);
}
