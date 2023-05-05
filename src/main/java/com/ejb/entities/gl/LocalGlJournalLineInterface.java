package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlJournalLineInterface")
@Table(name = "GL_JRNL_LN_INTRFC")
public class LocalGlJournalLineInterface extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JLI_CODE", nullable = false)
    private Integer jliCode;

    @Column(name = "JLI_LN_NMBR", columnDefinition = "SMALLINT")
    private short jliLineNumber;

    @Column(name = "JLI_DBT", columnDefinition = "TINYINT")
    private byte jliDebit;

    @Column(name = "JLI_AMNT", columnDefinition = "DOUBLE")
    private double jliAmount = 0;

    @Column(name = "JLI_COA_ACCNT_NMBR", columnDefinition = "VARCHAR")
    private String jliCoaAccountNumber;

    @Column(name = "JLI_AD_CMPNY", columnDefinition = "INT")
    private Integer jliAdCompany;

    @JoinColumn(name = "GL_JOURNAL_INTERFACE", referencedColumnName = "JRI_CODE")
    @ManyToOne
    private LocalGlJournalInterface glJournalInterface;

    public Integer getJliCode() {

        return jliCode;
    }

    public void setJliCode(Integer JLI_CODE) {

        this.jliCode = JLI_CODE;
    }

    public short getJliLineNumber() {

        return jliLineNumber;
    }

    public void setJliLineNumber(short JLI_LN_NMBR) {

        this.jliLineNumber = JLI_LN_NMBR;
    }

    public byte getJliDebit() {

        return jliDebit;
    }

    public void setJliDebit(byte JLI_DBT) {

        this.jliDebit = JLI_DBT;
    }

    public double getJliAmount() {

        return jliAmount;
    }

    public void setJliAmount(double JLI_AMNT) {

        this.jliAmount = JLI_AMNT;
    }

    public String getJliCoaAccountNumber() {

        return jliCoaAccountNumber;
    }

    public void setJliCoaAccountNumber(String JLI_COA_ACCNT_NMBR) {

        this.jliCoaAccountNumber = JLI_COA_ACCNT_NMBR;
    }

    public Integer getJliAdCompany() {

        return jliAdCompany;
    }

    public void setJliAdCompany(Integer JLI_AD_CMPNY) {

        this.jliAdCompany = JLI_AD_CMPNY;
    }

    public LocalGlJournalInterface getGlJournalInterface() {

        return glJournalInterface;
    }

    public void setGlJournalInterface(LocalGlJournalInterface glJournalInterface) {

        this.glJournalInterface = glJournalInterface;
    }

}