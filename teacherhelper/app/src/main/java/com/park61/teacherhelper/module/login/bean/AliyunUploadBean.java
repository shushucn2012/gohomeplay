package com.park61.teacherhelper.module.login.bean;

import java.io.Serializable;

public class AliyunUploadBean implements Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 4526617287439222620L;
	private String key;
	private String contentType;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
