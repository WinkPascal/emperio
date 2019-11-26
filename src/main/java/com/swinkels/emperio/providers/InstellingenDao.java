package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Instellingen;

public interface InstellingenDao {
	public boolean save(Instellingen bedrijf);

	public void getInstellingen(Instellingen instellingen);

	public boolean update(Instellingen instellingen);
}
