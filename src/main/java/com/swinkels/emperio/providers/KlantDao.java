package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Klant;

public interface KlantDao {
	
	public ArrayList<Klant> zoekKlant(String bedrijf, String klantRequest);

	public ArrayList<Klant> getKlanten(String bedrijf, int pageNummer);
	
	public boolean setKlant(Klant klant);
	
	public Klant getKlantId(Klant klant);

	public Klant getKlant(Bedrijf bedrijf, int id);
}
