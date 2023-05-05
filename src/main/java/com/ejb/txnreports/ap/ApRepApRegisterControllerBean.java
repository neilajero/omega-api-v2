package com.ejb.txnreports.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ap.*;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepApRegisterDetails;

import jakarta.ejb.*;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.*;

@Stateless(name = "ApRepApRegisterControllerEJB")
public class ApRepApRegisterControllerBean extends EJBContextClass implements ApRepApRegisterController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApSupplierBalanceHome apSupplierBalanceHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;


    public void executeSpApRepRegister(String STORED_PROCEDURE, String SUPPLIER_CODE, Date DT_FRM, Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_PAYMENT, boolean INCLUDE_DIRECT_CHECK, String BRANCH_CODES, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepApRegisterControllerBean executeSpApRepRegister");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(STORED_PROCEDURE).registerStoredProcedureParameter("supplierCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("dateFrom", Date.class, ParameterMode.IN).registerStoredProcedureParameter("dateTo", Date.class, ParameterMode.IN).registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("includePayment", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("includeDirectCheck", Boolean.class, ParameterMode.IN).registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN).registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN).registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("supplierCode", SUPPLIER_CODE);
            spQuery.setParameter("dateFrom", DT_FRM);
            spQuery.setParameter("dateTo", DT_TO);
            spQuery.setParameter("includeUnposted", INCLUDE_UNPOSTED);
            spQuery.setParameter("includePayment", INCLUDE_PAYMENT);
            spQuery.setParameter("includeDirectCheck", INCLUDE_DIRECT_CHECK);
            spQuery.setParameter("branchCode", BRANCH_CODES);
            spQuery.setParameter("adCompany", AD_CMPNY);

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");

            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

    }

    // Execute AP Register
    public ArrayList executeApRepApRegister(HashMap criteria, String ORDER_BY, String GROUP_BY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean INCLUDE_DC, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepApRegisterControllerBean executeApRepApRegister");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Date dateFrom = null;
            Date dateLast = null;
            int dtChck = 0;
            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedPayments")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            jbossQl.append("SELECT OBJECT(vou) FROM ApVoucher vou ");

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("voucherBatch")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apVoucherBatch.vbName = '").append(criteria.get("voucherBatch")).append("' ");
                obj[ctr] = criteria.get("voucherBatch");
                ctr++;
            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.apSupplierType.stName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierType");
                ctr++;
            }

            if (criteria.containsKey("supplierClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("vou.vouPosted = 1 ");

                } else {

                    jbossQl.append("vou.vouVoid = 0 ");
                }
            }

            if (criteria.containsKey("paymentStatus")) {

                String paymentStatus = (String) criteria.get("paymentStatus");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                if (paymentStatus.equals("PAID")) {

                    jbossQl.append("vou.vouAmountDue = vou.vouAmountPaid ");

                } else if (paymentStatus.equals("UNPAID")) {

                    jbossQl.append("vou.vouAmountDue <> vou.vouAmountPaid ");
                }
            }

            if (adBrnchList.isEmpty()) {
                System.out.print("dito");
                throw new GlobalNoRecordFoundException();

            } else {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouAdCompany = ").append(AD_CMPNY).append(" ");
            Collection apVouchers = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);

            if (SHOW_ENTRIES) {

                for (Object voucher : apVouchers) {

                    boolean first = true;

                    LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                    Collection apDistributionRecords = apVoucher.getApDistributionRecords();

                    for (Object distributionRecord : apDistributionRecords) {
                        dtChck = dtChck + 1;
                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                        ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();

                        if (dtChck == 1 && criteria.get("dateFrom") == null) {
                            dateFrom = apVoucher.getVouDate();
                        }
                        dateLast = apVoucher.getVouDate();
                        mdetails.setArgDate(apVoucher.getVouDate());
                        mdetails.setArgDocumentNumber(apVoucher.getVouDocumentNumber());
                        mdetails.setArgReferenceNumber(apVoucher.getVouReferenceNumber());
                        mdetails.setArgDescription(apVoucher.getVouDescription());
                        mdetails.setArgSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());
                        mdetails.setArgSplSupplierClass(apVoucher.getApSupplier().getApSupplierClass().getScName());
                        mdetails.setArgSplName(apVoucher.getApSupplier().getSplName());
                        mdetails.setArgSplTin(apVoucher.getApSupplier().getSplTin());
                        mdetails.setArgSplAddress(apVoucher.getApSupplier().getSplAddress());
                        mdetails.setArgPoNumber(apVoucher.getVouPoNumber() == null ? "" : apVoucher.getVouPoNumber());
                        // type
                        if (apVoucher.getApSupplier().getApSupplierType() == null) {

                            mdetails.setArgSplSupplierType("UNDEFINE");

                        } else {

                            mdetails.setArgSplSupplierType(apVoucher.getApSupplier().getApSupplierType().getStName());
                        }

                        if (first) {

                            double AMNT_DUE = 0d;

                            if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                                AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            } else {

                                LocalApVoucher apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, apVoucher.getVouAdBranch(), AD_CMPNY);

                                AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apVoucher.getVouBillAmount(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());
                            }

                            mdetails.setArgAmount(AMNT_DUE);

                            first = false;
                        }

                        if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                            mdetails.setArgType("VOUCHER");

                        } else {

                            mdetails.setArgType("DEBIT MEMO");
                        }

                        mdetails.setOrderBy(ORDER_BY);

                        // distribution record details
                        mdetails.setArgDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                        mdetails.setArgDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                        if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                            mdetails.setArgDrDebitAmount(apDistributionRecord.getDrAmount());

                        } else {

                            mdetails.setArgDrCreditAmount(apDistributionRecord.getDrAmount());
                        }

                        list.add(mdetails);
                    }
                }

            } else {

                for (Object voucher : apVouchers) {

                    LocalApVoucher apVoucher = (LocalApVoucher) voucher;
                    dtChck = dtChck + 1;
                    ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();

                    if (dtChck == 1 && criteria.get("dateFrom") == null) {
                        dateFrom = apVoucher.getVouDate();
                    }
                    dateLast = apVoucher.getVouDate();

                    mdetails.setArgDate(apVoucher.getVouDate());

                    mdetails.setArgDocumentNumber(apVoucher.getVouDocumentNumber());
                    mdetails.setArgReferenceNumber(apVoucher.getVouReferenceNumber());
                    mdetails.setArgDescription(apVoucher.getVouDescription());
                    mdetails.setArgSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());
                    mdetails.setArgSplSupplierClass(apVoucher.getApSupplier().getApSupplierClass().getScName());
                    mdetails.setArgSplName(apVoucher.getApSupplier().getSplName());
                    mdetails.setArgSplTin(apVoucher.getApSupplier().getSplTin());
                    mdetails.setArgSplAddress(apVoucher.getApSupplier().getSplAddress());
                    mdetails.setArgPoNumber(apVoucher.getVouPoNumber() == null ? "" : apVoucher.getVouPoNumber());

                    // type
                    if (apVoucher.getApSupplier().getApSupplierType() == null) {

                        mdetails.setArgSplSupplierType("UNDEFINE");

                    } else {

                        mdetails.setArgSplSupplierType(apVoucher.getApSupplier().getApSupplierType().getStName());
                    }

                    double AMNT_DUE = 0d;

                    if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                        mdetails.setArgType("VOUCHER");

                    } else {

                        LocalApVoucher apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, apVoucher.getVouAdBranch(), AD_CMPNY);

                        AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apVoucher.getVouBillAmount(), AD_CMPNY) * -1, adCompany.getGlFunctionalCurrency().getFcPrecision());

                        mdetails.setArgType("DEBIT MEMO");
                    }

                    mdetails.setArgAmount(AMNT_DUE);
                    mdetails.setOrderBy(ORDER_BY);

                    list.add(mdetails);
                }
            }

            // Payments

            if (criteria.get("includedPayments").equals("YES")) {
                if (criteria.containsKey("documentNumberFrom")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("documentNumberTo")) {

                    criteriaSize--;
                }

                obj = new Object[criteriaSize];
                firstArgument = true;
                ctr = 0;

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

                if (criteria.containsKey("supplierCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
                }

                if (criteria.containsKey("supplierType")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.apSupplier.apSupplierType.stName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("supplierType");
                    ctr++;
                }

                if (criteria.containsKey("supplierClass")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.apSupplier.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("supplierClass");
                    ctr++;
                }

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("chk.chkDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }

                if (criteria.containsKey("dateTo")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("chk.chkDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("includedUnposted")) {

                    String unposted = (String) criteria.get("includedUnposted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (unposted.equals("NO")) {

                        jbossQl.append("chk.chkPosted = 1 ");

                    } else {

                        jbossQl.append("chk.chkVoid = 0 ");
                    }
                }

                if (adBrnchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.chkAdBranch in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (!firstLoop) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) o;

                        jbossQl.append(mdetails.getBrCode());
                    }

                    jbossQl.append(") ");

                    firstArgument = false;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkAdCompany = ").append(AD_CMPNY).append(" ");

                Collection apChecks = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);

                if (SHOW_ENTRIES) {

                    for (Object check : apChecks) {

                        boolean first = true;

                        LocalApCheck apCheck = (LocalApCheck) check;

                        if (apCheck.getChkType() == "PAYMENT") {
                            Collection apDistributionRecords = apCheck.getApDistributionRecords();

                            for (Object distributionRecord : apDistributionRecords) {

                                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                                ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();
                                mdetails.setArgDate(apCheck.getChkDate());
                                mdetails.setArgDocumentNumber(apCheck.getChkDocumentNumber());
                                mdetails.setArgReferenceNumber(apCheck.getChkReferenceNumber());
                                mdetails.setArgDescription(apCheck.getChkDescription());
                                mdetails.setArgSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                                mdetails.setArgSplSupplierClass(apCheck.getApSupplier().getApSupplierClass().getScName());
                                mdetails.setArgSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                                mdetails.setArgSplTin(apCheck.getApSupplier().getSplTin());
                                mdetails.setArgSplAddress(apCheck.getApSupplier().getSplAddress());
                                mdetails.setArgCheckType(apCheck.getChkType());

                                // type
                                if (apCheck.getApSupplier().getApSupplierType() == null) {

                                    mdetails.setArgSplSupplierType("UNDEFINE");

                                } else {

                                    mdetails.setArgSplSupplierType(apCheck.getApSupplier().getApSupplierType().getStName());
                                }

                                if (first) {

                                    double AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    mdetails.setArgAmount(AMNT_DUE);

                                    first = false;
                                }

                                mdetails.setOrderBy(ORDER_BY);

                                // distribution record details
                                mdetails.setArgDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                mdetails.setArgDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                    mdetails.setArgDrDebitAmount(apDistributionRecord.getDrAmount());

                                } else {

                                    mdetails.setArgDrCreditAmount(apDistributionRecord.getDrAmount());
                                }

                                mdetails.setArgType("CHECK");

                                list.add(mdetails);
                            }
                        }
                    }

                } else {

                    for (Object check : apChecks) {
                        LocalApCheck apCheck = (LocalApCheck) check;
                        if (apCheck.getChkType().equals("PAYMENT")) {
                            ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();
                            mdetails.setArgDate(apCheck.getChkDate());
                            mdetails.setArgDocumentNumber(apCheck.getChkDocumentNumber());
                            mdetails.setArgReferenceNumber(apCheck.getChkReferenceNumber());
                            mdetails.setArgDescription(apCheck.getChkDescription());
                            mdetails.setArgSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                            mdetails.setArgSplSupplierClass(apCheck.getApSupplier().getApSupplierClass().getScName());
                            mdetails.setArgSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                            mdetails.setArgSplTin(apCheck.getApSupplier().getSplTin());
                            mdetails.setArgSplAddress(apCheck.getApSupplier().getSplAddress());
                            mdetails.setArgCheckType(apCheck.getChkType());
                            // type
                            if (apCheck.getApSupplier().getApSupplierType() == null) {

                                mdetails.setArgSplSupplierType("UNDEFINE");

                            } else {

                                mdetails.setArgSplSupplierType(apCheck.getApSupplier().getApSupplierType().getStName());
                            }

                            double AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                            mdetails.setArgAmount(AMNT_DUE);
                            mdetails.setOrderBy(ORDER_BY);
                            mdetails.setArgType("CHECK");

                            list.add(mdetails);
                        }
                    }
                }
            }

            // DC
            double drAmnt = 0d;
            double balance = 0d;
            double begbalance = 0d;
            double ptdbalance = 0d;
            double SplBalance = 0d;
            String checksup = "";

            if (INCLUDE_DC && GROUP_BY.equals("SUPPLIER CODE")) {
                boolean issetBegBal = true;

                if (criteria.containsKey("documentNumberFrom")) {

                    criteriaSize--;
                }

                if (criteria.containsKey("documentNumberTo")) {

                    criteriaSize--;
                }

                obj = new Object[criteriaSize];
                firstArgument = true;
                ctr = 0;

                jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

                if (criteria.containsKey("supplierCode")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
                }

                if (criteria.containsKey("supplierType")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.apSupplier.apSupplierType.stName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("supplierType");
                    ctr++;
                }

                if (criteria.containsKey("supplierClass")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.apSupplier.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("supplierClass");
                    ctr++;
                }

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("chk.chkDate>=?").append(ctr + 1).append(" ");

                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }

                if (criteria.containsKey("dateTo")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("chk.chkDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("includedUnposted")) {

                    String unposted = (String) criteria.get("includedUnposted");

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (unposted.equals("NO")) {

                        jbossQl.append("chk.chkPosted = 1 ");

                    } else {

                        jbossQl.append("chk.chkVoid = 0 ");
                    }
                }

                if (adBrnchList.isEmpty()) {

                    throw new GlobalNoRecordFoundException();

                } else {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("chk.chkAdBranch in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (!firstLoop) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) o;

                        jbossQl.append(mdetails.getBrCode());
                    }

                    jbossQl.append(") ");

                    firstArgument = false;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                GregorianCalendar lastDateGc = new GregorianCalendar();
                if (criteria.get("dateTo") == null) {
                    lastDateGc.setTime(dateLast);
                } else {
                    dateLast = (Date) criteria.get("dateTo");
                    lastDateGc.setTime(dateLast);
                }

                if (criteria.get("dateFrom") != null) {
                    dateFrom = (Date) criteria.get("dateFrom");
                }

                GregorianCalendar firstDateGc = new GregorianCalendar(lastDateGc.get(Calendar.YEAR), lastDateGc.get(Calendar.MONTH), lastDateGc.getActualMinimum(Calendar.DATE), 0, 0, 0);

                GregorianCalendar lastMonthDateGc2 = null;

                jbossQl.append("chk.chkAdCompany = ").append(AD_CMPNY).append(" ");
                String supplierCode = (String) criteria.get("supplierCode");

                Collection apChecks = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);
                if (supplierCode != null && supplierCode != "") {
                    LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode((String) criteria.get("supplierCode"), AD_CMPNY);

                    ApRepApRegisterDetails mdetails2 = new ApRepApRegisterDetails();

                    mdetails2.setArgDocumentNumber("");
                    mdetails2.setArgReferenceNumber("");
                    mdetails2.setArgDescription("Balance Forward");
                    mdetails2.setArgSplSupplierCode(apSupplier.getSplSupplierCode());
                    mdetails2.setArgSplSupplierClass(apSupplier.getApSupplierClass().getScName());
                    mdetails2.setArgSplName(apSupplier.getSplName());
                    mdetails2.setArgSplTin(apSupplier.getSplTin());
                    mdetails2.setArgSplAddress(apSupplier.getSplAddress());

                    Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(dateFrom, apSupplier.getSplCode(), AD_CMPNY);

                    if (apSupplierBalances.isEmpty()) {
                        balance = 0;

                    } else {

                        ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                        LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                        balance = (apSupplierBalance.getSbBalance());
                        mdetails2.setArgDate(apSupplierBalance.getSbDate());

                        if (apSupplierBalance.getApSupplier().getApSupplierType() == null) {

                            mdetails2.setArgSplSupplierType("UNDEFINE");

                        } else {

                            mdetails2.setArgSplSupplierType(apSupplierBalance.getApSupplier().getApSupplierType().getStName());
                        }

                        // get beginning balance OK
                        if (issetBegBal) {
                            GregorianCalendar lastMonthDateGc = new GregorianCalendar();
                            lastMonthDateGc.setTime(dateFrom);
                            lastMonthDateGc.add(Calendar.MONTH, -1);

                            lastMonthDateGc2 = new GregorianCalendar(lastMonthDateGc.get(Calendar.YEAR), lastMonthDateGc.get(Calendar.MONTH), lastMonthDateGc.getActualMaximum(Calendar.DATE), 0, 0, 0);

                            Collection apBegBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(lastMonthDateGc2.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                            for (Object begBalance : apBegBalances) {
                                LocalApSupplierBalance apBegBalance = (LocalApSupplierBalance) begBalance;

                                begbalance = (apBegBalance.getSbBalance());
                            }

                            issetBegBal = false;
                        }

                        // get ptd balance OK (same as Net Transactions)

                        double priorBalance = 0d;

                        Collection apSupplierPriorBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(firstDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                        if (!apSupplierPriorBalances.isEmpty()) {

                            ArrayList apSupplierPriorBalanceList = new ArrayList(apSupplierPriorBalances);

                            LocalApSupplierBalance apSupplierPriorBalance = (LocalApSupplierBalance) apSupplierPriorBalanceList.get(apSupplierPriorBalanceList.size() - 1);

                            priorBalance = apSupplierPriorBalance.getSbBalance();
                        }

                        ptdbalance = (apSupplierBalance.getSbBalance() - priorBalance) - drAmnt;
                    }

                    // get Interest Income

                    double interestIncome = 0d;

                    GregorianCalendar vouDateGc = new GregorianCalendar();
                    vouDateGc.setTime(dateFrom);

                    GregorianCalendar firstDayOfMonth = new GregorianCalendar(vouDateGc.get(Calendar.YEAR), vouDateGc.get(Calendar.MONTH), vouDateGc.getActualMinimum(Calendar.DATE), 0, 0, 0);

                    String referenceNumber = "INT-" + EJBCommon.convertSQLDateToString(vouDateGc.getTime());

                    Collection apInterestIncomes = apVoucherHome.findByVouReferenceNumberAndSplName(referenceNumber, apSupplier.getSplName(), AD_CMPNY);

                    for (Object income : apInterestIncomes) {

                        LocalApVoucher apInterestIncome = (LocalApVoucher) income;
                        interestIncome = apInterestIncome.getVouAmountDue();
                    }

                    SplBalance = (balance + interestIncome);

                    mdetails2.setArgAmount(SplBalance);
                    mdetails2.setOrderBy(ORDER_BY);
                    mdetails2.setArgType("CHECK");
                    checksup = apSupplier.getSplSupplierCode();
                    if (SplBalance != 0) {
                        list.add(mdetails2);
                    }

                } else {

                    Collection apSuppliers = apSupplierHome.findEnabledSplAllOrderBySplSupplierCode(AD_CMPNY);

                    for (Object supplier : apSuppliers) {

                        LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                        ApRepApRegisterDetails mdetails2 = new ApRepApRegisterDetails();

                        mdetails2.setArgDocumentNumber("");
                        mdetails2.setArgReferenceNumber("");
                        mdetails2.setArgDescription("Balance Forward");
                        mdetails2.setArgSplSupplierCode(apSupplier.getSplSupplierCode());
                        mdetails2.setArgSplSupplierClass(apSupplier.getApSupplierClass().getScName());
                        mdetails2.setArgSplName(apSupplier.getSplName());
                        mdetails2.setArgSplTin(apSupplier.getSplTin());
                        mdetails2.setArgSplAddress(apSupplier.getSplAddress());

                        Collection apSupplierBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(dateFrom, apSupplier.getSplCode(), AD_CMPNY);

                        if (apSupplierBalances.isEmpty()) {
                            balance = 0;

                        } else {

                            ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                            LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                            balance = (apSupplierBalance.getSbBalance());
                            mdetails2.setArgDate(apSupplierBalance.getSbDate());

                            if (apSupplierBalance.getApSupplier().getApSupplierType() == null) {

                                mdetails2.setArgSplSupplierType("UNDEFINE");

                            } else {

                                mdetails2.setArgSplSupplierType(apSupplierBalance.getApSupplier().getApSupplierType().getStName());
                            }

                            // get beginning balance OK
                            if (issetBegBal) {
                                GregorianCalendar lastMonthDateGc = new GregorianCalendar();
                                lastMonthDateGc.setTime(dateFrom);
                                lastMonthDateGc.add(Calendar.MONTH, -1);

                                lastMonthDateGc2 = new GregorianCalendar(lastMonthDateGc.get(Calendar.YEAR), lastMonthDateGc.get(Calendar.MONTH), lastMonthDateGc.getActualMaximum(Calendar.DATE), 0, 0, 0);

                                Collection apBegBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(lastMonthDateGc2.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                                for (Object begBalance : apBegBalances) {
                                    LocalApSupplierBalance apBegBalance = (LocalApSupplierBalance) begBalance;

                                    begbalance = (apBegBalance.getSbBalance());
                                }

                                issetBegBal = false;
                            }

                            // get ptd balance OK (same as Net Transactions)

                            double priorBalance = 0d;

                            Collection apSupplierPriorBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(firstDateGc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                            if (!apSupplierPriorBalances.isEmpty()) {

                                ArrayList apSupplierPriorBalanceList = new ArrayList(apSupplierPriorBalances);

                                LocalApSupplierBalance apSupplierPriorBalance = (LocalApSupplierBalance) apSupplierPriorBalanceList.get(apSupplierPriorBalanceList.size() - 1);

                                priorBalance = apSupplierPriorBalance.getSbBalance();
                            }

                            ptdbalance = (apSupplierBalance.getSbBalance() - priorBalance) - drAmnt;
                        }

                        // get Interest Income

                        double interestIncome = 0d;

                        GregorianCalendar vouDateGc = new GregorianCalendar();
                        vouDateGc.setTime(dateFrom);

                        GregorianCalendar firstDayOfMonth = new GregorianCalendar(vouDateGc.get(Calendar.YEAR), vouDateGc.get(Calendar.MONTH), vouDateGc.getActualMinimum(Calendar.DATE), 0, 0, 0);

                        String referenceNumber = "INT-" + EJBCommon.convertSQLDateToString(vouDateGc.getTime());

                        Collection apInterestIncomes = apVoucherHome.findByVouReferenceNumberAndSplName(referenceNumber, apSupplier.getSplName(), AD_CMPNY);

                        for (Object income : apInterestIncomes) {
                            LocalApVoucher apInterestIncome = (LocalApVoucher) income;
                            interestIncome = apInterestIncome.getVouAmountDue();
                        }

                        SplBalance = (balance + interestIncome);

                        mdetails2.setArgAmount(SplBalance);
                        mdetails2.setOrderBy(ORDER_BY);
                        mdetails2.setArgType("CHECK");
                        checksup = apSupplier.getSplSupplierCode();
                        if (SplBalance != 0) {
                            list.add(mdetails2);
                        }
                    }
                }

                if (SHOW_ENTRIES) {

                    for (Object check : apChecks) {

                        boolean first = true;

                        LocalApCheck apCheck = (LocalApCheck) check;

                        for (Object o : adBrnchList) {
                            AdBranchDetails mdetail = (AdBranchDetails) o;
                            if (apCheck.getChkType().equals("DIRECT")) {

                                Collection apDistributionRecords = apDistributionRecordHome.findChkByDateAndCoaAccountDescriptionAndSupplier((Date) criteria.get("dateTo"), apCheck.getApSupplier().getSplCode(), mdetail.getBrCode(), AD_CMPNY);

                                for (Object distributionRecord : apDistributionRecords) {

                                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                                    ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();
                                    mdetails.setArgDate(apCheck.getChkDate());
                                    mdetails.setArgDocumentNumber(apCheck.getChkDocumentNumber());
                                    mdetails.setArgReferenceNumber(apCheck.getChkReferenceNumber());
                                    mdetails.setArgDescription(apCheck.getChkDescription());
                                    mdetails.setArgSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                                    mdetails.setArgSplSupplierClass(apCheck.getApSupplier().getApSupplierClass().getScName());
                                    mdetails.setArgSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                                    mdetails.setArgSplTin(apCheck.getApSupplier().getSplTin());
                                    mdetails.setArgSplAddress(apCheck.getApSupplier().getSplAddress());
                                    mdetails.setArgCheckType(apCheck.getChkType());

                                    // type
                                    if (apCheck.getApSupplier().getApSupplierType() == null) {

                                        mdetails.setArgSplSupplierType("UNDEFINE");

                                    } else {

                                        mdetails.setArgSplSupplierType(apCheck.getApSupplier().getApSupplierType().getStName());
                                    }

                                    Iterator d2 = adBrnchList.iterator();
                                    boolean addCheck = false;
                                    while (d2.hasNext()) {

                                        AdBranchDetails mdetail2 = (AdBranchDetails) d2.next();

                                        if (apCheck.getChkType().equals("DIRECT")) {

                                            Collection apDistributionRecords2 = apDistributionRecordHome.findChkByDateAndCoaAccountDescriptionAndSupplier((Date) criteria.get("dateTo"), apCheck.getApSupplier().getSplCode(), mdetail2.getBrCode(), AD_CMPNY);

                                            Iterator j2 = apDistributionRecords.iterator();
                                            String chkDoc = apCheck.getChkDocumentNumber();
                                            while (j2.hasNext()) {

                                                addCheck = true;

                                                LocalApDistributionRecord apDistributionRecord2 = (LocalApDistributionRecord) j2.next();

                                                if (apDistributionRecord.getDrDebit() != 0) {
                                                    if (chkDoc == apDistributionRecord2.getApCheck().getChkDocumentNumber()) {
                                                        drAmnt = drAmnt + apDistributionRecord2.getDrAmount();
                                                    }
                                                } else {
                                                    if (chkDoc == apDistributionRecord.getApCheck().getChkDocumentNumber()) {
                                                        drAmnt = drAmnt - apDistributionRecord2.getDrAmount();
                                                    }
                                                }

                                                if (first) {

                                                    double AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), drAmnt, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                                    if (apDistributionRecord.getApCheck().getChkDocumentNumber() == apCheck.getChkDocumentNumber()) {
                                                        if (AMNT_DUE < 0) {
                                                            Debug.print("AMNT_DUE: " + AMNT_DUE);
                                                            mdetails.setArgAmount(AMNT_DUE);
                                                        } else {
                                                            Debug.print("AMNT_DUE: " + AMNT_DUE);
                                                            mdetails.setArgAmount(AMNT_DUE);
                                                        }
                                                    }

                                                    first = false;
                                                }
                                            }
                                        }
                                    }

                                    mdetails.setOrderBy(ORDER_BY);

                                    // distribution record details
                                    mdetails.setArgDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                                    mdetails.setArgDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                                    if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                        mdetails.setArgDrDebitAmount(apDistributionRecord.getDrAmount());

                                    } else {

                                        mdetails.setArgDrCreditAmount(apDistributionRecord.getDrAmount());
                                    }

                                    balance = 0;
                                    drAmnt = 0;
                                    mdetails.setArgType("CHECK");
                                    if (addCheck) {
                                        list.add(mdetails);
                                    }
                                    addCheck = false;
                                }
                            }
                        }
                    }

                } else {

                    for (Object check : apChecks) {
                        LocalApCheck apCheck = (LocalApCheck) check;

                        ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();

                        mdetails.setArgDate(apCheck.getChkDate());
                        mdetails.setArgDocumentNumber(apCheck.getChkDocumentNumber());
                        mdetails.setArgReferenceNumber(apCheck.getChkReferenceNumber());
                        mdetails.setArgDescription(apCheck.getChkDescription());
                        mdetails.setArgSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());
                        mdetails.setArgSplSupplierClass(apCheck.getApSupplier().getApSupplierClass().getScName());
                        mdetails.setArgSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                        mdetails.setArgSplTin(apCheck.getApSupplier().getSplTin());
                        mdetails.setArgSplAddress(apCheck.getApSupplier().getSplAddress());
                        mdetails.setArgCheckType(apCheck.getChkType());

                        if (apCheck.getApSupplier().getApSupplierType() == null) {

                            mdetails.setArgSplSupplierType("UNDEFINE");

                        } else {

                            mdetails.setArgSplSupplierType(apCheck.getApSupplier().getApSupplierType().getStName());
                        }
                        Iterator d = adBrnchList.iterator();
                        boolean addCheck = false;
                        while (d.hasNext()) {
                            AdBranchDetails mdetail2 = (AdBranchDetails) d.next();
                            if (apCheck.getChkType().equals("DIRECT")) {

                                Collection apDistributionRecords = apDistributionRecordHome.findChkByDateAndCoaAccountDescriptionAndSupplier((Date) criteria.get("dateTo"), apCheck.getApSupplier().getSplCode(), mdetail2.getBrCode(), AD_CMPNY);

                                Iterator j = apDistributionRecords.iterator();
                                String chkDoc = apCheck.getChkDocumentNumber();
                                while (j.hasNext()) {

                                    addCheck = true;

                                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                                    if (apDistributionRecord.getDrDebit() != 0) {
                                        if (chkDoc == apDistributionRecord.getApCheck().getChkDocumentNumber()) {
                                            drAmnt = drAmnt + apDistributionRecord.getDrAmount();
                                        }
                                    } else {
                                        if (chkDoc == apDistributionRecord.getApCheck().getChkDocumentNumber()) {
                                            drAmnt = drAmnt - apDistributionRecord.getDrAmount();
                                        }
                                    }
                                    double samp = 0;
                                    double AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), drAmnt, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                                    if (apDistributionRecord.getApCheck().getChkDocumentNumber() == apCheck.getChkDocumentNumber()) {
                                        if (AMNT_DUE < 0) {
                                            mdetails.setArgAmount(AMNT_DUE);
                                        } else {
                                            samp = samp + AMNT_DUE;
                                            mdetails.setArgAmount(AMNT_DUE);
                                        }
                                    }
                                }
                            }
                        }

                        balance = 0;
                        drAmnt = 0;
                        mdetails.setOrderBy(ORDER_BY);
                        mdetails.setArgType("CHECK");
                        if (addCheck) {
                            list.add(mdetails);
                        }
                        addCheck = false;
                    }
                }
            }

            if (list.isEmpty() || list.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            // sort

            switch (GROUP_BY) {
                case "SUPPLIER CODE":

                    list.sort(ApRepApRegisterDetails.SupplierCodeComparator);

                    break;
                case "SUPPLIER TYPE":

                    list.sort(ApRepApRegisterDetails.SupplierTypeComparator);

                    break;
                case "SUPPLIER CLASS":

                    list.sort(ApRepApRegisterDetails.SupplierClassComparator);

                    break;
                default:

                    list.sort(ApRepApRegisterDetails.NoGroupComparator);
                    break;
            }

            list = setInterestIncome(list, GROUP_BY, criteria.containsKey("dateFrom") ? (Date) criteria.get("dateFrom") : null, AD_CMPNY);

            if (SHOW_ENTRIES) {
                if (SUMMARIZE) {
                    list.sort(ApRepApRegisterDetails.CoaAccountNumberComparator);
                }
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // Get All Supplier Class
    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean getApScAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierClasses = apSupplierClassHome.findEnabledScAll(AD_CMPNY);

            for (Object supplierClass : apSupplierClasses) {

                LocalApSupplierClass apSupplierClass = (LocalApSupplierClass) supplierClass;

                list.add(apSupplierClass.getScName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get All Supplier Types
    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean getApStAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierTypes = apSupplierTypeHome.findEnabledStAll(AD_CMPNY);

            for (Object supplierType : apSupplierTypes) {

                LocalApSupplierType apSupplierType = (LocalApSupplierType) supplierType;

                list.add(apSupplierType.getStName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get All Vouchers
    public ArrayList getAllVouchers(HashMap criteria, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepApRegisterControllerBean getAllVoucherNumber");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedPayments")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            jbossQl.append("SELECT OBJECT(vou) FROM ApVoucher vou ");

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("voucherBatch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apVoucherBatch.vbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("voucherBatch");
                ctr++;
            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.apSupplierType.stName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierType");
                ctr++;
            }

            if (criteria.containsKey("supplierClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("vou.vouDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("vou.vouPosted = 1 ");

                } else {

                    jbossQl.append("vou.vouVoid = 0 ");
                }
            }

            if (criteria.containsKey("paymentStatus")) {

                String paymentStatus = (String) criteria.get("paymentStatus");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                Debug.print("paymentStatus: " + paymentStatus);
                if (paymentStatus.equals("PAID")) {

                    jbossQl.append("vou.vouAmountDue = vou.vouAmountPaid ");

                } else if (paymentStatus.equals("UNPAID")) {

                    jbossQl.append("vou.vouAmountDue <> vou.vouAmountPaid ");
                }
            }

            if (adBrnchList.isEmpty()) {
                System.out.print("dito");
                throw new GlobalNoRecordFoundException();

            } else {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouAdCompany = ").append(AD_CMPNY).append(" ");
            Collection apVouchers = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);

            double toalAmount = 0d;
            int numberOfTransaction = 0;

            for (Object voucher : apVouchers) {

                LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                ApRepApRegisterDetails mdetails = new ApRepApRegisterDetails();

                Collection paymentSchedList = apVoucherPaymentScheduleHome.findOpenVpsByVouCode(apVoucher.getVouCode(), AD_CMPNY);

                for (Object o : paymentSchedList) {

                    LocalApVoucherPaymentSchedule apVoucherPaymentSchedule = (LocalApVoucherPaymentSchedule) o;

                    mdetails.setArgVpsCode(apVoucherPaymentSchedule.getVpsCode());
                }
                mdetails.setArgDate(apVoucher.getVouDate());

                mdetails.setArgDocumentNumber(apVoucher.getVouDocumentNumber());
                mdetails.setArgReferenceNumber(apVoucher.getVouReferenceNumber());
                mdetails.setArgDescription(apVoucher.getVouDescription());
                mdetails.setArgSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());
                mdetails.setArgSplSupplierClass(apVoucher.getApSupplier().getApSupplierClass().getScName());
                mdetails.setArgSplName(apVoucher.getApSupplier().getSplName());
                mdetails.setArgSplTin(apVoucher.getApSupplier().getSplTin());
                mdetails.setArgSplAddress(apVoucher.getApSupplier().getSplAddress());
                mdetails.setArgPoNumber(apVoucher.getVouPoNumber() == null ? "" : apVoucher.getVouPoNumber());
                mdetails.setArgFunctionalCurrencyName(apVoucher.getGlFunctionalCurrency().getFcName());
                mdetails.setArgPosted(apVoucher.getVouPosted() != 0);
                mdetails.setArgVoid(apVoucher.getVouVoid() != 0);
                mdetails.setArgAmountPaid(apVoucher.getVouAmountPaid());
                mdetails.setArgCurrency(apVoucher.getGlFunctionalCurrency().getFcName());

                if (apVoucher.getApSupplier().getSplAccountNumber() == null) {
                    mdetails.setArgSplSupplierAccountNumber(apVoucher.getApSupplier().getSplAccountNumber());
                } else {
                    mdetails.setArgSplSupplierAccountNumber("");
                }

                // type
                if (apVoucher.getApSupplier().getApSupplierType() == null) {

                    mdetails.setArgSplSupplierType("UNDEFINE");

                } else {

                    mdetails.setArgSplSupplierType(apVoucher.getApSupplier().getApSupplierType().getStName());
                }

                double AMNT_DUE = 0d;

                if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                    AMNT_DUE = EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apVoucher.getVouAmountDue(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision());

                    mdetails.setArgType("VOUCHER");
                    mdetails.setArgAmount(AMNT_DUE);
                    list.add(mdetails);
                } else {
                    continue;
                }
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // Get All Voucher Batch - OPEN
    public ArrayList getApOpenVbAll(String DPRTMNT, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean getApOpenVbAll");

        ArrayList list = new ArrayList();

        try {
            Collection apVoucherBatches = null;

            if (DPRTMNT.equals("") || DPRTMNT.equals("default") || DPRTMNT.equals("NO RECORD FOUND")) {
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbType("VOUCHER", AD_BRNCH, AD_CMPNY);

            } else {
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbTypeDepartment("VOUCHER", DPRTMNT, AD_BRNCH, AD_CMPNY);
            }

            for (Object voucherBatch : apVoucherBatches) {

                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;

                list.add(apVoucherBatch.getVbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get Company Details
    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get Branch Responsibility
    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepApRegisterControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;
                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());
                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // Get Bank Account
    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // PRIVATE METHODS

    // Convert Foreign To Functional Currency
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // Set Interest Income
    private ArrayList setInterestIncome(ArrayList list, String GROUP_BY, Date dateFrom, Integer AD_CMPNY) {

        Debug.print("ApRepApRegisterControllerBean setInterestIncome");

        try {

            // set running balance, beginning balance
            // set interest income

            double ARG_BLNC = 0d;
            double ARG_BGNNG_BLNC = 0d;
            double ARG_PREV_INTRST_INCM = 0d;

            String group = null;

            LocalApSupplier apSupplier = null;
            Collection apSupplierBalances = null;
            Collection apInterestVouchers = null;

            Iterator listIter = list.iterator();

            while (listIter.hasNext()) {

                ApRepApRegisterDetails details = (ApRepApRegisterDetails) listIter.next();

                switch (GROUP_BY) {
                    case "SUPPLIER CODE":

                        if (group == null || !group.equals(details.getArgSplSupplierCode())) {

                            ARG_BLNC = 0d;
                            ARG_BGNNG_BLNC = 0d;
                            ARG_PREV_INTRST_INCM = 0d;

                            apSupplier = apSupplierHome.findBySplSupplierCode(details.getArgSplSupplierCode(), AD_CMPNY);

                            if (dateFrom != null) {
                                // get beginning balance

                                GregorianCalendar gc = new GregorianCalendar();
                                gc.setTime(dateFrom);

                                apSupplierBalances = apSupplierBalanceHome.findByBeforeSbDateAndSplCode(gc.getTime(), apSupplier.getSplCode(), AD_CMPNY);

                                if (!apSupplierBalances.isEmpty()) {

                                    ArrayList apSupplierBalanceList = new ArrayList(apSupplierBalances);

                                    LocalApSupplierBalance apSupplierBalance = (LocalApSupplierBalance) apSupplierBalanceList.get(apSupplierBalanceList.size() - 1);

                                    ARG_BGNNG_BLNC = apSupplierBalance.getSbBalance();
                                }

                                // get prev month interest income

                                String referenceNumber = "INT-" + EJBCommon.convertSQLDateToString(dateFrom);

                                apInterestVouchers = apVoucherHome.findByVouReferenceNumberAndVouPostedAndSplName(referenceNumber, EJBCommon.TRUE, apSupplier.getSplName(), AD_CMPNY);

                                for (Object interestVoucher : apInterestVouchers) {

                                    LocalApVoucher apInterestVoucher = (LocalApVoucher) interestVoucher;
                                    ARG_PREV_INTRST_INCM += apInterestVoucher.getVouAmountDue();
                                }
                            }

                            ARG_BLNC += ARG_BGNNG_BLNC;
                            group = details.getArgSplSupplierCode();
                        }

                        break;
                    case "SUPPLIER TYPE":

                        if (group == null || !group.equals(details.getArgSplSupplierType())) {

                            ARG_BLNC = 0d;
                            group = details.getArgSplSupplierType();
                        }

                        break;
                    case "SUPPLIER CLASS":

                        if (group == null || !group.equals(details.getArgSplSupplierClass())) {

                            ARG_BLNC = 0d;
                            group = details.getArgSplSupplierClass();
                        }
                        break;
                }

                if (details.getArgType().equals("CHECK")) {

                    ARG_BLNC -= details.getArgAmount();

                } else {

                    ARG_BLNC += details.getArgAmount();
                }

                details.setArgBalance(ARG_BLNC);

                details.setArgBeginningBalance(ARG_BGNNG_BLNC);

                details.setArgPrevInterestIncome(ARG_PREV_INTRST_INCM);

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Get Updated Voucher Reference by Document Number
    public void getUpdateVoucherReferenceByDocumentNumber(String VOU_DCMNT_NMBR, String VOU_RFRNC_NMBR, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepApRegisterControllerBean getUpdateVoucherReferenceByDocumentNumber");

        ArrayList list = new ArrayList();

        try {

            LocalApVoucher apVoucher = apVoucherHome.findByVouDocumentNumber(VOU_DCMNT_NMBR, AD_CMPNY);

            apVoucher.setVouReferenceNumber(VOU_RFRNC_NMBR);

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApRepApRegisterControllerBean ejbCreate");
    }

}