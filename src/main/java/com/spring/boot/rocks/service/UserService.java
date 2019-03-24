package com.spring.boot.rocks.service;

import java.util.List;

import com.spring.boot.rocks.model.AppUser;

public interface UserService {
	AppUser findByUsername(String username);

	AppUser findByUserId(long userid);

	void save(AppUser user);

	List<AppUser> findAllUsers();

	void updateUser(AppUser user);

	void deleteUserByUsername(String emailid);

}
