package com.ejb.restfulapi.inv.models;

import java.io.Serializable;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "InvLocationModel")
@Table(name = "INV_LCTN")
public class LocalInvLocationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "LOC_CODE", nullable = false)
	@JsonProperty("id")
	private java.lang.Integer locCode;

	@Column(name = "LOC_NM")
	@JsonProperty("locationName")
	private java.lang.String locName;

	@Column(name = "LOC_LV_TYP")
	@JsonProperty("type")
	private java.lang.String locLvType;

	@Column(name = "LOC_AD_CMPNY")
	@JsonProperty("companyId")
	private java.lang.Integer locAdCompany;

	public java.lang.Integer getLocCode() {
		return locCode;
	}

	public void setLocCode(java.lang.Integer locCode) {
		this.locCode = locCode;
	}

	public java.lang.String getLocName() {
		return locName;
	}

	public void setLocName(java.lang.String locName) {
		this.locName = locName;
	}

	public java.lang.String getLocLvType() {
		return locLvType;
	}

	public void setLocLvType(java.lang.String locLvType) {
		this.locLvType = locLvType;
	}

	public java.lang.Integer getLocAdCompany() {
		return locAdCompany;
	}

	public void setLocAdCompany(java.lang.Integer locAdCompany) {
		this.locAdCompany = locAdCompany;
	}
}