package com.swinkels.emperio.providers;


import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;

public interface DagDao {
	public boolean saveDag(Dag dag);
	
	public void getWeekRooster(Bedrijf bedrijf);

}
