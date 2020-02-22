package com.swinkels.emperio.providers.Instellingen;

import com.swinkels.emperio.objects.instellingen.InstellingenFacade;

public interface InstellingenDao {
	public boolean save(InstellingenFacade bedrijf);

	public void getInstellingen(InstellingenFacade instellingen);

	public boolean update(InstellingenFacade instellingen);

	public boolean updateInplanSettings(InstellingenFacade instellingen);

	public void getInplanSettings(InstellingenFacade instellingen);
}
