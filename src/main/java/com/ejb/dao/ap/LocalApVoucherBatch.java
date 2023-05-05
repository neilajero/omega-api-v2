package com.ejb.dao.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.entities.ap.LocalApRecurringVoucher;
import com.ejb.entities.ap.LocalApVoucher;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity(name = "ApVoucherBatch")
@Table(name = "AP_VCHR_BTCH")
public class LocalApVoucherBatch extends NativeQueryHome implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "VB_CODE", nullable = false)
  private java.lang.Integer vbCode;

  @Column(name = "VB_NM")
  private java.lang.String vbName;

  @Column(name = "VB_DESC")
  private java.lang.String vbDescription;

  @Column(name = "VB_STATUS")
  private java.lang.String vbStatus;

  @Column(name = "VB_TYP")
  private java.lang.String vbType;

  @Column(name = "VB_DT_CRTD")
  private Date vbDateCreated;

  @Column(name = "VB_CRTD_BY")
  private java.lang.String vbCreatedBy;

  @Column(name = "VB_DPRTMNT")
  private java.lang.String vbDepartment;

  @Column(name = "VB_AD_BRNCH")
  private java.lang.Integer vbAdBranch;

  @Column(name = "VB_AD_CMPNY")
  private java.lang.Integer vbAdCompany;

  @OneToMany(mappedBy = "apVoucherBatch", fetch = FetchType.LAZY)
  private Collection<LocalApVoucher> apVouchers = new java.util.ArrayList<>();

  @OneToMany(mappedBy = "apVoucherBatch", fetch = FetchType.LAZY)
  private Collection<LocalApPurchaseOrder> apPurchaseOrders =
          new java.util.ArrayList<>();

  @OneToMany(mappedBy = "apVoucherBatch", fetch = FetchType.LAZY)
  private Collection<LocalApRecurringVoucher> apRecurringVouchers =
          new java.util.ArrayList<>();

  public java.lang.Integer getVbCode() {
    return vbCode;
  }

  public void setVbCode(java.lang.Integer VB_CODE) {
    this.vbCode = VB_CODE;
  }

  public java.lang.String getVbName() {
    return vbName;
  }

  public void setVbName(java.lang.String VB_NM) {
    this.vbName = VB_NM;
  }

  public java.lang.String getVbDescription() {
    return vbDescription;
  }

  public void setVbDescription(java.lang.String VB_DESC) {
    this.vbDescription = VB_DESC;
  }

  public java.lang.String getVbStatus() {
    return vbStatus;
  }

  public void setVbStatus(java.lang.String VB_STATUS) {
    this.vbStatus = VB_STATUS;
  }

  public java.lang.String getVbType() {
    return vbType;
  }

  public void setVbType(java.lang.String VB_TYP) {
    this.vbType = VB_TYP;
  }

  public Date getVbDateCreated() {
    return vbDateCreated;
  }

  public void setVbDateCreated(Date VB_DT_CRTD) {
    this.vbDateCreated = VB_DT_CRTD;
  }

  public java.lang.String getVbCreatedBy() {
    return vbCreatedBy;
  }

  public void setVbCreatedBy(java.lang.String VB_CRTD_BY) {
    this.vbCreatedBy = VB_CRTD_BY;
  }

  public java.lang.String getVbDepartment() {
    return vbDepartment;
  }

  public void setVbDepartment(java.lang.String VB_DPRTMNT) {
    this.vbDepartment = VB_DPRTMNT;
  }

  public java.lang.Integer getVbAdBranch() {
    return vbAdBranch;
  }

  public void setVbAdBranch(java.lang.Integer VB_AD_BRNCH) {
    this.vbAdBranch = VB_AD_BRNCH;
  }

  public java.lang.Integer getVbAdCompany() {
    return vbAdCompany;
  }

  public void setVbAdCompany(java.lang.Integer VB_AD_CMPNY) {
    this.vbAdCompany = VB_AD_CMPNY;
  }

  @XmlTransient
  public Collection getApVouchers() {
    return apVouchers;
  }

  public void setApVouchers(Collection apVouchers) {
    this.apVouchers = apVouchers;
  }

  @XmlTransient
  public Collection getApPurchaseOrders() {
    return apPurchaseOrders;
  }

  public void setApPurchaseOrders(Collection apPurchaseOrders) {
    this.apPurchaseOrders = apPurchaseOrders;
  }

  @XmlTransient
  public Collection getApRecurringVouchers() {
    return apRecurringVouchers;
  }

  public void setApRecurringVouchers(Collection apRecurringVouchers) {
    this.apRecurringVouchers = apRecurringVouchers;
  }

  public void addApVoucher(LocalApVoucher entity) {
    try {
      entity.setApVoucherBatch(this);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void dropApVoucher(LocalApVoucher entity) {
    try {
      entity.setApVoucherBatch(null);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void addApRecurringVoucher(LocalApRecurringVoucher entity) {
    try {
      entity.setApVoucherBatch(this);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void dropApRecurringVoucher(LocalApRecurringVoucher entity) {
    try {
      entity.setApVoucherBatch(null);
    } catch (Exception ex) {
      throw ex;
    }
  }
}