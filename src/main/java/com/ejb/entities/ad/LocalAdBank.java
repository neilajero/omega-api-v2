package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdBank")
@Table(name = "AD_BNK")
public class LocalAdBank extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BNK_CODE", nullable = false)
    private Integer bnkCode;

    @Column(name = "BNK_NM", columnDefinition = "VARCHAR")
    private String bnkName;

    @Column(name = "BNK_BRNCH", columnDefinition = "VARCHAR")
    private String bnkBranch;

    @Column(name = "BNK_NMBR", columnDefinition = "BIGINT")
    private long bnkNumber;

    @Column(name = "BNK_INSTTTN", columnDefinition = "VARCHAR")
    private String bnkInstitution;

    @Column(name = "BNK_DESC", columnDefinition = "VARCHAR")
    private String bnkDescription;

    @Column(name = "BNK_ADDRSS", columnDefinition = "VARCHAR")
    private String bnkAddress;

    @Column(name = "BNK_ENBL", columnDefinition = "TINYINT")
    private byte bnkEnable;

    @Column(name = "BNK_AD_CMPNY", columnDefinition = "INT")
    private Integer bnkAdCompany;

    @OneToMany(mappedBy = "adBank", fetch = FetchType.LAZY)
    private List<LocalAdBankAccount> adBankAccounts;

    public Integer getBnkCode() {

        return bnkCode;
    }

    public void setBnkCode(Integer BNK_CODE) {

        this.bnkCode = BNK_CODE;
    }

    public String getBnkName() {

        return bnkName;
    }

    public void setBnkName(String BNK_NM) {

        this.bnkName = BNK_NM;
    }

    public String getBnkBranch() {

        return bnkBranch;
    }

    public void setBnkBranch(String BNK_BRNCH) {

        this.bnkBranch = BNK_BRNCH;
    }

    public long getBnkNumber() {

        return bnkNumber;
    }

    public void setBnkNumber(long BNK_NMBR) {

        this.bnkNumber = BNK_NMBR;
    }

    public String getBnkInstitution() {

        return bnkInstitution;
    }

    public void setBnkInstitution(String BNK_INSTTTN) {

        this.bnkInstitution = BNK_INSTTTN;
    }

    public String getBnkDescription() {

        return bnkDescription;
    }

    public void setBnkDescription(String BNK_DESC) {

        this.bnkDescription = BNK_DESC;
    }

    public String getBnkAddress() {

        return bnkAddress;
    }

    public void setBnkAddress(String BNK_ADDRSS) {

        this.bnkAddress = BNK_ADDRSS;
    }

    public byte getBnkEnable() {

        return bnkEnable;
    }

    public void setBnkEnable(byte BNK_ENBL) {

        this.bnkEnable = BNK_ENBL;
    }

    public Integer getBnkAdCompany() {

        return bnkAdCompany;
    }

    public void setBnkAdCompany(Integer BNK_AD_CMPNY) {

        this.bnkAdCompany = BNK_AD_CMPNY;
    }

    @XmlTransient
    public List getAdBankAccounts() {

        return adBankAccounts;
    }

    public void setAdBankAccounts(List adBankAccounts) {

        this.adBankAccounts = adBankAccounts;
    }

    public void addAdBankAccount(LocalAdBankAccount entity) {

        try {
            entity.setAdBank(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBankAccount(LocalAdBankAccount entity) {

        try {
            entity.setAdBank(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}