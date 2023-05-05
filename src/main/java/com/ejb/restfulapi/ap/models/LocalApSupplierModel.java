package com.ejb.restfulapi.ap.models;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import com.ejb.entities.ad.LocalAdBranchSupplier;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "ApSupplierModel")
@Table(name = "AP_SPPLR")
public class LocalApSupplierModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "SPL_CODE", nullable = false)
	@JsonProperty("id")
	private java.lang.Integer splCode;

	@Column(name = "SPL_SPPLR_CODE")
	@JsonProperty("supplierCode")
	private java.lang.String splSupplierCode;

	@Column(name = "SPL_NM")
	@JsonProperty("name")
	private java.lang.String splName;
	
	@Column(name = "SPL_ENBL")
	@JsonProperty("enable")
	private byte splEnable;
	
	@Column(name = "SPL_AD_CMPNY")
	@JsonProperty("companyId")
	private java.lang.Integer splAdCompany;
	
	@OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
	private Collection<LocalAdBranchSupplier> adBranchSuppliers = new java.util.ArrayList<>();
	
	public java.lang.Integer getSplCode() {
		return splCode;
	}

	public void setSplCode(java.lang.Integer splCode) {
		this.splCode = splCode;
	}

	public java.lang.String getSplSupplierCode() {
		return splSupplierCode;
	}

	public void setSplSupplierCode(java.lang.String splSupplierCode) {
		this.splSupplierCode = splSupplierCode;
	}

	public java.lang.String getSplName() {
		return splName;
	}

	public void setSplName(java.lang.String splName) {
		this.splName = splName;
	}
	
	public byte getSplEnable() {
		return splEnable;
	}

	public void setSplEnable(byte splEnable) {
		this.splEnable = splEnable;
	}
	
	public java.lang.Integer getSplAdCompany() {
		return splAdCompany;
	}

	public void setSplAdCompany(java.lang.Integer splAdCompany) {
		this.splAdCompany = splAdCompany;
	}
	
	@XmlTransient
	public Collection<LocalAdBranchSupplier> getAdBranchSuppliers() {
		return adBranchSuppliers;
	}

	public void setAdBranchSuppliers(Collection<LocalAdBranchSupplier> adBranchSuppliers) {
		this.adBranchSuppliers = adBranchSuppliers;
	}
}