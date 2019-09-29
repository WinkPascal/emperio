package com.swinkels.emperio.objects;

public class Product {
	int id;
	int hoeveelheid;
	String naam;

	public Product(int id, int hoeveelheid, String naam) {
		this.naam = naam;
		this.hoeveelheid = hoeveelheid;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getHoeveelheid() {
		return hoeveelheid;
	}

	public String getNaam() {
		return naam;
	}

}
