package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;

import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ad.AdApprovalDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdRepApprovalListDetails;
import com.util.Debug;

@Stateless(name = "AdRepApprovalListControllerEJB")
public class AdRepApprovalListControllerBean extends EJBContextClass implements AdRepApprovalListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;

    public ArrayList getAdApprovalDocumentAll(Integer companyCode) {

        Debug.print("AdRepApprovalListControllerBean getAdApprovalDocumentAll");

        ArrayList list = new ArrayList();
        try {

            Collection adApprovalDocuments = adApprovalDocumentHome.findAdcAll(companyCode);

            for (Object approvalDocument : adApprovalDocuments) {

                LocalAdApprovalDocument adApprovalDocument = (LocalAdApprovalDocument) approvalDocument;

                list.add(adApprovalDocument.getAdcType());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeAdRepApprovalList(HashMap criteria, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepApprovalListControllerBean executeAdRepApprovalList");

        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(adc) FROM AdApprovalDocument adc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("documentType")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("documentType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("adc.adcType LIKE '%").append(criteria.get("documentType")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("adc.adcAdCompany=").append(companyCode).append(" ORDER BY adc.adcType ");

            // approval details

            AdApprovalDetails adApprovalDetails = this.getApprovalDetails(companyCode);

            // approval documents

            Collection adApprovalDocuments = adApprovalDocumentHome.getAdcByCriteria(jbossQl.toString(), obj);

            if (adApprovalDocuments.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object approvalDocument : adApprovalDocuments) {

                LocalAdApprovalDocument adApprovalDocument = (LocalAdApprovalDocument) approvalDocument;

                // approval enable

                boolean enable = this.checkDocumentEnable(adApprovalDetails, adApprovalDocument.getAdcType());

                // amount limit/s

                Collection amountLimits = adApprovalDocument.getAdAmountLimits();

                for (Object amountLimit : amountLimits) {

                    LocalAdAmountLimit adAmountLimit = (LocalAdAmountLimit) amountLimit;

                    // approval users

                    Collection adApprovalUsers = adAmountLimit.getAdApprovalUsers();

                    for (Object approvalUser : adApprovalUsers) {

                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                        AdRepApprovalListDetails details = new AdRepApprovalListDetails();

                        details.setAlDocumentType(adApprovalDocument.getAdcType());
                        details.setAlAmount(adAmountLimit.getCalAmountLimit());
                        details.setAlUserName(adApprovalUser.getAdUser().getUsrName());
                        details.setAlUserType(adApprovalUser.getAuType());
                        details.setAlEnable(enable);
                        details.setAlAndOr(adAmountLimit.getCalAndOr());

                        list.add(details);
                    }
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

    public AdCompanyDetails getAdCompany(Integer companyCode) {

        Debug.print("AdRepApprovalListControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

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

    // private method
    private AdApprovalDetails getApprovalDetails(Integer companyCode) {
        try {

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

            AdApprovalDetails details = new AdApprovalDetails();
            details.setAprEnableApCheck(adApproval.getAprEnableApCheck());
            details.setAprEnableApDebitMemo(adApproval.getAprEnableApDebitMemo());
            details.setAprEnableApVoucher(adApproval.getAprEnableApVoucher());
            details.setAprEnableArCreditMemo(adApproval.getAprEnableArCreditMemo());
            details.setAprEnableArInvoice(adApproval.getAprEnableArInvoice());
            details.setAprEnableArReceipt(adApproval.getAprEnableArReceipt());
            details.setAprEnableCmAdjustment(adApproval.getAprEnableCmAdjustment());
            details.setAprEnableCmFundTransfer(adApproval.getAprEnableCmFundTransfer());
            details.setAprEnableGlJournal(adApproval.getAprEnableGlJournal());
            details.setAprEnableInvAdjustment(adApproval.getAprEnableInvAdjustment());
            details.setAprEnableInvBuild(adApproval.getAprEnableInvBuild());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean checkDocumentEnable(AdApprovalDetails details, String documentType) {

        boolean enable = false;

        if (documentType.equals("GL JOURNAL")) {

            if (details.getAprEnableGlJournal() == 1) enable = true;
        }

        if (documentType.equals("AP VOUCHER")) {

            if (details.getAprEnableApVoucher() == 1) enable = true;
        }

        if (documentType.equals("AP DEBIT MEMO")) {

            if (details.getAprEnableApDebitMemo() == 1) enable = true;
        }

        if (documentType.equals("AP CHECK")) {

            if (details.getAprEnableApCheck() == 1) enable = true;
        }

        if (documentType.equals("AR INVOICE")) {

            if (details.getAprEnableArInvoice() == 1) enable = true;
        }

        if (documentType.equals("AR CREDIT MEMO")) {

            if (details.getAprEnableArCreditMemo() == 1) enable = true;
        }

        if (documentType.equals("AR RECEIPT")) {

            if (details.getAprEnableArReceipt() == 1) enable = true;
        }

        if (documentType.equals("CM ADJUSTMENT")) {

            if (details.getAprEnableCmAdjustment() == 1) enable = true;
        }

        if (documentType.equals("CM FUND TRANSFER")) {

            if (details.getAprEnableCmFundTransfer() == 1) enable = true;
        }

        if (documentType.equals("INV ADJUSTMENT")) {

            if (details.getAprEnableInvAdjustment() == 1) enable = true;
        }

        if (documentType.equals("INV BUILD ASSEMBLY")) {

            if (details.getAprEnableInvBuild() == 1) enable = true;
        }

        return enable;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdRepApprovalListControllerBean ejbCreate");
    }
}