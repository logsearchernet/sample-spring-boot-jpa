package com.sample.form;

import java.io.Serializable;

import javax.validation.constraints.Size;

public class FileForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size(min=2, max=30)
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
