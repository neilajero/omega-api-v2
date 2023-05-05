package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlBudgetAccountAssignment")
@Table(name = "GL_BDGT_ACCNT_ASSGNMNT")
public class LocalGlBudgetAccountAssignment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAA_CODE", nullable = false)
    private Integer baaCode;

    @Column(name = "BAA_ACCNT_FRM", columnDefinition = "VARCHAR")
    private String baaAccountFrom;

    @Column(name = "BAA_ACCNT_TO", columnDefinition = "VARCHAR")
    private String baaAccountTo;

    @Column(name = "BAA_TYP", columnDefinition = "VARCHAR")
    private String baaType;

    @Column(name = "BAA_AD_CMPNY", columnDefinition = "INT")
    private Integer baaAdCompany;

    @JoinColumn(name = "GL_BUDGET_ORGANIZATION", referencedColumnName = "BO_CODE")
    @ManyToOne
    private LocalGlBudgetOrganization glBudgetOrganization;

    public Integer getBaaCode() {

        return baaCode;
    }

    public void setBaaCode(Integer BAA_CODE) {

        this.baaCode = BAA_CODE;
    }

    public String getBaaAccountFrom() {

        return baaAccountFrom;
    }

    public void setBaaAccountFrom(String BAA_ACCNT_FRM) {

        this.baaAccountFrom = BAA_ACCNT_FRM;
    }

    public String getBaaAccountTo() {

        return baaAccountTo;
    }

    public void setBaaAccountTo(String BAA_ACCNT_TO) {

        this.baaAccountTo = BAA_ACCNT_TO;
    }

    public String getBaaType() {

        return baaType;
    }

    public void setBaaType(String BAA_TYP) {

        this.baaType = BAA_TYP;
    }

    public Integer getBaaAdCompany() {

        return baaAdCompany;
    }

    public void setBaaAdCompany(Integer BAA_AD_CMPNY) {

        this.baaAdCompany = BAA_AD_CMPNY;
    }

    public LocalGlBudgetOrganization getGlBudgetOrganization() {

        return glBudgetOrganization;
    }

    public void setGlBudgetOrganization(LocalGlBudgetOrganization glBudgetOrganization) {

        this.glBudgetOrganization = glBudgetOrganization;
    }

}