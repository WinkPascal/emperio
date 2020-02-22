package com.swinkels.emperio.support;

import java.util.ArrayList;
import java.util.List;

import com.swinkels.emperio.objects.email.Email;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantBuilder;
import com.swinkels.emperio.objects.security.Bedrijf;

public class Service {

	public static void main(String[] args) {
		KlantBuilder builder = new KlantBuilder();

		ArrayList<Klant> klanten = new ArrayList<Klant>();
		Klant klant1 = builder.make();
		klant1.setId(1);
		klanten.add(klant1);

		Klant klant2 = builder.make();
		klant2.setId(2);
		klanten.add(klant2);

		Klant klant3 = builder.make();
		klant3.setId(3);
		klanten.add(klant3);

		Bedrijf bedrijf = new Bedrijf();
		bedrijf.setBedrijfsNaam("asd");
		bedrijf.setEmail("pascal.wink@gmail.com");
		Email email = new Email(bedrijf);
		email.setKlanten(klanten);
		email.setOnderwerp("Test");
		email.setInhoud("nog een test");
		email.send();
	}
}
