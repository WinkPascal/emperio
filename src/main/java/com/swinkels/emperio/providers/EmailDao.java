package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Email;

public interface EmailDao {

	public void save(Email email);

	public void getEmail(Email email);

	public ArrayList<Email> getEmails(Bedrijf bedrijf, int hoeveelheid);
}
