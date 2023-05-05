package com.ejb.txnapi.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.entities.ad.LocalAdApprovalUser;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.exception.ad.AdAURequesterMustBeEnteredOnceException;
import com.ejb.exception.ad.AdAUUserCannotBeAnApproverException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdApprovalUserDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

@Stateless(name = "AdApprovalUserApiControllerEJB")
public class AdApprovalUserApiControllerBean extends EJBContextClass implements AdApprovalUserApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    public LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    public LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    public LocalAdUserHome adUserHome;

    @Override
    public void addAdAuEntry(AdApprovalUserDetails details, Integer amountLimitCode, String username, Integer companyCode)
            throws GlobalRecordAlreadyExistException, AdAURequesterMustBeEnteredOnceException, AdAUUserCannotBeAnApproverException {
        Debug.print("AdApprovalUserControllerBean addAdAuEntry");

        String companyShortName = details.getCompanyShortName();

        try {
            LocalAdApprovalUser adApprovalUser;
            LocalAdAmountLimit adAmountLimit = adAmountLimitHome.findByPrimaryKey(amountLimitCode, companyShortName);
            try {
                adApprovalUser = adApprovalUserHome.findByUsrNameAndCalCode(username, amountLimitCode, companyCode, companyShortName);
                throw new GlobalRecordAlreadyExistException();
            } catch (FinderException ex) {
            }

            if (details.getAuType().equals(EJBCommon.REQUESTER)) {
                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndApprovalTypeAndUsrNameAndAclDept(
                                details.getAuType(), adAmountLimit.getAdApprovalDocument().getAdcCode(), username,
                                adAmountLimit.getCalDept(), adAmountLimit.getCalAmountLimit(), companyCode);
                        throw new AdAURequesterMustBeEnteredOnceException();
                    } else {
                        adApprovalUser = adApprovalUserHome.findByAclCodeAndApprovalTypeAndUsrName(details.getAuType(),
                                adAmountLimit.getAdApprovalCoaLine().getAclCode(), username, companyCode);
                        throw new AdAURequesterMustBeEnteredOnceException();
                    }
                } catch (FinderException ex) {
                }
            } else {
                try {
                    if (adAmountLimit.getAdApprovalDocument() != null) {
                        adApprovalUser = adApprovalUserHome.findByAdcCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(
                                username, EJBCommon.REQUESTER, adAmountLimit.getCalAmountLimit(),
                                adAmountLimit.getAdApprovalDocument().getAdcCode(), companyCode);
                        throw new AdAUUserCannotBeAnApproverException();
                    } else {
                        adApprovalUser = adApprovalUserHome.findByAclCodeAndUsrNameAndRequesterTypeLessThanAmountLimit(
                                username, EJBCommon.REQUESTER, adAmountLimit.getCalAmountLimit(),
                                adAmountLimit.getAdApprovalCoaLine().getAclCode(), companyCode);
                        throw new AdAUUserCannotBeAnApproverException();
                    }
                } catch (FinderException ex) {
                }
            }

            // create new amount limit
            adApprovalUser = adApprovalUserHome.create(details.getAuLevel(), details.getAuType(), details.getAuOr(), companyCode);
            adAmountLimit.addAdApprovalUser(adApprovalUser);

            LocalAdUser adUser = adUserHome.findByUsrName(username, companyCode, companyShortName);
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

}