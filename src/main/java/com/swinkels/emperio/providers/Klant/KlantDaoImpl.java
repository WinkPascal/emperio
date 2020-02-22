package com.swinkels.emperio.providers.Klant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantBuilder;
import com.swinkels.emperio.objects.klant.KlantBuilderInterface;
import com.swinkels.emperio.objects.klant.KlantInterface;
import com.swinkels.emperio.objects.klant.KlantStatestieken;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.providers.Klant.KlantDao;
import com.swinkels.emperio.providers.MariadbBaseDao;
import com.swinkels.emperio.support.Validator;

public class KlantDaoImpl extends MariadbBaseDao implements KlantDao {

	
	public List<KlantInterface> getKlanten(int lowLimit, int highLimit,String sort, String klantRequest) {
		List<KlantInterface> klanten = new ArrayList<KlantInterface>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "select k.*, sum(b.prijs) as inkomsten,count(a.id) as afspraken \n" + 
					  "from klant k left join afspraak a on k.id = a.klantId \n" + 
					  "left join afspraakBehandeling b on b.afspraakId = a.id \n"
					+ "where k.naam LIKE '%" + klantRequest + "%' \n "
					+ "and k.BedrijfBedrijfsnaam = '" + Security.getKey() + "' \n"
					+ "group by k.id \n" 
					+ "ORDER BY "+sort+" \n"
					+ "LIMIT "+lowLimit+", "+highLimit);
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				KlantInterface klant = new KlantBuilder()
						.setId(dbResultSet.getInt("id"))
						.setNaam(dbResultSet.getString("naam"))
						.setEmail(Validator.nullValidator(dbResultSet.getString("emailadres")))
						.setTel(Validator.nullValidator(dbResultSet.getString("telefoonnummer")))
						.setGeslacht(dbResultSet.getString("geslacht"))
						.setAdres(Validator.nullValidator(dbResultSet.getString("adres")))
						.build();
				KlantStatestieken  statestieken = new KlantStatestieken(dbResultSet.getDouble("inkomsten"), dbResultSet.getInt("afspraken"));
				klant.setStatestieken(statestieken);
				klanten.add(klant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klanten;
	}
	public Klant getKlantById(int id) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
				"SELECT k.naam, k.geslacht, k.emailadres, k.telefoonnummer, k.adres, count(a.id) as afspraken, sum(b.prijs) as prijs "+
						  "from klant k left join afspraak a on k.id = a.klantId \n" + 
						  "left join afspraakBehandeling b on b.afspraakId = a.id \n"
				+ "where k.BedrijfBedrijfsnaam = '"+Security.getKey() +"' "
				+ "and k.id = "+id);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				KlantBuilder builder = new KlantBuilder();
				builder.setNaam(dbResultSet.getString("naam"));
				builder.setGeslacht(dbResultSet.getString("geslacht"));
				builder.setEmail(Validator.nullValidator(dbResultSet.getString("emailadres")));
				builder.setTel(Validator.nullValidator(dbResultSet.getString("telefoonnummer")));
				builder.setAdres(Validator.nullValidator(dbResultSet.getString("adres")));
				Klant klant = builder.build();
				KlantStatestieken statestieken = new KlantStatestieken(dbResultSet.getDouble("prijs") , dbResultSet.getInt("afspraken"));
				klant.setStatestieken(statestieken);
				return klant;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean save(Klant klant) {
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
