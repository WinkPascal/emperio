package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Klant;

public class KlantDaoImpl extends MariadbBaseDao implements KlantDao {

	// klaten wordenopgehaalt door de zoekbalk
	public ArrayList<Klant> zoekKlant(String bedrijf, String klantRequest) {
		ArrayList<Klant> klanten = new ArrayList<Klant>();

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from klant where naam LIKE '%" + klantRequest + "%' and bedrijf = '" + bedrijf + "'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				String email = dbResultSet.getString("email");
				String telefoon = dbResultSet.getString("telefoon");
				String geslacht = dbResultSet.getString("geslacht");

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
		
		
		int top = pageNummer * 20;
		int low = top - 19;
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from klant where bedrijf = '" + bedrijf + "' ORDER BY naam LIMIT "+low+", "+top+"");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				String email = dbResultSet.getString("email");
				String telefoon = dbResultSet.getString("telefoon");
				String geslacht = dbResultSet.getString("geslacht");

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
			PreparedStatement pstmt;
			if (klant.getEmail() != null) {
				if (klant.getTel() != null) {
					// heeft een email en telefoon
					pstmt = con.prepareStatement("insert into klant(bedrijf, naam, email, geslacht, telefoon) values( "
							+ "'" + klant.getBedrijf().getEmail() + "', " + "'" + klant.getNaam() + "', " + "'"
							+ klant.getEmail() + "', " + "'" + klant.getGeslacht() + "', " + "'" + klant.getTel()
							+ "')");
				} else {
					// heeft alleen een email
					pstmt = con.prepareStatement("insert into klant(bedrijf, naam, email, geslacht) values( " + "'"
							+ klant.getBedrijf().getEmail() + "', " + "'" + klant.getNaam() + "', " + "'"
							+ klant.getEmail() + "', " + "'" + klant.getGeslacht() + "')");
				}
			} else {
				if (klant.getTel() != null) {
					// klant heeft alleen een telefoon
					pstmt = con.prepareStatement("insert into klant(bedrijf, naam, geslacht, telefoon) values( " + "'"
							+ klant.getBedrijf().getEmail() + "', " + "'" + klant.getNaam() + "', " + "'"
							+ klant.getGeslacht() + "', " + "'" + klant.getTel() + "')");
				} else {
					pstmt = con.prepareStatement("insert into klant(bedrijf, naam, geslacht) values( " + "'"
							+ klant.getBedrijf().getEmail() + "', " + "'" + klant.getNaam() + "', " + "'"
							+ klant.getGeslacht() + "')");
				}
			}
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public Klant getKlantId(Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			if (klant.getEmail() != null) {
				if (klant.getTel() != null) {
					// heeft een email en telefoon
					pstmt = con.prepareStatement("select id from klant where " + "bedrijf = '"
							+ klant.getBedrijf().getEmail() + "' " + "AND naam = '" + klant.getNaam() + "' "
							+ "AND telefoon = '" + klant.getTel() + "' " + "AND email = '" + klant.getEmail() + "'");
				} else {
					// heeft alleen een email
					pstmt = con.prepareStatement("select id from klant where " + "bedrijf = '"
							+ klant.getBedrijf().getEmail() + "' " + "AND naam = '" + klant.getNaam() + "' "
							+ "AND email = '" + klant.getEmail() + "'");
				}
			} else {
				if (klant.getTel() != null) {
					pstmt = con.prepareStatement("select id from klant where " + "bedrijf = '"
							+ klant.getBedrijf().getEmail() + "' " + "AND naam = '" + klant.getNaam() + "' "
							+ "AND telefoon = '" + klant.getTel() + "'");
				} else {
					pstmt = con.prepareStatement("select id from klant where " + "bedrijf = '"
							+ klant.getBedrijf().getEmail() + "' " + "AND naam = '" + klant.getNaam() + "'");
				}
			}
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				klant.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klant;
	}

	public Klant getKlant(Bedrijf bedrijf, int id) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT naam, geslacht, email, telefoon "
				+ "from klant "
				+ "where bedrijf = '"+bedrijf.getEmail()+"' "
				+ "and id = "+id);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				
				String naam = dbResultSet.getString("naam");
				String geslacht = dbResultSet.getString("geslacht");
				String email = dbResultSet.getString("email");
				String telefoon = dbResultSet.getString("telefoon");
				
				Klant klant = new Klant(id, naam, email, telefoon, geslacht);


				return klant;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Klant getKlantIdByEmail(Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from klant where " +
				    "bedrijf = '"+ klant.getBedrijf().getEmail() + "' " + 
				    "AND naam = '" + klant.getNaam() + "' " +
				    "AND email = '"+klant.getEmail()+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				klant.setId(id);	
				return klant;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klant;
	}

	public Klant getKlantIdByPhone(Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from klant where " +
				    "bedrijf = '"+ klant.getBedrijf().getEmail() + "' " + 
				    "AND naam = '" + klant.getNaam() + "' " +
				    "AND telefoon = '"+klant.getTel()+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				klant.setId(id);	
				return klant;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		klant.setId(0);	

		return klant;
	}
}
