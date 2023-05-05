package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AdUserResponsibility")
@Table(name = "AD_USR_RSPNSBLTY")
public class LocalAdUserResponsibility extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UR_CODE", nullable = false)
    private Integer urCode;

    @Column(name = "UR_DT_FRM", columnDefinition = "DATETIME")
    private Date urDateFrom;

    @Column(name = "UR_DT_TO", columnDefinition = "DATETIME")
    private Date urDateTo;

    @Column(name = "UR_AD_CMPNY", columnDefinition = "INT")
    private Integer urAdCompany;

    @JoinColumn(name = "UR_RS_CODE", referencedColumnName = "AD_RS_CODE")
    @ManyToOne
    private LocalAdResponsibility adResponsibility;

    @JoinColumn(name = "UR_USR_CODE", referencedColumnName = "USR_CODE")
    @ManyToOne
    private LocalAdUser adUser;

    public Integer getUrCode() {

        return urCode;
    }

    public void setUrCode(Integer UR_CODE) {

        this.urCode = UR_CODE;
    }

    public Date getUrDateFrom() {

        return urDateFrom;
    }

    public void setUrDateFrom(Date UR_DT_FRM) {

        this.urDateFrom = UR_DT_FRM;
    }

    public Date getUrDateTo() {

        return urDateTo;
    }

    public void setUrDateTo(Date UR_DT_TO) {

        this.urDateTo = UR_DT_TO;
    }

    public Integer getUrAdCompany() {

        return urAdCompany;
    }

    public void setUrAdCompany(Integer UR_AD_CMPNY) {

        this.urAdCompany = UR_AD_CMPNY;
    }

    public LocalAdResponsibility getAdResponsibility() {

        return adResponsibility;
    }

    public void setAdResponsibility(LocalAdResponsibility adResponsibility) {

        this.adResponsibility = adResponsibility;
    }

    public LocalAdUser getAdUser() {

        return adUser;
    }

    public void setAdUser(LocalAdUser adUser) {

        this.adUser = adUser;
    }

}