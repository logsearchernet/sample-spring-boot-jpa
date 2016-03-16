package com.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sample.dao.UserDao;
import com.sample.dao.UserRoleDao;
import com.sample.model.UserRoleId;
import com.sample.model.UserRolesEntity;
import com.sample.model.UsersEntity;

@Component
public class RegisterService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UsersEntity findByEmail(String email){
		return userDao.find(email);
	}
	
	@Transactional
	public void register(String email, String password, String role){
		
		password = passwordEncoder.encode(password);
		// Handle UsersEntity
		UsersEntity user = userDao.find(email);
		if (user == null) {
			user = new UsersEntity();
			user.setEmail(email);
			user.setPassword(password);
			user.setEnabled(true);
			
			userDao.create(user);
		} else {
			user.setEmail(email);
			user.setPassword(password);
			user.setEnabled(true);
			
			userDao.update(user);
		}
		
		// Handle UserRolesEntity
		UserRoleId userRoleId = new UserRoleId(email, role);
		UserRolesEntity userRole = userRoleDao.find(userRoleId);
		if (userRole == null){
			userRole = new UserRolesEntity();
			userRole.setUserRoleId(userRoleId);
			userRoleDao.create(userRole);
		} else {
			userRole.setUserRoleId(userRoleId);
			userRoleDao.update(userRole);
		}
	}
}
