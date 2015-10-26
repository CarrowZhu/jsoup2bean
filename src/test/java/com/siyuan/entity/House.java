package com.siyuan.entity;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class House {
	
	private Garden garden;
	
	private String title;
	
	private String price;
	
	private String totalPrice;
	
	private int roomCount;
	
	private int livingCount;
	
	private double area;
	
	private int floor;
	
	private String decoration;
	
	private String direction;
	
	private String type;
	
	private String bigArea;
	
	private String smallArea;
	
	private Date buildTime;
	
	public House() {
		
	}

	public Garden getGarden() {
		return garden;
	}

	public void setGarden(Garden garden) {
		this.garden = garden;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public int getLivingCount() {
		return livingCount;
	}

	public void setLivingCount(int livingCount) {
		this.livingCount = livingCount;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBigArea() {
		return bigArea;
	}

	public void setBigArea(String bigArea) {
		this.bigArea = bigArea;
	}

	public String getSmallArea() {
		return smallArea;
	}

	public void setSmallArea(String smallArea) {
		this.smallArea = smallArea;
	}

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
