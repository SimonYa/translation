package org.lwj.vo;

import java.io.Serializable;

public class InsertUserVo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6843164524422009836L;

	private String name;

	private String password;

	private String mobile;
	
	private String validateCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
}
