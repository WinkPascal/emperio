package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.service.ServiceFilter;

public class BedrijfDaoImpl extends MariadbBaseDao implements BedrijfDao{
	
	
	public ArrayList<Date> getDagTijden(Bedrijf bedrijf, Date date){
		ArrayList<Date> dagTijden = new ArrayList<Date>();
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		System.out.println(dayOfWeek);
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT openingstijd, sluitingstijd"
				 + " FROM `dag` "
				 + "WHERE bedrijf ='"+bedrijf.getEmail()+"' "
				 + "and dag = "+dayOfWeek+"; ");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				String sluitingsTijd = dbResultSet.getString("sluitingstijd");
				
				Date openingsTijdDate = ServiceFilter.StringToDateFormatter(openingsTijd, "HH:mm");
				Date sluitingsTijdDate = ServiceFilter.StringToDateFormatter(sluitingsTijd, "HH:mm");
				
				dagTijden.add(openingsTijdDate);
				dagTijden.add(sluitingsTijdDate);
			}
		} catch (SQLException e) {
			return null;
		}
		return dagTijden;
	}
}
