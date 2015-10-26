package com.siyuan.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Garden {
	
	private String name;
	
	private String address;
	
	public Garden() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
