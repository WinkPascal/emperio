package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.service.ServiceFilter;

public class AfspraakDaoImpl extends MariadbBaseDao implements AfspraakDao {

	public ArrayList<Afspraak> getAfsprakenBetweenDates(Date beginDate, Date eindDate, Bedrijf bedrijf) {
		ArrayList<Afspraak> afsprakenVandaag = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select a.id, a.timestamp, k.naam as klantnaam, \n" + 
					"       b.naam as behandelingnaam, b.lengte, b.prijs\n" + 
					"from klant k join afspraak a on a.klant = k.id \n" + 
					"             join afspraakbehandeling ab on ab.afspraak = a.id \n" + 
					"             join bedrijf m on k.bedrijf = m.email \n" + 
					"             join behandeling b on b.id = ab.behandeling \n" + 
					"where m.email = '"+bedrijf.getEmail()+"' AND \n" + 
					"timestamp BETWEEN '"+ServiceFilter.DateToStringFormatter(beginDate, "yyyy-MM-dd")+"' AND "
							        + "'"+ServiceFilter.DateToStringFormatter(eindDate, "yyyy-MM-dd")+"'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//Behandeling
				String behandelingnaam = dbResultSet.getString("behandelingnaam");
				Date lengte = ServiceFilter.StringToDateFormatter(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");
				
				Behandeling behandeling = new Behandeling(behandelingnaam, lengte, prijs);

				//Afspraak
				int id = dbResultSet.getInt("id");
				if(afsprakenVandaag.size() == 0){
					//Dit is de eerste afspraak die wordt aangemaakt
					String timestampString = dbResultSet.getString("timestamp");
					timestampString = timestampString.substring(0, timestampString.length()-5);
					Date timestampDate =  ServiceFilter.StringToDateFormatter(timestampString, "yyyy-MM-dd HH:mm");
					
					//klant
					String klantNaam = dbResultSet.getString("klantNaam");
					Klant klant = new Klant(klantNaam);
					
					
					Afspraak newAfspraak = new Afspraak(id, timestampDate, klant);
					newAfspraak.addBehandeling(behandeling);

					afsprakenVandaag.add(newAfspraak);
				} else {
					boolean nietAlleenBehandeling = true;
					for(Afspraak afspraak : afsprakenVandaag) {
						//check of de afspraak al is opghehaalt
						if(afspraak.getId() == id) {
							//alleen de behandeling wordt toegevoegd
							nietAlleenBehandeling = false;
							afspraak.addBehandeling(behandeling);
						}
					}
					if(nietAlleenBehandeling) {
						String timestampString = dbResultSet.getString("timestamp");
						timestampString = timestampString.substring(0, timestampString.length()-5);
						Date timestamp = ServiceFilter.StringToDateFormatter(timestampString,"yyyy-MM-dd HH:mm");
						//klant
						String klantNaam = dbResultSet.getString("klantNaam");
						Klant klant = new Klant(klantNaam);
					
						Afspraak newAfspraak = new Afspraak(id, timestamp, klant);
						newAfspraak.addBehandeling(behandeling);

						afsprakenVandaag.add(newAfspraak);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return afsprakenVandaag;
	}

	public boolean setAfspraak(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
			"insert into afspraak(klant, timestamp) values("
			+ "'"+afspraak.getKlant().getId()+"', "
			+ "'"+ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"')");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Afspraak getAfspraakId(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from afspraak "
					+ "where klant = '"+afspraak.getKlant().getId()+"' "
					+ "and timestamp = '"+ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//afspraak
				int id = dbResultSet.getInt("id");
				
				afspraak.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return afspraak;
	}

	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date beginDate){
		ArrayList<Afspraak> afspraken = new ArrayList<Afspraak>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);            
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		Date eindDate = calendar.getTime();
		
		String beginDateString = ServiceFilter.DateToStringFormatter(beginDate, "yyyy-MM-dd");
		String eindDateString = ServiceFilter.DateToStringFormatter(eindDate, "yyyy-MM-dd");
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("SELECT * " + 
					"FROM afspraak " + 
					"WHERE timestamp " + 
					"BETWEEN '"+beginDateString+"' AND " + 
					"'"+eindDateString+"' " + 
					"order by timestamp ");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//afspraak
		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return afspraken;
	}

	public Afspraak getAfspraak(Bedrijf bedrijf, int afspraakId) {
		try (Connection con = super.getConnection()) {
			Afspraak afspraak = new Afspraak();

			PreparedStatement pstmt = con.prepareStatement( 
					"select a.id, a.timestamp, k.naam as klantnaam, \n" + 
					"       b.naam as behandelingnaam, b.lengte, b.prijs\n" + 
					"from klant k join afspraak a on a.klant = k.id \n" + 
					"             join afspraakbehandeling ab on ab.afspraak = a.id \n" + 
					"             join bedrijf m on k.bedrijf = m.email \n" + 
					"             join behandeling b on b.id = ab.behandeling \n" + 
					"where m.email = '"+bedrijf.getEmail()+"' "+ 
					"and a.id = "+afspraakId);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//Behandeling
				String behandelingnaam = dbResultSet.getString("behandelingnaam");
				Date lengte = ServiceFilter.StringToDateFormatter(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");
				
				Behandeling behandeling = new Behandeling(behandelingnaam, lengte, prijs);

				//Afspraak
				int id = dbResultSet.getInt("id");
				int i = 1;
				if(i == 1) {
					// de klant en afspraak met de eerste behandeling wordt aangemaakt
					String timestampString = dbResultSet.getString("timestamp");
					timestampString = timestampString.substring(0, timestampString.length()-5);
					Date timestamp = ServiceFilter.StringToDateFormatter(timestampString,"yyyy-MM-dd HH:mm");
					//klant
					String klantNaam = dbResultSet.getString("klantNaam");
					Klant klant = new Klant(klantNaam);
				
					afspraak = new Afspraak(id, timestamp, klant);
					afspraak.addBehandeling(behandeling);
					i++;
				} else {
					afspraak.addBehandeling(behandeling);
				}
			}
			return afspraak;
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
