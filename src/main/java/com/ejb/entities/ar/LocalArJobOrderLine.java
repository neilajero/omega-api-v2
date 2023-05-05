package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArJobOrderLine")
@Table(name = "AR_JB_ORDR_LN")
public class LocalArJobOrderLine extends NativeQueryHome implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "JOL_CODE", nullable = false)
  private Integer jolCode;

  @Column(name = "JOL_LN", columnDefinition = "SMALLINT")
  private short jolLine;

  @Column(name = "JOL_IDESC", columnDefinition = "VARCHAR")
  private String jolLineIDesc;

  @Column(name = "JOL_QTY", columnDefinition = "DOUBLE")
  private double jolQuantity = 0;

  @Column(name = "JOL_UNT_PRC", columnDefinition = "DOUBLE")
  private double jolUnitPrice = 0;

  @Column(name = "JOL_AMNT", columnDefinition = "DOUBLE")
  private double jolAmount = 0;

  @Column(name = "JOL_DSCNT_1", columnDefinition = "DOUBLE")
  private double jolDiscount1 = 0;

  @Column(name = "JOL_DSCNT_2", columnDefinition = "DOUBLE")
  private double jolDiscount2 = 0;

  @Column(name = "JOL_DSCNT_3", columnDefinition = "DOUBLE")
  private double jolDiscount3 = 0;

  @Column(name = "JOL_DSCNT_4", columnDefinition = "DOUBLE")
  private double jolDiscount4 = 0;

  @Column(name = "JOL_TTL_DSCNT", columnDefinition = "DOUBLE")
  private double jolTotalDiscount = 0;

  @Column(name = "JOL_RQST_QTY", columnDefinition = "DOUBLE")
  private double jolRequestQuantity = 0;

  @Column(name = "JOL_MISC", columnDefinition = "VARCHAR")
  private String jolMisc;

  @Column(name = "JOL_TX", columnDefinition = "TINYINT")
  private byte jolTax;

  @Column(name = "JOL_AD_CMPNY", columnDefinition = "INT")
  private Integer jolAdCompany;

  @JoinColumn(name = "AR_JOB_ORDER", referencedColumnName = "JO_CODE")
  @ManyToOne
  private LocalArJobOrder arJobOrder;

  @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
  @ManyToOne
  private LocalInvItemLocation invItemLocation;

  @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
  @ManyToOne
  private LocalInvUnitOfMeasure invUnitOfMeasure;

  @OneToMany(mappedBy = "arJobOrderLine", fetch = FetchType.LAZY)
  private List<LocalArJobOrderAssignment> arJobOrderAssignments;

  @OneToMany(mappedBy = "arJobOrderLine", fetch = FetchType.LAZY)
  private List<LocalArJobOrderInvoiceLine> arJobOrderInvoiceLines;

  @OneToMany(mappedBy = "arJobOrderLine", fetch = FetchType.LAZY)
  private List<LocalInvTag> invTags;

  public Integer getJolCode() {

    return jolCode;
  }

  public void setJolCode(Integer JOL_CODE) {

    this.jolCode = JOL_CODE;
  }

  public short getJolLine() {

    return jolLine;
  }

  public void setJolLine(short JOL_LN) {

    this.jolLine = JOL_LN;
  }

  public String getJolLineIDesc() {

    return jolLineIDesc;
  }

  public void setJolLineIDesc(String JOL_IDESC) {

    this.jolLineIDesc = JOL_IDESC;
  }

  public double getJolQuantity() {

    return jolQuantity;
  }

  public void setJolQuantity(double JOL_QTY) {

    this.jolQuantity = JOL_QTY;
  }

  public double getJolUnitPrice() {

    return jolUnitPrice;
  }

  public void setJolUnitPrice(double JOL_UNT_PRC) {

    this.jolUnitPrice = JOL_UNT_PRC;
  }

  public double getJolAmount() {

    return jolAmount;
  }

  public void setJolAmount(double JOL_AMNT) {

    this.jolAmount = JOL_AMNT;
  }

  public double getJolDiscount1() {

    return jolDiscount1;
  }

  public void setJolDiscount1(double JOL_DSCNT_1) {

    this.jolDiscount1 = JOL_DSCNT_1;
  }

  public double getJolDiscount2() {

    return jolDiscount2;
  }

  public void setJolDiscount2(double JOL_DSCNT_2) {

    this.jolDiscount2 = JOL_DSCNT_2;
  }

  public double getJolDiscount3() {

    return jolDiscount3;
  }

  public void setJolDiscount3(double JOL_DSCNT_3) {

    this.jolDiscount3 = JOL_DSCNT_3;
  }

  public double getJolDiscount4() {

    return jolDiscount4;
  }

  public void setJolDiscount4(double JOL_DSCNT_4) {

    this.jolDiscount4 = JOL_DSCNT_4;
  }

  public double getJolTotalDiscount() {

    return jolTotalDiscount;
  }

  public void setJolTotalDiscount(double JOL_TTL_DSCNT) {

    this.jolTotalDiscount = JOL_TTL_DSCNT;
  }

  public double getJolRequestQuantity() {

    return jolRequestQuantity;
  }

  public void setJolRequestQuantity(double JOL_RQST_QTY) {

    this.jolRequestQuantity = JOL_RQST_QTY;
  }

  public String getJolMisc() {

    return jolMisc;
  }

  public void setJolMisc(String JOL_MISC) {

    this.jolMisc = JOL_MISC;
  }

  public byte getJolTax() {

    return jolTax;
  }

  public void setJolTax(byte JOL_TX) {

    this.jolTax = JOL_TX;
  }

  public Integer getJolAdCompany() {

    return jolAdCompany;
  }

  public void setJolAdCompany(Integer JOL_AD_CMPNY) {

    this.jolAdCompany = JOL_AD_CMPNY;
  }

  public LocalArJobOrder getArJobOrder() {

    return arJobOrder;
  }

  public void setArJobOrder(LocalArJobOrder arJobOrder) {

    this.arJobOrder = arJobOrder;
  }

  public LocalInvItemLocation getInvItemLocation() {

    return invItemLocation;
  }

  public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

    this.invItemLocation = invItemLocation;
  }

  public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

    return invUnitOfMeasure;
  }

  public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

    this.invUnitOfMeasure = invUnitOfMeasure;
  }

  @XmlTransient
  public List getArJobOrderAssignments() {

    return arJobOrderAssignments;
  }

  public void setArJobOrderAssignments(List arJobOrderAssignments) {

    this.arJobOrderAssignments = arJobOrderAssignments;
  }

  @XmlTransient
  public List getArJobOrderInvoiceLines() {

    return arJobOrderInvoiceLines;
  }

  public void setArJobOrderInvoiceLines(List arJobOrderInvoiceLines) {

    this.arJobOrderInvoiceLines = arJobOrderInvoiceLines;
  }

  @XmlTransient
  public List getInvTags() {

    return invTags;
  }

  public void setInvTags(List invTags) {

    this.invTags = invTags;
  }

}