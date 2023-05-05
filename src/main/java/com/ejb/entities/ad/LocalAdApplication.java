package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "AdApplication")
@Table(name = "AD_APPLCTN")
public class LocalAdApplication extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APP_CODE", nullable = false)
    private Integer appCode;

    @Column(name = "APP_NM", columnDefinition = "VARCHAR")
    private String appName;

    @Column(name = "APP_DESC", columnDefinition = "VARCHAR")
    private String appDescription;

    @Column(name = "APP_INSTLLD", columnDefinition = "TINYINT")
    private byte appInstalled;

    @Column(name = "APP_AD_CMPNY", columnDefinition = "INT")
    private Integer appAdCompany;

    @OneToMany(mappedBy = "adApplication", fetch = FetchType.LAZY)
    private List<LocalAdDocumentCategory> adDocumentCategories;

    @OneToMany(mappedBy = "adApplication", fetch = FetchType.LAZY)
    private List<LocalAdDocumentSequence> adDocumentSequences;

    @OneToMany(mappedBy = "adApplication", fetch = FetchType.LAZY)
    private List<LocalAdResponsibility> adResponsibilities;


    public Integer getAppCode() {

        return appCode;
    }

    public void setAppCode(Integer APP_CODE) {

        this.appCode = APP_CODE;
    }

    public String getAppName() {

        return appName;
    }

    public void setAppName(String APP_NM) {

        this.appName = APP_NM;
    }

    public String getAppDescription() {

        return appDescription;
    }

    public void setAppDescription(String APP_DESC) {

        this.appDescription = APP_DESC;
    }

    public byte getAppInstalled() {

        return appInstalled;
    }

    public void setAppInstalled(byte APP_INSTLLD) {

        this.appInstalled = APP_INSTLLD;
    }

    public Integer getAppAdCompany() {

        return appAdCompany;
    }

    public void setAppAdCompany(Integer APP_AD_CMPNY) {

        this.appAdCompany = APP_AD_CMPNY;
    }

    @XmlTransient
    public List getAdDocumentCategories() {

        return adDocumentCategories;
    }

    public void setAdDocumentCategories(List adDocumentCategories) {

        this.adDocumentCategories = adDocumentCategories;
    }

    @XmlTransient
    public List getAdDocumentSequences() {

        return adDocumentSequences;
    }

    public void setAdDocumentSequences(List adDocumentSequences) {

        this.adDocumentSequences = adDocumentSequences;
    }

    @XmlTransient
    public List getAdResponsibilities() {

        return adResponsibilities;
    }

    public void setAdResponsibilities(List adResponsibilities) {

        this.adResponsibilities = adResponsibilities;
    }

    public void addAdDocumentCategory(LocalAdDocumentCategory entity) {

        try {
            entity.setAdApplication(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdDocumentCategory(LocalAdDocumentCategory entity) {

        try {
            entity.setAdApplication(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdDocumentSequence(LocalAdDocumentSequence entity) {

        try {
            entity.setAdApplication(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdDocumentSequence(LocalAdDocumentSequence entity) {

        try {
            entity.setAdApplication(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdResponsibility(LocalAdResponsibility entity) {

        try {
            entity.setAdApplication(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdResponsibility(LocalAdResponsibility entity) {

        try {
            entity.setAdApplication(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}