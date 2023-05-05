package com.ejb.txn.ad;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdApprovalCoaLineDetails;
import com.util.ad.AdApprovalDocumentDetails;
import com.util.mod.ad.AdModApprovalCoaLineDetails;

import java.lang.*;

@Stateless(name = "AdApprovalCoaLineControllerEJB")
public class AdApprovalCoaLineControllerBean extends EJBContextClass implements AdApprovalCoaLineController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdApprovalCoaLineHome adApprovalCoaLineHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;

    public ArrayList getAdAclAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdApprovalCoaLineControllerBean getAdAclAll");

        ArrayList list = new ArrayList();
        try {
            Collection adApprovalCoaLines = adApprovalCoaLineHome.findAclAll(companyCode);
            if (adApprovalCoaLines.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            for (Object approvalCoaLine : adApprovalCoaLines) {
                LocalAdApprovalCoaLine adApprovalCoaLine = (LocalAdApprovalCoaLine) approvalCoaLine;
                AdModApprovalCoaLineDetails mdetails = new AdModApprovalCoaLineDetails();
                mdetails.setAclCode(adApprovalCoaLine.getAclCode());
                // get account number
                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adApprovalCoaLine.getGlChartOfAccount().getCoaCode());
                mdetails.setAclCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
                mdetails.setAclCoaDescription(glChartOfAccount.getCoaAccountDescription());
                list.add(mdetails);
            }
            return list;
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdApprovalDocumentDetails getAdAdcByAdcCode(Integer ADC_CODE, Integer companyCode) {

        Debug.print("AdApprovalCoaLineControllerBean getAdAdcByAdcCode");

        try {
            LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByPrimaryKey(ADC_CODE);
            AdApprovalDocumentDetails details = new AdApprovalDocumentDetails();
            details.setAdcType(adApprovalDocument.getAdcType());
            return details;
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdAclEntry(String COA_ACCNT_NMBR, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("AdApprovalCoaLineControllerBean addAdAclEntry");

        try {
            LocalAdApprovalCoaLine adApprovalCoaLine;
            LocalGlChartOfAccount glChartOfAccount;
            // Check if valid account number
            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(COA_ACCNT_NMBR, companyCode);

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }
            // Check if there is already a COA line with this account number
            try {

                adApprovalCoaLine = adApprovalCoaLineHome.findByCoaAccountNumber(COA_ACCNT_NMBR, companyCode);
                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {
            }
            // create new amount limit
            adApprovalCoaLine = adApprovalCoaLineHome.create(companyCode);
            glChartOfAccount.addAdApprovalCoaLine(adApprovalCoaLine);
        } catch (GlobalAccountNumberInvalidException | GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdAclEntry(AdApprovalCoaLineDetails details, String COA_ACCNT_NMBR, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException {

        Debug.print("AdApprovalCoaLineControllerBean updateAdCalEntry");

        try {
            LocalAdApprovalCoaLine adApprovalCoaLine;
            LocalGlChartOfAccount glChartOfAccount;
            try {

                adApprovalCoaLine = adApprovalCoaLineHome.findByAclCodeAndCoaAccountNumber(details.getAclCode(), COA_ACCNT_NMBR, companyCode);
                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {
            }
            try {
                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(COA_ACCNT_NMBR, companyCode);
            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // Find and Update Coa Line
            adApprovalCoaLine = adApprovalCoaLineHome.findByPrimaryKey(details.getAclCode());
            glChartOfAccount.addAdApprovalCoaLine(adApprovalCoaLine);
        } catch (GlobalRecordAlreadyExistException | GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdAclEntry(Integer ACL_CODE, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdApprovalCoaLineControllerBean deleteAdAclEntry");

        try {
            LocalAdApprovalCoaLine adApprovalCoaLine = adApprovalCoaLineHome.findByPrimaryKey(ACL_CODE);
            em.remove(adApprovalCoaLine);
        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("AdApprovalCoaLineControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdApprovalCoaLineControllerBean ejbCreate");
    }
}