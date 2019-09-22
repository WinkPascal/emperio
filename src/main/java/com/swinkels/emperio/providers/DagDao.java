package com.swinkels.emperio.providers;

import java.text.ParseException;
import java.util.Date;

import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Bedrijf;

public interface DagDao {
	public Dag getTijdenFromDate(Date datum, Bedrijf bedrijf) throws ParseException;
}
