package com.lxf.weatherdemo.model;

public class City {
	private int id;
	private String code;
	private String name;
	private int province;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the province
	 */
	public int getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(int province) {
		this.province = province;
	}
}
