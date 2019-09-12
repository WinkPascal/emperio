package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.swinkels.emperio.objects.Behandeling;

public class BehandelingDaoImpl extends MariadbBaseDao implements BehandelingDao{
	public ArrayList<Behandeling> behandelingenByGeslacht(String geslacht, String bedrijf){
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select * from behandeling where geslacht = '"+geslacht+"' and bedrijf = '"+bedrijf+"'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				String behandelingsNaam = dbResultSet.getString("naam");
				String beschrijving = dbResultSet.getString("beschrijving");
				String lengte = dbResultSet.getString("lengte");
				double prijs = dbResultSet.getDouble("prijs");

				Behandeling behandeling = new Behandeling(id, behandelingsNaam, beschrijving, lengte, prijs);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return behandelingen;
	}

}
