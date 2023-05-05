package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArInvoiceBatch")
@Table(name = "AR_INVC_BTCH")
public class LocalArInvoiceBatch extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IB_CODE", nullable = false)
    private Integer ibCode;

    @Column(name = "IB_NM", columnDefinition = "VARCHAR")
    private String ibName;

    @Column(name = "IB_DESC", columnDefinition = "VARCHAR")
    private String ibDescription;

    @Column(name = "IB_STATUS", columnDefinition = "VARCHAR")
    private String ibStatus;

    @Column(name = "IB_TYP", columnDefinition = "VARCHAR")
    private String ibType;

    @Column(name = "IB_ALLW_INTRST", columnDefinition = "TINYINT")
    private byte ibAllowInterest;

    @Column(name = "IB_INTRST_RT", columnDefinition = "DOUBLE")
    private double ibInterestRate = 0;

    @Column(name = "IB_DT_CRTD", columnDefinition = "DATETIME")
    private Date ibDateCreated;

    @Column(name = "IB_CRTD_BY", columnDefinition = "VARCHAR")
    private String ibCreatedBy;

    @Column(name = "IB_AD_BRNCH", columnDefinition = "INT")
    private Integer ibAdBranch;

    @Column(name = "IB_AD_CMPNY", columnDefinition = "INT")
    private Integer ibAdCompany;

    @OneToMany(mappedBy = "arInvoiceBatch", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;

    public Integer getIbCode() {

        return ibCode;
    }

    public void setIbCode(Integer IB_CODE) {

        this.ibCode = IB_CODE;
    }

    public String getIbName() {

        return ibName;
    }

    public void setIbName(String IB_NM) {

        this.ibName = IB_NM;
    }

    public String getIbDescription() {

        return ibDescription;
    }

    public void setIbDescription(String IB_DESC) {

        this.ibDescription = IB_DESC;
    }

    public String getIbStatus() {

        return ibStatus;
    }

    public void setIbStatus(String IB_STATUS) {

        this.ibStatus = IB_STATUS;
    }

    public String getIbType() {

        return ibType;
    }

    public void setIbType(String IB_TYP) {

        this.ibType = IB_TYP;
    }

    public byte getIbAllowInterest() {

        return ibAllowInterest;
    }

    public void setIbAllowInterest(byte IB_ALLW_INTRST) {

        this.ibAllowInterest = IB_ALLW_INTRST;
    }

    public double getIbInterestRate() {

        return ibInterestRate;
    }

    public void setIbInterestRate(double IB_INTRST_RT) {

        this.ibInterestRate = IB_INTRST_RT;
    }

    public Date getIbDateCreated() {

        return ibDateCreated;
    }

    public void setIbDateCreated(Date IB_DT_CRTD) {

        this.ibDateCreated = IB_DT_CRTD;
    }

    public String getIbCreatedBy() {

        return ibCreatedBy;
    }

    public void setIbCreatedBy(String IB_CRTD_BY) {

        this.ibCreatedBy = IB_CRTD_BY;
    }

    public Integer getIbAdBranch() {

        return ibAdBranch;
    }

    public void setIbAdBranch(Integer IB_AD_BRNCH) {

        this.ibAdBranch = IB_AD_BRNCH;
    }

    public Integer getIbAdCompany() {

        return ibAdCompany;
    }

    public void setIbAdCompany(Integer IB_AD_CMPNY) {

        this.ibAdCompany = IB_AD_CMPNY;
    }

    @XmlTransient
    public List getArInvoices() {

        return arInvoices;
    }

    public void setArInvoices(List arInvoices) {

        this.arInvoices = arInvoices;
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setArInvoiceBatch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setArInvoiceBatch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}