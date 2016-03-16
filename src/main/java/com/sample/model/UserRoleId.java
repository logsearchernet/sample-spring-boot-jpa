package com.sample.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserRoleId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4421873166334057465L;
	private String email;
	private String role;
	
	public UserRoleId(){
		super();
	}
	
	public UserRoleId(String email, String role) {
		super();
		this.email = email;
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
        if (!(obj instanceof UserRoleId)) return false;
        if (obj == null) return false;
        UserRoleId pk = (UserRoleId) obj;
        return pk.email.equals(email) && pk.role.equals(role);
	}

	@Override
	public int hashCode() {
		return (int) email.hashCode() + role.hashCode();
	}
}
