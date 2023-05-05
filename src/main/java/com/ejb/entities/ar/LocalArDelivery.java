package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArDelivery")
@Table(name = "AR_DLVRY")
public class LocalArDelivery extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DV_CODE", nullable = false)
    private Integer dvCode;

    @Column(name = "DV_DLVRY_NMBR", columnDefinition = "VARCHAR")
    private String dvDeliveryNumber;

    @Column(name = "DV_CNTNR", columnDefinition = "VARCHAR")
    private String dvContainer;

    @Column(name = "DV_BKNG_TM", columnDefinition = "VARCHAR")
    private String dvBookingTime;

    @Column(name = "DV_TBS_FEE_PLL_OUT", columnDefinition = "VARCHAR")
    private String dvTabsFeePullOut;

    @Column(name = "DV_RLSD_DT", columnDefinition = "VARCHAR")
    private String dvReleasedDate;

    @Column(name = "DV_DLVRD_DT", columnDefinition = "VARCHAR")
    private String dvDeliveredDate;

    @Column(name = "DV_DLVRD_TO", columnDefinition = "VARCHAR")
    private String dvDeliveredTo;

    @Column(name = "DV_PLT_NO", columnDefinition = "VARCHAR")
    private String dvPlateNo;

    @Column(name = "DV_DRVR_NM", columnDefinition = "VARCHAR")
    private String dvDriverName;

    @Column(name = "DV_EMPTY_RTRN_DT", columnDefinition = "VARCHAR")
    private String dvEmptyReturnDate;

    @Column(name = "DV_EMPTY_RTRN_TO", columnDefinition = "VARCHAR")
    private String dvEmptyReturnTo;

    @Column(name = "DV_TBS_FEE_RTRN", columnDefinition = "VARCHAR")
    private String dvTabsFeeReturn;

    @Column(name = "DV_STTS", columnDefinition = "VARCHAR")
    private String dvStatus;

    @Column(name = "DV_RMRKS", columnDefinition = "VARCHAR")
    private String dvRemarks;

    @JoinColumn(name = "AR_SALES_ORDER", referencedColumnName = "SO_CODE")
    @ManyToOne
    private LocalArSalesOrder arSalesOrder;

    public Integer getDvCode() {

        return dvCode;
    }

    public void setDvCode(Integer DV_CODE) {

        this.dvCode = DV_CODE;
    }

    public String getDvDeliveryNumber() {

        return dvDeliveryNumber;
    }

    public void setDvDeliveryNumber(String DV_DLVRY_NMBR) {

        this.dvDeliveryNumber = DV_DLVRY_NMBR;
    }

    public String getDvContainer() {

        return dvContainer;
    }

    public void setDvContainer(String DV_CNTNR) {

        this.dvContainer = DV_CNTNR;
    }

    public String getDvBookingTime() {

        return dvBookingTime;
    }

    public void setDvBookingTime(String DV_BKNG_TM) {

        this.dvBookingTime = DV_BKNG_TM;
    }

    public String getDvTabsFeePullOut() {

        return dvTabsFeePullOut;
    }

    public void setDvTabsFeePullOut(String DV_TBS_FEE_PLL_OUT) {

        this.dvTabsFeePullOut = DV_TBS_FEE_PLL_OUT;
    }

    public String getDvReleasedDate() {

        return dvReleasedDate;
    }

    public void setDvReleasedDate(String DV_RLSD_DT) {

        this.dvReleasedDate = DV_RLSD_DT;
    }

    public String getDvDeliveredDate() {

        return dvDeliveredDate;
    }

    public void setDvDeliveredDate(String DV_DLVRD_DT) {

        this.dvDeliveredDate = DV_DLVRD_DT;
    }

    public String getDvDeliveredTo() {

        return dvDeliveredTo;
    }

    public void setDvDeliveredTo(String DV_DLVRD_TO) {

        this.dvDeliveredTo = DV_DLVRD_TO;
    }

    public String getDvPlateNo() {

        return dvPlateNo;
    }

    public void setDvPlateNo(String DV_PLT_NO) {

        this.dvPlateNo = DV_PLT_NO;
    }

    public String getDvDriverName() {

        return dvDriverName;
    }

    public void setDvDriverName(String DV_DRVR_NM) {

        this.dvDriverName = DV_DRVR_NM;
    }

    public String getDvEmptyReturnDate() {

        return dvEmptyReturnDate;
    }

    public void setDvEmptyReturnDate(String DV_EMPTY_RTRN_DT) {

        this.dvEmptyReturnDate = DV_EMPTY_RTRN_DT;
    }

    public String getDvEmptyReturnTo() {

        return dvEmptyReturnTo;
    }

    public void setDvEmptyReturnTo(String DV_EMPTY_RTRN_TO) {

        this.dvEmptyReturnTo = DV_EMPTY_RTRN_TO;
    }

    public String getDvTabsFeeReturn() {

        return dvTabsFeeReturn;
    }

    public void setDvTabsFeeReturn(String DV_TBS_FEE_RTRN) {

        this.dvTabsFeeReturn = DV_TBS_FEE_RTRN;
    }

    public String getDvStatus() {

        return dvStatus;
    }

    public void setDvStatus(String DV_STTS) {

        this.dvStatus = DV_STTS;
    }

    public String getDvRemarks() {

        return dvRemarks;
    }

    public void setDvRemarks(String DV_RMRKS) {

        this.dvRemarks = DV_RMRKS;
    }

    public LocalArSalesOrder getArSalesOrder() {

        return arSalesOrder;
    }

    public void setArSalesOrder(LocalArSalesOrder arSalesOrder) {

        this.arSalesOrder = arSalesOrder;
    }

}