package com.sample.form;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.sample.model.UserRolesEntity;

public class UserForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9051532887303284192L;
	@Size(min=6, max=30)
	@Email
	private String email;
	private Boolean enabled;
	@NotEmpty
	private Set<UserRolesEntity> roles;
	
	public UserForm() {
		super();
	}
	
	public UserForm(String email, Boolean enabled, Set<UserRolesEntity> roles) {
		super();
		this.email = email;
		this.enabled = enabled;
		this.roles = roles;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Set<UserRolesEntity> getRoles() {
		return roles;
	}
	public void setRoles(Set<UserRolesEntity> roles) {
		this.roles = roles;
	}
}
