package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdFormFunction")
@Table(name = "AD_FRM_FNCTN")
public class LocalAdFormFunction extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FF_CODE", nullable = false)
    private Integer ffCode;

    @Column(name = "FF_NM", columnDefinition = "VARCHAR")
    private String ffName;

    @OneToMany(mappedBy = "adFormFunction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdFormFunctionResponsibility> adFormFunctionResponsibilities;

    public Integer getFfCode() {

        return ffCode;
    }

    public void setFfCode(Integer FF_CODE) {

        this.ffCode = FF_CODE;
    }

    public String getFfName() {

        return ffName;
    }

    public void setFfName(String FF_NM) {

        this.ffName = FF_NM;
    }

    @XmlTransient
    public List getAdFormFunctionResponsibilities() {

        return adFormFunctionResponsibilities;
    }

    public void setAdFormFunctionResponsibilities(List adFormFunctionResponsibilities) {

        this.adFormFunctionResponsibilities = adFormFunctionResponsibilities;
    }

    public void addAdFormFunctionResponsibility(LocalAdFormFunctionResponsibility entity) {

        try {
            entity.setAdFormFunction(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdFormFunctionResponsibility(LocalAdFormFunctionResponsibility entity) {

        try {
            entity.setAdFormFunction(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}