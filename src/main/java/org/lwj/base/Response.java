package org.lwj.base;
import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String code = "200";
	private String message = "success";
	private Object data = null;
	private List dataList = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public Response() {

	}
	
	public Response(String code, String message, Object data,List dataList) {
		this.code = code;
		this.message = message;
		this.data = data;
		this.dataList=dataList;
	}

}
