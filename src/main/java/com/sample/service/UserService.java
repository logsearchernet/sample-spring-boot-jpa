package com.sample.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sample.dao.UserDao;
import com.sample.dao.UserRoleDao;
import com.sample.form.UserForm;
import com.sample.model.UserRoleId;
import com.sample.model.UserRolesEntity;
import com.sample.model.UsersEntity;

@Component
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<UserForm> findUser(int pageNumber, int pageSize){
		
		List<UserForm> formList = new ArrayList<UserForm>();
		List<UsersEntity> userList = userDao.loadPaginationHere(pageNumber, pageSize);
		for (UsersEntity user : userList) {
			String email = user.getEmail();
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("email", email);
			List<UserRolesEntity> userRoleList = userRoleDao.findWithNamedQuery("userRole.findByEmail", mapParam);
			Set<String> userRoleSet = new HashSet<String>();
			for (UserRolesEntity userRolesEntity : userRoleList) {
				userRoleSet.add(userRolesEntity.getUserRoleId().getRole());
			}
			
			UserForm form = new UserForm(email, user.isEnabled(), userRoleSet);
			formList.add(form);
		}
		return formList;
	}
	
	public UsersEntity findEntity4UserValidate(String email, String password){
		UsersEntity user = userDao.find(email);
		String dbPassword = user.getPassword();
		if (passwordEncoder.matches(password, dbPassword)){
			return user;
		}
		return null;
		/*password = passwordEncoder.encode(password);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("password", password);
		return userDao.findSingleWithNamedQuery("user.findByEmailAndPassword", params);*/
	}
	
	public UsersEntity findEntityById(String email) { 
		return userDao.find(email);
	}

	public UserForm findById(String email) {
		UsersEntity user = userDao.find(email);
		if (user != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			List<UserRolesEntity> userRoleList = userRoleDao.findWithNamedQuery("userRole.findByEmail", params);
					
			Set<String> userRoleSet = new HashSet<String>();
			for (UserRolesEntity userRolesEntity : userRoleList) {
				UserRoleId userRoleId = userRolesEntity.getUserRoleId();
				userRoleSet.add(userRoleId.getRole());
			}
			UserForm form = new UserForm(email, user.isEnabled(), userRoleSet);
			
			return form;
		}
		return null;
	}
	
	@Transactional
	public void save(UserForm form){
		String password = form.getPassword();
		String email = form.getEmail();
		boolean enabled = form.isEnabled();
		Set<String> roles = form.getRoles();
		
		// Handle UsersEntity
		UsersEntity user = userDao.find(email);
		if (user == null) {
			user = new UsersEntity();
			user.setEmail(email);
			password = passwordEncoder.encode(password);
			user.setPassword(password);
			user.setEnabled(enabled);
			
			userDao.create(user);
		} else {
			user.setEmail(email);
			user.setEnabled(enabled);
			
			userDao.update(user);
		}
		
		// Handle UserRolesEntity
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		userRoleDao.deleteWithNamedQuery("userRole.findByEmail", params);
		for (String role : roles) {
			UserRoleId userRoleId = new UserRoleId(email, role);
			UserRolesEntity userRole = new UserRolesEntity();
			userRole.setUserRoleId(userRoleId);
			userRoleDao.create(userRole);
		}
		
	}

	@Transactional
	public void updateUserEntity(UsersEntity user) {
		String password = user.getPassword();
		password = passwordEncoder.encode(password);
		user.setPassword(password);
		userDao.update(user);
	}
}
