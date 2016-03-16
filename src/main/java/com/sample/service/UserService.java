package com.sample.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sample.dao.UserDao;
import com.sample.dao.UserRoleDao;
import com.sample.form.UserForm;
import com.sample.model.UserRolesEntity;
import com.sample.model.UsersEntity;

@Component
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	public List<UserForm> findUser(int pageNumber, int pageSize){
		
		List<UserForm> formList = new ArrayList<UserForm>();
		List<UsersEntity> userList = userDao.loadPaginationHere(pageNumber, pageSize);
		for (UsersEntity user : userList) {
			String email = user.getEmail();
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("email", email);
			List<UserRolesEntity> userRoleList = userRoleDao.findWithNamedQuery("userRole.findByEmail", mapParam);
			
			Set<UserRolesEntity> userRoleSet = new HashSet<UserRolesEntity>(userRoleList);
			UserForm form = new UserForm(email, user.isEnabled(), userRoleSet);
			formList.add(form);
		}
		return formList;
	}
}
