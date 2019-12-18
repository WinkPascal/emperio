package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.objects.KlantBuilder;
import com.swinkels.emperio.support.Validator;

public class KlantDaoImpl extends MariadbBaseDao implements KlantDao {

	// klaten wordenopgehaalt door de zoekbalk
	public void zoekKlant(Bedrijf bedrijf, String klantRequest) {
		ArrayList<Klant> klanten = new ArrayList<Klant>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * "
					+ "from klant "
					+ "where naam LIKE '%" + klantRequest + "%' "
					+ "and BedrijfBedrijfsnaam = '" + bedrijf.getBedrijfsNaam() + "'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				
				Klant klant = new KlantBuilder()
						.setId(dbResultSet.getInt("id"))
						.setNaam(dbResultSet.getString("naam"))
						.setEmail(Validator.nullValidator(dbResultSet.getString("emailadres")))
						.setTel(Validator.nullValidator(dbResultSet.getString("telefoonnummer")))
						.setGeslacht(dbResultSet.getString("geslacht"))
						.setAdres(Validator.nullValidator(dbResultSet.getString("adres")))
						.make();
				klanten.add(klant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bedrijf.setKlanten(klanten);
	}
	
	public void getKlanten(Bedrijf bedrijf, int page, String sort, String klantRequest) {
		int top = page * 20;
		int low = top - 20;
		ArrayList<Klant> klanten = new ArrayList<Klant>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "select k.*, sum(b.prijs) as inkomsten, sum(a.id) as afspraken \n"
					+ "from klant k join afspraak a on k.id = a.klantId \n"
					+ "				join afspraakBehandeling f on a.id = f.afspraakId \n"
					+ "				join behandeling b on f.behandelingId = b.id \n"
					+ "where k.naam LIKE '%" + klantRequest + "%' \n"
					+ "and k.BedrijfBedrijfsnaam = '" + bedrijf.getBedrijfsNaam() + "' \n"
					+ "group by k.id \n" 
					+ "ORDER BY "+sort+" \n"
					+ "LIMIT "+low+", "+top);
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				Klant klant = new KlantBuilder()
						.setId(dbResultSet.getInt("id"))
						.setNaam(dbResultSet.getString("naam"))
						.setEmail(Validator.nullValidator(dbResultSet.getString("emailadres")))
						.setTel(Validator.nullValidator(dbResultSet.getString("telefoonnummer")))
						.setGeslacht(dbResultSet.getString("geslacht"))
						.setAdres(Validator.nullValidator(dbResultSet.getString("adres")))
						.make();
				klant.setHoeveeleheidAfspraken(dbResultSet.getInt("afspraken"));
				klant.setHoeveelheidInkomsten(dbResultSet.getDouble("inkomsten"));
				klanten.add(klant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bedrijf.setKlanten(klanten);
	}

	public ArrayList<Klant> getKlantenWithLimit(Bedrijf bedrijf, int low, int top) {
		ArrayList<Klant> klanten = new ArrayList<Klant>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "select k.*, sum(b.prijs) as inkomsten, sum(a.id) "
					+ "from klant k join afspraak a on k.id = a.klantId"
					+ "				join afspraakBehandeling f on a.id = f.afspraakId "
					+ "				join behandeling b on f.behandelingId = b.id "
					+ "where BedrijfBedrijfsnaam = '" + bedrijf.getBedrijfsNaam()+"' " 
					+ "ORDER BY naam "
					+ "LIMIT "+low+", "+top);
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {

				Klant klant =  new KlantBuilder()
									.setId(dbResultSet.getInt("id"))
									.setNaam(dbResultSet.getString("naam"))
									.setEmail(Validator.nullValidator(dbResultSet.getString("emailadres")))
									.setTel(Validator.nullValidator(dbResultSet.getString("telefoonnummer")))
									.setGeslacht(dbResultSet.getString("geslacht"))
									.setAdres(Validator.nullValidator(dbResultSet.getString("adres")))

									.make();
				
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
			// heeft een email en telefoon
			pstmt = con.prepareStatement("insert into klant("
					+ "BedrijfBedrijfsnaam, naam,  emailadres, geslacht, telefoonnummer, adres) "
					+ "values('" + klant.getBedrijf().getBedrijfsNaam() + 
					 "', '" + klant.getNaam() + 
					 "', '"+ klant.getEmail() + "', " +
					 "'" + klant.getGeslacht() + "', " + 
					 "'" + klant.getTel() + 
					 "', '"+klant.getAdres()+"')");
			System.out.println(pstmt);
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
					pstmt = con.prepareStatement("select id from klant where " + 
							"BedrijfBedrijfsnaam = '"+ klant.getBedrijf().getBedrijfsNaam() + "' " + 
							"AND naam = '" + klant.getNaam() + "' " +
							"AND telefoonnummer  = '" + klant.getTel() + "' " + 
							"AND emailadres = '" + klant.getEmail() + "'");
				} else {
					// heeft alleen een email
					pstmt = con.prepareStatement("select id from klant where " + 
							"BedrijfBedrijfsnaam = '"+ klant.getBedrijf().getBedrijfsNaam() + "' " + 	
							"AND naam = '" + klant.getNaam() + "' " +
							"AND emailadres  = '" + klant.getEmail() + "'");
				}
			} else {
				//alleen telefoon
				pstmt = con.prepareStatement("select id from klant where " + 
						"BedrijfBedrijfsnaam = '"+ klant.getBedrijf().getBedrijfsNaam() + "' " + 							
						"AND naam = '" + klant.getNaam() + "' " +
						"AND telefoonnummer = '" + klant.getTel() + "'");				
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

	public void getKlant(Bedrijf bedrijf, Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
				"SELECT k.naam, k.geslacht, k.emailadres, k.telefoonnummer, k.adres, count(a.id) as afspraken, sum(b.prijs) as prijs"
				+ " from afspraak a join klant k on a.klantId = k.id \n" 
				+ "		    join afspraakBehandeling l on l.afspraakId = a.id \n"  
				+ "			join behandeling b on b.id = l.behandelingId \n"
				+ "where k.BedrijfBedrijfsnaam = '"+bedrijf.getBedrijfsNaam()+"' "
				+ "and k.id = "+klant.getId());
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				klant.setNaam(dbResultSet.getString("naam"));
				klant.setGeslacht(dbResultSet.getString("geslacht"));
				klant.setEmail(Validator.nullValidator(dbResultSet.getString("emailadres")));
				klant.setTel(Validator.nullValidator(dbResultSet.getString("telefoonnummer")));
				klant.setAdres(Validator.nullValidator(dbResultSet.getString("adres")));
				klant.setHoeveeleheidAfspraken(dbResultSet.getInt("afspraken"));
				klant.setHoeveelheidInkomsten(dbResultSet.getInt("prijs"));
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getKlantIdByEmail(Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from klant where " +
				    "BedrijfBedrijfsnaam = '"+ klant.getBedrijf().getEmail() + "' " + 
				    "AND naam = '" + klant.getNaam() + "' " +
				    "AND emailadres = '"+klant.getEmail()+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				klant.setId(dbResultSet.getInt("id"));
				return true;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getKlantIdByPhone(Klant klant) {
		System.out.println("telefoon zoeken");
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from klant where " +
				    "BedrijfBedrijfsnaam = '"+ klant.getBedrijf().getBedrijfsNaam() + "' " + 
				    "AND naam = '" + klant.getNaam() + "' " +
				    "AND telefoonnummer = '"+klant.getTel()+"';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				klant.setId(dbResultSet.getInt("id"));
				return true;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
