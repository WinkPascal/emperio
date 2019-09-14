package com.swinkels.emperio.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceFilter {
	
	public static String emailCheck(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(mat.matches()){
        	return "email";
        }if(email.length() == 0){
        	return "leeg";
        } else {
            return "fout";	
        }
    }
	
	public static boolean phoneCheck(String phone) {
		if(phone.length() != 0) {
			return true;
		} else {
			return false;
		}
	}
}
