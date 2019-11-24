package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.Dag;

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
				  "insert into dag (bedrijf, dag, openingstijd, sluitingstijd) \n "
				+ "values('"+dag.getBedrijf().getEmail()+"', "+dag.getDag()+", "+openingsTijd+", "+sluitingsTijd+")");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
}
