/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApFindVoucherControllerBean
 * @created February 17, 2004, 9:47 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.ap.ApModVoucherDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApFindVoucherControllerEJB")
public class ApFindVoucherControllerBean extends EJBContextClass implements ApFindVoucherController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;

    public ArrayList getAdLvDEPARTMENT(Integer AD_CMPNY) {

        Debug.print("ApFindVoucherControllerBean getAdLvDEPARTMENT");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AD DEPARTMENT", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApVoucherBatch(Integer AD_CMPNY) {

        Debug.print("ApFindVoucherControllerBean getAdPrfEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApVoucherBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApVouByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindVoucherControllerBean getApVouByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(vou) FROM ApVoucher vou ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentRequest")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            Debug.print("criteria=" + criteria);

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("batchName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apVoucherBatch.vbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
                ctr++;
            }

            if (criteria.containsKey("user")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouLastModifiedBy=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("user");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouDebitMemo=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("debitMemo");
            ctr++;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            if ((Byte) criteria.get("paymentRequest") == 1) {

                Debug.print("o d2?");
                jbossQl.append("vou.vouType='REQUEST'");
            } else {
                Debug.print("d2 ba?");

                // jbossQl.append("vou.vouType NOT LIKE 'REQUEST' ");
                jbossQl.append("(vou.vouType IS NULL OR vou.vouType NOT LIKE 'REQUEST') ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("voucherVoid");
            ctr++;

            if (criteria.containsKey("voucherGenerated")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouGenerated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("voucherGenerated");
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

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT")) {

                    jbossQl.append("vou.vouApprovalStatus IS NULL AND vou.vouReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("vou.vouReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("vou.vouApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
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

                    jbossQl.append("vou.vouAmountDue=vou.vouAmountPaid ");

                } else if (paymentStatus.equals("UNPAID")) {

                    jbossQl.append("(vou.vouAmountDue < vou.vouAmountPaid OR vou.vouAmountDue > vou.vouAmountPaid) ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("SUPPLIER CODE")) {

                orderBy = "vou.apSupplier.splSupplierCode";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "vou.vouDocumentNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", vou.vouDate");

            } else {

                jbossQl.append("ORDER BY vou.vouDate");
            }

            Debug.print("jbossQl.toString()" + jbossQl);
            Collection apVouchers = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (apVouchers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object voucher : apVouchers) {

                LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                ApModVoucherDetails mdetails = new ApModVoucherDetails();
                mdetails.setVouType(apVoucher.getVouType());
                mdetails.setVouCode(apVoucher.getVouCode());
                mdetails.setVouSplName(apVoucher.getApSupplier().getSplName());
                mdetails.setVouDate(apVoucher.getVouDate());
                mdetails.setVouDocumentNumber(apVoucher.getVouDocumentNumber());
                mdetails.setVouReferenceNumber(apVoucher.getVouReferenceNumber());

                if (apVoucher.getVouDebitMemo() == EJBCommon.FALSE) {

                    mdetails.setVouAmountDue(apVoucher.getVouAmountDue());

                } else {

                    mdetails.setVouAmountDue(apVoucher.getVouBillAmount());
                }

                mdetails.setVouAmountPaid(apVoucher.getVouAmountPaid());
                mdetails.setVouDebitMemo(apVoucher.getVouDebitMemo());

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apVoucher.getVouAdBranch());
                mdetails.setVouBranchCode(adBranch.getBrBranchCode());

                String status = apVoucher.getVouApprovalStatus();
                if (apVoucher.getVouVoid() == EJBCommon.TRUE) {
                    status = "VOID";
                } else if (apVoucher.getVouPosted() == EJBCommon.FALSE) {
                    status = "DRAFT";
                } else if (status.equals("N/A")) {
                    status = "POSTED";
                }
                mdetails.setVouStatus(status);

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getApVouSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindVoucherControllerBean getApVouSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(vou) FROM ApVoucher vou ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentRequest")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("batchName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apVoucherBatch.vbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
                ctr++;
            }

            if (criteria.containsKey("user")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouLastModifiedBy=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("user");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouDebitMemo=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("debitMemo");
            ctr++;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            if ((Byte) criteria.get("paymentRequest") == 1) {
                Debug.print("o d2?");
                jbossQl.append("vou.vouType='REQUEST'");
            } else {
                Debug.print("d2 ba?");
                // jbossQl.append("vou.vouType NOT LIKE 'REQUEST' ");
                jbossQl.append("(vou.vouType IS NULL OR vou.vouType NOT LIKE 'REQUEST') ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("voucherVoid");
            ctr++;

            if (criteria.containsKey("voucherGenerated")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouGenerated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("voucherGenerated");
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

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT")) {

                    jbossQl.append("vou.vouApprovalStatus IS NULL AND vou.vouReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("vou.vouReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("vou.vouApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vou.vouPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
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

                    jbossQl.append("vou.vouAmountDue=vou.vouAmountPaid ");

                } else if (paymentStatus.equals("UNPAID")) {

                    jbossQl.append("(vou.vouAmountDue < vou.vouAmountPaid OR vou.vouAmountDue > vou.vouAmountPaid) ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("vou.vouAdCompany=").append(AD_CMPNY).append(" ");

            Debug.print("ssQl=" + jbossQl);

            Collection apVouchers = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, 0, 0);

            if (apVouchers.size() == 0) throw new GlobalNoRecordFoundException();

            return apVouchers.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApFindVoucherControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApFindVoucherControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApFindVoucherControllerBean ejbCreate");
    }
}