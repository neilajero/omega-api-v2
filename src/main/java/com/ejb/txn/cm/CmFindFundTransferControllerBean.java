/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmFindFundTransferControllerBean
 * @created
 * @author
 */
package com.ejb.txn.cm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.cm.CmModFundTransferEntryDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmFindFundTransferControllerEJB")
public class CmFindFundTransferControllerBean extends EJBContextClass implements CmFindFundTransferController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmFindFundTransferControllerBean getAdBaAll");

        Collection adBankAccounts = null;

        LocalAdBankAccount adBankAccount = null;

        ArrayList list = new ArrayList();

        try {

            adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBankAccounts.isEmpty()) {

            return null;
        }

        for (Object bankAccount : adBankAccounts) {

            adBankAccount = (LocalAdBankAccount) bankAccount;

            list.add(adBankAccount.getBaName());
        }

        return list;
    }

    public ArrayList getCmFtByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmFindFundTransferControllerBean getCmFtByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

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

            if (criteria.containsKey("bankAccountFrom")) {

                firstArgument = false;

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByBaName((String) criteria.get("bankAccountFrom"), AD_CMPNY);

                jbossQl.append("WHERE ft.ftAdBaAccountFrom=?").append(ctr + 1).append(" ");
                obj[ctr] = adBankAccountFrom.getBaCode();
                ctr++;
            }

            if (criteria.containsKey("bankAccountTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByBaName((String) criteria.get("bankAccountTo"), AD_CMPNY);

                jbossQl.append("ft.ftAdBaAccountTo=?").append(ctr + 1).append(" ");
                obj[ctr] = adBankAccountTo.getBaCode();
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ft.ftDocumentNumber>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("ft.ftDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ft.ftReferenceNumber=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumber");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ft.ftDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("ft.ftDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
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

                    jbossQl.append("ft.ftApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("ft.ftReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("ft.ftApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ft.ftVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("fundTransferVoid");
            ctr++;

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ft.ftPosted=?").append(ctr + 1).append(" ");

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

            jbossQl.append("ft.ftAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ft.ftAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY != null && ORDER_BY.equals("REFERENCE NUMBER")) {

                orderBy = "ft.ftReferenceNumber, ft.ftDate";

            } else if (ORDER_BY != null && ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "ft.ftDocumentNumber, ft.ftDate";

            } else {

                orderBy = "ft.ftDate";
            }

            jbossQl.append("ORDER BY ").append(orderBy);

            Collection cmFundTransfers = null;

            cmFundTransfers = cmFundTransferHome.getFtByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (cmFundTransfers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object fundTransfer : cmFundTransfers) {

                LocalCmFundTransfer cmFundTransfer = (LocalCmFundTransfer) fundTransfer;

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountFrom());

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByPrimaryKey(cmFundTransfer.getFtAdBaAccountTo());

                CmModFundTransferEntryDetails mdetails = new CmModFundTransferEntryDetails();
                mdetails.setFtCode(cmFundTransfer.getFtCode());
                mdetails.setFtAmount(cmFundTransfer.getFtAmount());
                mdetails.setFtDate(cmFundTransfer.getFtDate());
                mdetails.setFtDocumentNumber(cmFundTransfer.getFtDocumentNumber());
                mdetails.setFtReferenceNumber(cmFundTransfer.getFtReferenceNumber());
                mdetails.setFtVoid(cmFundTransfer.getFtVoid());
                mdetails.setFtAdBankAccountNameFrom(adBankAccountFrom.getBaName());
                mdetails.setFtAdBankAccountNameTo(adBankAccountTo.getBaName());
                mdetails.setFtCurrencyFrom(adBankAccountFrom.getGlFunctionalCurrency().getFcName());
                mdetails.setFtCurrencyTo(adBankAccountTo.getGlFunctionalCurrency().getFcName());

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

    public Integer getCmFtSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmFindFundTransferControllerBean getCmFtSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ft) FROM CmFundTransfer ft ");

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

            if (criteria.containsKey("bankAccountFrom")) {

                firstArgument = false;

                LocalAdBankAccount adBankAccountFrom = adBankAccountHome.findByBaName((String) criteria.get("bankAccountFrom"), AD_CMPNY);

                jbossQl.append("WHERE ft.ftAdBaAccountFrom=?").append(ctr + 1).append(" ");
                obj[ctr] = adBankAccountFrom.getBaCode();
                ctr++;
            }

            if (criteria.containsKey("bankAccountTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                LocalAdBankAccount adBankAccountTo = adBankAccountHome.findByBaName((String) criteria.get("bankAccountTo"), AD_CMPNY);

                jbossQl.append("ft.ftAdBaAccountTo=?").append(ctr + 1).append(" ");
                obj[ctr] = adBankAccountTo.getBaCode();
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ft.ftDocumentNumber>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("ft.ftDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ft.ftReferenceNumber=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumber");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ft.ftDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("ft.ftDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
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

                    jbossQl.append("ft.ftApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("ft.ftReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("ft.ftApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ft.ftVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("fundTransferVoid");
            ctr++;

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ft.ftPosted=?").append(ctr + 1).append(" ");

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

            jbossQl.append("ft.ftAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ft.ftAdCompany=").append(AD_CMPNY).append(" ");

            Collection cmFundTransfers = null;

            cmFundTransfers = cmFundTransferHome.getFtByCriteria(jbossQl.toString(), obj, 0, 0);

            if (cmFundTransfers.size() == 0) throw new GlobalNoRecordFoundException();

            return cmFundTransfers.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("CmFindFundTransferControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmFindFundTransferControllerBean ejbCreate");
    }
}