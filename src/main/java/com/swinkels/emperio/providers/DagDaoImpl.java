package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;

public class DagDaoImpl extends MariadbBaseDao implements DagDao {

	public Dag getTijdenFromDate(Date datum, Bedrijf bedrijf) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(datum);
		int weekNummer = cal.get(Calendar.WEEK_OF_YEAR);
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
			  "select openingstijd, sluitingstijd "
			+ "from dag where dag = "+weekNummer+" "
			+ "and bedrijf = '"+bedrijf.getEmail()+"')");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String openingsTijd = dbResultSet.getString("openingstijd");
				String sluitingsTijd = dbResultSet.getString("sluitingstijd");
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				Date openingsTijdDate = format.parse(openingsTijd);
				Date sluitingsTijdDate = format.parse(sluitingsTijd);

				Dag dag = new Dag(openingsTijdDate, sluitingsTijdDate);
				return dag;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
