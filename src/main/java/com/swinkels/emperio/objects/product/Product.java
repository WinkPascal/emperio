package com.swinkels.emperio.objects.product;

import java.util.HashMap;

import com.swinkels.emperio.providers.Product.ProductDao;
import com.swinkels.emperio.providers.Product.ProductDaoImpl;

public class Product implements ProductInterface{
	ProductDao productDao = new ProductDaoImpl();
	
	private int id;
	private int hoeveelheid;
	private String naam;

	public Product(int id, int hoeveelheid, String naam) {
		this.naam = naam;
		this.hoeveelheid = hoeveelheid;
		this.id = id;
	}
	
	public HashMap<String, String> toDTO(){
		HashMap<String, String> dto = new HashMap<String, String>();
		dto.put("id", Integer.toString(id));
		dto.put("hoeveelheid", Integer.toString(hoeveelheid));
		dto.put("naam", naam);
		return dto;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

}
