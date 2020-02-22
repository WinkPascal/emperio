package com.swinkels.emperio.objects.product;

import java.util.HashMap;

public interface ProductInterface {
	public void update();
	public void delete(int id);
	public void save();
	
	public HashMap<String, String> toDTO();
}
