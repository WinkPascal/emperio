package com.swinkels.emperio.providers.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.swinkels.emperio.objects.product.Product;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.providers.MariadbBaseDao;
import com.swinkels.emperio.providers.Product.ProductDao;

public class ProductDaoImpl  extends MariadbBaseDao implements ProductDao {
	
	public List<Product> getProducten(int lowLimit, int highLimit, String search){
		List<Product> producten = new ArrayList<Product>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * \n" + 
					"from product \n" + 
					"where BedrijfBedrijfsnaam = '"+Security.getKey()+"' " +
					"and naam LIKE '%"+search+"%' \n"+
					"ORDER BY naam " +
					"LIMIT "+lowLimit+", "+highLimit+"");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				int hoeveelheid = dbResultSet.getInt("hoeveelheid");
				String naam = dbResultSet.getString("naam");
				Product product = new Product(id, hoeveelheid, naam);
				producten.add(product);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}		
		return producten;
	}

	
	public boolean save(Product product) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(""
					+ "insert into product (BedrijfBedrijfsnaam, naam, hoeveelheid) \n" + 
					"VALUES('asd', 'test', 5);");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}


}
