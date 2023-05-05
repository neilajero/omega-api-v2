package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlJournalBatch")
@Table(name = "GL_JRNL_BTCH")
public class LocalGlJournalBatch extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JB_CODE", nullable = false)
    private Integer jbCode;

    @Column(name = "JB_NM", columnDefinition = "VARCHAR")
    private String jbName;

    @Column(name = "JB_DESC", columnDefinition = "VARCHAR")
    private String jbDescription;

    @Column(name = "JB_STATUS", columnDefinition = "VARCHAR")
    private String jbStatus;

    @Column(name = "JB_DT_CRTD", columnDefinition = "DATETIME")
    private Date jbDateCreated;

    @Column(name = "JB_CRTD_BY", columnDefinition = "VARCHAR")
    private String jbCreatedBy;

    @Column(name = "JB_AD_BRNCH", columnDefinition = "INT")
    private Integer jbAdBranch;

    @Column(name = "JB_AD_CMPNY", columnDefinition = "INT")
    private Integer jbAdCompany;

    @OneToMany(mappedBy = "glJournalBatch", fetch = FetchType.LAZY)
    private List<LocalGlJournal> glJournals;

    @OneToMany(mappedBy = "glJournalBatch", fetch = FetchType.LAZY)
    private List<LocalGlRecurringJournal> glRecurringJournals;

    public Integer getJbCode() {

        return jbCode;
    }

    public void setJbCode(Integer JB_CODE) {

        this.jbCode = JB_CODE;
    }

    public String getJbName() {

        return jbName;
    }

    public void setJbName(String JB_NM) {

        this.jbName = JB_NM;
    }

    public String getJbDescription() {

        return jbDescription;
    }

    public void setJbDescription(String JB_DESC) {

        this.jbDescription = JB_DESC;
    }

    public String getJbStatus() {

        return jbStatus;
    }

    public void setJbStatus(String JB_STATUS) {

        this.jbStatus = JB_STATUS;
    }

    public Date getJbDateCreated() {

        return jbDateCreated;
    }

    public void setJbDateCreated(Date JB_DT_CRTD) {

        this.jbDateCreated = JB_DT_CRTD;
    }

    public String getJbCreatedBy() {

        return jbCreatedBy;
    }

    public void setJbCreatedBy(String JB_CRTD_BY) {

        this.jbCreatedBy = JB_CRTD_BY;
    }

    public Integer getJbAdBranch() {

        return jbAdBranch;
    }

    public void setJbAdBranch(Integer JB_AD_BRNCH) {

        this.jbAdBranch = JB_AD_BRNCH;
    }

    public Integer getJbAdCompany() {

        return jbAdCompany;
    }

    public void setJbAdCompany(Integer JB_AD_CMPNY) {

        this.jbAdCompany = JB_AD_CMPNY;
    }

    @XmlTransient
    public List getGlJournals() {

        return glJournals;
    }

    public void setGlJournals(List glJournals) {

        this.glJournals = glJournals;
    }

    @XmlTransient
    public List getGlRecurringJournals() {

        return glRecurringJournals;
    }

    public void setGlRecurringJournals(List glRecurringJournals) {

        this.glRecurringJournals = glRecurringJournals;
    }

    public void addGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlJournalBatch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlJournalBatch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlRecurringJournal(LocalGlRecurringJournal entity) {

        try {
            entity.setGlJournalBatch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlRecurringJournal(LocalGlRecurringJournal entity) {

        try {
            entity.setGlJournalBatch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}