package com.swinkels.emperio.providers.Product;

import java.util.ArrayList;
import java.util.List;

import com.swinkels.emperio.objects.product.Product;
import com.swinkels.emperio.objects.security.Bedrijf;

public interface ProductDao {

	public boolean save(Product product);

	public List<Product> getProducten(int lowLimit, int highLimit, String search);
}
