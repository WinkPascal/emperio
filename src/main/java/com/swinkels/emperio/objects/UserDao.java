package com.swinkels.emperio.objects;

public interface UserDao {
	public String findRoleForUser(String name, String pass);
}