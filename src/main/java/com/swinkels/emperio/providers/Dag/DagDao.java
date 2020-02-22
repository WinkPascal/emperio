package com.swinkels.emperio.providers.Dag;


import java.util.Date;
import java.util.List;

import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;

public interface DagDao {
	public boolean saveDag(Dag dag);
	
	public boolean updateDag(Dag dag);

	public List<Dag> getWeekRooster();

	public void getOpeningsTijden(Bedrijf bedrijf, Date vandaag);
}
