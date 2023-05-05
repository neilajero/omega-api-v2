package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlRecurringJournal")
@Table(name = "GL_RCRRNG_JRNL")
public class LocalGlRecurringJournal extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RJ_CODE", nullable = false)
    private Integer rjCode;

    @Column(name = "RJ_NM", columnDefinition = "VARCHAR")
    private String rjName;

    @Column(name = "RJ_DESC", columnDefinition = "VARCHAR")
    private String rjDescription;

    @Column(name = "RJ_USR_NM1", columnDefinition = "VARCHAR")
    private String rjUserName1;

    @Column(name = "RJ_USR_NM2", columnDefinition = "VARCHAR")
    private String rjUserName2;

    @Column(name = "RJ_USR_NM3", columnDefinition = "VARCHAR")
    private String rjUserName3;

    @Column(name = "RJ_USR_NM4", columnDefinition = "VARCHAR")
    private String rjUserName4;

    @Column(name = "RJ_USR_NM5", columnDefinition = "VARCHAR")
    private String rjUserName5;

    @Column(name = "RJ_SCHDL", columnDefinition = "VARCHAR")
    private String rjSchedule;

    @Column(name = "RJ_NXT_RN_DT", columnDefinition = "DATETIME")
    private Date rjNextRunDate;

    @Column(name = "RJ_LST_RN_DT", columnDefinition = "DATETIME")
    private Date rjLastRunDate;

    @Column(name = "RJ_AD_BRNCH", columnDefinition = "INT")
    private Integer rjAdBranch;

    @Column(name = "RJ_AD_CMPNY", columnDefinition = "INT")
    private Integer rjAdCompany;

    @JoinColumn(name = "GL_JOURNAL_BATCH", referencedColumnName = "JB_CODE")
    @ManyToOne
    private LocalGlJournalBatch glJournalBatch;

    @JoinColumn(name = "GL_JOURNAL_CATEGORY", referencedColumnName = "JC_CODE")
    @ManyToOne
    private LocalGlJournalCategory glJournalCategory;

    @OneToMany(mappedBy = "glRecurringJournal", fetch = FetchType.LAZY)
    private List<LocalGlRecurringJournalLine> glRecurringJournalLines;

    public Integer getRjCode() {

        return rjCode;
    }

    public void setRjCode(Integer RJ_CODE) {

        this.rjCode = RJ_CODE;
    }

    public String getRjName() {

        return rjName;
    }

    public void setRjName(String RJ_NM) {

        this.rjName = RJ_NM;
    }

    public String getRjDescription() {

        return rjDescription;
    }

    public void setRjDescription(String RJ_DESC) {

        this.rjDescription = RJ_DESC;
    }

    public String getRjUserName1() {

        return rjUserName1;
    }

    public void setRjUserName1(String RJ_USR_NM1) {

        this.rjUserName1 = RJ_USR_NM1;
    }

    public String getRjUserName2() {

        return rjUserName2;
    }

    public void setRjUserName2(String RJ_USR_NM2) {

        this.rjUserName2 = RJ_USR_NM2;
    }

    public String getRjUserName3() {

        return rjUserName3;
    }

    public void setRjUserName3(String RJ_USR_NM3) {

        this.rjUserName3 = RJ_USR_NM3;
    }

    public String getRjUserName4() {

        return rjUserName4;
    }

    public void setRjUserName4(String RJ_USR_NM4) {

        this.rjUserName4 = RJ_USR_NM4;
    }

    public String getRjUserName5() {

        return rjUserName5;
    }

    public void setRjUserName5(String RJ_USR_NM5) {

        this.rjUserName5 = RJ_USR_NM5;
    }

    public String getRjSchedule() {

        return rjSchedule;
    }

    public void setRjSchedule(String RJ_SCHDL) {

        this.rjSchedule = RJ_SCHDL;
    }

    public Date getRjNextRunDate() {

        return rjNextRunDate;
    }

    public void setRjNextRunDate(Date RJ_NXT_RN_DT) {

        this.rjNextRunDate = RJ_NXT_RN_DT;
    }

    public Date getRjLastRunDate() {

        return rjLastRunDate;
    }

    public void setRjLastRunDate(Date RJ_LST_RN_DT) {

        this.rjLastRunDate = RJ_LST_RN_DT;
    }

    public Integer getRjAdBranch() {

        return rjAdBranch;
    }

    public void setRjAdBranch(Integer RJ_AD_BRNCH) {

        this.rjAdBranch = RJ_AD_BRNCH;
    }

    public Integer getRjAdCompany() {

        return rjAdCompany;
    }

    public void setRjAdCompany(Integer RJ_AD_CMPNY) {

        this.rjAdCompany = RJ_AD_CMPNY;
    }

    public LocalGlJournalBatch getGlJournalBatch() {

        return glJournalBatch;
    }

    public void setGlJournalBatch(LocalGlJournalBatch glJournalBatch) {

        this.glJournalBatch = glJournalBatch;
    }

    public LocalGlJournalCategory getGlJournalCategory() {

        return glJournalCategory;
    }

    public void setGlJournalCategory(LocalGlJournalCategory glJournalCategory) {

        this.glJournalCategory = glJournalCategory;
    }

    @XmlTransient
    public List getGlRecurringJournalLines() {

        return glRecurringJournalLines;
    }

    public void setGlRecurringJournalLines(List glRecurringJournalLines) {

        this.glRecurringJournalLines = glRecurringJournalLines;
    }

    public void addGlRecurringJournalLine(LocalGlRecurringJournalLine entity) {

        try {
            entity.setGlRecurringJournal(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlRecurringJournalLine(LocalGlRecurringJournalLine entity) {

        try {
            entity.setGlRecurringJournal(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}