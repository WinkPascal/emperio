package com.swinkels.emperio.providers.AfspraakBehandeling;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.rooster.Afspraak;

import java.util.List;

public interface AfspraakBehandelingDao {
	public void saveAfspraakBehandeling(Behandeling behandeling, Afspraak afspraak);

    public List<Behandeling> getAfspraakInfo(int id);
}
