package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArJobOrderAssignment")
@Table(name = "AR_JB_ORDR_ASSGNMNT")
public class LocalArJobOrderAssignment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JA_CODE", nullable = false)
    private Integer jaCode;

    @Column(name = "JA_LN", columnDefinition = "SMALLINT")
    private short jaLine;

    @Column(name = "JA_RMKS", columnDefinition = "VARCHAR")
    private String jaRemarks;

    @Column(name = "JA_QTY", columnDefinition = "DOUBLE")
    private double jaQuantity = 0;

    @Column(name = "JA_UNT_CST", columnDefinition = "DOUBLE")
    private double jaUnitCost = 0;

    @Column(name = "JA_AMNT", columnDefinition = "DOUBLE")
    private double jaAmount = 0;

    @Column(name = "JA_SO", columnDefinition = "TINYINT")
    private byte jaSo;

    @Column(name = "JA_AD_CMPNY", columnDefinition = "INT")
    private Integer jaAdCompany;

    @JoinColumn(name = "AR_JOB_ORDER_LINE", referencedColumnName = "JOL_CODE")
    @ManyToOne
    private LocalArJobOrderLine arJobOrderLine;

    @JoinColumn(name = "AR_PERSONEL", referencedColumnName = "PE_CODE")
    @ManyToOne
    private LocalArPersonel arPersonel;

    public Integer getJaCode() {

        return jaCode;
    }

    public void setJaCode(Integer JA_CODE) {

        this.jaCode = JA_CODE;
    }

    public short getJaLine() {

        return jaLine;
    }

    public void setJaLine(short JA_LN) {

        this.jaLine = JA_LN;
    }

    public String getJaRemarks() {

        return jaRemarks;
    }

    public void setJaRemarks(String JA_RMKS) {

        this.jaRemarks = JA_RMKS;
    }

    public double getJaQuantity() {

        return jaQuantity;
    }

    public void setJaQuantity(double JA_QTY) {

        this.jaQuantity = JA_QTY;
    }

    public double getJaUnitCost() {

        return jaUnitCost;
    }

    public void setJaUnitCost(double JA_UNT_CST) {

        this.jaUnitCost = JA_UNT_CST;
    }

    public double getJaAmount() {

        return jaAmount;
    }

    public void setJaAmount(double JA_AMNT) {

        this.jaAmount = JA_AMNT;
    }

    public byte getJaSo() {

        return jaSo;
    }

    public void setJaSo(byte JA_SO) {

        this.jaSo = JA_SO;
    }

    public Integer getJaAdCompany() {

        return jaAdCompany;
    }

    public void setJaAdCompany(Integer JA_AD_CMPNY) {

        this.jaAdCompany = JA_AD_CMPNY;
    }

    public LocalArJobOrderLine getArJobOrderLine() {

        return arJobOrderLine;
    }

    public void setArJobOrderLine(LocalArJobOrderLine arJobOrderLine) {

        this.arJobOrderLine = arJobOrderLine;
    }

    public LocalArPersonel getArPersonel() {

        return arPersonel;
    }

    public void setArPersonel(LocalArPersonel arPersonel) {

        this.arPersonel = arPersonel;
    }

}