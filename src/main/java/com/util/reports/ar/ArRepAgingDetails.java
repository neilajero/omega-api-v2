/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Comparator;
import java.util.Date;

public class ArRepAgingDetails implements java.io.Serializable {

  private String AG_CSTMR_CODE;
  private String AG_CSTMR_NM;
  private String AG_SLSPRSN;
  private String AG_INVC_NMBR;
  private String AG_RFRNC_NMBR;
  private short AG_INSTLLMNT_NMBR;
  private double AG_INSTLLMNT_AMNT;
  private String AG_CSTMR_TYPE;
  private String AG_CSTMR_CLASS;
  private double AG_AMNT;
  private Date AG_LST_OR_DT;
  private double AG_AGD_IN_MNTH;
  private double AG_BCKT0;
  private double AG_BCKT1;
  private double AG_BCKT2;
  private double AG_BCKT3;
  private double AG_BCKT4;
  private double AG_BCKT5;
  private String AG_ORDER_BY;
  private String AG_GROUP_BY;
  private String AG_DSCRPTN;

  // additional fields
  private Date AG_TRNSCTN_DT;
  private Date AG_IPS_DUE_DT;
  private double AG_INVC_AGE;
  private char AG_VOU_FC_SYMBL;

  public ArRepAgingDetails() {
  }

  public String getAgCustomerCode() {

    return AG_CSTMR_CODE;
  }

  public void setAgCustomerCode(String AG_CSTMR_CODE) {

    this.AG_CSTMR_CODE = AG_CSTMR_CODE;
  }

  public String getAgCustomerName() {

    return AG_CSTMR_NM;
  }

  public void setAgCustomerName(String AG_CSTMR_NM) {

    this.AG_CSTMR_NM = AG_CSTMR_NM;
  }

  public String getAgSalesPerson() {

    return AG_SLSPRSN;
  }

  public void setAgSalesPerson(String AG_SLSPRSN) {

    this.AG_SLSPRSN = AG_SLSPRSN;
  }

  public String getAgInvoiceNumber() {

    return AG_INVC_NMBR;
  }

  public void setAgInvoiceNumber(String AG_INVC_NMBR) {

    this.AG_INVC_NMBR = AG_INVC_NMBR;
  }

  public String getAgReferenceNumber() {

    return AG_RFRNC_NMBR;
  }

  public void setAgReferenceNumber(String AG_RFRNC_NMBR) {

    this.AG_RFRNC_NMBR = AG_RFRNC_NMBR;
  }

  public short getAgInstallmentNumber() {

    return AG_INSTLLMNT_NMBR;
  }

  public void setAgInstallmentNumber(short AG_INSTLLMNT_NMBR) {

    this.AG_INSTLLMNT_NMBR = AG_INSTLLMNT_NMBR;
  }

  public double getAgInstallmentAmount() {

    return AG_INSTLLMNT_AMNT;
  }

  public void setAgInstallmentAmount(double AG_INSTLLMNT_AMNT) {

    this.AG_INSTLLMNT_AMNT = AG_INSTLLMNT_AMNT;
  }

  public double getAgAmount() {

    return AG_AMNT;
  }

  public void setAgAmount(double AG_AMNT) {

    this.AG_AMNT = AG_AMNT;
  }

  public Date getAgLastOrDate() {

    return AG_LST_OR_DT;
  }

  public void setAgLastOrDate(Date AG_LST_OR_DT) {

    this.AG_LST_OR_DT = AG_LST_OR_DT;
  }

  public double getAgAgedInMonth() {

    return AG_AGD_IN_MNTH;
  }

  public void setAgAgedInMonth(double AG_AGD_IN_MNTH) {

    this.AG_AGD_IN_MNTH = AG_AGD_IN_MNTH;
  }

  public double getAgBucket0() {

    return AG_BCKT0;
  }

  public void setAgBucket0(double AG_BCKT0) {

    this.AG_BCKT0 = AG_BCKT0;
  }

  public double getAgBucket1() {

    return AG_BCKT1;
  }

  public void setAgBucket1(double AG_BCKT1) {

    this.AG_BCKT1 = AG_BCKT1;
  }

  public double getAgBucket2() {

    return AG_BCKT2;
  }

  public void setAgBucket2(double AG_BCKT2) {

    this.AG_BCKT2 = AG_BCKT2;
  }

  public double getAgBucket3() {

    return AG_BCKT3;
  }

  public void setAgBucket3(double AG_BCKT3) {

    this.AG_BCKT3 = AG_BCKT3;
  }

  public double getAgBucket4() {

    return AG_BCKT4;
  }

  public void setAgBucket4(double AG_BCKT4) {

    this.AG_BCKT4 = AG_BCKT4;
  }

  public double getAgBucket5() {

    return AG_BCKT5;
  }

  public void setAgBucket5(double AG_BCKT5) {

    this.AG_BCKT5 = AG_BCKT5;
  }

  public Date getAgTransactionDate() {

    return AG_TRNSCTN_DT;
  }

  public void setAgTransactionDate(Date AG_TRNSCTN_DT) {

    this.AG_TRNSCTN_DT = AG_TRNSCTN_DT;
  }

  public String getAgCustomerType() {

    return AG_CSTMR_TYPE;
  }

  public void setAgCustomerType(String AG_CSTMR_TYPE) {

    this.AG_CSTMR_TYPE = AG_CSTMR_TYPE;
  }

  public String getAgCustomerClass() {

    return AG_CSTMR_CLASS;
  }

  public void setAgCustomerClass(String AG_CSTMR_CLASS) {

    this.AG_CSTMR_CLASS = AG_CSTMR_CLASS;
  }

  public String getOrderBy() {

    return AG_ORDER_BY;
  }

  public void setOrderBy(String AG_ORDER_BY) {

    this.AG_ORDER_BY = AG_ORDER_BY;
  }

  public String getGroupBy() {

    return AG_GROUP_BY;
  }

  public void setGroupBy(String AG_GROUP_BY) {

    this.AG_GROUP_BY = AG_GROUP_BY;
  }

  public Date getAgIpsDueDate() {

    return AG_IPS_DUE_DT;
  }

  public void setAgIpsDueDate(Date AG_IPS_DUE_DT) {

    this.AG_IPS_DUE_DT = AG_IPS_DUE_DT;
  }

  public double getAgInvoiceAge() {

    return AG_INVC_AGE;
  }

  public void setAgInvoiceAge(double AG_INVC_AGE) {

    this.AG_INVC_AGE = AG_INVC_AGE;
  }

  public char getAgVouFcSymbol() {

    return AG_VOU_FC_SYMBL;
  }

  public void setAgVouFcSymbol(char AG_VOU_FC_SYMBL) {

    this.AG_VOU_FC_SYMBL = AG_VOU_FC_SYMBL;
  }

  public String getAgDescription() {

    return AG_DSCRPTN;
  }

  public void setAgDescription(String AG_DSCRPTN) {

    this.AG_DSCRPTN = AG_DSCRPTN;
  }

  public static Comparator SARComparator =
          (AG, anotherAG) -> {

            String AG_CUST_CSTMR_NM1 = ((ArRepAgingDetails) AG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP1 = ((ArRepAgingDetails) AG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS1 = ((ArRepAgingDetails) AG).getAgCustomerClass();
            Date AG_DT1 = ((ArRepAgingDetails) AG).getAgTransactionDate();
            String AG_INVC_NMBR1 = ((ArRepAgingDetails) AG).getAgInvoiceNumber();
            String AG_SLS_PRSN1 = ((ArRepAgingDetails) AG).getAgSalesPerson();

            String AG_CUST_CSTMR_NM2 = ((ArRepAgingDetails) anotherAG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP2 = ((ArRepAgingDetails) anotherAG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS2 = ((ArRepAgingDetails) anotherAG).getAgCustomerClass();
            Date AG_DT2 = ((ArRepAgingDetails) anotherAG).getAgTransactionDate();
            String AG_INVC_NMBR2 = ((ArRepAgingDetails) anotherAG).getAgInvoiceNumber();
            String AG_SLS_PRSN2 = ((ArRepAgingDetails) anotherAG).getAgSalesPerson();

            String ORDER_BY = ((ArRepAgingDetails) AG).getOrderBy();

            if (!(AG_SLS_PRSN1.equals(AG_SLS_PRSN2))) {

              return AG_SLS_PRSN1.compareTo(AG_SLS_PRSN2);

            } else {

              return AG_INVC_NMBR1.compareTo(AG_INVC_NMBR2);
            }
          };

  public static Comparator CustomerCodeComparator =
          (AG, anotherAG) -> {
            String AG_CUST_CSTMR_CD1 = ((ArRepAgingDetails) AG).getAgCustomerCode();
            String AG_CUST_CSTMR_NM1 = ((ArRepAgingDetails) AG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP1 = ((ArRepAgingDetails) AG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS1 = ((ArRepAgingDetails) AG).getAgCustomerClass();
            Date AG_DT1 = ((ArRepAgingDetails) AG).getAgTransactionDate();
            String AG_INVC_NMBR1 = ((ArRepAgingDetails) AG).getAgInvoiceNumber();

            String AG_CUST_CSTMR_CD2 = ((ArRepAgingDetails) anotherAG).getAgCustomerCode();
            String AG_CUST_CSTMR_NM2 = ((ArRepAgingDetails) anotherAG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP2 = ((ArRepAgingDetails) anotherAG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS2 = ((ArRepAgingDetails) anotherAG).getAgCustomerClass();
            Date AG_DT2 = ((ArRepAgingDetails) anotherAG).getAgTransactionDate();
            String AG_INVC_NMBR2 = ((ArRepAgingDetails) anotherAG).getAgInvoiceNumber();

            String ORDER_BY = ((ArRepAgingDetails) AG).getOrderBy();

            if (!(AG_CUST_CSTMR_CD1.equals(AG_CUST_CSTMR_CD2))) {

              return AG_CUST_CSTMR_CD1.compareTo(AG_CUST_CSTMR_CD2);

            } else {

              if (ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

                return AG_DT1.compareTo(AG_DT2);

              } else if (ORDER_BY.equals("CUSTOMER NAME")
                  && !(AG_CUST_CSTMR_NM1.equals(AG_CUST_CSTMR_NM2))) {

                return AG_CUST_CSTMR_NM1.compareTo(AG_CUST_CSTMR_NM2);

              } else if (ORDER_BY.equals("CUSTOMER TYPE")
                  && !(AG_CUST_CSTMR_TYP1.equals(AG_CUST_CSTMR_TYP2))) {

                return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

              } else {

                return AG_INVC_NMBR1.compareTo(AG_INVC_NMBR2);
              }
            }
          };

  public static Comparator CustomerNameComparator =
          (AG, anotherAG) -> {

            String AG_CUST_CSTMR_NM1 = ((ArRepAgingDetails) AG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP1 = ((ArRepAgingDetails) AG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS1 = ((ArRepAgingDetails) AG).getAgCustomerClass();
            Date AG_DT1 = ((ArRepAgingDetails) AG).getAgTransactionDate();
            String AG_INVC_NMBR1 = ((ArRepAgingDetails) AG).getAgInvoiceNumber();

            String AG_CUST_CSTMR_NM2 = ((ArRepAgingDetails) anotherAG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP2 = ((ArRepAgingDetails) anotherAG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS2 = ((ArRepAgingDetails) anotherAG).getAgCustomerClass();
            Date AG_DT2 = ((ArRepAgingDetails) anotherAG).getAgTransactionDate();
            String AG_INVC_NMBR2 = ((ArRepAgingDetails) anotherAG).getAgInvoiceNumber();

            String ORDER_BY = ((ArRepAgingDetails) AG).getOrderBy();

            if (!(AG_CUST_CSTMR_NM1.equals(AG_CUST_CSTMR_NM2))) {

              return AG_CUST_CSTMR_NM1.compareTo(AG_CUST_CSTMR_NM2);

            } else {

              if (ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

                return AG_DT1.compareTo(AG_DT2);

              } else if (ORDER_BY.equals("CUSTOMER NAME")
                  && !(AG_CUST_CSTMR_NM1.equals(AG_CUST_CSTMR_NM2))) {

                return AG_CUST_CSTMR_NM1.compareTo(AG_CUST_CSTMR_NM2);

              } else if (ORDER_BY.equals("CUSTOMER TYPE")
                  && !(AG_CUST_CSTMR_TYP1.equals(AG_CUST_CSTMR_TYP2))) {

                return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

              } else {

                return AG_INVC_NMBR1.compareTo(AG_INVC_NMBR2);
              }
            }
          };

  public static Comparator CustomerTypeComparator =
          (AG, anotherAG) -> {

            String AG_CUST_CSTMR_NM1 = ((ArRepAgingDetails) AG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP1 = ((ArRepAgingDetails) AG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS1 = ((ArRepAgingDetails) AG).getAgCustomerClass();
            Date AG_DT1 = ((ArRepAgingDetails) AG).getAgTransactionDate();
            String AG_INVC_NMBR1 = ((ArRepAgingDetails) AG).getAgInvoiceNumber();

            String AG_CUST_CSTMR_NM2 = ((ArRepAgingDetails) anotherAG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP2 = ((ArRepAgingDetails) anotherAG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS2 = ((ArRepAgingDetails) anotherAG).getAgCustomerClass();
            Date AG_DT2 = ((ArRepAgingDetails) anotherAG).getAgTransactionDate();
            String AG_INVC_NMBR2 = ((ArRepAgingDetails) anotherAG).getAgInvoiceNumber();

            String ORDER_BY = ((ArRepAgingDetails) AG).getOrderBy();

            if (!(AG_CUST_CSTMR_TYP1.equals(AG_CUST_CSTMR_TYP2))) {

              return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

            } else {

              if (ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

                return AG_DT1.compareTo(AG_DT2);

              } else if (ORDER_BY.equals("CUSTOMER NAME")
                  && !(AG_CUST_CSTMR_NM1.equals(AG_CUST_CSTMR_NM2))) {

                return AG_CUST_CSTMR_NM1.compareTo(AG_CUST_CSTMR_NM2);

              } else if (ORDER_BY.equals("CUSTOMER TYPE")
                  && !(AG_CUST_CSTMR_TYP1.equals(AG_CUST_CSTMR_TYP2))) {

                return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

              } else {

                return AG_INVC_NMBR1.compareTo(AG_INVC_NMBR2);
              }
            }
          };

  public static Comparator CustomerClassComparator =
          (AG, anotherAG) -> {

            String AG_CUST_CSTMR_NM1 = ((ArRepAgingDetails) AG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP1 = ((ArRepAgingDetails) AG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS1 = ((ArRepAgingDetails) AG).getAgCustomerClass();
            Date AG_DT1 = ((ArRepAgingDetails) AG).getAgTransactionDate();
            String AG_INVC_NMBR1 = ((ArRepAgingDetails) AG).getAgInvoiceNumber();

            String AG_CUST_CSTMR_NM2 = ((ArRepAgingDetails) anotherAG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP2 = ((ArRepAgingDetails) anotherAG).getAgCustomerType();
            String AG_CUST_CSTMR_CLSS2 = ((ArRepAgingDetails) anotherAG).getAgCustomerClass();
            Date AG_DT2 = ((ArRepAgingDetails) anotherAG).getAgTransactionDate();
            String AG_INVC_NMBR2 = ((ArRepAgingDetails) anotherAG).getAgInvoiceNumber();

            String ORDER_BY = ((ArRepAgingDetails) AG).getOrderBy();

            if (!(AG_CUST_CSTMR_CLSS1.equals(AG_CUST_CSTMR_CLSS2))) {

              return AG_CUST_CSTMR_CLSS1.compareTo(AG_CUST_CSTMR_CLSS2);

            } else {

              if (ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

                return AG_DT1.compareTo(AG_DT2);

              } else if (ORDER_BY.equals("CUSTOMER NAME")
                  && !(AG_CUST_CSTMR_NM1.equals(AG_CUST_CSTMR_NM2))) {

                return AG_CUST_CSTMR_NM1.compareTo(AG_CUST_CSTMR_NM2);

              } else if (ORDER_BY.equals("CUSTOMER TYPE")
                  && !(AG_CUST_CSTMR_TYP1.equals(AG_CUST_CSTMR_TYP2))) {

                return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

              } else {

                return AG_INVC_NMBR1.compareTo(AG_INVC_NMBR2);
              }
            }
          };

  public static Comparator NoClassComparator =
          (AG, anotherAG) -> {

            String AG_CUST_CSTMR_NM1 = ((ArRepAgingDetails) AG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP1 = ((ArRepAgingDetails) AG).getAgCustomerType();
            Date AG_DT1 = ((ArRepAgingDetails) AG).getAgTransactionDate();
            String AG_INVC_NMBR1 = ((ArRepAgingDetails) AG).getAgInvoiceNumber();

            String AG_CUST_CSTMR_NM2 = ((ArRepAgingDetails) anotherAG).getAgCustomerName();
            String AG_CUST_CSTMR_TYP2 = ((ArRepAgingDetails) anotherAG).getAgCustomerType();
            Date AG_DT2 = ((ArRepAgingDetails) anotherAG).getAgTransactionDate();
            String AG_INVC_NMBR2 = ((ArRepAgingDetails) anotherAG).getAgInvoiceNumber();

            String ORDER_BY =
                ((ArRepAgingDetails) AG).getOrderBy() == null
                    ? ""
                    : ((ArRepAgingDetails) AG).getOrderBy();

            if (ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

              return AG_DT1.compareTo(AG_DT2);

            } else if (ORDER_BY.equals("CUSTOMER NAME")
                && !(AG_CUST_CSTMR_NM1.equals(AG_CUST_CSTMR_NM2))) {

              return AG_CUST_CSTMR_NM1.compareTo(AG_CUST_CSTMR_NM2);

            } else if (ORDER_BY.equals("CUSTOMER TYPE")
                && !(AG_CUST_CSTMR_TYP1.equals(AG_CUST_CSTMR_TYP2))) {

              return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

            } else {

              return AG_INVC_NMBR1.compareTo(AG_INVC_NMBR2);
            }
          };
} // ArRepAgingDetails class