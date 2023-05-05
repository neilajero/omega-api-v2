/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArReceiptBatchPrintControllerBean
 * @created May 17, 2004, 3:12 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "ArReceiptBatchPrintControllerEJB")
public class ArReceiptBatchPrintControllerBean extends EJBContextClass implements ArReceiptBatchPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;

    public ArrayList getArOpenRbAll(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArReceiptBatchPrintControllerBean getArOpenRbAll");
        ArrayList list = new ArrayList();
        try {

            Collection arReceiptBatches = arReceiptBatchHome.findOpenRbAll(AD_BRNCH, AD_CMPNY);

            for (Object receiptBatch : arReceiptBatches) {

                LocalArReceiptBatch arReceiptBatch = (LocalArReceiptBatch) receiptBatch;

                list.add(arReceiptBatch.getRbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableArReceiptBatch(Integer AD_CMPNY) {
        Debug.print("ArReceiptBatchPrintControllerBean getAdPrfEnableArReceiptBatch");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableArReceiptBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArRctByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArReceiptBatchPrintControllerBean getArRctByCriteria");
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj;

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            if (criteria.containsKey("miscType")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("bankAccount")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("batchName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arReceiptBatch.rbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("receiptType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptType");
                ctr++;
            }

            if (criteria.containsKey("receiptVoid")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptVoid");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberTo");
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

                    jbossQl.append("rct.rctApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("rct.rctReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("rct.rctApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rct.rctAdBranch=").append(AD_BRNCH).append(" AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "rct.adBankAccount.baName";

                    break;
                case "CUSTOMER CODE":

                    orderBy = "rct.arCustomer.cstCustomerCode";

                    break;
                case "RECEIPT NUMBER":

                    orderBy = "rct.rctNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", rct.rctDate");

            } else {

                jbossQl.append("ORDER BY rct.rctDate");
            }

            Collection arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arReceipts.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                if (arReceipt.getRctType().equals("MISC")) {

                    String miscType = (String) criteria.get("miscType");

                    if ((miscType.equals("ITEMS") && arReceipt.getArInvoiceLineItems().isEmpty()) || (miscType.equals("MEMO LINES") && arReceipt.getArInvoiceLines().isEmpty())) {
                        continue;
                    }
                }

                ArModReceiptDetails mdetails = new ArModReceiptDetails();
                mdetails.setRctCode(arReceipt.getRctCode());
                mdetails.setRctType(arReceipt.getRctType());
                mdetails.setRctDate(arReceipt.getRctDate());
                mdetails.setRctNumber(arReceipt.getRctNumber());
                mdetails.setRctAmount(arReceipt.getRctAmount());
                mdetails.setRctCstCustomerCode(arReceipt.getArCustomer().getCstCustomerCode());
                mdetails.setRctBaName(arReceipt.getAdBankAccount().getBaName());

                list.add(mdetails);
            }

            if (list.isEmpty()) {
                throw new GlobalNoRecordFoundException();
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
        Debug.print("ArReceiptBatchPrintControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArReceiptBatchPrintControllerBean ejbCreate");
    }

}