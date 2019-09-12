package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Klant;

public interface KlantDao {
	
	public ArrayList<Klant> zoekKlant(String bedrijf, String klantRequest);

	public ArrayList<Klant> getKlanten(String bedrijf, int pageNummer);
}
