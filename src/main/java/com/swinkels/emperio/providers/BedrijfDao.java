package com.swinkels.emperio.providers;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;

public interface BedrijfDao {
	public ArrayList<Date> getDagTijden(Bedrijf bedrijf, Date date);

	public ArrayList<Dag> getWeekRooster(Bedrijf bedrijf);
	
}
