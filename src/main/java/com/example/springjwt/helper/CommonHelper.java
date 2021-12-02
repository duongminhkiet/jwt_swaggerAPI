package com.example.springjwt.helper;

import com.example.springjwt.SpringJwtApplication;
import com.example.springjwt.entities.User;

public class CommonHelper {
	public static boolean isExistingUser(User user) {
		if(SpringJwtApplication.listUserGlobal != null && SpringJwtApplication.listUserGlobal.size()>0) {
			for(User u : SpringJwtApplication.listUserGlobal) {
				if(u.getUsername().equalsIgnoreCase(user.getUsername())) {
					return true;
				}
			}
		}
		return false;
	}
	public static User getUserByUserName(String userName) {
		if(SpringJwtApplication.listUserGlobal != null && SpringJwtApplication.listUserGlobal.size()>0) {
			for(User u : SpringJwtApplication.listUserGlobal) {
				if(u.getUsername().equalsIgnoreCase(userName)) {
					return u;
				}
			}
		}
		return null;
	}
}
