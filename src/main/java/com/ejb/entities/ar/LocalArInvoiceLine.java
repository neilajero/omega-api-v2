package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArInvoiceLine")
@Table(name = "AR_INVC_LN")
public class LocalArInvoiceLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_IL_CODE", nullable = false)
    private Integer ilCode;

    @Column(name = "IL_DESC", columnDefinition = "VARCHAR")
    private String ilDescription;

    @Column(name = "IL_QNTTY", columnDefinition = "DOUBLE")
    private double ilQuantity = 0;

    @Column(name = "IL_UNT_PRC", columnDefinition = "DOUBLE")
    private double ilUnitPrice = 0;

    @Column(name = "IL_AMNT", columnDefinition = "DOUBLE")
    private double ilAmount = 0;

    @Column(name = "IL_TX_AMNT", columnDefinition = "DOUBLE")
    private double ilTaxAmount = 0;

    @Column(name = "IL_TX", columnDefinition = "TINYINT")
    private byte ilTax;

    @Column(name = "IL_AD_CMPNY", columnDefinition = "INT")
    private Integer ilAdCompany;

    @JoinColumn(name = "AR_INVOICE", referencedColumnName = "INV_CODE")
    @ManyToOne
    private LocalArInvoice arInvoice;

    @JoinColumn(name = "AR_PDC", referencedColumnName = "PDC_CODE")
    @ManyToOne
    private LocalArPdc arPdc;

    @JoinColumn(name = "AR_RECEIPT", referencedColumnName = "RCT_CODE")
    @ManyToOne
    private LocalArReceipt arReceipt;

    @JoinColumn(name = "AR_STANDARD_MEMO_LINE", referencedColumnName = "SML_CODE")
    @ManyToOne
    private LocalArStandardMemoLine arStandardMemoLine;

    public Integer getIlCode() {

        return ilCode;
    }

    public void setIlCode(Integer AR_IL_CODE) {

        this.ilCode = AR_IL_CODE;
    }

    public String getIlDescription() {

        return ilDescription;
    }

    public void setIlDescription(String IL_DESC) {

        this.ilDescription = IL_DESC;
    }

    public double getIlQuantity() {

        return ilQuantity;
    }

    public void setIlQuantity(double IL_QNTTY) {

        this.ilQuantity = IL_QNTTY;
    }

    public double getIlUnitPrice() {

        return ilUnitPrice;
    }

    public void setIlUnitPrice(double IL_UNT_PRC) {

        this.ilUnitPrice = IL_UNT_PRC;
    }

    public double getIlAmount() {

        return ilAmount;
    }

    public void setIlAmount(double IL_AMNT) {

        this.ilAmount = IL_AMNT;
    }

    public double getIlTaxAmount() {

        return ilTaxAmount;
    }

    public void setIlTaxAmount(double IL_TX_AMNT) {

        this.ilTaxAmount = IL_TX_AMNT;
    }

    public byte getIlTax() {

        return ilTax;
    }

    public void setIlTax(byte IL_TX) {

        this.ilTax = IL_TX;
    }

    public Integer getIlAdCompany() {

        return ilAdCompany;
    }

    public void setIlAdCompany(Integer IL_AD_CMPNY) {

        this.ilAdCompany = IL_AD_CMPNY;
    }

    public LocalArInvoice getArInvoice() {

        return arInvoice;
    }

    public void setArInvoice(LocalArInvoice arInvoice) {

        this.arInvoice = arInvoice;
    }

    public LocalArPdc getArPdc() {

        return arPdc;
    }

    public void setArPdc(LocalArPdc arPdc) {

        this.arPdc = arPdc;
    }

    public LocalArReceipt getArReceipt() {

        return arReceipt;
    }

    public void setArReceipt(LocalArReceipt arReceipt) {

        this.arReceipt = arReceipt;
    }

    public LocalArStandardMemoLine getArStandardMemoLine() {

        return arStandardMemoLine;
    }

    public void setArStandardMemoLine(LocalArStandardMemoLine arStandardMemoLine) {

        this.arStandardMemoLine = arStandardMemoLine;
    }

}