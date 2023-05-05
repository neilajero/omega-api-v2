package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity(name = "AdAmountLimit")
@Table(name = "AD_AMNT_LMT")
public class LocalAdAmountLimit extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AD_CAL_CODE", nullable = false)
    private Integer calCode;

    @Column(name = "CAL_DEPT", columnDefinition = "VARCHAR")
    private String calDept;

    @Column(name = "CAL_AMNT_LMT", columnDefinition = "DOUBLE")
    private double calAmountLimit = 0;

    @Column(name = "CAL_AND_OR", columnDefinition = "VARCHAR")
    private String calAndOr;

    @Column(name = "CAL_AD_CMPNY", columnDefinition = "INT")
    private Integer calAdCompany;

    @JoinColumn(name = "AD_APPROVAL_COA_LINE", referencedColumnName = "ACL_CODE")
    @ManyToOne
    private LocalAdApprovalCoaLine adApprovalCoaLine;

    @JoinColumn(name = "AD_APPROVAL_DOCUMENT", referencedColumnName = "ADC_CODE")
    @ManyToOne
    private LocalAdApprovalDocument adApprovalDocument;

    @OneToMany(mappedBy = "adAmountLimit", fetch = FetchType.LAZY)
    private List<LocalAdApprovalUser> adApprovalUsers;

    public Integer getCalCode() {

        return calCode;
    }

    public void setCalCode(Integer calCode) {

        this.calCode = calCode;
    }

    public String getCalDept() {

        return calDept;
    }

    public void setCalDept(String calDept) {

        this.calDept = calDept;
    }

    public double getCalAmountLimit() {

        return calAmountLimit;
    }

    public void setCalAmountLimit(double calAmountLimit) {

        this.calAmountLimit = calAmountLimit;
    }

    public String getCalAndOr() {

        return calAndOr;
    }

    public void setCalAndOr(String calAndOr) {

        this.calAndOr = calAndOr;
    }

    public Integer getCalAdCompany() {

        return calAdCompany;
    }

    public void setCalAdCompany(Integer calAdCompany) {

        this.calAdCompany = calAdCompany;
    }

    public LocalAdApprovalCoaLine getAdApprovalCoaLine() {

        return adApprovalCoaLine;
    }

    public void setAdApprovalCoaLine(LocalAdApprovalCoaLine adApprovalCoaLine) {

        this.adApprovalCoaLine = adApprovalCoaLine;
    }

    public LocalAdApprovalDocument getAdApprovalDocument() {

        return adApprovalDocument;
    }

    public void setAdApprovalDocument(LocalAdApprovalDocument adApprovalDocument) {

        this.adApprovalDocument = adApprovalDocument;
    }

    @XmlTransient
    public Collection getAdApprovalUsers() {

        return adApprovalUsers;
    }

    public void setAdApprovalUsers(List adApprovalUsers) {

        this.adApprovalUsers = adApprovalUsers;
    }

    public void addAdApprovalUser(LocalAdApprovalUser entity) {

        try {
            entity.setAdAmountLimit(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdApprovalUser(LocalAdApprovalUser entity) {

        try {
            entity.setAdAmountLimit(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}