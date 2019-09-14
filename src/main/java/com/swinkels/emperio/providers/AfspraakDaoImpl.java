package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Klant;

public class AfspraakDaoImpl extends MariadbBaseDao implements AfspraakDao {

	public ArrayList<Afspraak> getAfsprakenVandaag(String date, String bedrijf) {
		ArrayList<Afspraak> afsprakenVandaag = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from afspraak where bedrijf ='" + bedrijf + "' and tijd = '" + date + "'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				System.out.println(dbResultSet.getString("id"));
				// klant
				String klantNaam = dbResultSet.getString("naam");
				// afspraak
				int id = dbResultSet.getInt("id");
				String tijd = dbResultSet.getString("tijd");
				String lengte = dbResultSet.getString("lengte");
				// behandeling
				String behandelingsNaam = dbResultSet.getString("naam");

				Klant klant = new Klant(klantNaam);
				Afspraak afspraak = new Afspraak(id, tijd, lengte, klant);
				afsprakenVandaag.add(afspraak);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return afsprakenVandaag;
	}

	public boolean setAfspraak(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			//PreparedStatement pstmt = con.prepareStatement("insert into klant(bedrijf, naam, email, telefoon, geslacht)"
			//		+ "values('" + bedrijf + "', '" + afspraakKlantNaam + "','" + afspraakKlantEmail + "','"
			//		+ afspraakKlantTel + "', '" + afspraakKlantGeslacht + "')");
			//System.out.println(pstmt);
			//pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getAfspraakId(Afspraak afspraak) {
		return 0;
	}
}
