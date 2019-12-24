package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Product;

public class ProductDaoImpl  extends MariadbBaseDao implements ProductDao{
	
	public ArrayList<Product> getProductenByPage(Bedrijf bedrijf, int pageNummer){		
		ArrayList<Product> producten = new ArrayList<Product>();
		int top = pageNummer * 10;
		int low = top - 10;
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * \n" + 
					"from product \n" + 
					"where bedrijf = '"+bedrijf.getEmail()+"' " + 
					"ORDER BY naam " +
					"LIMIT "+low+", "+top+"");
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
	
	public boolean setProduct(Product product) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(""
					+ "INSERT INTO product(bedrijf, hoeveelheid, naam) VALUES("
					+ "'"+product.getBedrijf().getEmail()+"', "
					+ ""+product.getHoeveelheid()+", "
					+ "'"+product.getNaam()+"');");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
}
