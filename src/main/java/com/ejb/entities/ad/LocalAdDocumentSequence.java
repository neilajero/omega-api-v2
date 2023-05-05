package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "AdDocumentSequence")
@Table(name = "AD_DCMNT_SQNC")
public class LocalAdDocumentSequence extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DS_CODE", nullable = false)
    private Integer dsCode;

    @Column(name = "DS_SQNC_NM", columnDefinition = "VARCHAR")
    private String dsSequenceName;

    @Column(name = "DS_DT_FRM", columnDefinition = "DATETIME")
    private Date dsDateFrom;

    @Column(name = "DS_DT_TO", columnDefinition = "DATETIME")
    private Date dsDateTo;

    @Column(name = "DS_NMBRNG_TYP", columnDefinition = "VARCHAR")
    private char dsNumberingType;

    @Column(name = "DS_INTL_VL", columnDefinition = "VARCHAR")
    private String dsInitialValue;

    @Column(name = "DS_AD_CMPNY", columnDefinition = "INT")
    private Integer dsAdCompany;

    @JoinColumn(name = "AD_APPLICATION", referencedColumnName = "APP_CODE")
    @ManyToOne
    private LocalAdApplication adApplication;

    @OneToMany(mappedBy = "adDocumentSequence", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdDocumentSequenceAssignment> adDocumentSequenceAssignments;


    public Integer getDsCode() {

        return dsCode;
    }

    public void setDsCode(Integer DS_CODE) {

        this.dsCode = DS_CODE;
    }

    public String getDsSequenceName() {

        return dsSequenceName;
    }

    public void setDsSequenceName(String DS_SQNC_NM) {

        this.dsSequenceName = DS_SQNC_NM;
    }

    public Date getDsDateFrom() {

        return dsDateFrom;
    }

    public void setDsDateFrom(Date DS_DT_FRM) {

        this.dsDateFrom = DS_DT_FRM;
    }

    public Date getDsDateTo() {

        return dsDateTo;
    }

    public void setDsDateTo(Date DS_DT_TO) {

        this.dsDateTo = DS_DT_TO;
    }

    public char getDsNumberingType() {

        return dsNumberingType;
    }

    public void setDsNumberingType(char DS_NMBRNG_TYP) {

        this.dsNumberingType = DS_NMBRNG_TYP;
    }

    public String getDsInitialValue() {

        return dsInitialValue;
    }

    public void setDsInitialValue(String DS_INTL_VL) {

        this.dsInitialValue = DS_INTL_VL;
    }

    public Integer getDsAdCompany() {

        return dsAdCompany;
    }

    public void setDsAdCompany(Integer DS_AD_CMPNY) {

        this.dsAdCompany = DS_AD_CMPNY;
    }

    public LocalAdApplication getAdApplication() {

        return adApplication;
    }

    public void setAdApplication(LocalAdApplication adApplication) {

        this.adApplication = adApplication;
    }

    @XmlTransient
    public List getAdDocumentSequenceAssignments() {

        return adDocumentSequenceAssignments;
    }

    public void setAdDocumentSequenceAssignments(List adDocumentSequenceAssignments) {

        this.adDocumentSequenceAssignments = adDocumentSequenceAssignments;
    }

    public void addAdDocumentSequenceAssignment(LocalAdDocumentSequenceAssignment entity) {

        try {
            entity.setAdDocumentSequence(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdDocumentSequenceAssignment(LocalAdDocumentSequenceAssignment entity) {

        try {
            entity.setAdDocumentSequence(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }


}