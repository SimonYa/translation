package org.lwj.vo;

import java.io.Serializable;

public class UpdatePassVo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2661096446230531188L;

	private String password;

	private String mobile;
	
	private String validateCode;

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
