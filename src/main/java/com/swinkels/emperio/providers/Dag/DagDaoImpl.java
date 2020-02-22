package com.swinkels.emperio.providers.Dag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.providers.Dag.DagDao;
import com.swinkels.emperio.providers.MariadbBaseDao;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.DatabaseDateAdapter;

public class DagDaoImpl extends MariadbBaseDao implements DagDao {
	public boolean saveDag(Dag dag) {
		try (Connection con = super.getConnection()) {
			String openingsTijd;
			String sluitingsTijd;
			if(dag.getOpeningsTijd() == null) {
				openingsTijd = "null";
				sluitingsTijd = "null";
			} else {
				openingsTijd = "'"+dag.getOpeningsTijd().getHours()+":"+dag.getOpeningsTijd().getMinutes()+"'";
				sluitingsTijd = "'"+dag.getSluitingsTijd().getHours()+":"+dag.getSluitingsTijd().getMinutes()+"'";				
			}

			PreparedStatement pstmt = con.prepareStatement(
				  "insert into dag (BedrijfBedrijfsnaam, dagnummer, openingstijd, sluitingstijd) \n "
				+ "values('"+dag.getBedrijf().getBedrijfsNaam()+"', "+dag.getDag()+", "+openingsTijd+", "+sluitingsTijd+")");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
	
	public boolean updateDag(Dag dag) {
		try (Connection con = super.getConnection()) {
			String openingsTijd;
			String sluitingsTijd;
			if(dag.getOpeningsTijd() == null) {
				openingsTijd = "null";
				sluitingsTijd = "null";
			} else {
				openingsTijd = "'"+dag.getOpeningsTijd().getHours()+":"+dag.getOpeningsTijd().getMinutes()+"'";
				sluitingsTijd = "'"+dag.getSluitingsTijd().getHours()+":"+dag.getSluitingsTijd().getMinutes()+"'";				
			}

			PreparedStatement pstmt = con.prepareStatement(
					"UPDATE dag \n" + 
					"SET openingstijd = "+openingsTijd+", \n"
					+ "sluitingstijd = "+sluitingsTijd+" \n"  
					+ "WHERE BedrijfBedrijfsnaam = '"+dag.getBedrijf().getBedrijfsNaam()+"' \n "
					+ "AND dagnummer = "+dag.getDag());
					
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
	
	//wordt gebruikt bij: getWerkdagen
	public List<Dag> getWeekRooster(){
		List<Dag> dagen = new ArrayList<Dag>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT dagnummer, openingstijd, sluitingstijd"
				 + " FROM dag "
				 + "WHERE BedrijfBedrijfsnaam ='"+Security.getKey()+"' "
				 + "ORDER BY dagnummer");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				if(openingsTijd == null) {
					int dagNummer = dbResultSet.getInt("dagnummer");
					Dag  dag = new Dag(dagNummer, null, null);
					dagen.add(dag);
				} else {
					String sluitingsTijd = dbResultSet.getString("sluitingstijd");
					int dagNummer = dbResultSet.getInt("dagnummer");				
					Date openingsTijdDate = DatabaseDateAdapter.StringToDate(openingsTijd, "HH:mm");
					Date sluitingsTijdDate = DatabaseDateAdapter.StringToDate(sluitingsTijd, "HH:mm");
					Dag  dag = new Dag(dagNummer, openingsTijdDate, sluitingsTijdDate);
					dagen.add(dag);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}		
		return dagen;
	}
	
	public void getOpeningsTijden(Bedrijf bedrijf, Date date){
		int dayOfWeek = Adapter.getDagNummerFromDate(date);
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT openingstijd, sluitingstijd"
				 + " FROM `dag` "
				 + "WHERE  	BedrijfBedrijfsnaam ='"+bedrijf.getBedrijfsNaam()+"' "
				 + "and dagnummer = "+dayOfWeek+" "
				 + "ORDER BY dagnummer;");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				String sluitingsTijd = dbResultSet.getString("sluitingstijd");
				if(openingsTijd != null) {
					Date openingsTijdDate = DatabaseDateAdapter.StringToDate(openingsTijd, "HH:mm");
					Date sluitingsTijdDate = DatabaseDateAdapter.StringToDate(sluitingsTijd, "HH:mm");
					Dag dag = new Dag(openingsTijdDate, sluitingsTijdDate, bedrijf);
					bedrijf.addDag(dag);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
