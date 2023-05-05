/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApFindCheckControllerBean
 * @created February 20, 2004, 8:38 PM
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
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApCheckBatch;
import com.ejb.dao.ap.LocalApCheckBatchHome;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.ap.ApModCheckDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApFindCheckControllerEJB")
public class ApFindCheckControllerBean extends EJBContextClass implements ApFindCheckController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalApCheckBatchHome apCheckBatchHome;
    @EJB
    private LocalApCheckHome apCheckHome;

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApFindCheckControllerBean getAdBaAll");

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

    public ArrayList getApOpenCbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApFindCheckControllerBean getApOpenCbAll");

        ArrayList list = new ArrayList();

        try {

            Collection apCheckBatches = apCheckBatchHome.findOpenCbAll(AD_BRNCH, AD_CMPNY);

            for (Object checkBatch : apCheckBatches) {

                LocalApCheckBatch apCheckBatch = (LocalApCheckBatch) checkBatch;

                list.add(apCheckBatch.getCbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApCheckBatch(Integer AD_CMPNY) {

        Debug.print("ApFindCheckControllerBean getAdPrfEnableApCheckBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApCheckBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdPrfDefaultCheckType(Integer AD_CMPNY) {

        Debug.print("ApFindCheckControllerBean getAdPrfDefaultCheckType");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApFindCheckDefaultType();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApChkByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindCheckControllerBean getApChkByCriteria");

        try {

            ArrayList chkList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

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

            obj = new Object[criteriaSize];

            if (criteria.containsKey("batchName")) {

                firstArgument = false;

                jbossQl.append("WHERE chk.apCheckBatch.cbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("checkType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkType");
                ctr++;
            }

            if (criteria.containsKey("checkVoid")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkVoid");
                ctr++;
            }

            if (criteria.containsKey("checkPrinted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkPrinted=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkPrinted");
                ctr++;
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
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

            if (criteria.containsKey("checkNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("checkNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.chkDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberTo");
                ctr++;
            }

            if (criteria.containsKey("released")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkReleased=?").append(ctr + 1).append(" ");

                String released = (String) criteria.get("released");

                if (released.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

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

                    jbossQl.append("chk.chkApprovalStatus IS NULL AND chk.chkReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("chk.chkReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("chk.chkApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.chkPosted=?").append(ctr + 1).append(" ");

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

            jbossQl.append("chk.chkAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("chk.chkAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "chk.adBankAccount.baName";

                    break;
                case "SUPPLIER CODE":

                    orderBy = "chk.apSupplier.splSupplierCode";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "chk.chkDocumentNumber";

                    break;
                case "CHECK NUMBER":

                    orderBy = "chk.chkNumber";

                    break;
                case "REFERENCE NUMBER":

                    orderBy = "chk.chkReferenceNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", chk.chkDate");

            } else {

                jbossQl.append("ORDER BY chk.chkDate");
            }

            Debug.print("QL + " + jbossQl);

            Collection apChecks = null;

            try {

                apChecks = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (apChecks.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object check : apChecks) {

                LocalApCheck apCheck = (LocalApCheck) check;

                ApModCheckDetails mdetails = new ApModCheckDetails();
                mdetails.setChkCode(apCheck.getChkCode());
                mdetails.setChkType(apCheck.getChkType());
                mdetails.setChkDate(apCheck.getChkDate());
                mdetails.setChkNumber(apCheck.getChkNumber());
                mdetails.setChkDocumentNumber(apCheck.getChkDocumentNumber());
                mdetails.setChkReferenceNumber(apCheck.getChkReferenceNumber());
                mdetails.setChkAmount(apCheck.getChkAmount());
                mdetails.setChkCrossCheck(apCheck.getChkCrossCheck());
                mdetails.setChkReleased(apCheck.getChkReleased());
                mdetails.setChkSplName(apCheck.getChkSupplierName() == null ? apCheck.getApSupplier().getSplName() : apCheck.getChkSupplierName());
                mdetails.setChkBaName(apCheck.getAdBankAccount().getBaName());

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apCheck.getChkAdBranch());

                mdetails.setBrnhCde(adBranch.getBrBranchCode());

                String status = apCheck.getChkApprovalStatus();
                if (apCheck.getChkVoid() == EJBCommon.TRUE) {
                    status = "VOID";
                } else if (apCheck.getChkPosted() == EJBCommon.FALSE) {
                    status = "DRAFT";
                } else if (status.equals("N/A")) {
                    status = "POSTED";
                }
                mdetails.setChkStatus(status);

                chkList.add(mdetails);
            }

            return chkList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getApChkSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindCheckControllerBean getApChkSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

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

            obj = new Object[criteriaSize];

            if (criteria.containsKey("batchName")) {

                firstArgument = false;

                jbossQl.append("WHERE chk.apCheckBatch.cbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("checkType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkType");
                ctr++;
            }

            if (criteria.containsKey("checkVoid")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkVoid");
                ctr++;
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
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

            if (criteria.containsKey("checkNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("checkNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.chkDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberTo");
                ctr++;
            }

            if (criteria.containsKey("released")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkReleased=?").append(ctr + 1).append(" ");

                String released = (String) criteria.get("released");

                if (released.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

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

                    jbossQl.append("chk.chkApprovalStatus IS NULL AND chk.chkReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("chk.chkReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("chk.chkApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("chk.chkPosted=?").append(ctr + 1).append(" ");

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

            jbossQl.append("chk.chkAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("chk.chkAdCompany=").append(AD_CMPNY).append(" ");

            Collection apChecks = null;

            try {

                apChecks = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (apChecks.isEmpty()) throw new GlobalNoRecordFoundException();

            return apChecks.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApFindCheckControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApFindCheckControllerBean getAdPrfApUseSupplierPulldown");

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

        Debug.print("ApFindCheckControllerBean ejbCreate");
    }

    // private methods

}