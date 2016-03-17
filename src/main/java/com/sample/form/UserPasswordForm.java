package com.sample.form;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.sample.model.UserRoleId;

public class UserPasswordForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9051532887303284192L;
	@Size(min=6, max=30)
	@Email
	private String email;
	@Size(min=6, max=12)
	private String password;
	@Size(min=6, max=12)
	private String password2;
	@Size(min=6, max=12)
	private String passwordOld;
	
	public UserPasswordForm() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}
	
	
}
