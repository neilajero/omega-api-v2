package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdCompany;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "GenField")
@Table(name = "GEN_FLD")
public class LocalGenField extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FL_CODE", nullable = false)
    private Integer flCode;

    @Column(name = "FL_NM", columnDefinition = "VARCHAR")
    private String flName;

    @Column(name = "FL_DESC", columnDefinition = "VARCHAR")
    private String flDescription;

    @Column(name = "FL_SGMNT_SPRTR", columnDefinition = "VARCHAR")
    private char flSegmentSeparator;

    @Column(name = "FL_NMBR_OF_SGMNT", columnDefinition = "SMALLINT")
    private short flNumberOfSegment;

    @Column(name = "FL_FRZ_RLLP", columnDefinition = "TINYINT")
    private byte flFreezeRollup;

    @Column(name = "FL_ENBL", columnDefinition = "TINYINT")
    private byte flEnable;

    @Column(name = "FL_AD_CMPNY", columnDefinition = "INT")
    private Integer flAdCompany;

    @OneToMany(mappedBy = "genField", fetch = FetchType.LAZY)
    private List<LocalGenSegment> genSegments;

    @OneToMany(mappedBy = "genField", fetch = FetchType.LAZY)
    private List<LocalAdCompany> adCompanies;

    public Integer getFlCode() {

        return flCode;
    }

    public void setFlCode(Integer FL_CODE) {

        this.flCode = FL_CODE;
    }

    public String getFlName() {

        return flName;
    }

    public void setFlName(String FL_NM) {

        this.flName = FL_NM;
    }

    public String getFlDescription() {

        return flDescription;
    }

    public void setFlDescription(String FL_DESC) {

        this.flDescription = FL_DESC;
    }

    public char getFlSegmentSeparator() {

        return flSegmentSeparator;
    }

    public void setFlSegmentSeparator(char FL_SGMNT_SPRTR) {

        this.flSegmentSeparator = FL_SGMNT_SPRTR;
    }

    public short getFlNumberOfSegment() {

        return flNumberOfSegment;
    }

    public void setFlNumberOfSegment(short FL_NMBR_OF_SGMNT) {

        this.flNumberOfSegment = FL_NMBR_OF_SGMNT;
    }

    public byte getFlFreezeRollup() {

        return flFreezeRollup;
    }

    public void setFlFreezeRollup(byte FL_FRZ_RLLP) {

        this.flFreezeRollup = FL_FRZ_RLLP;
    }

    public byte getFlEnable() {

        return flEnable;
    }

    public void setFlEnable(byte FL_ENBL) {

        this.flEnable = FL_ENBL;
    }

    public Integer getFlAdCompany() {

        return flAdCompany;
    }

    public void setFlAdCompany(Integer FL_AD_CMPNY) {

        this.flAdCompany = FL_AD_CMPNY;
    }

    @XmlTransient
    public List getGenSegments() {

        return genSegments;
    }

    public void setGenSegments(List genSegments) {

        this.genSegments = genSegments;
    }

    @XmlTransient
    public List getAdCompanies() {

        return adCompanies;
    }

    public void setAdCompanies(List adCompanies) {

        this.adCompanies = adCompanies;
    }

    public void addGenSegment(LocalGenSegment entity) {

        try {
            entity.setGenField(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenSegment(LocalGenSegment entity) {

        try {
            entity.setGenField(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdCompany(LocalAdCompany entity) {

        try {
            entity.setGenField(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdCompany(LocalAdCompany entity) {

        try {
            entity.setGenField(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}