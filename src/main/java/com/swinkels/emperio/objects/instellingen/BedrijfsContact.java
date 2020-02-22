package com.swinkels.emperio.objects.instellingen;

import java.util.HashMap;
import java.util.Map;

public class BedrijfsContact {
	private String bedrijfsEmailString;
	private String bedrijfsTelefoonString;
	private String bedrijfsAdresString;

	protected void save(){

	}

	protected BedrijfsContact(String bedrijfsEmailString, String bedrijfsTelefoonString, String bedrijfsAdresString) {
		this.bedrijfsEmailString = bedrijfsEmailString;
		this.bedrijfsTelefoonString = bedrijfsTelefoonString;
		this.bedrijfsAdresString = bedrijfsAdresString;
	}

	protected HashMap<String, String> toDto() {
		HashMap<String, String> dto = new HashMap<String, String>();
		if(bedrijfsEmailString != null) {
			dto.put("bedrijfsEmail", bedrijfsEmailString);
		} else {
			dto.put("bedrijfsEmail", "");
		}
		if(bedrijfsTelefoonString != null) {
			dto.put("bedrijfsTelefoon", bedrijfsTelefoonString);
		} else {
			dto.put("bedrijfsTelefoon", "");
		}
		if(bedrijfsAdresString != null) {
			dto.put("bedrijfsAdres", bedrijfsAdresString);
		} else {
			dto.put("bedrijfsAdres", "");
		}

		return dto;
	}
}
