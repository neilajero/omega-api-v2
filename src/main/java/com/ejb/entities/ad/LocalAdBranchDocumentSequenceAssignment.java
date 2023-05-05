package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchDocumentSequenceAssignment")
@Table(name = "AD_BR_DSA")
public class LocalAdBranchDocumentSequenceAssignment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BDS_CODE", nullable = false)
    private Integer bdsCode;

    @Column(name = "BDS_NXT_SQNC", columnDefinition = "VARCHAR")
    private String bdsNextSequence;

    @Column(name = "BDS_AD_CMPNY", columnDefinition = "INT")
    private Integer bdsAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AD_DOCUMENT_SEQUENCE_ASSIGNMENT", referencedColumnName = "DSA_CODE")
    @ManyToOne
    private LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;

    public Integer getBdsCode() {

        return bdsCode;
    }

    public void setBdsCode(Integer BDS_CODE) {

        this.bdsCode = BDS_CODE;
    }

    public String getBdsNextSequence() {

        return bdsNextSequence;
    }

    public void setBdsNextSequence(String BDS_NXT_SQNC) {

        this.bdsNextSequence = BDS_NXT_SQNC;
    }

    public Integer getBdsAdCompany() {

        return bdsAdCompany;
    }

    public void setBdsAdCompany(Integer BDS_AD_CMPNY) {

        this.bdsAdCompany = BDS_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalAdDocumentSequenceAssignment getAdDocumentSequenceAssignment() {

        return adDocumentSequenceAssignment;
    }

    public void setAdDocumentSequenceAssignment(
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment) {

        this.adDocumentSequenceAssignment = adDocumentSequenceAssignment;
    }

}