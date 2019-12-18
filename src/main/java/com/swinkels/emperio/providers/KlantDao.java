package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Klant;

public interface KlantDao {

	public void getKlanten(Bedrijf bedrijf, int page, String sort, String klantRequest);

	public ArrayList<Klant> getKlantenWithLimit(Bedrijf bedrijf, int low, int top);

	public boolean setKlant(Klant klant);

	public Klant getKlantId(Klant klant);

	public boolean getKlantIdByEmail(Klant klant);

	public boolean getKlantIdByPhone(Klant klant);

	public void zoekKlant(Bedrijf bedrijf, String request);

	public void getKlant(Bedrijf bedrijf, Klant klant);
}
