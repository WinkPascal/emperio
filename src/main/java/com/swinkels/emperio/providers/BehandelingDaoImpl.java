package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.service.ServiceFilter;

public class BehandelingDaoImpl extends MariadbBaseDao implements BehandelingDao {
	public ArrayList<Behandeling> behandelingenByGeslacht(String geslacht, String bedrijf) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from behandeling where geslacht = '" + geslacht + "' and bedrijf = '" + bedrijf + "'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String behandelingsNaam = dbResultSet.getString("naam");
				String beschrijving = dbResultSet.getString("beschrijving");
				Date lengte = ServiceFilter.StringToDateFormatter(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");

				Behandeling behandeling = new Behandeling(id, behandelingsNaam, beschrijving, lengte, prijs, geslacht);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return behandelingen;
	}

	public ArrayList<Behandeling> getTop5Behandelingen(Bedrijf bedrijf, Date date) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("SELECT count(b.id) as hoeveelheid, b.id, b.naam \n"
					+ "from behandeling b join afspraakbehandeling ab on b.id = ab.behandeling \n"
					+ "				   join afspraak a on a.id = ab.afspraak\n" + "where b.bedrijf = '"
					+ bedrijf.getEmail() + "' \n" + "      and a.timestamp < SYSDATE()\n" + "      and a.timestamp > '"
					+ ServiceFilter.DateToStringFormatter(date, "YYYY-MM-dd") + "'\n" + "GROUP by b.id\n"
					+ "ORDER by COUNT(b.id) desc\n" + "LIMIT 5;");
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

		return behandelingen;
	}

	public ArrayList<Behandeling> getBehandelingen(String bedrijf, int pageNummer, String geslacht, String sort) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		int top = pageNummer * 20;
		int low = top - 20;

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select b.id, b.naam, b.beschrijving, b.geslacht, b.prijs, b.lengte, count(a.id) as afspraken, sum(b.prijs) as inkomsten \n"
							+ "from behandeling b join afspraakbehandeling a on b.id = a.behandeling \n"
							+ "where b.bedrijf = '" + bedrijf + "'\n" + "AND b.geslacht " + geslacht + " \n"
							+ "group by b.id \n" + "order by " + sort + "\n LIMIT " + low + ", " + top + ";");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				String beschrijving = dbResultSet.getString("beschrijving");
				Double prijs = dbResultSet.getDouble("prijs");
				String lengteString = dbResultSet.getString("lengte");

				Double inkomsten = dbResultSet.getDouble("inkomsten");
				int afspraken = dbResultSet.getInt("afspraken");

				Date lengte = ServiceFilter.StringToDateFormatter(lengteString, "HH:mm");

				Behandeling behandeling = new Behandeling(id, naam, beschrijving, lengte, prijs, geslacht);
				behandeling.setAfspraken(afspraken);
				behandeling.setInkomsten(inkomsten);

				behandelingen.add(behandeling);
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
					"insert into behandeling(bedrijf, naam, prijs, beschrijving, lengte, geslacht) values( " + "'"
							+ behandeling.getBedrijf().getEmail() + "', " + "'" + behandeling.getNaam() + "', " + ""
							+ behandeling.getPrijs() + ", " + "'" + behandeling.getBeschrijving() + "', " + "'"
							+ ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm") + "', " + "'"
							+ behandeling.getGeslacht() + "')");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Behandeling> getAllBehandelingen(Bedrijf bedrijf) {
		System.out.println("sssssss");
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * " + 
					"FROM behandeling " + 
					"WHERE bedrijf = '" + bedrijf.getEmail() + "';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				String prijsString = dbResultSet.getString("prijs");
				double prijs = Double.valueOf(prijsString);
				String beschrijving = dbResultSet.getString("beschrijving");
				String lengteString = dbResultSet.getString("lengte");
				Date lengte = ServiceFilter.StringToDateFormatter(lengteString, "HH:mm");
				String geslacht = dbResultSet.getString("geslacht");

				Behandeling behandeling = new Behandeling(id, naam, beschrijving, lengte, prijs, geslacht);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return behandelingen;
	}

}
