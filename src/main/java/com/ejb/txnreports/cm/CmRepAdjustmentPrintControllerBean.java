/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmRepAdjustmentPrintControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.cm;

import java.util.ArrayList;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.mod.cm.CmModAdjustmentDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepAdjustmentPrintControllerEJB")
public class CmRepAdjustmentPrintControllerBean extends EJBContextClass implements CmRepAdjustmentPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;


    public ArrayList executeCmRepAdjustmentPrint(ArrayList adjCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepAdjustmentPrintControllerBean executeCmRepAdjustmentPrint");

        ArrayList list = new ArrayList();

        try {
            Debug.print(adjCodeList.size() + " --");

            for (Object o : adjCodeList) {

                Integer ADJ_CODE = (Integer) o;

                LocalCmAdjustment cmAdjustment = null;

                try {

                    cmAdjustment = cmAdjustmentHome.findByPrimaryKey(ADJ_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                Debug.print("pass 1");

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByAdcType("CM ADJUSTMENT", AD_CMPNY);

                if (adApprovalDocument.getAdcPrintOption().equals("PRINT APPROVED ONLY")) {

                    if (cmAdjustment.getAdjApprovalStatus() == null || cmAdjustment.getAdjApprovalStatus().equals("PENDING")) {

                        continue;
                    }

                } else if (adApprovalDocument.getAdcPrintOption().equals("PRINT UNAPPROVED ONLY")) {

                    if (cmAdjustment.getAdjApprovalStatus() != null && (cmAdjustment.getAdjApprovalStatus().equals("N/A") || cmAdjustment.getAdjApprovalStatus().equals("APPROVED"))) {

                        continue;
                    }
                }
                Debug.print("pass 2");
                if (adApprovalDocument.getAdcAllowDuplicate() == EJBCommon.FALSE && cmAdjustment.getAdjPrinted() == EJBCommon.TRUE) {

                    continue;
                }

                // show duplicate

                boolean showDuplicate = adApprovalDocument.getAdcTrackDuplicate() == EJBCommon.TRUE && cmAdjustment.getAdjPrinted() == EJBCommon.TRUE;

                // set printed

                cmAdjustment.setAdjPrinted(EJBCommon.TRUE);

                Debug.print("pass final " + cmAdjustment.getAdjType());

                CmModAdjustmentDetails mdetails = new CmModAdjustmentDetails();

                Debug.print("pass final 1");
                mdetails.setAdjType(cmAdjustment.getAdjType());
                if (cmAdjustment.getArCustomer() != null) {
                    mdetails.setAdjCustomerCode(cmAdjustment.getArCustomer().getCstCustomerCode());
                    mdetails.setAdjCustomerName(cmAdjustment.getArCustomer().getCstName());
                }
                Debug.print("pass final 2");
                mdetails.setAdjDate(cmAdjustment.getAdjDate());
                mdetails.setAdjDocumentNumber(cmAdjustment.getAdjDocumentNumber());

                Debug.print("pass final 3");
                mdetails.setAdjBaName(cmAdjustment.getAdBankAccount().getAdBank().getBnkName());
                Debug.print("pass final 4");
                mdetails.setAdjReferenceNumber(cmAdjustment.getAdjReferenceNumber());
                mdetails.setAdjAmount(cmAdjustment.getAdjAmount());
                mdetails.setAdjAmountApplied(cmAdjustment.getAdjAmountApplied());
                mdetails.setAdjMemo(cmAdjustment.getAdjMemo());

                list.add(mdetails);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;
        } catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepReceiptPrintControllerBean getAdCompany");

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
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

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

        Debug.print("CmRepAdjustmentPrintBean ejbCreate");
    }
}