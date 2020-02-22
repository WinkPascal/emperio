package com.swinkels.emperio.objects.instellingen;

import com.swinkels.emperio.providers.Instellingen.InstellingenDao;
import com.swinkels.emperio.providers.Instellingen.InstellingenDaoImpl;

import java.util.HashMap;

public class InstellingenFacade implements InstellingenFacadeInterface{
	InstellingenDao instellingenDao = new InstellingenDaoImpl();

	public InstellingenFacade() {
	}
	
	public InstellingenFacade(boolean emailKlantInvoer, boolean telefoonKlantInvoer,
			boolean adresKlantInvoer, String kleurKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2,
			double maximumPrijsVanKlasse2, String kleurKlasse3, String bedrijfsEmail, String bedrijfsTelefoon,
			String bedrijfsAdres) {
		InplanInstellingen inplanInstellingen = new InplanInstellingen(emailKlantInvoer , telefoonKlantInvoer, adresKlantInvoer);
		inplanInstellingen.save();
		AfspraakKleuren afspraakKleuren = new AfspraakKleuren(kleurKlasse1 , maximumPrijsVanKlasse1,kleurKlasse2, maximumPrijsVanKlasse2, kleurKlasse3);
		afspraakKleuren.save();
		BedrijfsContact bedrijfsContact = new BedrijfsContact(bedrijfsEmail, bedrijfsTelefoon,bedrijfsAdres);
		bedrijfsContact.save();
	}

	public void saveInplanSettings(boolean telefoonKlantInvoer, boolean emailKlantInvoer,
							  boolean adresKlantInvoer, String bedrijfsEmail, String bedrijfsTelefoon,
							  String bedrijfsAdres){
		InplanInstellingen inplanInstellingen = new InplanInstellingen(emailKlantInvoer , telefoonKlantInvoer, adresKlantInvoer);
		inplanInstellingen.save();
		BedrijfsContact bedrijfsContact = new BedrijfsContact(bedrijfsEmail, bedrijfsTelefoon,bedrijfsAdres);
		bedrijfsContact.save();
	}
	public void saveAfspraakKleuren(String kleurKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2, double maximumPrijsVanKlasse2, String kleurKlasse3){
		AfspraakKleuren afspraakKleuren = new AfspraakKleuren(kleurKlasse1 , maximumPrijsVanKlasse1,kleurKlasse2, maximumPrijsVanKlasse2, kleurKlasse3);
		afspraakKleuren.save();
	}

	public HashMap<String, String> getInplanSettingsDTO() {
		InplanInstellingen inplanInstellingen = instellingenDao.getInplanSettings();
		return inplanInstellingen.toDto();
	}

	public HashMap<String, String> toDto(){

	}

	public boolean save() {
		if (instellingenDao.save(this)) {
			return true;
		} else {
			return false;
		}
	}
}
