package com.swinkels.emperio.providers.Bedrijf;

import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.product.Product;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;

public interface BedrijfDao {
	public boolean save(Bedrijf bedrijf);

	public boolean setInvoerKlant(Bedrijf bedrijf, String contact, boolean telefoon, boolean email, boolean adres);

	public void getInfo(Bedrijf bedrijf);

	public void getKlantPaginaSettings(Bedrijf bedrijf);

	public boolean needsSetup(String username);
}
