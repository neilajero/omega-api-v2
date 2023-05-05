package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlAccountRange")
@Table(name = "GL_ACCNT_RNG")
public class LocalGlAccountRange extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_CODE", nullable = false)
    private Integer arCode;

    @Column(name = "AR_LN", columnDefinition = "SMALLINT")
    private short arLine;

    @Column(name = "AR_ACCNT_LW", columnDefinition = "VARCHAR")
    private String arAccountLow;

    @Column(name = "AR_ACCNT_HGH", columnDefinition = "VARCHAR")
    private String arAccountHigh;

    @Column(name = "AR_AD_CMPNY", columnDefinition = "INT")
    private Integer arAdCompany;

    @JoinColumn(name = "GL_ORGANIZATION", referencedColumnName = "ORG_CODE")
    @ManyToOne
    private LocalGlOrganization glOrganization;

    public Integer getArCode() {

        return arCode;
    }

    public void setArCode(Integer AR_CODE) {

        this.arCode = AR_CODE;
    }

    public short getArLine() {

        return arLine;
    }

    public void setArLine(short AR_LN) {

        this.arLine = AR_LN;
    }

    public String getArAccountLow() {

        return arAccountLow;
    }

    public void setArAccountLow(String AR_ACCNT_LW) {

        this.arAccountLow = AR_ACCNT_LW;
    }

    public String getArAccountHigh() {

        return arAccountHigh;
    }

    public void setArAccountHigh(String AR_ACCNT_HGH) {

        this.arAccountHigh = AR_ACCNT_HGH;
    }

    public Integer getArAdCompany() {

        return arAdCompany;
    }

    public void setArAdCompany(Integer AR_AD_CMPNY) {

        this.arAdCompany = AR_AD_CMPNY;
    }

    public LocalGlOrganization getGlOrganization() {

        return glOrganization;
    }

    public void setGlOrganization(LocalGlOrganization glOrganization) {

        this.glOrganization = glOrganization;
    }

}