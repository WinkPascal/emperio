package com.swinkels.emperio.providers;

import com.swinkels.emperio.objects.Instellingen;

public interface InstellingenDao {
	public boolean save(Instellingen bedrijf);

	public void getInstellingen(Instellingen instellingen);

	public boolean update(Instellingen instellingen);

	public boolean updateInplanSettings(Instellingen instellingen);

	public void getInplanSettings(Instellingen instellingen);
}
