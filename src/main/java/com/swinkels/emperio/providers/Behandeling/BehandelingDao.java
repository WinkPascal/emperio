package com.swinkels.emperio.providers.Behandeling;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.security.Bedrijf;

public interface BehandelingDao {
	// inplannen
	public void behandelingenByGeslacht(String geslacht, Bedrijf bedrijf);

	// statestieken
	public void getTop5Behandelingen(Bedrijf bedrijf, Date date);

	// bahendelingenlijst
	public ArrayList<Behandeling> getBehandelingen(Bedrijf bedrijf, int pageNummer, String geslacht, String sort,
			String zoek);

	public void getBehandeling(Behandeling behandeling);

	public boolean save(Behandeling behandeling);

	public boolean delete(Behandeling behandeling);

	public boolean update(Behandeling behandeling);
}
