package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Instellingen;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.service.ServiceFilter;
import com.swinkels.emperio.support.Validator;

public class InstellingenDaoImpl extends MariadbBaseDao implements InstellingenDao{

	public boolean save(Instellingen instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"insert into instellingen "
					+ "values('"+instellingen.getBedrijf().getBedrijfsNaam()+"', '"+instellingen.getKleurKlasse1()+"', "
					+ ""+instellingen.getMaximumPrijsVanKlasse1()+", " 
					+ "'"+instellingen.getKleurKlasse2()+"', "+instellingen.getMaximumPrijsVanKlasse2()+", "
					+ "'"+instellingen.getKleurKlasse3()+"',  "
					+ ""+instellingen.isTelefoonKlantInvoer()+ ", "+instellingen.isEmailKlantInvoer() + ", "
					+ ""+instellingen.isAdresKlantInvoer()+", "+instellingen.isBedrijfsEmail()+", "
					+ ""+instellingen.isBedrijfsTelefoon()+", "+instellingen.isBedrijfsAdres()+")");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public boolean update(Instellingen instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"UPDATE instellingen SET "
					+ "kleurKlasse = '"+instellingen.getKleurKlasse1()+"', \n"
					+ "maximumprijs1 = "+instellingen.getMaximumPrijsVanKlasse1()+", \n"
					+ "kleurKlasse2 = '"+instellingen.getKleurKlasse2()+"', \n"
					+ "maximumprijs2 = "+instellingen.getMaximumPrijsVanKlasse2()+", \n"
					+ "kleurKlasse3 = '"+instellingen.getKleurKlasse3()+"', \n"
					
					+ "emailKlantInvoer = "+instellingen.isEmailKlantInvoer()+", \n"
					+ "telefoonKlantInover  = "+instellingen.isTelefoonKlantInvoer() +", \n"
					+ "AdresKlantInvoer = "+instellingen.isAdresKlantInvoer()+", \n"
					
					+ "bedrijfsEmail  = "+instellingen.isBedrijfsEmail()+", \n"
					+ "bedrijfsTelefoon = "+instellingen.isBedrijfsTelefoon()+", \n"
					+ "bedrijfsAdres  = "+instellingen.isBedrijfsAdres()+" \n "
					+ "WHERE BedrijfBedrijfsnaam = '"+instellingen.getBedrijf().getBedrijfsNaam()+"'");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public void getInstellingen(Instellingen instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from instellingen where BedrijfBedrijfsnaam = '" + instellingen.getBedrijf().getBedrijfsNaam() +"'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				instellingen.setKleurKlasse1(dbResultSet.getString("kleurKlasse"));
				instellingen.setMaximumPrijsVanKlasse1(dbResultSet.getDouble("maximumprijs1"));
				instellingen.setKleurKlasse2(dbResultSet.getString("kleurKlasse2"));
				instellingen.setMaximumPrijsVanKlasse2(dbResultSet.getDouble("maximumprijs2"));
				instellingen.setKleurKlasse3(dbResultSet.getString("kleurKlasse3"));
				
				instellingen.setEmailKlantInvoer(dbResultSet.getBoolean("emailKlantInvoer"));
				instellingen.setTelefoonKlantInvoer(dbResultSet.getBoolean("telefoonKlantInover"));
				instellingen.setAdresKlantInvoer(dbResultSet.getBoolean("AdresKlantInvoer"));

				instellingen.setBedrijfsEmail(dbResultSet.getBoolean("bedrijfsEmail"));
				instellingen.setBedrijfsTelefoon(dbResultSet.getBoolean("bedrijfsTelefoon"));
				instellingen.setBedrijfsAdres(dbResultSet.getBoolean("bedrijfsAdres"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}
