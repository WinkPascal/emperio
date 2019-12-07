package com.swinkels.emperio.providers;

public interface EmailDao {
	public void getEmailsByRequest(String request, int page);
}
