/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class AdApprovalUserControllerBean
 * @created March 24, 2004, 8:04 PM
 * @author Enrico C. Yap
 */
package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;

import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.exception.ad.AdAURequesterMustBeEnteredOnceException;
import com.ejb.exception.ad.AdAUUserCannotBeARequesterException;
import com.ejb.exception.ad.AdAUUserCannotBeAnApproverException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.ad.AdAmountLimitDetails;
import com.util.ad.AdApprovalUserDetails;
import com.util.mod.ad.AdModApprovalUserDetails;

@Stateless(name = "AdApprovalUserControllerEJB")
public class AdApprovalUserControllerBean extends EJBContextClass implements AdApprovalUserController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;

    public ArrayList getAdAuByCalCode(Integer amountLimitCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdApprovalUserControllerBean getAdAuByCalCode");

        ArrayList list = new ArrayList();
        try {
            Collection adApprovalUsers = adApprovalUserHome.findByCalCode(amountLimitCode, companyCode);
            if (adApprovalUsers.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            for (Object approvalUser : adApprovalUsers) {
                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                AdModApprovalUserDetails mdetails = new AdModApprovalUserDetails();
                mdetails.setAuCode(adApprovalUser.getAuCode());
                mdetails.setAuLevel(adApprovalUser.getAuLevel());
                mdetails.setAuType(adApprovalUser.getAuType());
                mdetails.setAuUsrName(adApprovalUser.getAdUser().getUsrName());
                mdetails.setAuOr(adApprovalUser.getAuOr());
                list.add(mdetails);
            }
            return list;
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public AdAmountLimitDetails getAdCalByCalCode(Integer amountLimitCode, Integer companyCode) {

        Debug.print("AdApprovalUserControllerBean getAdCalByCalCode");

        try {
            LocalAdAmountLimit adAmountLimit = adAmountLimitHome.findByPrimaryKey(amountLimitCode);
            AdAmountLimitDetails details = new AdAmountLimitDetails();
            details.setCalCode(adAmountLimit.getCalCode());
            details.setCalAmountLimit(adAmountLimit.getCalAmountLimit());
            details.setCalAndOr(adAmountLimit.getCalAndOr());
            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdAuEntry(AdApprovalUserDetails details, Integer amountLimitCode, String username, Integer companyCode) throws GlobalRecordAlreadyExistException, AdAURequesterMustBeEnteredOnceException, AdAUUserCannotBeAnApproverException {

        Debug.print("AdApprovalUserControllerBean addAdAuEntry");

        try {
            LocalAdApprovalUser adApprovalUser;
            LocalAdAmountLimit adAmountLimit = adAmountLimitHome.findByPrimaryKey(amountLimitCode);
            try {
                adApprovalUser = adApprovalUserHome.findByUsrNameAndCalCode(username, amountLimitCode, companyCode);
                throw new GlobalRecordAlreadyExistException();
            } catch (FinderException ex) {
            }

            if (details.getAuType().equals("REQUESTER")) {
                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndApprovalTypeAndUsrNameAndAclDept(details.getAuType(), adAmountLimit.getAdApprovalDocument().getAdcCode(), username, adAmountLimit.getCalDept(), adAmountLimit.getCalAmountLimit(), companyCode);
                        throw new AdAURequesterMustBeEnteredOnceException();
                    } else {
                        adApprovalUser = adApprovalUserHome.findByAclCodeAndApprovalTypeAndUsrName(details.getAuType(), adAmountLimit.getAdApprovalCoaLine().getAclCode(), username, companyCode);
                        throw new AdAURequesterMustBeEnteredOnceException();
                    }
                } catch (FinderException ex) {
                }
            } else {
                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(username, "REQUESTER", adAmountLimit.getCalAmountLimit(), adAmountLimit.getAdApprovalDocument().getAdcCode(), companyCode);
                        throw new AdAUUserCannotBeAnApproverException();
                    } else {
                        adApprovalUser = adApprovalUserHome.findByAclCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(username, "REQUESTER", adAmountLimit.getCalAmountLimit(), adAmountLimit.getAdApprovalCoaLine().getAclCode(), companyCode);
                        throw new AdAUUserCannotBeAnApproverException();
                    }
                } catch (FinderException ex) {
                }
            }

            // create new amount limit
            adApprovalUser = adApprovalUserHome.create(details.getAuLevel(), details.getAuType(), details.getAuOr(), companyCode);
            adAmountLimit.addAdApprovalUser(adApprovalUser);

            LocalAdUser adUser = adUserHome.findByUsrName(username, companyCode);
            adUser.addAdApprovalUser(adApprovalUser);

        } catch (GlobalRecordAlreadyExistException | AdAUUserCannotBeAnApproverException |
                 AdAURequesterMustBeEnteredOnceException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateAdAuEntry(AdApprovalUserDetails details, Integer amountLimitCode, String username, Integer companyCode) throws GlobalRecordAlreadyExistException, AdAURequesterMustBeEnteredOnceException, AdAUUserCannotBeAnApproverException, AdAUUserCannotBeARequesterException {

        Debug.print("AdApprovalUserControllerBean updateAdAuEntry");

        try {
            LocalAdApprovalUser adApprovalUser;
            LocalAdAmountLimit adAmountLimit = adAmountLimitHome.findByPrimaryKey(amountLimitCode);
            try {
                adApprovalUser = adApprovalUserHome.findByUsrNameAndCalCode(username, amountLimitCode, companyCode);
                if (adApprovalUser != null && !adApprovalUser.getAuCode().equals(details.getAuCode())) {
                    throw new GlobalRecordAlreadyExistException();
                }
            } catch (FinderException ex) {
            }

            if (details.getAuType().equals("REQUESTER")) {
                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndApprovalTypeAndUsrNameAndAclDept(details.getAuType(), adAmountLimit.getAdApprovalDocument().getAdcCode(), username, adAmountLimit.getCalDept(), adAmountLimit.getCalAmountLimit(), companyCode);
                        throw new AdAURequesterMustBeEnteredOnceException();

                    } else {
                        adApprovalUser = adApprovalUserHome.findByAclCodeAndApprovalTypeAndUsrName(details.getAuType(), adAmountLimit.getAdApprovalCoaLine().getAclCode(), username, companyCode);
                        throw new AdAURequesterMustBeEnteredOnceException();
                    }
                } catch (FinderException ex) {
                }

                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndUsrNameAndApproverTypeLessThanAmountLimit(username, "APPROVER", adAmountLimit.getCalAmountLimit(), adAmountLimit.getAdApprovalDocument().getAdcCode(), companyCode);
                        throw new AdAUUserCannotBeARequesterException();
                    } else {

                        adApprovalUser = adApprovalUserHome.findByAclCodeAndUsrNameAndApproverTypeLessThanAmountLimit(username, "APPROVER", adAmountLimit.getCalAmountLimit(), adAmountLimit.getAdApprovalCoaLine().getAclCode(), companyCode);
                        throw new AdAUUserCannotBeARequesterException();
                    }
                } catch (FinderException ex) {
                }

            } else {

                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(username, "REQUESTER", adAmountLimit.getCalAmountLimit(), adAmountLimit.getAdApprovalDocument().getAdcCode(), companyCode);
                        throw new AdAUUserCannotBeAnApproverException();
                    } else {
                        adApprovalUser = adApprovalUserHome.findByAclCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(username, "REQUESTER", adAmountLimit.getCalAmountLimit(), adAmountLimit.getAdApprovalCoaLine().getAclCode(), companyCode);
                        throw new AdAUUserCannotBeAnApproverException();
                    }
                } catch (FinderException ex) {
                }
            }

            // Find and Update Amount Limit
            adApprovalUser = adApprovalUserHome.findByPrimaryKey(details.getAuCode());
            adApprovalUser.setAuLevel(details.getAuLevel());
            adApprovalUser.setAuType(details.getAuType());
            adApprovalUser.setAuOr(details.getAuOr());

            adAmountLimit.addAdApprovalUser(adApprovalUser);

            LocalAdUser adUser = adUserHome.findByUsrName(username, companyCode);
            adUser.addAdApprovalUser(adApprovalUser);

        } catch (GlobalRecordAlreadyExistException | AdAUUserCannotBeARequesterException |
                 AdAUUserCannotBeAnApproverException | AdAURequesterMustBeEnteredOnceException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdAuEntry(Integer approvalUserCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("AdApprovalUserControllerBean deleteAdAuEntry");

        try {
            LocalAdApprovalUser adApprovalUser = adApprovalUserHome.findByPrimaryKey(approvalUserCode);
            em.remove(adApprovalUser);
        } catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("AdApprovalUserControllerBean getGlFcPrecisionUnit");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdApprovalUserControllerBean ejbCreate");
    }
}