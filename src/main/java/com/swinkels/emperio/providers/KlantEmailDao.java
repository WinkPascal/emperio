package com.swinkels.emperio.providers;

import com.swinkels.emperio.objects.Email;
import com.swinkels.emperio.objects.Klant;

public interface KlantEmailDao {
	public void save(Email email, Klant klant);
}
