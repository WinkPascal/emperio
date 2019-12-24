package com.swinkels.emperio.providers;

import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Product;

public interface ProductDao {
	public ArrayList<Product> getProductenByPage(Bedrijf bedrijf, int page);

	public boolean setProduct(Product product);
}
