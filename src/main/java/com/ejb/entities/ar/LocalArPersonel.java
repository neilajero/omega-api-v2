package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArPersonel")
@Table(name = "AR_PRSNL")
public class LocalArPersonel extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PE_CODE", nullable = false)
    private Integer peCode;

    @Column(name = "PE_ID_NMBR", columnDefinition = "VARCHAR")
    private String peIdNumber;

    @Column(name = "PE_NM", columnDefinition = "VARCHAR")
    private String peName;

    @Column(name = "PE_DESC", columnDefinition = "VARCHAR")
    private String peDescription;

    @Column(name = "PE_ADDRSS", columnDefinition = "VARCHAR")
    private String peAddress;

    @Column(name = "PE_RT", columnDefinition = "DOUBLE")
    private double peRate = 0;

    @Column(name = "PE_AD_CMPNY", columnDefinition = "INT")
    private Integer peAdCompany;

    @JoinColumn(name = "AR_PERSONEL_TYPE", referencedColumnName = "AR_PT_CODE")
    @ManyToOne
    private LocalArPersonelType arPersonelType;

    @OneToMany(mappedBy = "arPersonel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArJobOrderAssignment> arJobOrderAssignment;

    public Integer getPeCode() {

        return peCode;
    }

    public void setPeCode(Integer PE_CODE) {

        this.peCode = PE_CODE;
    }

    public String getPeIdNumber() {

        return peIdNumber;
    }

    public void setPeIdNumber(String PE_ID_NMBR) {

        this.peIdNumber = PE_ID_NMBR;
    }

    public String getPeName() {

        return peName;
    }

    public void setPeName(String PE_NM) {

        this.peName = PE_NM;
    }

    public String getPeDescription() {

        return peDescription;
    }

    public void setPeDescription(String PE_DESC) {

        this.peDescription = PE_DESC;
    }

    public String getPeAddress() {

        return peAddress;
    }

    public void setPeAddress(String PE_ADDRSS) {

        this.peAddress = PE_ADDRSS;
    }

    public double getPeRate() {

        return peRate;
    }

    public void setPeRate(double PE_RT) {

        this.peRate = PE_RT;
    }

    public Integer getPeAdCompany() {

        return peAdCompany;
    }

    public void setPeAdCompany(Integer PE_AD_CMPNY) {

        this.peAdCompany = PE_AD_CMPNY;
    }

    public LocalArPersonelType getArPersonelType() {

        return arPersonelType;
    }

    public void setArPersonelType(LocalArPersonelType arPersonelType) {

        this.arPersonelType = arPersonelType;
    }

    @XmlTransient
    public List getArJobOrderAssignment() {

        return arJobOrderAssignment;
    }

    public void setArJobOrderAssignment(List arJobOrderAssignment) {

        this.arJobOrderAssignment = arJobOrderAssignment;
    }

}