package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.BehandelingBuilder;
import com.swinkels.emperio.support.Adapter;

public class BehandelingDaoImpl extends MariadbBaseDao implements BehandelingDao {

	public ArrayList<Behandeling> getAllBehandelingen(Bedrijf bedrijf) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * " + 
					"FROM behandeling " + 
					"WHERE BedrijfBedrijfsnaam = '" + bedrijf.getBedrijfsNaam() + "';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				String prijsString = dbResultSet.getString("prijs");
				double prijs = Double.valueOf(prijsString);
				String beschrijving = dbResultSet.getString("beschrijving");
				String lengteString = dbResultSet.getString("lengte");
				Date lengte = Adapter.StringToDate(lengteString, "HH:mm");
				String geslacht = dbResultSet.getString("geslacht");

				Behandeling behandeling = new Behandeling(id, naam, beschrijving, lengte, prijs, geslacht);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return behandelingen;
	}
	
	public void behandelingenByGeslacht(String geslacht, Bedrijf bedrijf) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from behandeling "
					+ "where geslacht = '" + geslacht + "' "
					+ "and BedrijfBedrijfsnaam = '" + bedrijf.getBedrijfsNaam() + "'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String behandelingsNaam = dbResultSet.getString("naam");
				String beschrijving = dbResultSet.getString("beschrijving");
				Date lengte = Adapter.StringToDate(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");

				Behandeling behandeling = new Behandeling(id, behandelingsNaam, beschrijving, lengte, prijs, geslacht);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bedrijf.setBehandelingen(behandelingen);
	}

	public void getTop5Behandelingen(Bedrijf bedrijf, Date date) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("SELECT count(b.id) as hoeveelheid, b.id, b.naam \n"
					+ "from behandeling b join afspraakBehandeling ab on b.id = ab.behandelingId \n"
					+ "				   join afspraak a on a.id = ab.afspraakId\n" + "where b.BedrijfBedrijfsnaam = '"
					+ bedrijf.getBedrijfsNaam() + "' \n" + "      and a.timestamp < SYSDATE()\n" + "      and a.timestamp > '"
					+ Adapter.DateToString(date, "YYYY-MM-dd") + "'\n" + "GROUP by b.id\n"
					+ "ORDER by COUNT(b.id) desc\n" + "LIMIT 5;");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {

				int id = dbResultSet.getInt("id");
				int count = dbResultSet.getInt("hoeveelheid");
				String behandelingsNaam = dbResultSet.getString("naam");

				Behandeling behandeling = new Behandeling(id, behandelingsNaam, count);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bedrijf.setBehandelingen(behandelingen);
	}

	public ArrayList<Behandeling> getBehandelingen(Bedrijf bedrijf, int pageNummer, String geslacht, String sort) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		int top = pageNummer * 20;
		int low = top - 20;

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select b.id, b.naam, b.beschrijving, b.geslacht, b.prijs, b.lengte, count(a.id) as afspraken, sum(b.prijs) as inkomsten \n"
							+ "from behandeling b left join afspraakBehandeling a on b.id = a.behandelingId \n"
							+ "where b.BedrijfBedrijfsnaam = '" + bedrijf.getBedrijfsNaam() + "'\n" + "AND b.geslacht " + geslacht + " \n"
							+ "group by b.id \n" + "order by " + sort + "\n LIMIT " + low + ", " + top + ";");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String lengteString = dbResultSet.getString("lengte");

				Behandeling behandeling = new BehandelingBuilder()
											.setId(dbResultSet.getInt("id"))
											.setBeschrijving(dbResultSet.getString("beschrijving"))
											.setGeslacht(dbResultSet.getString("geslacht"))
											.setLengte(Adapter.StringToDate(lengteString, "HH:mm"))
											.setPrijs(dbResultSet.getDouble("prijs"))
											.setNaam(dbResultSet.getString("naam"))
											.make();
				behandeling.setAfspraken(dbResultSet.getInt("afspraken"));
				behandeling.setInkomsten(dbResultSet.getDouble("inkomsten"));

				bedrijf.addBehandeling(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return behandelingen;
	}

	public boolean save(Behandeling behandeling) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			// heeft een email en telefoon
			pstmt = con.prepareStatement(
					"insert into behandeling (BedrijfBedrijfsnaam, lengte, geslacht, naam, prijs, beschrijving) "
					+ "values('" + behandeling.getBedrijf().getBedrijfsNaam() + "', '"
					+ Adapter.DateToString(behandeling.getLengte(), "HH:mm") + "', '"
					+ behandeling.getGeslacht() + "', '"
					+ behandeling.getNaam() + "', " + ""
					+ behandeling.getPrijs() + ", '" 
					+ behandeling.getBeschrijving() + "')");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(Behandeling behandeling) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"UPDATE behandeling \n" + 
					"SET status = 'verwijdert' \n" + 
					"WHERE id = "+behandeling.getId()+
					" AND BedrijfBedrijfsnaam='"+behandeling.getBedrijf().getBedrijfsNaam()+"'");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void getBehandeling(Behandeling behandeling) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select b.id, b.naam, b.beschrijving, b.geslacht, b.prijs, b.lengte, count(a.id) as afspraken, sum(b.prijs) as inkomsten \n"
							+ "from behandeling b left join afspraakBehandeling a on b.id = a.behandelingId \n"
							+ "where b.BedrijfBedrijfsnaam = '" + behandeling.getBedrijf().getBedrijfsNaam() + "'\n" 
							+ "AND b.id = " + behandeling.getId() +" \n"
							+ "group by b.id");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				behandeling.setId(dbResultSet.getInt("id"));
				behandeling.setBeschrijving(dbResultSet.getString("beschrijving"));
				behandeling.setGeslacht(dbResultSet.getString("geslacht"));
				behandeling.setLengte(Adapter.StringToDate(dbResultSet.getString("lengte"), "HH:mm"));
				behandeling.setPrijs(dbResultSet.getDouble("prijs"));
				behandeling.setNaam(dbResultSet.getString("naam"));
				behandeling.setAfspraken(dbResultSet.getInt("afspraken"));
				behandeling.setInkomsten(dbResultSet.getDouble("inkomsten"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
