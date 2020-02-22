package com.swinkels.emperio.providers.Klant;

import java.util.ArrayList;
import java.util.List;

import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantInterface;
import com.swinkels.emperio.objects.klant.KlantStatestieken;
import com.swinkels.emperio.objects.security.Bedrijf;

public interface KlantDao {

	public List<KlantInterface> getKlanten(int lowLimit, int highLimit, String sort, String klantRequest);

	public Klant getKlantById(int id);

	public Klant getKlantId(Klant klant);

	public boolean save(Klant klant);

	public boolean getKlantIdByEmail(Klant klant);

	public boolean getKlantIdByPhone(Klant klant);

	public void zoekKlant(Bedrijf bedrijf, String request);

}
