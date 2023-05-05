package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArReceiptCreditBalance")
@Table(name = "AR_RCPT_CRDT_BLNC")
public class LocalArReceiptCreditBalance extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RCB_CODE", nullable = false)
    private Integer rcbCode;

    @Column(name = "RCB_ADJ_CODE", columnDefinition = "INT")
    private Integer rcbAdjCode;

    @Column(name = "RCB_RCT_CODE", columnDefinition = "INT")
    private Integer rcbRctCode;

    @Column(name = "RCB_AMNT", columnDefinition = "DOUBLE")
    private double rcbAmount = 0;

    public Integer getRcbCode() {

        return rcbCode;
    }

    public void setRcbCode(Integer RCB_CODE) {

        this.rcbCode = RCB_CODE;
    }

    public Integer getRcbAdjCode() {

        return rcbAdjCode;
    }

    public void setRcbAdjCode(Integer RCB_ADJ_CODE) {

        this.rcbAdjCode = RCB_ADJ_CODE;
    }

    public Integer getRcbRctCode() {

        return rcbRctCode;
    }

    public void setRcbRctCode(Integer RCB_RCT_CODE) {

        this.rcbRctCode = RCB_RCT_CODE;
    }

    public double getRcbAmount() {

        return rcbAmount;
    }

    public void setRcbAmount(double RCB_AMNT) {

        this.rcbAmount = RCB_AMNT;
    }

}