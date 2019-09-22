package com.swinkels.emperio.objects;

import java.util.Date;

public class Dag {
	int id;
	Bedrijf bedrijf;
	Date openingsTijd;
	Date sluitingsTijd;
	int dag;
	
	public Dag(Date openingsTijd, Date sluitingsTijd) {
		this.openingsTijd = openingsTijd;
		this.sluitingsTijd = sluitingsTijd;
	}
}
