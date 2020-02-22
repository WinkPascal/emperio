package com.swinkels.emperio.objects.rooster;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.providers.AfspraakBehandeling.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandeling.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.Afspraak.AfspraakDao;
import com.swinkels.emperio.providers.Afspraak.AfspraakDaoImpl;
import com.swinkels.emperio.support.JavascriptDateAdapter;

public class Afspraak implements AfspraakInterface {
	private AfspraakDao afspraakDao = new AfspraakDaoImpl();
	private AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();

	private Klant klant;
	private ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

	private int id;
	private Date timestamp;
	private Double prijs;

	public Afspraak(int id, Date timestamp, Double prijs) {
		this.id = id;
		this.timestamp = timestamp;
		this.prijs = prijs;
	}


	//functionaliteit

	public void getAfspraakInfoFromDatabase(){
		behandelingen = afspraakBehandelingDao.getBehandelingenbyAfspraak(id);
		klant = afspraakDao.getKlant(id);
	}


	public boolean delete(){
		afspraakDao.deleteAfspraak(id);
	}

	public boolean save(){

	}



	//contact met controller


	public HashMap<String, String> toDTO() {
		HashMap<String, String> dto = new HashMap<>();
		dto.put("id", Integer.toString(id));
		dto.put("timestamp", JavascriptDateAdapter.DateToString(timestamp, "yyyy-MM-dd HH:mm"));
		dto.put("prijs", Double.toString(prijs));
		return dto;
	}

	public List<Behandeling> getBehandelingen(){
		return behandelingen;
	}

	public Klant getKlant() {
		return klant;
	}

	public void setKlant(Klant klant) {
		this.klant = klant;
	}
	public void setBehandelingen(ArrayList<Behandeling> behandelingen){
		this.behandelingen = behandelingen;
	}
}
