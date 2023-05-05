package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdUser;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "GlUserStaticReport")
@Table(name = "GL_USR_STTC_RPRT")
public class LocalGlUserStaticReport extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USTR_CODE", nullable = false)
    private Integer ustrCode;

    @Column(name = "USTR_AD_CMPNY", columnDefinition = "INT")
    private Integer ustrAdCompany;

    @JoinColumn(name = "AD_USER", referencedColumnName = "USR_CODE")
    @ManyToOne
    private LocalAdUser adUser;

    @JoinColumn(name = "GL_STATIC_REPORT", referencedColumnName = "SR_CODE")
    @ManyToOne
    private LocalGlStaticReport glStaticReport;

    public Integer getUstrCode() {

        return ustrCode;
    }

    public void setUstrCode(Integer USTR_CODE) {

        this.ustrCode = USTR_CODE;
    }

    public Integer getUstrAdCompany() {

        return ustrAdCompany;
    }

    public void setUstrAdCompany(Integer USTR_AD_CMPNY) {

        this.ustrAdCompany = USTR_AD_CMPNY;
    }

    public LocalAdUser getAdUser() {

        return adUser;
    }

    public void setAdUser(LocalAdUser adUser) {

        this.adUser = adUser;
    }

    public LocalGlStaticReport getGlStaticReport() {

        return glStaticReport;
    }

    public void setGlStaticReport(LocalGlStaticReport glStaticReport) {

        this.glStaticReport = glStaticReport;
    }

}