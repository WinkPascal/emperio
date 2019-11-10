package com.swinkels.emperio.objects;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.swinkels.emperio.providers.MariadbBaseDao;

public class UserMariadbDaoImpl extends MariadbBaseDao implements UserDao {

	public String findRoleForUser(String name, String pass) {
		String result = null;
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("select * from bedrijf " + "where email= '"
					+ name + "' and wachtwoord= '" + pass + "'");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				System.out.println(dbResultSet.getString("role"));
				result = dbResultSet.getString("role");
			}
		} catch (SQLException e) {
			System.out.println(" sadasds");
			e.printStackTrace();
		}
		return result;
	}
}
