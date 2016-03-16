package com.sample.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "sample_user_roles")
public class UserRolesEntity  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1263650936812915617L;

	@EmbeddedId
	private UserRoleId userRoleId;

	public UserRoleId getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(UserRoleId userRoleId) {
		this.userRoleId = userRoleId;
	}

	
	
}
