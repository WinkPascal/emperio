package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.service.ServiceFilter;

public class AfspraakBehandelingDaoImpl extends MariadbBaseDao implements AfspraakBehandelingDao{

	public boolean saveAfspraakBehandeling(Behandeling behandeling, Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
				//heeft een email en telefoon
				pstmt = con.prepareStatement("insert into afspraakbehandeling(afspraak, behandeling) "
						+ "values("+afspraak.getId()+", "+behandeling.getId()+")");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
