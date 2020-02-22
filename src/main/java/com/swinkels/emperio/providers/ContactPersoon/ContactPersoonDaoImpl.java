package com.swinkels.emperio.providers.ContactPersoon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.security.ContactPersoon;
import com.swinkels.emperio.providers.MariadbBaseDao;

public class ContactPersoonDaoImpl extends MariadbBaseDao implements ContactPersoonDao {

	public boolean save(ContactPersoon contactPersoon) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"insert into contactPersoon "
					+ "values('"+contactPersoon.getBedrijf().getBedrijfsNaam()+"', "
					+ "'"+contactPersoon.getVoornaam()+"', '"+contactPersoon.getAchternaam()+"', "
					+ "'"+contactPersoon.getRekeningnummer()+"', '"+contactPersoon.getTelefoonnummer()+"', "
					+ "'"+contactPersoon.getEmailadres()+"' )");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
