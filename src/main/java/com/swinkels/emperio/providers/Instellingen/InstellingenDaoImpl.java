package com.swinkels.emperio.providers.Instellingen;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.swinkels.emperio.objects.instellingen.InstellingenFacade;
import com.swinkels.emperio.providers.Instellingen.InstellingenDao;
import com.swinkels.emperio.providers.MariadbBaseDao;

public class InstellingenDaoImpl extends MariadbBaseDao implements InstellingenDao {

	public boolean save(InstellingenFacade instellingen) {
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

	public boolean update(InstellingenFacade instellingen) {
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

	public void getInstellingen(InstellingenFacade instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
				"select * from instellingen "
				+ "where BedrijfBedrijfsnaam = '" + instellingen.getBedrijf().getBedrijfsNaam() +"'");
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
	
	
	public boolean updateKleurKlasse(InstellingenFacade instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"UPDATE instellingen SET "
					+ "kleurKlasse = '"+instellingen.getKleurKlasse1()+"', \n"
					+ "maximumprijs1 = "+instellingen.getMaximumPrijsVanKlasse1()+", \n"
					+ "kleurKlasse2 = '"+instellingen.getKleurKlasse2()+"', \n"
					+ "maximumprijs2 = "+instellingen.getMaximumPrijsVanKlasse2()+", \n"
					+ "kleurKlasse3 = '"+instellingen.getKleurKlasse3()+"', \n"
					+ "WHERE BedrijfBedrijfsnaam = '"+instellingen.getBedrijfsNaam()+"'");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
	
	public void getKleurKlasse(InstellingenFacade instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
				"select i.* \n"
				+ "from instellingen i \n"
				+ "where BedrijfBedrijfsnaam = '" + instellingen.getBedrijfsNaam() +"'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				instellingen.setKleurKlasse1(dbResultSet.getString("kleurKlasse"));
				instellingen.setMaximumPrijsVanKlasse1(dbResultSet.getDouble("maximumprijs1"));
				instellingen.setKleurKlasse2(dbResultSet.getString("kleurKlasse2"));
				instellingen.setMaximumPrijsVanKlasse2(dbResultSet.getDouble("maximumprijs2"));
				instellingen.setKleurKlasse3(dbResultSet.getString("kleurKlasse3"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public boolean updateInplanSettings(InstellingenFacade instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"UPDATE instellingen SET "
					+ "emailKlantInvoer = "+instellingen.isEmailKlantInvoer()+", \n"
					+ "telefoonKlantInover  = "+instellingen.isTelefoonKlantInvoer() +", \n"
					+ "AdresKlantInvoer = "+instellingen.isAdresKlantInvoer()+", \n"
					+ "bedrijfsEmail  = '"+instellingen.getBedrijfsEmailString()+"', \n"
					+ "bedrijfsTelefoon = '"+instellingen.getBedrijfsTelefoonString()+"', \n"
					+ "bedrijfsAdres  = '"+instellingen.getBedrijfsAdresString()+"' \n "
					+ "WHERE BedrijfBedrijfsnaam = '"+instellingen.getBedrijfsNaam()+"'");
				System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public void getInplanSettings(InstellingenFacade instellingen) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
				"select i.* \n"
				+ "from instellingen i \n"
				+ "where BedrijfBedrijfsnaam = '" + instellingen.getBedrijfsNaam() +"'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				instellingen.setEmailKlantInvoer(dbResultSet.getBoolean("emailKlantInvoer"));
				instellingen.setTelefoonKlantInvoer(dbResultSet.getBoolean("telefoonKlantInover"));
				instellingen.setAdresKlantInvoer(dbResultSet.getBoolean("AdresKlantInvoer"));

				instellingen.setBedrijfsEmailString(dbResultSet.getString("bedrijfsEmail"));
				instellingen.setBedrijfsTelefoonString(dbResultSet.getString("bedrijfsTelefoon"));
				instellingen.setBedrijfsAdresString(dbResultSet.getString("bedrijfsAdres"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
