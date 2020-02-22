package com.swinkels.emperio.objects.rooster;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.providers.Afspraak.AfspraakDao;
import com.swinkels.emperio.providers.Afspraak.AfspraakDaoImpl;

public interface AfspraakInterface {

	static List<Afspraak> getLaatste5AfsprakenFromKlant(int id){
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		return afspraakDao.getAfsprakenFromKlantId(id);
	}
	static List<Afspraak> getAfsprakenBetweenDates(Date date1, Date date2) {
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		return afspraakDao.getAfsprakenBetweenDates(date1, date2);
	}



	boolean delete();
	boolean save();
	void getAfspraakInfoFromDatabase();



	HashMap<String, String> toDTO();
	Klant getKlant();
	List<Behandeling> getBehandelingen();
    void setBehandelingen(ArrayList<Behandeling> behandelingen);
}
