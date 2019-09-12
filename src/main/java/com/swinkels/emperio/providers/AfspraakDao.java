package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Afspraak;

public interface AfspraakDao {
	
	public ArrayList<Afspraak> getAfsprakenVandaag(String date, String bedrijf);

	public boolean setAfspraak(String afspraakKlantNaam, String afspraakKlantGeslacht, String afspraakKlantEmail, String afspraakKlantTel, int afspraakBehandeling, String afspraakTijd, String afspraakDatum, String bedrijf);
}
