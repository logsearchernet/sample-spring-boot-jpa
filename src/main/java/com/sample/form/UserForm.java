package com.sample.form;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.sample.model.UserRoleId;

public class UserForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9051532887303284192L;
	@Size(min=6, max=30)
	@Email
	private String email;
	@Size(min=6, max=12)
	private String password;
	private boolean enabled;
	
	private Set<String> roles;
	
	public UserForm() {
		super();
	}
	
	public UserForm(String email, boolean enabled, Set<String> roles) {
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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
