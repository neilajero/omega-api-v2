package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlBudget")
@Table(name = "GL_BDGT")
public class LocalGlBudget extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BGT_CODE", nullable = false)
    private Integer bgtCode;

    @Column(name = "BGT_NM", columnDefinition = "VARCHAR")
    private String bgtName;

    @Column(name = "BGT_DESC", columnDefinition = "VARCHAR")
    private String bgtDescription;

    @Column(name = "BGT_STATUS", columnDefinition = "VARCHAR")
    private String bgtStatus;

    @Column(name = "BGT_FRST_PRD", columnDefinition = "VARCHAR")
    private String bgtFirstPeriod;

    @Column(name = "BGT_LST_PRD", columnDefinition = "VARCHAR")
    private String bgtLastPeriod;

    @Column(name = "BGT_AD_CMPNY", columnDefinition = "INT")
    private Integer bgtAdCompany;

    @OneToMany(mappedBy = "glBudget", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlBudgetAmount> glBudgetAmounts;

    public Integer getBgtCode() {

        return bgtCode;
    }

    public void setBgtCode(Integer BGT_CODE) {

        this.bgtCode = BGT_CODE;
    }

    public String getBgtName() {

        return bgtName;
    }

    public void setBgtName(String BGT_NM) {

        this.bgtName = BGT_NM;
    }

    public String getBgtDescription() {

        return bgtDescription;
    }

    public void setBgtDescription(String BGT_DESC) {

        this.bgtDescription = BGT_DESC;
    }

    public String getBgtStatus() {

        return bgtStatus;
    }

    public void setBgtStatus(String BGT_STATUS) {

        this.bgtStatus = BGT_STATUS;
    }

    public String getBgtFirstPeriod() {

        return bgtFirstPeriod;
    }

    public void setBgtFirstPeriod(String BGT_FRST_PRD) {

        this.bgtFirstPeriod = BGT_FRST_PRD;
    }

    public String getBgtLastPeriod() {

        return bgtLastPeriod;
    }

    public void setBgtLastPeriod(String BGT_LST_PRD) {

        this.bgtLastPeriod = BGT_LST_PRD;
    }

    public Integer getBgtAdCompany() {

        return bgtAdCompany;
    }

    public void setBgtAdCompany(Integer BGT_AD_CMPNY) {

        this.bgtAdCompany = BGT_AD_CMPNY;
    }

    @XmlTransient
    public List getGlBudgetAmounts() {

        return glBudgetAmounts;
    }

    public void setGlBudgetAmounts(List glBudgetAmounts) {

        this.glBudgetAmounts = glBudgetAmounts;
    }

    public void addGlBudgetAmount(LocalGlBudgetAmount entity) {

        try {
            entity.setGlBudget(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAmount(LocalGlBudgetAmount entity) {

        try {
            entity.setGlBudget(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}