package com.swinkels.emperio.objects.klant;

import java.util.List;

import com.swinkels.emperio.providers.Klant.KlantDao;
import com.swinkels.emperio.providers.Klant.KlantDaoImpl;

public class KlantManager {
	private static KlantDao klantDao = new KlantDaoImpl();

	private int page;
	private String search;
	private int sort;
	
	public KlantManager(int page, String search, int sort) {
		this.page = page;
		this.search = search;
		this.sort = sort;
	}

	public List<KlantInterface> getKlanten(){
		int highLimit = page * 20;
		int lowLimit = highLimit - 20 ;
		
		return klantDao.getKlanten(lowLimit, highLimit, getSort(), search);
	}
	
	private String getSort() {
		switch(sort) {
		case 1:
			return "count(a.id) desc";
		case 2:
			return "sum(b.prijs) desc";
		case 3:
			return "count(a.id) asc";
		default:
			return "sum(b.prijs) asd";		
		}
	}

}
