package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Product;
import com.swinkels.emperio.service.ServiceFilter;

public class BedrijfDaoImpl extends MariadbBaseDao implements BedrijfDao{
	
	
	public ArrayList<Bedrijf> getBedrijven(int page){
		ArrayList<Bedrijf> bedrijven = new ArrayList<Bedrijf>();
		int top = page * 10;
		int low = top - 10;
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT email, telefoon, adres, naam \n" + 
					"FROM bedrijf \n" + 
					"WHERE role = \"user\" \n" + 
					"group by email LIMIT "+low+", "+top+"");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				
				String email = dbResultSet.getString("email");
				String telefoon = dbResultSet.getString("telefoon");
				String adres = dbResultSet.getString("adres");
				String naam = dbResultSet.getString("naam");

				
				Bedrijf bedrijf = new Bedrijf(email, naam, telefoon, adres);
				bedrijven.add(bedrijf);
			}
		} catch (SQLException e) {
			return null;
		}
		
		return bedrijven;
	}
	
	public ArrayList<Date> getDagTijden(Bedrijf bedrijf, Date date){
		ArrayList<Date> dagTijden = new ArrayList<Date>();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT openingstijd, sluitingstijd"
				 + " FROM `dag` "
				 + "WHERE bedrijf ='"+bedrijf.getEmail()+"' "
				 + "and dag = "+dayOfWeek+" "
				 + "ORDER BY dag;");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				String sluitingsTijd = dbResultSet.getString("sluitingstijd");
				
				Date openingsTijdDate = ServiceFilter.StringToDateFormatter(openingsTijd, "HH:mm");
				Date sluitingsTijdDate = ServiceFilter.StringToDateFormatter(sluitingsTijd, "HH:mm");
				
				dagTijden.add(openingsTijdDate);
				dagTijden.add(sluitingsTijdDate);
			}
		} catch (SQLException e) {
			return null;
		}
		return dagTijden;
	}
	
	//wordt gebruikt bij: getWerkdagen
	public ArrayList<Dag> getWeekRooster(Bedrijf bedrijf){
		ArrayList<Dag> dagen = new ArrayList<Dag>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT dag, openingstijd, sluitingstijd"
				 + " FROM `dag` "
				 + "WHERE bedrijf ='"+bedrijf.getEmail()+"' "
				 + "ORDER BY dag");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				String sluitingsTijd = dbResultSet.getString("sluitingstijd");
				int dagNummer = dbResultSet.getInt("dag");
				
				//format de tijden
				Date openingsTijdDate = ServiceFilter.StringToDateFormatter(openingsTijd, "HH:mm");
				Date sluitingsTijdDate = ServiceFilter.StringToDateFormatter(sluitingsTijd, "HH:mm");
				
				Dag  dag = new Dag(dagNummer, openingsTijdDate, sluitingsTijdDate);
				
				dagen.add(dag);
			}
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		
		return dagen;
	}
	
	// wordt gebruikt bij
		//getProductenByPage();
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

	public boolean saveBedrijf(Bedrijf bedrijf) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO bedrijf(adres, email, naam, role, telefoon, wachtwoord) VALUES('"
				  +	bedrijf.getAdres()+"', '"
				  + bedrijf.getEmail()+"', '"
				  + bedrijf.getNaam() + "', '"
				  + "user', '"
				  + bedrijf.getTel()+"', '"
				  + bedrijf.getWachtwoord()+"')");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
	
	public boolean setInvoerKlant(Bedrijf bedrijf, String contact, boolean telefoon, boolean email, boolean adres) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"UPDATE bedrijf"+
					" SET invoerveldEmail = "+email+", \n" + 
					"invoerveldTelefoon = "+telefoon+", \n" + 
					"invoerveldAdres = "+adres+",\n" + 
					"verplichtContactVeld = 'email'\n" + 
					"WHERE email='"+ bedrijf.getEmail()+"';");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}	
}
