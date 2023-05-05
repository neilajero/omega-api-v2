package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArReceiptBatch")
@Table(name = "AR_RCPT_BTCH")
public class LocalArReceiptBatch extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RB_CODE", nullable = false)
    private Integer rbCode;
    @Column(name = "RB_NM", columnDefinition = "VARCHAR")
    private String rbName;
    @Column(name = "RB_DESC", columnDefinition = "VARCHAR")
    private String rbDescription;
    @Column(name = "RB_STATUS", columnDefinition = "VARCHAR")
    private String rbStatus;
    @Column(name = "RB_TYP", columnDefinition = "VARCHAR")
    private String rbType;
    @Column(name = "RB_DT_CRTD", columnDefinition = "DATETIME")
    private Date rbDateCreated;
    @Column(name = "RB_CRTD_BY", columnDefinition = "VARCHAR")
    private String rbCreatedBy;
    @Column(name = "RB_AD_BRNCH", columnDefinition = "INT")
    private Integer rbAdBranch;
    @Column(name = "RB_AD_CMPNY", columnDefinition = "INT")
    private Integer rbAdCompany;
    @OneToMany(mappedBy = "arReceiptBatch", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;

    public LocalArReceiptBatch() {

    }

    public Integer getRbCode() {

        return rbCode;
    }

    public void setRbCode(Integer RB_CODE) {

        this.rbCode = RB_CODE;
    }

    public String getRbName() {

        return rbName;
    }

    public void setRbName(String RB_NM) {

        this.rbName = RB_NM;
    }

    public String getRbDescription() {

        return rbDescription;
    }

    public void setRbDescription(String RB_DESC) {

        this.rbDescription = RB_DESC;
    }

    public String getRbStatus() {

        return rbStatus;
    }

    public void setRbStatus(String RB_STATUS) {

        this.rbStatus = RB_STATUS;
    }

    public String getRbType() {

        return rbType;
    }

    public void setRbType(String RB_TYP) {

        this.rbType = RB_TYP;
    }

    public Date getRbDateCreated() {

        return rbDateCreated;
    }

    public void setRbDateCreated(Date RB_DT_CRTD) {

        this.rbDateCreated = RB_DT_CRTD;
    }

    public String getRbCreatedBy() {

        return rbCreatedBy;
    }

    public void setRbCreatedBy(String RB_CRTD_BY) {

        this.rbCreatedBy = RB_CRTD_BY;
    }

    public Integer getRbAdBranch() {

        return rbAdBranch;
    }

    public void setRbAdBranch(Integer RB_AD_BRNCH) {

        this.rbAdBranch = RB_AD_BRNCH;
    }

    public Integer getRbAdCompany() {

        return rbAdCompany;
    }

    public void setRbAdCompany(Integer RB_AD_CMPNY) {

        this.rbAdCompany = RB_AD_CMPNY;
    }

    @XmlTransient
    public List getArReceipts() {

        return arReceipts;
    }

    public void setArReceipts(List arReceipts) {

        this.arReceipts = arReceipts;
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setArReceiptBatch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setArReceiptBatch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}