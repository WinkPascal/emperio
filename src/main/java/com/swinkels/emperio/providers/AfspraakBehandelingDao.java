package com.swinkels.emperio.providers;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;

public interface AfspraakBehandelingDao {
	public boolean saveAfspraakBehandeling(Behandeling behandeling, Afspraak afspraak);
}
