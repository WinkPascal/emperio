package com.swinkels.emperio.support;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;

public class Service {
	public static void main(String [] args)
	{
		
		Bedrijf bedrijf = new Bedrijf();
		bedrijf.setEmail("pawiwink@s.com");
		bedrijf.setTelefoon("0615574740");
		bedrijf.setPostcode("1211BH");
		bedrijf.setWachtwoord("PascalWink1!");
		
		
		ArrayList<String> errors = bedrijf.validate();
		if(errors.size() != 0) {
			for(String error : errors) {
				System.out.println(error);
			}
		} else {
			System.out.println("geen errors");
		}

	}
}
