package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;

import com.ejb.entities.ad.LocalAdApprovalDocument;
import com.ejb.dao.ad.LocalAdApprovalDocumentHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.EJBContextClass;
import com.util.ad.AdApprovalDocumentDetails;
import com.util.Debug;

@Stateless(name = "AdApprovalDocumentControllerEJB")
public class AdApprovalDocumentControllerBean extends EJBContextClass implements AdApprovalDocumentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;

    public ArrayList getAdAdcAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdApprovalControllerBean getAdAdcAll");

        ArrayList list = new ArrayList();
        try {

            Collection adApprovalDocuments = adApprovalDocumentHome.findAdcAll(companyCode);

            if (adApprovalDocuments.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            }

            for (Object approvalDocument : adApprovalDocuments) {

                LocalAdApprovalDocument adApprovalDocument = (LocalAdApprovalDocument) approvalDocument;

                AdApprovalDocumentDetails details = new AdApprovalDocumentDetails();
                details.setAdcCode(adApprovalDocument.getAdcCode());
                details.setAdcType(adApprovalDocument.getAdcType());
                details.setAdcPrintOption(adApprovalDocument.getAdcPrintOption());
                details.setAdcAllowDuplicate(adApprovalDocument.getAdcAllowDuplicate());
                details.setAdcTrackDuplicate(adApprovalDocument.getAdcTrackDuplicate());
                details.setAdcEnableCreditLimitChecking(adApprovalDocument.getAdcEnableCreditLimitChecking());

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdAdcEntry(AdApprovalDocumentDetails details, Integer companyCode) {

        Debug.print("AdApprovalControllerBean updateAdAdcEntry");

        try {

            LocalAdApprovalDocument adApprovalDocument = null;

            // update approval document setup

            adApprovalDocument = adApprovalDocumentHome.findByPrimaryKey(details.getAdcCode());

            adApprovalDocument.setAdcType(details.getAdcType());
            adApprovalDocument.setAdcPrintOption(details.getAdcPrintOption());
            adApprovalDocument.setAdcAllowDuplicate(details.getAdcAllowDuplicate());
            adApprovalDocument.setAdcTrackDuplicate(details.getAdcTrackDuplicate());

            if (details.getAdcType().equalsIgnoreCase("AR INVOICE"))
                adApprovalDocument.setAdcEnableCreditLimitChecking(details.getAdcEnableCreditLimitChecking());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdApprovalControllerBean ejbCreate");

    }
}