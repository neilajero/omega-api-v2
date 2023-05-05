package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AdLog")
@Table(name = "AD_LOG")
public class LocalAdLog extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_CODE", nullable = false)
    private Integer logCode;

    @Column(name = "LOG_DT", columnDefinition = "DATETIME")
    private Date logDate;

    @Column(name = "LOG_USRNM", columnDefinition = "VARCHAR")
    private String logUsername;

    @Column(name = "LOG_ACTN", columnDefinition = "VARCHAR")
    private String logAction;

    @Column(name = "LOG_DESC", columnDefinition = "VARCHAR")
    private String logDescription;

    @Column(name = "LOG_MDL", columnDefinition = "VARCHAR")
    private String logModule;

    @Column(name = "LOG_MDL_KY", columnDefinition = "INT")
    private Integer logModuleKey;

    public Integer getLogCode() {

        return logCode;
    }

    public void setLogCode(Integer LOG_CODE) {

        this.logCode = LOG_CODE;
    }

    public Date getLogDate() {

        return logDate;
    }

    public void setLogDate(Date LOG_DT) {

        this.logDate = LOG_DT;
    }

    public String getLogUsername() {

        return logUsername;
    }

    public void setLogUsername(String LOG_USRNM) {

        this.logUsername = LOG_USRNM;
    }

    public String getLogAction() {

        return logAction;
    }

    public void setLogAction(String LOG_ACTN) {

        this.logAction = LOG_ACTN;
    }

    public String getLogDescription() {

        return logDescription;
    }

    public void setLogDescription(String LOG_DESC) {

        this.logDescription = LOG_DESC;
    }

    public String getLogModule() {

        return logModule;
    }

    public void setLogModule(String LOG_MDL) {

        this.logModule = LOG_MDL;
    }

    public Integer getLogModuleKey() {

        return logModuleKey;
    }

    public void setLogModuleKey(Integer LOG_MDL_KY) {

        this.logModuleKey = LOG_MDL_KY;
    }

}