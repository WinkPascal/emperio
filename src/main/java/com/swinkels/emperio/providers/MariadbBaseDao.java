package com.swinkels.emperio.providers;

import java.sql.*;

public class MariadbBaseDao {
	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mariadb://localhost/emperio", "root", "PascalWink1!");
			//result = DriverManager.getConnection("jdbc:mariadb://localhost/emperio", "root", "PascalWink1!");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return connection;
	}
}