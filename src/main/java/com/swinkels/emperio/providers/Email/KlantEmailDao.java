package com.swinkels.emperio.providers.Email;

import com.swinkels.emperio.objects.email.Email;
import com.swinkels.emperio.objects.klant.Klant;

public interface KlantEmailDao {
	public void save(Email email, Klant klant);
}
