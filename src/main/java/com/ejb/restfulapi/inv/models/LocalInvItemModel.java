package com.ejb.restfulapi.inv.models;

import java.io.Serializable;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "InvItemModel")
@Table(name = "INV_ITM")
public class LocalInvItemModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "II_CODE", nullable = false)
	@JsonProperty("id")
	private java.lang.Integer iiCode;

	@Column(name = "II_NM")
	@JsonProperty("itemName")
	private java.lang.String iiName;

	@Column(name = "II_DESC")
	@JsonProperty("description")
	private java.lang.String iiDescription;

	@Column(name = "II_ENBL")
	@JsonProperty("enable")
	private byte iiEnable;

	@Column(name = "II_AD_CMPNY")
	@JsonProperty("companyId")
	private java.lang.Integer iiAdCompany;
	
	@JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
	@ManyToOne
	@JsonProperty("unitOfMeasure")
	private LocalInvUnitOfMeasureModel invUnitOfMeasure;

	public java.lang.Integer getIiCode() {
		return iiCode;
	}

	public void setIiCode(java.lang.Integer iiCode) {
		this.iiCode = iiCode;
	}

	public java.lang.String getIiName() {
		return iiName;
	}

	public void setIiName(java.lang.String iiName) {
		this.iiName = iiName;
	}

	public java.lang.String getIiDescription() {
		return iiDescription;
	}

	public byte getIiEnable() {
		return iiEnable;
	}

	public void setIiEnable(byte iiEnable) {
		this.iiEnable = iiEnable;
	}

	public java.lang.Integer getIiAdCompany() {
		return iiAdCompany;
	}

	public void setIiAdCompany(java.lang.Integer iiAdCompany) {
		this.iiAdCompany = iiAdCompany;
	}
	
	public LocalInvUnitOfMeasureModel getInvUnitOfMeasure() {
		return invUnitOfMeasure;
	}

	public void setInvUnitOfMeasure(LocalInvUnitOfMeasureModel invUnitOfMeasure) {
		this.invUnitOfMeasure = invUnitOfMeasure;
	}
}