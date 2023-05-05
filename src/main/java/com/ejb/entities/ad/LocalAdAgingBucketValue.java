package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdAgingBucketValue")
@Table(name = "AD_AGNG_BCKT_VL")
public class LocalAdAgingBucketValue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AD_AV_CODE", nullable = false)
    private Integer avCode;

    @Column(name = "AV_SQNC_NMBR", columnDefinition = "BIGINT")
    private short avSequenceNumber;

    @Column(name = "AV_TYP", columnDefinition = "VARCHAR")
    private String avType;

    @Column(name = "AV_DYS_FRM", columnDefinition = "SMALLINT")
    private long avDaysFrom;

    @Column(name = "AV_DYS_TO", columnDefinition = "SMALLINT")
    private long avDaysTo;

    @Column(name = "AV_DESC", columnDefinition = "VARCHAR")
    private String avDescription;

    @Column(name = "AV_AD_CMPNY", columnDefinition = "INT")
    private Integer avAdCompany;

    @JoinColumn(name = "AD_AGING_BUCKET", referencedColumnName = "AB_CODE")
    @ManyToOne
    private LocalAdAgingBucket adAgingBucket;

    public Integer getAvCode() {

        return avCode;
    }

    public void setAvCode(Integer AD_AV_CODE) {

        this.avCode = AD_AV_CODE;
    }

    public short getAvSequenceNumber() {

        return avSequenceNumber;
    }

    public void setAvSequenceNumber(short AV_SQNC_NMBR) {

        this.avSequenceNumber = AV_SQNC_NMBR;
    }

    public String getAvType() {

        return avType;
    }

    public void setAvType(String AV_TYP) {

        this.avType = AV_TYP;
    }

    public long getAvDaysFrom() {

        return avDaysFrom;
    }

    public void setAvDaysFrom(long AV_DYS_FRM) {

        this.avDaysFrom = AV_DYS_FRM;
    }

    public long getAvDaysTo() {

        return avDaysTo;
    }

    public void setAvDaysTo(long AV_DYS_TO) {

        this.avDaysTo = AV_DYS_TO;
    }

    public String getAvDescription() {

        return avDescription;
    }

    public void setAvDescription(String AV_DESC) {

        this.avDescription = AV_DESC;
    }

    public Integer getAvAdCompany() {

        return avAdCompany;
    }

    public void setAvAdCompany(Integer AV_AD_CMPNY) {

        this.avAdCompany = AV_AD_CMPNY;
    }

    public LocalAdAgingBucket getAdAgingBucket() {

        return adAgingBucket;
    }

    public void setAdAgingBucket(LocalAdAgingBucket adAgingBucket) {

        this.adAgingBucket = adAgingBucket;
    }

}