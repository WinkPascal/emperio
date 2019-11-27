package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;

public class AfspraakBehandelingDaoImpl extends MariadbBaseDao implements AfspraakBehandelingDao{

	public void saveAfspraakBehandeling(Behandeling behandeling, Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
				//heeft een email en telefoon
				pstmt = con.prepareStatement("insert into afspraakBehandeling(afspraakId, behandelingId) "
						+ "values("+afspraak.getId()+", "+behandeling.getId()+")");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
