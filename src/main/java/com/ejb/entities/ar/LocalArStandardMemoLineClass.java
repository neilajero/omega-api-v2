package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "ArStandardMemoLineClass")
@Table(name = "AR_STNDRD_MMO_LN_CLSS")
public class LocalArStandardMemoLineClass extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SMC_CODE", nullable = false)
    private Integer smcCode;

    @Column(name = "SMC_UNT_PRC", columnDefinition = "DOUBLE")
    private double smcUnitPrice = 0;

    @Column(name = "SMC_SML_DESC", columnDefinition = "VARCHAR")
    private String smcStandardMemoLineDescription;

    @Column(name = "SMC_CRTD_BY", columnDefinition = "VARCHAR")
    private String smcCreatedBy;

    @Column(name = "SMC_DT_CRTD", columnDefinition = "DATETIME")
    private Date smcDateCreated;

    @Column(name = "SMC_MDFD_BY", columnDefinition = "VARCHAR")
    private String smcModifiedBy;

    @Column(name = "SMC_DT_MDFD", columnDefinition = "DATETIME")
    private Date smcDateModified;

    @Column(name = "SMC_AD_BRNCH", columnDefinition = "INT")
    private Integer smcAdBranch;

    @Column(name = "SMC_AD_CMPNY", columnDefinition = "INT")
    private Integer smcAdCompany;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    @JoinColumn(name = "AR_CUSTOMER_CLASS", referencedColumnName = "CC_CODE")
    @ManyToOne
    private LocalArCustomerClass arCustomerClass;

    @JoinColumn(name = "AR_STANDARD_MEMO_LINE", referencedColumnName = "SML_CODE")
    @ManyToOne
    private LocalArStandardMemoLine arStandardMemoLine;

    public Integer getSmcCode() {

        return smcCode;
    }

    public void setSmcCode(Integer SMC_CODE) {

        this.smcCode = SMC_CODE;
    }

    public double getSmcUnitPrice() {

        return smcUnitPrice;
    }

    public void setSmcUnitPrice(double SMC_UNT_PRC) {

        this.smcUnitPrice = SMC_UNT_PRC;
    }

    public String getSmcStandardMemoLineDescription() {

        return smcStandardMemoLineDescription;
    }

    public void setSmcStandardMemoLineDescription(String SMC_SML_DESC) {

        this.smcStandardMemoLineDescription = SMC_SML_DESC;
    }

    public String getSmcCreatedBy() {

        return smcCreatedBy;
    }

    public void setSmcCreatedBy(String SMC_CRTD_BY) {

        this.smcCreatedBy = SMC_CRTD_BY;
    }

    public Date getSmcDateCreated() {

        return smcDateCreated;
    }

    public void setSmcDateCreated(Date SMC_DT_CRTD) {

        this.smcDateCreated = SMC_DT_CRTD;
    }

    public String getSmcModifiedBy() {

        return smcModifiedBy;
    }

    public void setSmcModifiedBy(String SMC_MDFD_BY) {

        this.smcModifiedBy = SMC_MDFD_BY;
    }

    public Date getSmcDateModified() {

        return smcDateModified;
    }

    public void setSmcDateModified(Date SMC_DT_MDFD) {

        this.smcDateModified = SMC_DT_MDFD;
    }

    public Integer getSmcAdBranch() {

        return smcAdBranch;
    }

    public void setSmcAdBranch(Integer SMC_AD_BRNCH) {

        this.smcAdBranch = SMC_AD_BRNCH;
    }

    public Integer getSmcAdCompany() {

        return smcAdCompany;
    }

    public void setSmcAdCompany(Integer SMC_AD_CMPNY) {

        this.smcAdCompany = SMC_AD_CMPNY;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

    public LocalArCustomerClass getArCustomerClass() {

        return arCustomerClass;
    }

    public void setArCustomerClass(LocalArCustomerClass arCustomerClass) {

        this.arCustomerClass = arCustomerClass;
    }

    public LocalArStandardMemoLine getArStandardMemoLine() {

        return arStandardMemoLine;
    }

    public void setArStandardMemoLine(LocalArStandardMemoLine arStandardMemoLine) {

        this.arStandardMemoLine = arStandardMemoLine;
    }

}