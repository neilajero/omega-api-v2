package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "InvTransactionalBudget")
@Table(name = "INV_TRNSCTNL_BDGT")
public class LocalInvTransactionalBudget extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TB_CODE", nullable = false)
    private Integer tbCode;

    @Column(name = "TB_NAME", columnDefinition = "VARCHAR")
    private String tbName;

    @Column(name = "TB_DESC", columnDefinition = "VARCHAR")
    private String tbDesc;

    @Column(name = "TB_QNTTY_JAN", columnDefinition = "DOUBLE")
    private double tbQuantityJan = 0;

    @Column(name = "TB_QNTTY_FEB", columnDefinition = "DOUBLE")
    private double tbQuantityFeb = 0;

    @Column(name = "TB_QNTTY_MRCH", columnDefinition = "DOUBLE")
    private double tbQuantityMrch = 0;

    @Column(name = "TB_QNTTY_APRL", columnDefinition = "DOUBLE")
    private double tbQuantityAprl = 0;

    @Column(name = "TB_QNTTY_MAY", columnDefinition = "DOUBLE")
    private double tbQuantityMay = 0;

    @Column(name = "TB_QNTTY_JUN", columnDefinition = "DOUBLE")
    private double tbQuantityJun = 0;

    @Column(name = "TB_QNTTY_JUL", columnDefinition = "DOUBLE")
    private double tbQuantityJul = 0;

    @Column(name = "TB_QNTTY_AUG", columnDefinition = "DOUBLE")
    private double tbQuantityAug = 0;

    @Column(name = "TB_QNTTY_SEP", columnDefinition = "DOUBLE")
    private double tbQuantitySep = 0;

    @Column(name = "TB_QNTTY_OCT", columnDefinition = "DOUBLE")
    private double tbQuantityOct = 0;

    @Column(name = "TB_QNTTY_NOV", columnDefinition = "DOUBLE")
    private double tbQuantityNov = 0;

    @Column(name = "TB_QNTTY_DEC", columnDefinition = "DOUBLE")
    private double tbQuantityDec = 0;

    @Column(name = "TB_AMNT", columnDefinition = "DOUBLE")
    private double tbAmount = 0;

    @Column(name = "TB_TTL_AMNT", columnDefinition = "DOUBLE")
    private double tbTotalAmount = 0;

    @Column(name = "TB_YR", columnDefinition = "INT")
    private Integer tbYear;

    @Column(name = "TB_AD_CMPNY", columnDefinition = "INT")
    private Integer tbAdCompany;

    @Column(name = "AD_LOOK_UP_VALUE", columnDefinition = "INT")
    private Integer tbAdLookupValue;

    @JoinColumn(name = "INV_ITEM", referencedColumnName = "II_CODE")
    @ManyToOne
    private LocalInvItem invItem;

    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;

    public Integer getTbCode() {

        return tbCode;
    }

    public void setTbCode(Integer TB_CODE) {

        this.tbCode = TB_CODE;
    }

    public String getTbName() {

        return tbName;
    }

    public void setTbName(String TB_NAME) {

        this.tbName = TB_NAME;
    }

    public String getTbDesc() {

        return tbDesc;
    }

    public void setTbDesc(String TB_DESC) {

        this.tbDesc = TB_DESC;
    }

    public double getTbQuantityJan() {

        return tbQuantityJan;
    }

    public void setTbQuantityJan(double TB_QNTTY_JAN) {

        this.tbQuantityJan = TB_QNTTY_JAN;
    }

    public double getTbQuantityFeb() {

        return tbQuantityFeb;
    }

    public void setTbQuantityFeb(double TB_QNTTY_FEB) {

        this.tbQuantityFeb = TB_QNTTY_FEB;
    }

    public double getTbQuantityMrch() {

        return tbQuantityMrch;
    }

    public void setTbQuantityMrch(double TB_QNTTY_MRCH) {

        this.tbQuantityMrch = TB_QNTTY_MRCH;
    }

    public double getTbQuantityAprl() {

        return tbQuantityAprl;
    }

    public void setTbQuantityAprl(double TB_QNTTY_APRL) {

        this.tbQuantityAprl = TB_QNTTY_APRL;
    }

    public double getTbQuantityMay() {

        return tbQuantityMay;
    }

    public void setTbQuantityMay(double TB_QNTTY_MAY) {

        this.tbQuantityMay = TB_QNTTY_MAY;
    }

    public double getTbQuantityJun() {

        return tbQuantityJun;
    }

    public void setTbQuantityJun(double TB_QNTTY_JUN) {

        this.tbQuantityJun = TB_QNTTY_JUN;
    }

    public double getTbQuantityJul() {

        return tbQuantityJul;
    }

    public void setTbQuantityJul(double TB_QNTTY_JUL) {

        this.tbQuantityJul = TB_QNTTY_JUL;
    }

    public double getTbQuantityAug() {

        return tbQuantityAug;
    }

    public void setTbQuantityAug(double TB_QNTTY_AUG) {

        this.tbQuantityAug = TB_QNTTY_AUG;
    }

    public double getTbQuantitySep() {

        return tbQuantitySep;
    }

    public void setTbQuantitySep(double TB_QNTTY_SEP) {

        this.tbQuantitySep = TB_QNTTY_SEP;
    }

    public double getTbQuantityOct() {

        return tbQuantityOct;
    }

    public void setTbQuantityOct(double TB_QNTTY_OCT) {

        this.tbQuantityOct = TB_QNTTY_OCT;
    }

    public double getTbQuantityNov() {

        return tbQuantityNov;
    }

    public void setTbQuantityNov(double TB_QNTTY_NOV) {

        this.tbQuantityNov = TB_QNTTY_NOV;
    }

    public double getTbQuantityDec() {

        return tbQuantityDec;
    }

    public void setTbQuantityDec(double TB_QNTTY_DEC) {

        this.tbQuantityDec = TB_QNTTY_DEC;
    }

    public double getTbAmount() {

        return tbAmount;
    }

    public void setTbAmount(double TB_AMNT) {

        this.tbAmount = TB_AMNT;
    }

    public double getTbTotalAmount() {

        return tbTotalAmount;
    }

    public void setTbTotalAmount(double TB_TTL_AMNT) {

        this.tbTotalAmount = TB_TTL_AMNT;
    }

    public Integer getTbYear() {

        return tbYear;
    }

    public void setTbYear(Integer TB_YR) {

        this.tbYear = TB_YR;
    }

    public Integer getTbAdCompany() {

        return tbAdCompany;
    }

    public void setTbAdCompany(Integer TB_AD_CMPNY) {

        this.tbAdCompany = TB_AD_CMPNY;
    }

    public Integer getTbAdLookupValue() {

        return tbAdLookupValue;
    }

    public void setTbAdLookupValue(Integer AD_LOOK_UP_VALUE) {

        this.tbAdLookupValue = AD_LOOK_UP_VALUE;
    }

    public LocalInvItem getInvItem() {

        return invItem;
    }

    public void setInvItem(LocalInvItem invItem) {

        this.invItem = invItem;
    }

    public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

        return invUnitOfMeasure;
    }

    public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

        this.invUnitOfMeasure = invUnitOfMeasure;
    }

}