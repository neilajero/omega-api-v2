package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdDocumentCategory")
@Table(name = "AD_DCMNT_CTGRY")
public class LocalAdDocumentCategory extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DC_CODE", nullable = false)
    private Integer dcCode;

    @Column(name = "DC_NM", columnDefinition = "VARCHAR")
    private String dcName;

    @Column(name = "DC_DESC", columnDefinition = "VARCHAR")
    private String dcDescription;

    @Column(name = "DC_AD_CMPNY", columnDefinition = "INT")
    private Integer dcAdCompany;

    @JoinColumn(name = "AD_APPLICATION", referencedColumnName = "APP_CODE")
    @ManyToOne
    private LocalAdApplication adApplication;

    @OneToMany(mappedBy = "adDocumentCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdDocumentSequenceAssignment> adDocumentSequenceAssignments;


    public Integer getDcCode() {

        return dcCode;
    }

    public void setDcCode(Integer DC_CODE) {

        this.dcCode = DC_CODE;
    }

    public String getDcName() {

        return dcName;
    }

    public void setDcName(String DC_NM) {

        this.dcName = DC_NM;
    }

    public String getDcDescription() {

        return dcDescription;
    }

    public void setDcDescription(String DC_DESC) {

        this.dcDescription = DC_DESC;
    }

    public Integer getDcAdCompany() {

        return dcAdCompany;
    }

    public void setDcAdCompany(Integer DC_AD_CMPNY) {

        this.dcAdCompany = DC_AD_CMPNY;
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
            entity.setAdDocumentCategory(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdDocumentSequenceAssignment(LocalAdDocumentSequenceAssignment entity) {

        try {
            entity.setAdDocumentCategory(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }


}