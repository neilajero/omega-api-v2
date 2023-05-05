package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "AdResponsibility")
@Table(name = "AD_RSPNSBLTY")
public class LocalAdResponsibility extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AD_RS_CODE", nullable = false)
    private Integer rsCode;

    @Column(name = "RS_NM", columnDefinition = "VARCHAR")
    private String rsName;

    @Column(name = "RS_DESC", columnDefinition = "VARCHAR")
    private String rsDescription;

    @Column(name = "RS_DT_FRM", columnDefinition = "DATETIME")
    private Date rsDateFrom;

    @Column(name = "RS_DT_TO", columnDefinition = "DATETIME")
    private Date rsDateTo;

    @Column(name = "RS_AD_CMPNY", columnDefinition = "INT")
    private Integer rsAdCompany;

    @JoinColumn(name = "RS_APP_CODE", referencedColumnName = "APP_CODE")
    @ManyToOne
    private LocalAdApplication adApplication;

    @OneToMany(mappedBy = "adResponsibility", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdFormFunctionResponsibility> adFormFunctionResponsibilities;

    @OneToMany(mappedBy = "adResponsibility", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdUserResponsibility> adUserResponsibilities;

    @OneToMany(mappedBy = "adResponsibility", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdBranchResponsibility> adBranchResponsibilities;

    public Integer getRsCode() {

        return rsCode;
    }

    public void setRsCode(Integer AD_RS_CODE) {

        this.rsCode = AD_RS_CODE;
    }

    public String getRsName() {

        return rsName;
    }

    public void setRsName(String RS_NM) {

        this.rsName = RS_NM;
    }

    public String getRsDescription() {

        return rsDescription;
    }

    public void setRsDescription(String RS_DESC) {

        this.rsDescription = RS_DESC;
    }

    public Date getRsDateFrom() {

        return rsDateFrom;
    }

    public void setRsDateFrom(Date RS_DT_FRM) {

        this.rsDateFrom = RS_DT_FRM;
    }

    public Date getRsDateTo() {

        return rsDateTo;
    }

    public void setRsDateTo(Date RS_DT_TO) {

        this.rsDateTo = RS_DT_TO;
    }

    public Integer getRsAdCompany() {

        return rsAdCompany;
    }

    public void setRsAdCompany(Integer RS_AD_CMPNY) {

        this.rsAdCompany = RS_AD_CMPNY;
    }

    public LocalAdApplication getAdApplication() {

        return adApplication;
    }

    public void setAdApplication(LocalAdApplication adApplication) {

        this.adApplication = adApplication;
    }

    @XmlTransient
    public List getAdFormFunctionResponsibilities() {

        return adFormFunctionResponsibilities;
    }

    public void setAdFormFunctionResponsibilities(List adFormFunctionResponsibilities) {

        this.adFormFunctionResponsibilities = adFormFunctionResponsibilities;
    }

    @XmlTransient
    public List getAdUserResponsibilities() {

        return adUserResponsibilities;
    }

    public void setAdUserResponsibilities(List adUserResponsibilities) {

        this.adUserResponsibilities = adUserResponsibilities;
    }

    @XmlTransient
    public List getAdBranchResponsibilities() {

        return adBranchResponsibilities;
    }

    public void setAdBranchResponsibilities(List adBranchResponsibilities) {

        this.adBranchResponsibilities = adBranchResponsibilities;
    }

    public void addAdFormFunctionResponsibility(LocalAdFormFunctionResponsibility entity) {

        try {
            entity.setAdResponsibility(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdFormFunctionResponsibility(LocalAdFormFunctionResponsibility entity) {

        try {
            entity.setAdResponsibility(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdUserResponsibility(LocalAdUserResponsibility entity) {

        try {
            entity.setAdResponsibility(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdUserResponsibility(LocalAdUserResponsibility entity) {

        try {
            entity.setAdResponsibility(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchResponsibility(LocalAdBranchResponsibility entity) {

        try {
            entity.setAdResponsibility(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchResponsibility(LocalAdBranchResponsibility entity) {

        try {
            entity.setAdResponsibility(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}