package com.swinkels.emperio.security;

public interface UserDao {
	public String findRoleForUser(String name, String pass);
}