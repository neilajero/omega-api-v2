package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlSetOfBook")
@Table(name = "GL_SOB")
public class LocalGlSetOfBook extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOB_CODE", nullable = false)
    private Integer sobCode;

    @Column(name = "SOB_YR_END_CLSD", columnDefinition = "TINYINT")
    private byte sobYearEndClosed;

    @Column(name = "SOB_AD_CMPNY", columnDefinition = "INT")
    private Integer sobAdCompany;

    @JoinColumn(name = "GL_ACCOUNTING_CALENDAR", referencedColumnName = "GL_AC_CODE")
    @ManyToOne
    private LocalGlAccountingCalendar glAccountingCalendar;

    @JoinColumn(name = "GL_TRANSACTION_CALENDAR", referencedColumnName = "GL_TC_CODE")
    @ManyToOne
    private LocalGlTransactionCalendar glTransactionCalendar;

    public Integer getSobCode() {

        return sobCode;
    }

    public void setSobCode(Integer SOB_CODE) {

        this.sobCode = SOB_CODE;
    }

    public byte getSobYearEndClosed() {

        return sobYearEndClosed;
    }

    public void setSobYearEndClosed(byte SOB_YR_END_CLSD) {

        this.sobYearEndClosed = SOB_YR_END_CLSD;
    }

    public Integer getSobAdCompany() {

        return sobAdCompany;
    }

    public void setSobAdCompany(Integer SOB_AD_CMPNY) {

        this.sobAdCompany = SOB_AD_CMPNY;
    }

    public LocalGlAccountingCalendar getGlAccountingCalendar() {

        return glAccountingCalendar;
    }

    public void setGlAccountingCalendar(LocalGlAccountingCalendar glAccountingCalendar) {

        this.glAccountingCalendar = glAccountingCalendar;
    }

    public LocalGlTransactionCalendar getGlTransactionCalendar() {

        return glTransactionCalendar;
    }

    public void setGlTransactionCalendar(LocalGlTransactionCalendar glTransactionCalendar) {

        this.glTransactionCalendar = glTransactionCalendar;
    }

}