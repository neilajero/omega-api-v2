package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArJobOrderType")
@Table(name = "AR_JB_ORDR_TYP")
public class LocalArJobOrderType extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOT_CODE", nullable = false)
    private Integer jotCode;

    @Column(name = "JOT_NM", columnDefinition = "VARCHAR")
    private String jotName;

    @Column(name = "JOT_DCMNT_TYP", columnDefinition = "VARCHAR")
    private String jotDocumentType;

    @Column(name = "JOT_RPRT_TYP", columnDefinition = "VARCHAR")
    private String jotReportType;

    @Column(name = "JOT_DESC", columnDefinition = "VARCHAR")
    private String jotDescription;

    @Column(name = "JOT_GL_COA_JB_ORDR_ACCNT", columnDefinition = "INT")
    private Integer jotGlCoaJobOrderAccount;

    @Column(name = "JOT_ENBL", columnDefinition = "TINYINT")
    private byte jotEnable;

    @Column(name = "JOT_AD_CMPNY", columnDefinition = "INT")
    private Integer jotAdCompany;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    @JoinColumn(name = "AR_WITHHOLDING_TAX_CODE", referencedColumnName = "AR_WTC_CODE")
    @ManyToOne
    private LocalArWithholdingTaxCode arWithholdingTaxCode;

    @OneToMany(mappedBy = "arJobOrderType", fetch = FetchType.LAZY)
    private List<LocalArJobOrder> arJobOrders;

    public Integer getJotCode() {

        return jotCode;
    }

    public void setJotCode(Integer JOT_CODE) {

        this.jotCode = JOT_CODE;
    }

    public String getJotName() {

        return jotName;
    }

    public void setJotName(String JOT_NM) {

        this.jotName = JOT_NM;
    }

    public String getJotDocumentType() {

        return jotDocumentType;
    }

    public void setJotDocumentType(String JOT_DCMNT_TYP) {

        this.jotDocumentType = JOT_DCMNT_TYP;
    }

    public String getJotReportType() {

        return jotReportType;
    }

    public void setJotReportType(String JOT_RPRT_TYP) {

        this.jotReportType = JOT_RPRT_TYP;
    }

    public String getJotDescription() {

        return jotDescription;
    }

    public void setJotDescription(String JOT_DESC) {

        this.jotDescription = JOT_DESC;
    }

    public Integer getJotGlCoaJobOrderAccount() {

        return jotGlCoaJobOrderAccount;
    }

    public void setJotGlCoaJobOrderAccount(Integer JOT_GL_COA_JB_ORDR_ACCNT) {

        this.jotGlCoaJobOrderAccount = JOT_GL_COA_JB_ORDR_ACCNT;
    }

    public byte getJotEnable() {

        return jotEnable;
    }

    public void setJotEnable(byte JOT_ENBL) {

        this.jotEnable = JOT_ENBL;
    }

    public Integer getJotAdCompany() {

        return jotAdCompany;
    }

    public void setJotAdCompany(Integer JOT_AD_CMPNY) {

        this.jotAdCompany = JOT_AD_CMPNY;
    }

    public LocalArTaxCode getArTaxCode() {

        return arTaxCode;
    }

    public void setArTaxCode(LocalArTaxCode arTaxCode) {

        this.arTaxCode = arTaxCode;
    }

    public LocalArWithholdingTaxCode getArWithholdingTaxCode() {

        return arWithholdingTaxCode;
    }

    public void setArWithholdingTaxCode(LocalArWithholdingTaxCode arWithholdingTaxCode) {

        this.arWithholdingTaxCode = arWithholdingTaxCode;
    }

    @XmlTransient
    public List getArJobOrders() {

        return arJobOrders;
    }

    public void setArJobOrders(List arJobOrders) {

        this.arJobOrders = arJobOrders;
    }

}