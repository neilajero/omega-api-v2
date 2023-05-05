package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "GlTransactionCalendarValue")
@Table(name = "GL_TRNSCTN_CLNDR_VL")
public class LocalGlTransactionCalendarValue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TCV_CODE", nullable = false)
    private Integer tcvCode;

    @Column(name = "TCV_DT", columnDefinition = "DATETIME")
    private Date tcvDate;

    @Column(name = "TCV_DY_OF_WK", columnDefinition = "SMALLINT")
    private short tcvDayOfWeek;

    @Column(name = "TCV_BSNSS_DY", columnDefinition = "TINYINT")
    private byte tcvBusinessDay;

    @Column(name = "TCV_AD_CMPNY", columnDefinition = "INT")
    private Integer tcvAdCompany;

    @JoinColumn(name = "GL_TRANSACTION_CALENDAR", referencedColumnName = "GL_TC_CODE")
    @ManyToOne
    private LocalGlTransactionCalendar glTransactionCalendar;

    public Integer getTcvCode() {

        return tcvCode;
    }

    public void setTcvCode(Integer TCV_CODE) {

        this.tcvCode = TCV_CODE;
    }

    public Date getTcvDate() {

        return tcvDate;
    }

    public void setTcvDate(Date TCV_DT) {

        this.tcvDate = TCV_DT;
    }

    public short getTcvDayOfWeek() {

        return tcvDayOfWeek;
    }

    public void setTcvDayOfWeek(short TCV_DY_OF_WK) {

        this.tcvDayOfWeek = TCV_DY_OF_WK;
    }

    public byte getTcvBusinessDay() {

        return tcvBusinessDay;
    }

    public void setTcvBusinessDay(byte TCV_BSNSS_DY) {

        this.tcvBusinessDay = TCV_BSNSS_DY;
    }

    public Integer getTcvAdCompany() {

        return tcvAdCompany;
    }

    public void setTcvAdCompany(Integer TCV_AD_CMPNY) {

        this.tcvAdCompany = TCV_AD_CMPNY;
    }

    public LocalGlTransactionCalendar getGlTransactionCalendar() {

        return glTransactionCalendar;
    }

    public void setGlTransactionCalendar(LocalGlTransactionCalendar glTransactionCalendar) {

        this.glTransactionCalendar = glTransactionCalendar;
    }

}