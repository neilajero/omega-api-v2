package com.ejb.restfulapi.ad.models;

import java.io.Serializable;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "AdBranchModel")
@Table(name = "AD_BRNCH")
public class LocalAdBranchModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "BR_CODE", nullable = false)
	@JsonProperty("id")
	private java.lang.Integer brCode;

	@Column(name = "BR_BRNCH_CODE")
	@JsonProperty("branchCode")
	private java.lang.String brBranchCode;

	@Column(name = "BR_NM")
	@JsonProperty("name")
	private java.lang.String brName;

	@Column(name = "BR_HD_QTR")
	@JsonProperty("headQuarters")
	private byte brHeadQuarter;

	@Column(name = "BR_AD_CMPNY")
	@JsonIgnore
	private java.lang.Integer brAdCompany;

	public java.lang.Integer getBrCode() {
		return brCode;
	}

	public void setBrCode(java.lang.Integer brCode) {
		this.brCode = brCode;
	}

	public java.lang.String getBrBranchCode() {
		return brBranchCode;
	}

	public void setBrBranchCode(java.lang.String brBranchCode) {
		this.brBranchCode = brBranchCode;
	}

	public java.lang.String getBrName() {
		return brName;
	}

	public void setBrName(java.lang.String brName) {
		this.brName = brName;
	}

	public byte getBrHeadQuarter() {
		return brHeadQuarter;
	}

	public void setBrHeadQuarter(byte brHeadQuarter) {
		this.brHeadQuarter = brHeadQuarter;
	}

	public java.lang.Integer getBrAdCompany() {
		return brAdCompany;
	}

	public void setBrAdCompany(java.lang.Integer brAdCompany) {
		this.brAdCompany = brAdCompany;
	}
}