package com.swinkels.emperio.providers.Email;

import java.util.ArrayList;

import com.swinkels.emperio.objects.email.Email;
import com.swinkels.emperio.objects.security.Bedrijf;

public interface EmailDao {

	public void save(Email email);

	public void getEmail(Email email);

	public ArrayList<Email> getEmails(Bedrijf bedrijf, int hoeveelheid);
}
