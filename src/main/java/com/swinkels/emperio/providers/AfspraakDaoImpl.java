package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.service.ServiceFilter;

public class AfspraakDaoImpl extends MariadbBaseDao implements AfspraakDao {

	public ArrayList<Afspraak> getAfsprakenVandaag(Date date, Bedrijf bedrijf) {
		ArrayList<Afspraak> afsprakenVandaag = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select a.id, a.timestamp, k.naam as klantnaam, \n" + 
					"       b.naam as behandelingnaam, b.lengte, b.prijs\n" + 
					"from klant k join afspraak a on a.klant = k.id \n" + 
					"             join afspraakbehandeling ab on ab.afspraak = a.id \n" + 
					"             join bedrijf m on k.bedrijf = m.email \n" + 
					"             join behandeling b on b.id = ab.behandeling \n" + 
					"where DATE(timestamp) = '"+ServiceFilter.DateToStringFormatter(date, "yyyy-MM-dd")+"' \n" + 
					"  and m.email = '"+bedrijf.getEmail()+"'");
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
					for(Afspraak afspraak : afsprakenVandaag) {

						if(afspraak.getId() == id) {
							//afspraak bestaat al
							//alleen de behandeling wordt toegevoegd
							afspraak.addBehandeling(behandeling);
						} else {
							//afspraak wordt aangemaakt
							String timestampString = dbResultSet.getString("timestamp");
							
							//klant
							String klantNaam = dbResultSet.getString("klantNaam");
							Klant klant = new Klant(klantNaam);
						
							Afspraak newAfspraak = new Afspraak(id, ServiceFilter.StringToDateFormatter(timestampString,"yyyy-MM-dd HH-mm"), klant);

							afsprakenVandaag.add(newAfspraak);
						}
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
			System.out.println(pstmt);
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
					"select * from afspraak "
					+ "where klant = '"+afspraak.getKlant().getId()+"' "
					+ "and timestamp = '"+ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"'");
			System.out.println(pstmt);
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

	public ArrayList<Afspraak> getOpenPlekken(Date date, String behandelingen) {
		// TODO Auto-generated method stub
		return null;
	}
}
