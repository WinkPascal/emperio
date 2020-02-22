package com.swinkels.emperio.providers.Email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.email.Email;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.providers.Email.KlantEmailDao;
import com.swinkels.emperio.providers.MariadbBaseDao;

public class KlantEmailDaoImpl extends MariadbBaseDao implements KlantEmailDao {

	@Override
	public void save(Email email, Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			// heeft een email en telefoon
			pstmt = con.prepareStatement("insert into klantEmail (klantId, emailId) \n"
					+ "values("+klant.getId()+","+email.getId()+")");
			System.out.println(pstmt);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		}		
	}
}
