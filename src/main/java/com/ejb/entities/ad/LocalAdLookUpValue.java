package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdLookUpValue")
@Table(name = "AD_LK_UP_VL")
public class LocalAdLookUpValue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LV_CODE", nullable = false)
    private Integer lvCode;

    @Column(name = "LV_NM", columnDefinition = "VARCHAR")
    private String lvName;

    @Column(name = "LV_DESC", columnDefinition = "VARCHAR")
    private String lvDescription;

    @Column(name = "LV_MSTR_CD", columnDefinition = "INT")
    private Integer lvMasterCode;

    @Column(name = "LV_ENBL", columnDefinition = "TINYINT")
    private byte lvEnable;

    @Column(name = "LV_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char lvDownloadStatus;

    @Column(name = "LV_AD_CMPNY", columnDefinition = "INT")
    private Integer lvAdCompany;

    @JoinColumn(name = "AD_LOOK_UP", referencedColumnName = "LU_CODE")
    @ManyToOne
    private LocalAdLookUp adLookUp;

    public Integer getLvCode() {

        return lvCode;
    }

    public void setLvCode(Integer LV_CODE) {

        this.lvCode = LV_CODE;
    }

    public String getLvName() {

        return lvName;
    }

    public void setLvName(String LV_NM) {

        this.lvName = LV_NM;
    }

    public String getLvDescription() {

        return lvDescription;
    }

    public void setLvDescription(String LV_DESC) {

        this.lvDescription = LV_DESC;
    }

    public Integer getLvMasterCode() {

        return lvMasterCode;
    }

    public void setLvMasterCode(Integer LV_MSTR_CD) {

        this.lvMasterCode = LV_MSTR_CD;
    }

    public byte getLvEnable() {

        return lvEnable;
    }

    public void setLvEnable(byte LV_ENBL) {

        this.lvEnable = LV_ENBL;
    }

    public char getLvDownloadStatus() {

        return lvDownloadStatus;
    }

    public void setLvDownloadStatus(char LV_DWNLD_STATUS) {

        this.lvDownloadStatus = LV_DWNLD_STATUS;
    }

    public Integer getLvAdCompany() {

        return lvAdCompany;
    }

    public void setLvAdCompany(Integer LV_AD_CMPNY) {

        this.lvAdCompany = LV_AD_CMPNY;
    }

    public LocalAdLookUp getAdLookUp() {

        return adLookUp;
    }

    public void setAdLookUp(LocalAdLookUp adLookUp) {

        this.adLookUp = adLookUp;
    }

}