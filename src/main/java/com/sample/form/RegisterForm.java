package com.sample.form;

import java.io.Serializable;

import javax.validation.constraints.Size;

public class RegisterForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8420023432886035322L;
	@Size(min=6, max=30)
	private String email;
	@Size(min=6, max=12)
	private String password;
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
	
}
