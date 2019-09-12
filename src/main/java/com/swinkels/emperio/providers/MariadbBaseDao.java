package com.swinkels.emperio.providers;

import java.sql.*;

public class MariadbBaseDao {
	// JDBC driver name and database URL

//	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
//	static final String DB_URL = "jdbc:mariadb://192.168.100.174/db";

	// Database credentials
//	static final String USER = "root";
//	static final String PASS = "PascalWink1!";

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