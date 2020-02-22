package com.swinkels.emperio.objects.product;

import java.util.List;

import com.swinkels.emperio.providers.Product.ProductDao;
import com.swinkels.emperio.providers.Product.ProductDaoImpl;

public class ProductManager {
	ProductDao productDao = new ProductDaoImpl();

	private int page;
	private String search;

	public ProductManager(int page, String search) {
		this.page = page;
		this.search = search;
	}

	public List<Product> getProducten() {
		int highLimit = page * 20;
		int lowLimit = highLimit - 20;
		return productDao.getProducten(lowLimit, highLimit, search);
	}

}
