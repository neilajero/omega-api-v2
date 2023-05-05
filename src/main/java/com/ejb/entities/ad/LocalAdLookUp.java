package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdLookUp")
@Table(name = "AD_LK_UP")
public class LocalAdLookUp extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LU_CODE", nullable = false)
    private Integer luCode;

    @Column(name = "LU_NM", columnDefinition = "VARCHAR")
    private String luName;

    @Column(name = "LU_DESC", columnDefinition = "VARCHAR")
    private String luDescription;

    @Column(name = "LU_AD_CMPNY", columnDefinition = "INT")
    private Integer luAdCompany;

    @OneToMany(mappedBy = "adLookUp", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdLookUpValue> adLookUpValues;

    public Integer getLuCode() {

        return luCode;
    }

    public void setLuCode(Integer LU_CODE) {

        this.luCode = LU_CODE;
    }

    public String getLuName() {

        return luName;
    }

    public void setLuName(String LU_NM) {

        this.luName = LU_NM;
    }

    public String getLuDescription() {

        return luDescription;
    }

    public void setLuDescription(String LU_DESC) {

        this.luDescription = LU_DESC;
    }

    public Integer getLuAdCompany() {

        return luAdCompany;
    }

    public void setLuAdCompany(Integer LU_AD_CMPNY) {

        this.luAdCompany = LU_AD_CMPNY;
    }

    @XmlTransient
    public List getAdLookUpValues() {

        return adLookUpValues;
    }

    public void setAdLookUpValues(List adLookUpValues) {

        this.adLookUpValues = adLookUpValues;
    }

    public void addAdLookUpValue(LocalAdLookUpValue entity) {

        try {
            entity.setAdLookUp(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdLookUpValue(LocalAdLookUpValue entity) {

        try {
            entity.setAdLookUp(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}