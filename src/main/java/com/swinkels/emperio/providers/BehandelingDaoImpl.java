package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.service.ServiceFilter;

public class BehandelingDaoImpl extends MariadbBaseDao implements BehandelingDao{
	public ArrayList<Behandeling> behandelingenByGeslacht(String geslacht, String bedrijf){
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from behandeling where geslacht = '"+geslacht+"' and bedrijf = '"+bedrijf+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String behandelingsNaam = dbResultSet.getString("naam");
				String beschrijving = dbResultSet.getString("beschrijving");
				Date lengte = ServiceFilter.StringToDateFormatter(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");

				Behandeling behandeling = new Behandeling(id, behandelingsNaam, beschrijving, lengte, prijs);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return behandelingen;
	}

	public boolean setBehandeling(Behandeling behandeling) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
				//heeft een email en telefoon
				pstmt = con.prepareStatement(
				"insert into behandeling(bedrijf, naam, prijs, beschrijving, lengte, geslacht) values( "
				+ "'"+behandeling.getBedrijf().getEmail()+"', "
				+ "'"+behandeling.getNaam()+"', "
				+ ""+behandeling.getPrijs()+", "
				+ "'"+behandeling.getBeschrijving()+"', "
				+ "'"+ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm")+"', "
				+ "'"+behandeling.getGeslacht()+"')");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Behandeling> getTop5Behandelingen(Bedrijf bedrijf, Date date) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT count(b.id) as hoeveelheid, b.id, b.naam \n" + 
					"from behandeling b join afspraakbehandeling ab on b.id = ab.behandeling \n" + 
					"				   join afspraak a on a.id = ab.afspraak\n" + 
					"where b.bedrijf = '"+bedrijf.getEmail()+"' \n" + 
					"      and a.timestamp < SYSDATE()\n" + 
					"      and a.timestamp > '"+ServiceFilter.DateToStringFormatter(date, "YYYY-MM-dd")+"'\n" + 
					"GROUP by b.id\n" + 
					"ORDER by COUNT(b.id) desc\n" + 
					"LIMIT 5;");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				
				int id = dbResultSet.getInt("id");
				int count = dbResultSet.getInt("hoeveelheid");
				String behandelingsNaam = dbResultSet.getString("naam");
				
				
				Behandeling behandeling = new Behandeling(id, behandelingsNaam,count);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return behandelingen;
	}

}
