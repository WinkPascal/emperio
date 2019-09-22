package com.swinkels.emperio.providers;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;

public interface BedrijfDao {
	public ArrayList<Date> getDagTijden(Bedrijf bedrijf, Date date);

}
