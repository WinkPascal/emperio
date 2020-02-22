package com.swinkels.emperio.objects.email;

import java.util.Date;
import java.util.HashMap;

public class EmailDetails extends Email {
	private int aantalKlanten;
	private Date verzendtijd;

	public EmailDetails(int id, String onderwerp, String inhoud, int aantalKlanten, Date verzendTijd) {
		super(id, onderwerp, inhoud);
		
	}

	public HashMap<String, String> toDTO() {
		HashMap<String, String> dto = new HashMap<>();
		
		return dto;
	}
}
