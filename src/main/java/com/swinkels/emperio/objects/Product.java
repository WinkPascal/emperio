package com.swinkels.emperio.objects;

public class Product {
	private int id;
	private int hoeveelheid;
	private String naam;
	private Bedrijf bedrijf;

	public Product(int id, int hoeveelheid, String naam) {
		this.naam = naam;
		this.hoeveelheid = hoeveelheid;
		this.id = id;
	}
	public Product(Bedrijf bedrijf, int hoeveelheid, String naam) {
		this.bedrijf = bedrijf;
		this.hoeveelheid = hoeveelheid;
		this.naam = naam;
	}

	public int getId() {
		return id;
	}	
	
	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public int getHoeveelheid() {
		return hoeveelheid;
	}

	public String getNaam() {
		return naam;
	}

}
