package com.swinkels.emperio.objects;

public class Bedrijf {
	String email;
	String naam;
	String tel;
	String adres;
	
	public Bedrijf(String bedrijfsNaam) {
		this.email = bedrijfsNaam;
	}
	
	public String getEmail() {
		return email;
	}
}
