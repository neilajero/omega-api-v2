package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlJournal;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdDocumentSequenceAssignment")
@Table(name = "AD_DCMNT_SQNC_ASSGNMNT")
public class LocalAdDocumentSequenceAssignment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DSA_CODE", nullable = false)
    private Integer dsaCode;

    @Column(name = "DSA_SOB_CODE", columnDefinition = "INT")
    private Integer dsaSobCode;

    @Column(name = "DSA_NXT_SQNC", columnDefinition = "VARCHAR")
    private String dsaNextSequence;

    @Column(name = "DSA_AD_CMPNY", columnDefinition = "INT")
    private Integer dsaAdCompany;

    @JoinColumn(name = "AD_DOCUMENT_CATEGORY", referencedColumnName = "DC_CODE")
    @ManyToOne
    private LocalAdDocumentCategory adDocumentCategory;

    @JoinColumn(name = "AD_DOCUMENT_SEQUENCE", referencedColumnName = "DS_CODE")
    @ManyToOne
    private LocalAdDocumentSequence adDocumentSequence;

    @OneToMany(mappedBy = "adDocumentSequenceAssignment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlJournal> glJournals;

    @OneToMany(mappedBy = "adDocumentSequenceAssignment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdBranchDocumentSequenceAssignment> adBranchDocumentSequenceAssignments;


    public Integer getDsaCode() {

        return dsaCode;
    }

    public void setDsaCode(Integer DSA_CODE) {

        this.dsaCode = DSA_CODE;
    }

    public Integer getDsaSobCode() {

        return dsaSobCode;
    }

    public void setDsaSobCode(Integer DSA_SOB_CODE) {

        this.dsaSobCode = DSA_SOB_CODE;
    }

    public String getDsaNextSequence() {

        return dsaNextSequence;
    }

    public void setDsaNextSequence(String DSA_NXT_SQNC) {

        this.dsaNextSequence = DSA_NXT_SQNC;
    }

    public Integer getDsaAdCompany() {

        return dsaAdCompany;
    }

    public void setDsaAdCompany(Integer DSA_AD_CMPNY) {

        this.dsaAdCompany = DSA_AD_CMPNY;
    }

    public LocalAdDocumentCategory getAdDocumentCategory() {

        return adDocumentCategory;
    }

    public void setAdDocumentCategory(LocalAdDocumentCategory adDocumentCategory) {

        this.adDocumentCategory = adDocumentCategory;
    }

    public LocalAdDocumentSequence getAdDocumentSequence() {

        return adDocumentSequence;
    }

    public void setAdDocumentSequence(LocalAdDocumentSequence adDocumentSequence) {

        this.adDocumentSequence = adDocumentSequence;
    }

    @XmlTransient
    public List getGlJournals() {

        return glJournals;
    }

    public void setGlJournals(List glJournals) {

        this.glJournals = glJournals;
    }

    @XmlTransient
    public List getAdBranchDocumentSequenceAssignments() {

        return adBranchDocumentSequenceAssignments;
    }

    public void setAdBranchDocumentSequenceAssignments(List adBranchDocumentSequenceAssignments) {

        this.adBranchDocumentSequenceAssignments = adBranchDocumentSequenceAssignments;
    }

    public void addGlJournal(LocalGlJournal entity) {

        try {
            entity.setAdDocumentSequenceAssignment(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournal(LocalGlJournal entity) {

        try {
            entity.setAdDocumentSequenceAssignment(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchDocumentSequenceAssignments(LocalAdBranchDocumentSequenceAssignment entity) {

        try {
            entity.setAdDocumentSequenceAssignment(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchDocumentSequenceAssignments(LocalAdBranchDocumentSequenceAssignment entity) {

        try {
            entity.setAdDocumentSequenceAssignment(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }


}