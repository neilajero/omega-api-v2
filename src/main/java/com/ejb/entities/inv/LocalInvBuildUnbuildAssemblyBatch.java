package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "InvBuildUnbuildAssemlyBatch")
@Table(name = "INV_BUA_BTCH")
public class LocalInvBuildUnbuildAssemblyBatch extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BB_CODE", nullable = false)
    private Integer bbCode;
    @Column(name = "BB_NM", columnDefinition = "VARCHAR")
    private String bbName;
    @Column(name = "BB_DESC", columnDefinition = "VARCHAR")
    private String bbDescription;
    @Column(name = "BB_STATUS", columnDefinition = "VARCHAR")
    private String bbStatus;
    @Column(name = "BB_TYP", columnDefinition = "VARCHAR")
    private String bbType;
    @Column(name = "BB_CRTD_BY", columnDefinition = "VARCHAR")
    private String bbCreatedBy;
    @Column(name = "BB_DT_CRTD", columnDefinition = "DATETIME")
    private Date bbDateCreated;
    @Column(name = "BB_AD_BRNCH", columnDefinition = "INT")
    private Integer bbAdBranch;
    @Column(name = "BB_AD_CMPNY", columnDefinition = "INT")
    private Integer bbAdCompany;

    public Integer getBbCode() {

        return bbCode;
    }

    public void setBbCode(Integer bbCode) {

        this.bbCode = bbCode;
    }

    public String getBbName() {

        return bbName;
    }

    public void setBbName(String bbName) {

        this.bbName = bbName;
    }

    public String getBbDescription() {

        return bbDescription;
    }

    public void setBbDescription(String bbDescription) {

        this.bbDescription = bbDescription;
    }

    public String getBbStatus() {

        return bbStatus;
    }

    public void setBbStatus(String bbStatus) {

        this.bbStatus = bbStatus;
    }

    public String getBbType() {

        return bbType;
    }

    public void setBbType(String bbType) {

        this.bbType = bbType;
    }

    public String getBbCreatedBy() {

        return bbCreatedBy;
    }

    public void setBbCreatedBy(String bbCreatedBy) {

        this.bbCreatedBy = bbCreatedBy;
    }

    public Date getBbDateCreated() {

        return bbDateCreated;
    }

    public void setBbDateCreated(Date bbDateCreated) {

        this.bbDateCreated = bbDateCreated;
    }

    public Integer getBbAdBranch() {

        return bbAdBranch;
    }

    public void setBbAdBranch(Integer bbAdBranch) {

        this.bbAdBranch = bbAdBranch;
    }

    public Integer getBbAdCompany() {

        return bbAdCompany;
    }

    public void setBbAdCompany(Integer bbAdCompany) {

        this.bbAdCompany = bbAdCompany;
    }

}