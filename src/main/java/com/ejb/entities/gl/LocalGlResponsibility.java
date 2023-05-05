package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlResponsibility")
@Table(name = "GL_RSPNSBLTY")
public class LocalGlResponsibility extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RES_CODE", nullable = false)
    private Integer resCode;

    @Column(name = "RES_ENBL", columnDefinition = "TINYINT")
    private byte resEnable;

    @Column(name = "RES_AD_CMPNY", columnDefinition = "INT")
    private Integer resAdCompany;

    @JoinColumn(name = "GL_ORGANIZATION", referencedColumnName = "ORG_CODE")
    @ManyToOne
    private LocalGlOrganization glOrganization;

    public Integer getResCode() {

        return resCode;
    }

    public void setResCode(Integer RES_CODE) {

        this.resCode = RES_CODE;
    }

    public byte getResEnable() {

        return resEnable;
    }

    public void setResEnable(byte RES_ENBL) {

        this.resEnable = RES_ENBL;
    }

    public Integer getResAdCompany() {

        return resAdCompany;
    }

    public void setResAdCompany(Integer RES_AD_CMPNY) {

        this.resAdCompany = RES_AD_CMPNY;
    }

    public LocalGlOrganization getGlOrganization() {

        return glOrganization;
    }

    public void setGlOrganization(LocalGlOrganization glOrganization) {

        this.glOrganization = glOrganization;
    }

}