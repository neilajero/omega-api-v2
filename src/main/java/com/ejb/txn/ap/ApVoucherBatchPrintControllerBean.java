/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApVoucherBatchPrintControllerBean
 * @created February 17, 2004, 9:47 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ap.ApModVoucherDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "ApVoucherBatchPrintControllerEJB")
public class ApVoucherBatchPrintControllerBean extends EJBContextClass implements ApVoucherBatchPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;

    public byte getAdPrfEnableApVoucherBatch(Integer AD_CMPNY) {

        Debug.print("ApVoucherBatchPrintControllerBean getAdPrfEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApVoucherBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApVouByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApVoucherBatchPrintControllerBean getApVouByCriteria");

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

            jbossQl.append("vou.vouVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("voucherVoid");
            ctr++;

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

                    jbossQl.append("vou.vouApprovalStatus IS NULL ");

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

                    jbossQl.append("vou.vouAmountDue < vou.vouAmountPaid OR vou.vouAmountDue > vou.vouAmountPaid ");
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

            jbossQl.append("NOT vou.vouType = 'REQUEST' AND vou.vouAdCompany=").append(AD_CMPNY).append(" ");

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

            Collection apVouchers = apVoucherHome.getVouByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (apVouchers.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object voucher : apVouchers) {

                LocalApVoucher apVoucher = (LocalApVoucher) voucher;

                ApModVoucherDetails mdetails = new ApModVoucherDetails();
                mdetails.setVouCode(apVoucher.getVouCode());
                mdetails.setVouSplSupplierCode(apVoucher.getApSupplier().getSplSupplierCode());
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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApVoucherBatchPrintControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApVoucherBatchPrintControllerBean getAdPrfApUseSupplierPulldown");

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

        Debug.print("ApVoucherBatchPrintControllerBean ejbCreate");
    }

}