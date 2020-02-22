package com.swinkels.emperio.objects.klant;

import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.AfspraakInterface;

public interface KlantInterface {
	public HashMap<String, String> toDTO();

	public KlantStatestieken getStatestieken();

	public void setStatestieken(KlantStatestieken statestieken);

	public List<Afspraak> getAfspraken();

	public void addAfspraak(Afspraak afspraak);

	public void setAfspraken(List<Afspraak> laatste5AfsprakenFromKlant);
	String getEmail();
}
