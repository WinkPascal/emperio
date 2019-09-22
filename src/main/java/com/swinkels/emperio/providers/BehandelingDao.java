package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Behandeling;

public interface BehandelingDao {
	public ArrayList<Behandeling> behandelingenByGeslacht(String geslacht, String bedrijf);
	
	public boolean setBehandeling(Behandeling behandeling);
}
