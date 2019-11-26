package com.swinkels.emperio.providers;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;

public interface BehandelingDao {
	public void behandelingenByGeslacht(String geslacht, Bedrijf bedrijf);

	public ArrayList<Behandeling> getTop5Behandelingen(Bedrijf bedrijf, Date date);

	public ArrayList<Behandeling> getBehandelingen(String bedrijf, int pageNummer, String geslacht, String sort);

	public boolean save(Behandeling behandeling);

	public ArrayList<Behandeling> getAllBehandelingen(Bedrijf bedrijf);
}
