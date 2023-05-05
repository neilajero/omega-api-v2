package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlBudgetOrganization")
@Table(name = "GL_BDGT_ORGNZTN")
public class LocalGlBudgetOrganization extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BO_CODE", nullable = false)
    private Integer boCode;

    @Column(name = "BO_NM", columnDefinition = "VARCHAR")
    private String boName;

    @Column(name = "BO_DESC", columnDefinition = "VARCHAR")
    private String boDescription;

    @Column(name = "BO_SGMNT_ORDR", columnDefinition = "VARCHAR")
    private String boSegmentOrder;

    @Column(name = "BO_PSSWRD", columnDefinition = "VARCHAR")
    private String boPassword;

    @Column(name = "BO_AD_CMPNY", columnDefinition = "INT")
    private Integer boAdCompany;

    @OneToMany(mappedBy = "glBudgetOrganization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlBudgetAmount> glBudgetAmounts;

    @OneToMany(mappedBy = "glBudgetOrganization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlBudgetAccountAssignment> glBudgetAccountAssignments;

    public Integer getBoCode() {

        return boCode;
    }

    public void setBoCode(Integer BO_CODE) {

        this.boCode = BO_CODE;
    }

    public String getBoName() {

        return boName;
    }

    public void setBoName(String BO_NM) {

        this.boName = BO_NM;
    }

    public String getBoDescription() {

        return boDescription;
    }

    public void setBoDescription(String BO_DESC) {

        this.boDescription = BO_DESC;
    }

    public String getBoSegmentOrder() {

        return boSegmentOrder;
    }

    public void setBoSegmentOrder(String BO_SGMNT_ORDR) {

        this.boSegmentOrder = BO_SGMNT_ORDR;
    }

    public String getBoPassword() {

        return boPassword;
    }

    public void setBoPassword(String BO_PSSWRD) {

        this.boPassword = BO_PSSWRD;
    }

    public Integer getBoAdCompany() {

        return boAdCompany;
    }

    public void setBoAdCompany(Integer BO_AD_CMPNY) {

        this.boAdCompany = BO_AD_CMPNY;
    }

    @XmlTransient
    public List getGlBudgetAmounts() {

        return glBudgetAmounts;
    }

    public void setGlBudgetAmounts(List glBudgetAmounts) {

        this.glBudgetAmounts = glBudgetAmounts;
    }

    @XmlTransient
    public List getGlBudgetAccountAssignments() {

        return glBudgetAccountAssignments;
    }

    public void setGlBudgetAccountAssignments(List glBudgetAccountAssignments) {

        this.glBudgetAccountAssignments = glBudgetAccountAssignments;
    }

    public void addGlBudgetAmount(LocalGlBudgetAmount entity) {

        try {
            entity.setGlBudgetOrganization(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAmount(LocalGlBudgetAmount entity) {

        try {
            entity.setGlBudgetOrganization(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlBudgetAccountAssignment(LocalGlBudgetAccountAssignment entity) {

        try {
            entity.setGlBudgetOrganization(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAccountAssignment(LocalGlBudgetAccountAssignment entity) {

        try {
            entity.setGlBudgetOrganization(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}