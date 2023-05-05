package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApCheckBatch")
@Table(name = "AP_CHCK_BTCH")
public class LocalApCheckBatch extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AP_CB_CODE", nullable = false)
    private Integer cbCode;

    @Column(name = "CB_NM", columnDefinition = "VARCHAR")
    private String cbName;

    @Column(name = "CB_DESC", columnDefinition = "VARCHAR")
    private String cbDescription;

    @Column(name = "CB_STATUS", columnDefinition = "VARCHAR")
    private String cbStatus;

    @Column(name = "CB_TYP", columnDefinition = "VARCHAR")
    private String cbType;

    @Column(name = "CB_DT_CRTD", columnDefinition = "DATETIME")
    private Date cbDateCreated;

    @Column(name = "CB_CRTD_BY", columnDefinition = "VARCHAR")
    private String cbCreatedBy;

    @Column(name = "CB_DPRTMNT", columnDefinition = "VARCHAR")
    private String cbDepartment;

    @Column(name = "CB_AD_BRNCH", columnDefinition = "INT")
    private Integer cbAdBranch;

    @Column(name = "CB_AD_CMPNY", columnDefinition = "INT")
    private Integer cbAdCompany;

    @OneToMany(mappedBy = "apCheckBatch", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks;

    public Integer getCbCode() {

        return cbCode;
    }

    public void setCbCode(Integer AP_CB_CODE) {

        this.cbCode = AP_CB_CODE;
    }

    public String getCbName() {

        return cbName;
    }

    public void setCbName(String CB_NM) {

        this.cbName = CB_NM;
    }

    public String getCbDescription() {

        return cbDescription;
    }

    public void setCbDescription(String CB_DESC) {

        this.cbDescription = CB_DESC;
    }

    public String getCbStatus() {

        return cbStatus;
    }

    public void setCbStatus(String CB_STATUS) {

        this.cbStatus = CB_STATUS;
    }

    public String getCbType() {

        return cbType;
    }

    public void setCbType(String CB_TYP) {

        this.cbType = CB_TYP;
    }

    public Date getCbDateCreated() {

        return cbDateCreated;
    }

    public void setCbDateCreated(Date CB_DT_CRTD) {

        this.cbDateCreated = CB_DT_CRTD;
    }

    public String getCbCreatedBy() {

        return cbCreatedBy;
    }

    public void setCbCreatedBy(String CB_CRTD_BY) {

        this.cbCreatedBy = CB_CRTD_BY;
    }

    public String getCbDepartment() {

        return cbDepartment;
    }

    public void setCbDepartment(String CB_DPRTMNT) {

        this.cbDepartment = CB_DPRTMNT;
    }

    public Integer getCbAdBranch() {

        return cbAdBranch;
    }

    public void setCbAdBranch(Integer CB_AD_BRNCH) {

        this.cbAdBranch = CB_AD_BRNCH;
    }

    public Integer getCbAdCompany() {

        return cbAdCompany;
    }

    public void setCbAdCompany(Integer CB_AD_CMPNY) {

        this.cbAdCompany = CB_AD_CMPNY;
    }

    @XmlTransient
    public List getApChecks() {

        return apChecks;
    }

    public void setApChecks(List apChecks) {

        this.apChecks = apChecks;
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setApCheckBatch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setApCheckBatch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}