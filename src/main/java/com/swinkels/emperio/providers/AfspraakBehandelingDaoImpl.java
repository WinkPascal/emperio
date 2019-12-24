package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.support.DatabaseDateAdapter;

public class AfspraakBehandelingDaoImpl extends MariadbBaseDao implements AfspraakBehandelingDao{

	public void saveAfspraakBehandeling(Behandeling behandeling, Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
				pstmt = con.prepareStatement("insert into afspraakBehandeling(afspraakId, behandelingId, prijs, lengte, naam, beschrijving) "
						+ "values("+afspraak.getId()+", "+behandeling.getId()+", "+behandeling.getPrijs()+", "
						+ "'"+DatabaseDateAdapter.DateToString(behandeling.getLengte(), "HH:mm")+"', "
						+ "'"+behandeling.getNaam()+"', '"+behandeling.getBeschrijving()+"')");
			System.out.println(pstmt);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
