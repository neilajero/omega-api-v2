package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

@Entity(name = "GlBudgetAmount")
@Table(name = "GL_BDGT_AMNT")
public class LocalGlBudgetAmount extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BGA_CODE", nullable = false)
    private Integer bgaCode;

    @Column(name = "BGA_CRTD_BY", columnDefinition = "VARCHAR")
    private String bgaCreatedBy;

    @Column(name = "BGA_DT_CRTD", columnDefinition = "DATETIME")
    private Date bgaDateCreated;

    @Column(name = "BGA_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String bgaLastModifiedBy;

    @Column(name = "BGA_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date bgaDateLastModified;

    @Column(name = "BGA_AD_CMPNY", columnDefinition = "INT")
    private Integer bgaAdCompany;

    @JoinColumn(name = "GL_BUDGET", referencedColumnName = "BGT_CODE")
    @ManyToOne
    private LocalGlBudget glBudget;

    @JoinColumn(name = "GL_BUDGET_ORGANIZATION", referencedColumnName = "BO_CODE")
    @ManyToOne
    private LocalGlBudgetOrganization glBudgetOrganization;

    @OneToMany(mappedBy = "glBudgetAmount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlBudgetAmountCoa> glBudgetAmountCoas;

    public Integer getBgaCode() {

        return bgaCode;
    }

    public void setBgaCode(Integer BGA_CODE) {

        this.bgaCode = BGA_CODE;
    }

    public String getBgaCreatedBy() {

        return bgaCreatedBy;
    }

    public void setBgaCreatedBy(String BGA_CRTD_BY) {

        this.bgaCreatedBy = BGA_CRTD_BY;
    }

    public Date getBgaDateCreated() {

        return bgaDateCreated;
    }

    public void setBgaDateCreated(Date BGA_DT_CRTD) {

        this.bgaDateCreated = BGA_DT_CRTD;
    }

    public String getBgaLastModifiedBy() {

        return bgaLastModifiedBy;
    }

    public void setBgaLastModifiedBy(String BGA_LST_MDFD_BY) {

        this.bgaLastModifiedBy = BGA_LST_MDFD_BY;
    }

    public Date getBgaDateLastModified() {

        return bgaDateLastModified;
    }

    public void setBgaDateLastModified(Date BGA_DT_LST_MDFD) {

        this.bgaDateLastModified = BGA_DT_LST_MDFD;
    }

    public Integer getBgaAdCompany() {

        return bgaAdCompany;
    }

    public void setBgaAdCompany(Integer BGA_AD_CMPNY) {

        this.bgaAdCompany = BGA_AD_CMPNY;
    }

    public LocalGlBudget getGlBudget() {

        return glBudget;
    }

    public void setGlBudget(LocalGlBudget glBudget) {

        this.glBudget = glBudget;
    }

    public LocalGlBudgetOrganization getGlBudgetOrganization() {

        return glBudgetOrganization;
    }

    public void setGlBudgetOrganization(LocalGlBudgetOrganization glBudgetOrganization) {

        this.glBudgetOrganization = glBudgetOrganization;
    }

    @XmlTransient
    public List getGlBudgetAmountCoas() {

        return glBudgetAmountCoas;
    }

    public void setGlBudgetAmountCoas(List glBudgetAmountCoas) {

        this.glBudgetAmountCoas = glBudgetAmountCoas;
    }

    public void addGlBudgetAmountCoa(LocalGlBudgetAmountCoa entity) {

        try {
            entity.setGlBudgetAmount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAmountCoa(LocalGlBudgetAmountCoa entity) {

        try {
            entity.setGlBudgetAmount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}