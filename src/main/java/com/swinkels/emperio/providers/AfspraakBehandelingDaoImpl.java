package com.swinkels.emperio.providers;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;

public class AfspraakBehandelingDaoImpl extends MariadbBaseDao implements AfspraakBehandelingDao{

	public boolean saveAfspraakBehandeling(Behandeling behandeling, Afspraak afspraak) {
		return false;
	}
	
}
