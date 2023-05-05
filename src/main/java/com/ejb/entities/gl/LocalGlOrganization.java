package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlOrganization")
@Table(name = "GL_ORGNZTN")
public class LocalGlOrganization extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORG_CODE", nullable = false)
    private Integer orgCode;

    @Column(name = "ORG_NM", columnDefinition = "VARCHAR")
    private String orgName;

    @Column(name = "ORG_DESC", columnDefinition = "VARCHAR")
    private String orgDescription;

    @Column(name = "ORG_MSTR_CODE", columnDefinition = "INT")
    private Integer orgMasterCode;

    @Column(name = "ORG_AD_CMPNY", columnDefinition = "INT")
    private Integer orgAdCompany;

    @OneToMany(mappedBy = "glOrganization", fetch = FetchType.LAZY)
    private List<LocalGlAccountRange> glAccountRanges;
    @OneToMany(mappedBy = "glOrganization", fetch = FetchType.LAZY)
    private List<LocalGlResponsibility> glResponsibilities;
    public Integer getOrgCode() {

        return orgCode;
    }

    public void setOrgCode(Integer ORG_CODE) {

        this.orgCode = ORG_CODE;
    }

    public String getOrgName() {

        return orgName;
    }

    public void setOrgName(String ORG_NM) {

        this.orgName = ORG_NM;
    }

    public String getOrgDescription() {

        return orgDescription;
    }

    public void setOrgDescription(String ORG_DESC) {

        this.orgDescription = ORG_DESC;
    }

    public Integer getOrgMasterCode() {

        return orgMasterCode;
    }

    public void setOrgMasterCode(Integer ORG_MSTR_CODE) {

        this.orgMasterCode = ORG_MSTR_CODE;
    }

    public Integer getOrgAdCompany() {

        return orgAdCompany;
    }

    public void setOrgAdCompany(Integer ORG_AD_CMPNY) {

        this.orgAdCompany = ORG_AD_CMPNY;
    }

    @XmlTransient
    public List getGlAccountRanges() {

        return glAccountRanges;
    }

    public void setGlAccountRanges(List glAccountRanges) {

        this.glAccountRanges = glAccountRanges;
    }

    @XmlTransient
    public List getGlResponsibilities() {

        return glResponsibilities;
    }

    public void setGlResponsibilities(List glResponsibilities) {

        this.glResponsibilities = glResponsibilities;
    }

    public void addGlAccountRange(LocalGlAccountRange entity) {

        try {
            entity.setGlOrganization(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlAccountRange(LocalGlAccountRange entity) {

        try {
            entity.setGlOrganization(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlResponsibility(LocalGlResponsibility entity) {

        try {
            entity.setGlOrganization(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlResponsibility(LocalGlResponsibility entity) {

        try {
            entity.setGlOrganization(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}