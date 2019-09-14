package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Klant;

public class KlantDaoImpl extends MariadbBaseDao implements KlantDao{

	//klaten wordenopgehaalt door de zoekbalk
	public ArrayList<Klant> zoekKlant(String bedrijf, String klantRequest) {
		ArrayList<Klant> klanten = new ArrayList<Klant>();
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from klant where naam LIKE '%"+klantRequest+"%' and bedrijf = '"+bedrijf+"'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam =dbResultSet.getString("naam");
				String email =dbResultSet.getString("email");
				String telefoon=dbResultSet.getString("telefoon");
				String geslacht=dbResultSet.getString("geslacht");
				
				Klant klant = new Klant(id, naam, email, telefoon, geslacht);
				klanten.add(klant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return klanten;
	}

	public ArrayList<Klant> getKlanten(String bedrijf, int pageNummer) {
		ArrayList<Klant> klanten = new ArrayList<Klant>();

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from klant where bedrijf = '"+bedrijf+"' ORDER BY naam LIMIT 0, 10");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam =dbResultSet.getString("naam");
				String email =dbResultSet.getString("email");
				String telefoon=dbResultSet.getString("telefoon");
				String geslacht=dbResultSet.getString("geslacht");
				
				Klant klant = new Klant(id, naam, email, telefoon, geslacht);
				klanten.add(klant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klanten;
	}
	public boolean setKlant(Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from klant where bedrijf = '"+bedrijf+"' ORDER BY naam LIMIT 0, 10");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeUpdate();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam =dbResultSet.getString("naam");
				String email =dbResultSet.getString("email");
				String telefoon=dbResultSet.getString("telefoon");
				String geslacht=dbResultSet.getString("geslacht");
				
				Klant klant = new Klant(id, naam, email, telefoon, geslacht);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int getKlantId(Klant klant) {
		int klantId = 0;
		
		return klantId;
	}

}
