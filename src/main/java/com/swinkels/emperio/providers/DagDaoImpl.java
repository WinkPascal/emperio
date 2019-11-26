package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.service.ServiceFilter;

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
	
	
	//wordt gebruikt bij: getWerkdagen
	public void getWeekRooster(Bedrijf bedrijf){
		ArrayList<Dag> dagen = new ArrayList<Dag>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT dagnummer, openingstijd, sluitingstijd"
				 + " FROM dag "
				 + "WHERE BedrijfBedrijfsnaam ='"+bedrijf.getBedrijfsNaam()+"' "
				 + "ORDER BY dagnummer");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				if(openingsTijd == null) {
					System.out.println(openingsTijd);
					continue;
				} else {
					String sluitingsTijd = dbResultSet.getString("sluitingstijd");
					int dagNummer = dbResultSet.getInt("dagnummer");
				
					//format de tijden
					Date openingsTijdDate = ServiceFilter.StringToDateFormatter(openingsTijd, "HH:mm");
					Date sluitingsTijdDate = ServiceFilter.StringToDateFormatter(sluitingsTijd, "HH:mm");
					
					System.out.println(dagNummer);
					System.out.println(openingsTijdDate);
					System.out.println(sluitingsTijdDate);
	
					Dag  dag = new Dag(dagNummer, openingsTijdDate, sluitingsTijdDate);
					System.out.println(dag);
					bedrijf.addDag(dag);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}		
	}
}
