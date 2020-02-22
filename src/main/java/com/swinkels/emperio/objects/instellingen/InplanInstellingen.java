package com.swinkels.emperio.objects.instellingen;

import java.util.HashMap;

public class InplanInstellingen {
	private boolean emailKlantInvoer;
	private boolean telefoonKlantInvoer;
	private boolean adresKlantInvoer;
	private BedrijfsContact bedrijfsContact;

	public InplanInstellingen(boolean emailKlantInvoer, boolean telefoonKlantInvoer, boolean adresKlantInvoer) {
		this.emailKlantInvoer = emailKlantInvoer;
		this.telefoonKlantInvoer = telefoonKlantInvoer;
		this.adresKlantInvoer = adresKlantInvoer;
	}

	public void save(){

	}



	public HashMap<String, String> toDto() {
		HashMap<String, String> dto = new HashMap<>();
		if (emailKlantInvoer) {
			dto.put("emailKlantInvoer", "true");
		} else {
			dto.put("emailKlantInvoer", "false");
		}

		if (telefoonKlantInvoer) {
			dto.put("telefoonKlantInvoer", "true");
		} else {
			dto.put("telefoonKlantInvoer", "false");
		}

		if (adresKlantInvoer) {
			dto.put("adresKlantInvoer", "true");
		} else {
			dto.put("adresKlantInvoer", "false");
		}





		dto.putAll(bedrijfsContact.toDto());
		return dto;
	}
}
