package com.ejb.entities.ad;

import com.ejb.entities.gl.LocalGlUserStaticReport;
import com.ejb.entities.inv.LocalInvTag;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "AdUser")
@Table(name = "AD_USR")
public class LocalAdUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_CODE", nullable = false)
    public Integer usrCode;

    @Column(name = "USR_NM", columnDefinition = "VARCHAR")
    public String usrName;
    @Column(name = "USR_PSSWRD", columnDefinition = "VARCHAR")
    public String usrPassword;
    @Column(name = "USR_PSSWRD_EXPRTN_CODE", columnDefinition = "SMALLINT")
    public short usrPasswordExpirationCode;
    @Column(name = "USR_PSSWRD_EXPRTN_DYS", columnDefinition = "SMALLINT")
    public short usrPasswordExpirationDays;
    @Column(name = "USR_PSSWRD_EXPRTN_ACCSS")
    public short usrPasswordExpirationAccess;
    @Column(name = "USR_DT_FRM", columnDefinition = "DATETIME")
    public Date usrDateFrom;
    @Column(name = "USR_DT_TO", columnDefinition = "DATETIME")
    public Date usrDateTo;
    @Column(name = "USR_DEPT", columnDefinition = "VARCHAR")
    private String usrDept;
    @Column(name = "USR_DESC", columnDefinition = "VARCHAR")
    private String usrDescription;
    @Column(name = "USR_PSTN", columnDefinition = "VARCHAR")
    private String usrPosition;
    @Column(name = "USR_EML_ADDRSS", columnDefinition = "VARCHAR")
    private String usrEmailAddress;
    @Column(name = "USR_HEAD", columnDefinition = "TINYINT")
    private byte usrHead;
    @Column(name = "USR_INSPCTR")
    private byte usrInspector;
    @Column(name = "USR_PO_APPRVL_CTR", columnDefinition = "VARCHAR")
    private String usrPurchaseOrderApprovalCounter;
    @Column(name = "USR_PSSWRD_CURR_ACCSS", columnDefinition = "SMALLINT")
    private short usrPasswordCurrentAccess;
    @Column(name = "USR_AD_CMPNY", columnDefinition = "INT")
    private Integer usrAdCompany;

    @Column(name = "USR_CNSL_PSSWRD", columnDefinition = "VARCHAR")
    private String usrConsolePass;

    @OneToMany(mappedBy = "adUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdUserResponsibility> adUserResponsibilities;

    @OneToMany(mappedBy = "adUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdApprovalUser> adApprovalUsers;

    @OneToMany(mappedBy = "adUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdApprovalQueue> adApprovalQueues;

    @OneToMany(mappedBy = "adUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlUserStaticReport> glUserStaticReports;

    @OneToMany(mappedBy = "adUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvTag> invTag;

    public Integer getUsrCode() {

        return usrCode;
    }

    public void setUsrCode(Integer USR_CODE) {

        this.usrCode = USR_CODE;
    }

    public String getUsrName() {

        return usrName;
    }

    public void setUsrName(String USR_NM) {

        this.usrName = USR_NM;
    }

    public String getUsrDept() {

        return usrDept;
    }

    public void setUsrDept(String USR_DEPT) {

        this.usrDept = USR_DEPT;
    }

    public String getUsrDescription() {

        return usrDescription;
    }

    public void setUsrDescription(String USR_DESC) {

        this.usrDescription = USR_DESC;
    }

    public String getUsrPosition() {

        return usrPosition;
    }

    public void setUsrPosition(String USR_PSTN) {

        this.usrPosition = USR_PSTN;
    }

    public String getUsrEmailAddress() {

        return usrEmailAddress;
    }

    public void setUsrEmailAddress(String USR_EML_ADDRSS) {

        this.usrEmailAddress = USR_EML_ADDRSS;
    }

    public byte getUsrHead() {

        return usrHead;
    }

    public void setUsrHead(byte USR_HEAD) {

        this.usrHead = USR_HEAD;
    }

    public byte getUsrInspector() {

        return usrInspector;
    }

    public void setUsrInspector(byte USR_INSPCTR) {

        this.usrInspector = USR_INSPCTR;
    }

    public String getUsrPassword() {

        return usrPassword;
    }

    public void setUsrPassword(String USR_PSSWRD) {

        this.usrPassword = USR_PSSWRD;
    }

    public short getUsrPasswordExpirationCode() {

        return usrPasswordExpirationCode;
    }

    public void setUsrPasswordExpirationCode(short USR_PSSWRD_EXPRTN_CODE) {

        this.usrPasswordExpirationCode = USR_PSSWRD_EXPRTN_CODE;
    }

    public short getUsrPasswordExpirationDays() {

        return usrPasswordExpirationDays;
    }

    public void setUsrPasswordExpirationDays(short USR_PSSWRD_EXPRTN_DYS) {

        this.usrPasswordExpirationDays = USR_PSSWRD_EXPRTN_DYS;
    }

    public short getUsrPasswordExpirationAccess() {

        return usrPasswordExpirationAccess;
    }

    public void setUsrPasswordExpirationAccess(short USR_PSSWRD_EXPRTN_ACCSS) {

        this.usrPasswordExpirationAccess = USR_PSSWRD_EXPRTN_ACCSS;
    }

    public String getUsrPurchaseOrderApprovalCounter() {

        return usrPurchaseOrderApprovalCounter;
    }

    public void setUsrPurchaseOrderApprovalCounter(String USR_PO_APPRVL_CTR) {

        this.usrPurchaseOrderApprovalCounter = USR_PO_APPRVL_CTR;
    }

    public short getUsrPasswordCurrentAccess() {

        return usrPasswordCurrentAccess;
    }

    public void setUsrPasswordCurrentAccess(short USR_PSSWRD_CURR_ACCSS) {

        this.usrPasswordCurrentAccess = USR_PSSWRD_CURR_ACCSS;
    }

    public Date getUsrDateFrom() {

        return usrDateFrom;
    }

    public void setUsrDateFrom(Date USR_DT_FRM) {

        this.usrDateFrom = USR_DT_FRM;
    }

    public Date getUsrDateTo() {

        return usrDateTo;
    }

    public void setUsrDateTo(Date USR_DT_TO) {

        this.usrDateTo = USR_DT_TO;
    }

    public Integer getUsrAdCompany() {

        return usrAdCompany;
    }

    public void setUsrAdCompany(Integer USR_AD_CMPNY) {

        this.usrAdCompany = USR_AD_CMPNY;
    }

    public String getUsrConsolePass() {

        return usrConsolePass;
    }

    public void setUsrConsolePass(String USR_CNSL_PSSWRD) {

        this.usrConsolePass = USR_CNSL_PSSWRD;
    }

    @XmlTransient
    public List getAdUserResponsibilities() {

        return adUserResponsibilities;
    }

    public void setAdUserResponsibilities(List adUserResponsibilities) {

        this.adUserResponsibilities = adUserResponsibilities;
    }

    @XmlTransient
    public List getAdApprovalUsers() {

        return adApprovalUsers;
    }

    public void setAdApprovalUsers(List adApprovalUsers) {

        this.adApprovalUsers = adApprovalUsers;
    }

    @XmlTransient
    public List getAdApprovalQueues() {

        return adApprovalQueues;
    }

    public void setAdApprovalQueues(List adApprovalQueues) {

        this.adApprovalQueues = adApprovalQueues;
    }

    @XmlTransient
    public List getGlUserStaticReports() {

        return glUserStaticReports;
    }

    public void setGlUserStaticReports(List glUserStaticReports) {

        this.glUserStaticReports = glUserStaticReports;
    }

    @XmlTransient
    public List getInvTag() {

        return invTag;
    }

    public void setInvTag(List invTag) {

        this.invTag = invTag;
    }

    public void addAdUserResponsibility(LocalAdUserResponsibility entity) {

        try {
            entity.setAdUser(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdUserResponsibility(LocalAdUserResponsibility entity) {

        try {
            entity.setAdUser(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdApprovalUser(LocalAdApprovalUser entity) {

        try {
            entity.setAdUser(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdApprovalUser(LocalAdApprovalUser entity) {

        try {
            entity.setAdUser(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdApprovalQueue(LocalAdApprovalQueue entity) {

        try {
            entity.setAdUser(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdApprovalQueue(LocalAdApprovalQueue entity) {

        try {
            entity.setAdUser(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlUserStaticReport(LocalGlUserStaticReport entity) {

        try {
            entity.setAdUser(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlUserStaticReport(LocalGlUserStaticReport entity) {

        try {
            entity.setAdUser(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void setValues(Object entity) {

        LocalAdUser adUser = (LocalAdUser) entity;
        this.usrCode = adUser.usrCode;
        this.usrName = adUser.usrName;
        this.usrDept = adUser.usrDept;
        this.usrDescription = adUser.usrDescription;
        this.usrPosition = adUser.usrPosition;
        this.usrEmailAddress = adUser.usrEmailAddress;
        this.usrHead = adUser.usrHead;
        this.usrInspector = adUser.usrInspector;
        this.usrPassword = adUser.usrPassword;
        this.usrPasswordExpirationCode = adUser.usrPasswordExpirationCode;
        this.usrPasswordExpirationDays = adUser.usrPasswordExpirationDays;
        this.usrPasswordExpirationAccess = adUser.usrPasswordExpirationAccess;
        this.usrPurchaseOrderApprovalCounter = adUser.usrPurchaseOrderApprovalCounter;
        this.usrPasswordCurrentAccess = adUser.usrPasswordCurrentAccess;
        this.usrDateFrom = adUser.usrDateFrom;
        this.usrDateTo = adUser.usrDateTo;
        this.usrAdCompany = adUser.usrAdCompany;
        this.usrConsolePass = adUser.usrConsolePass;
    }

}