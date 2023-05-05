/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class AdAmountLimitControllerBean
 * @modified June 13, 2022, 13:51
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ad;

import java.util.*;
import jakarta.ejb.*;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdAmountLimitDetails;
import com.util.ad.AdApprovalDocumentDetails;
import com.util.mod.ad.AdModApprovalCoaLineDetails;

@Stateless(name = "AdAmountLimitControllerEJB")
public class AdAmountLimitControllerBean extends EJBContextClass implements AdAmountLimitController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalCoaLineHome adApprovalCoaLineHome;

    public ArrayList getAdCalByAdcCode(Integer approvalDocumentCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdAmountLimitControllerBean getAdCalByAdcCode");

        ArrayList list = new ArrayList();
        try {
            Collection adAmountLimits = adAmountLimitHome.findByAdcCode(approvalDocumentCode, companyCode);
            if (adAmountLimits.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            for (Object amountLimit : adAmountLimits) {
                LocalAdAmountLimit adAmountLimit = (LocalAdAmountLimit) amountLimit;
                AdAmountLimitDetails details = new AdAmountLimitDetails();
                details.setCalCode(adAmountLimit.getCalCode());
                details.setCalDept(adAmountLimit.getCalDept());
                details.setCalAmountLimit(adAmountLimit.getCalAmountLimit());
                details.setCalAndOr(adAmountLimit.getCalAndOr());
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

    public AdApprovalDocumentDetails getAdAdcByAdcCode(Integer approvalDocumentCode, Integer companyCode) {

        Debug.print("AdAmountLimitControllerBean getAdAdcByAdcCode");

        try {
            LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByPrimaryKey(approvalDocumentCode);
            AdApprovalDocumentDetails details = new AdApprovalDocumentDetails();
            details.setAdcCode(adApprovalDocument.getAdcCode());
            details.setAdcType(adApprovalDocument.getAdcType());
            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdCalByAclCode(Integer approvalCoaLineCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdAmountLimitControllerBean getAdCalByAclCode");

        ArrayList list = new ArrayList();
        try {
            Collection adAmountLimits = adAmountLimitHome.findByAclCode(approvalCoaLineCode, companyCode);
            if (adAmountLimits.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            for (Object amountLimit : adAmountLimits) {
                LocalAdAmountLimit adAmountLimit = (LocalAdAmountLimit) amountLimit;
                AdAmountLimitDetails details = new AdAmountLimitDetails();
                details.setCalCode(adAmountLimit.getCalCode());
                details.setCalDept(adAmountLimit.getCalDept());
                details.setCalAmountLimit(adAmountLimit.getCalAmountLimit());
                details.setCalAndOr(adAmountLimit.getCalAndOr());
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

    public AdModApprovalCoaLineDetails getAdAclByAclCode(Integer approvalCoaLineCode, Integer companyCode) {

        Debug.print("AdAmountLimitControllerBean getAdAclByAclCode");

        try {

            LocalAdApprovalCoaLine adApprovalCoaLine = adApprovalCoaLineHome.findByPrimaryKey(approvalCoaLineCode);

            AdModApprovalCoaLineDetails mdetails = new AdModApprovalCoaLineDetails();
            mdetails.setAclCode(adApprovalCoaLine.getAclCode());

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adApprovalCoaLine.getGlChartOfAccount().getCoaCode());

            mdetails.setAclCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setAclCoaDescription(glChartOfAccount.getCoaAccountDescription());

            return mdetails;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdCalEntry(AdAmountLimitDetails details, Integer approvalDocumentCode, Integer approvalCoaLineCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdAmountLimitControllerBean addAdCalEntry");

        try {

            LocalAdAmountLimit adAmountLimit = null;

            try {

                if (approvalDocumentCode != null) {

                    adAmountLimit = adAmountLimitHome.findByCalDeptAndCalAmountLimitAndAdcCode(details.getCalDept(), details.getCalAmountLimit(), approvalDocumentCode, companyCode);

                    throw new GlobalRecordAlreadyExistException();

                } else {

                    adAmountLimit = adAmountLimitHome.findByCalAmountLimitAndAclCode(details.getCalAmountLimit(), approvalCoaLineCode, companyCode);

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // create new amount limit

            adAmountLimit = adAmountLimitHome.create(details.getCalDept(), details.getCalAmountLimit(), details.getCalAndOr(), companyCode);

            if (approvalDocumentCode != null) {

                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByPrimaryKey(approvalDocumentCode);
                adApprovalDocument.addAdAmountLimit(adAmountLimit);

            } else {

                LocalAdApprovalCoaLine adApprovalCoaLine = adApprovalCoaLineHome.findByPrimaryKey(approvalCoaLineCode);
                adApprovalCoaLine.addAdAmountLimit(adAmountLimit);
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdCalEntry(AdAmountLimitDetails details, Integer approvalDocumentCode, Integer approvalCoaLineCode, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdAmountLimitControllerBean updateAdCalEntry");

        try {

            LocalAdAmountLimit adAmountLimit = null;

            try {

                if (approvalDocumentCode != null) {

                    adAmountLimit = adAmountLimitHome.findByCalDeptAndCalAmountLimitAndAdcCode(details.getCalDept(), details.getCalAmountLimit(), approvalDocumentCode, companyCode);

                } else {

                    adAmountLimit = adAmountLimitHome.findByCalAmountLimitAndAclCode(details.getCalAmountLimit(), approvalCoaLineCode, companyCode);
                }

                if (adAmountLimit != null && !adAmountLimit.getCalCode().equals(details.getCalCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // Find and Update Amount Limit

            adAmountLimit = adAmountLimitHome.findByPrimaryKey(details.getCalCode());
            adAmountLimit.setCalDept(details.getCalDept());
            adAmountLimit.setCalAmountLimit(details.getCalAmountLimit());
            adAmountLimit.setCalAndOr(details.getCalAndOr());

            if (approvalDocumentCode != null) {

                LocalAdApprovalDocument adApprovalDocument = adApprovalDocumentHome.findByPrimaryKey(approvalDocumentCode);
                adApprovalDocument.addAdAmountLimit(adAmountLimit);

            } else {

                LocalAdApprovalCoaLine adApprovalCoaLine = adApprovalCoaLineHome.findByPrimaryKey(approvalCoaLineCode);
                adApprovalCoaLine.addAdAmountLimit(adAmountLimit);
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdCalEntry(Integer amountLimitCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdAmountLimitControllerBean deleteAdCalEntry");

        try {
            LocalAdAmountLimit adAmountLimit = adAmountLimitHome.findByPrimaryKey(amountLimitCode);
            em.remove(adAmountLimit);
        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("AdAmountLimitControllerBean getGlFcPrecisionUnit");

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

        Debug.print("AdAmountLimitControllerBean ejbCreate");
    }
}