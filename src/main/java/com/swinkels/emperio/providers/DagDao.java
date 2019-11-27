package com.swinkels.emperio.providers;


import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;

public interface DagDao {
	public boolean saveDag(Dag dag);
	
	public void getWeekRooster(Bedrijf bedrijf);

	public void getOpeningsTijden(Bedrijf bedrijf, Date vandaag);

}
