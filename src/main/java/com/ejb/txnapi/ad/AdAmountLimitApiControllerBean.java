package com.ejb.txnapi.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.entities.ad.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.AmountLimitRequest;
import com.ejb.restfulapi.ad.models.ApprovalUser;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.ad.AdAmountLimitDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Stateless(name = "AdAmountLimitApiControllerBeanEJB")
public class AdAmountLimitApiControllerBean extends EJBContextClass implements AdAmountLimitApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalDocumentHome adApprovalDocumentHome;
    @EJB
    private LocalAdApprovalCoaLineHome adApprovalCoaLineHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;

    @Override
    public ArrayList getAdCalByAdcCode(Integer approvalDocumentCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdAmountLimitApiControllerBean getAdCalByAdcCode");

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
        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void addAdCalEntry(AdAmountLimitDetails details, Integer approvalDocumentCode,
                              Integer approvalCoaLineCode, Integer companyCode, String companyShortName)
            throws GlobalRecordAlreadyExistException {

        Debug.print("AdAmountLimitApiControllerBean addAdCalEntry");

        try {
            IsRecordExists(details, approvalDocumentCode, approvalCoaLineCode, companyCode, companyShortName);
            LocalAdAmountLimit adAmountLimit;
            LocalAdApprovalUser adApprovalUser;
            LocalAdUser adUser;

            // create new amount limit
            adAmountLimit = adAmountLimitHome
                    .CalDept(details.getCalDept().toUpperCase())
                    .CalAmountLimit(details.getCalAmountLimit())
                    .CalAndOr(details.getCalAndOr().toUpperCase())
                    .CalAdCompany(companyCode)
                    .buildAmountLimit(companyShortName);

            // create approval users
            for (ApprovalUser approvalUser : details.getUsers()) {

                if(approvalUser.getType().equalsIgnoreCase(EJBCommon.APPROVER))
                {

                }

                adApprovalUser = adApprovalUserHome
                        .AuLevel(approvalUser.getLevel() != null ? approvalUser.getLevel().toUpperCase() : approvalUser.getLevel())
                        .AuType(approvalUser.getType().toUpperCase())
                        .AuAdCompany(companyCode)
                        .buildApprovalUser(companyShortName);

                adAmountLimit.addAdApprovalUser(adApprovalUser);

                // setup users
                adUser = adUserHome.findByUsrName(approvalUser.getUsername(),
                        companyCode, companyShortName);
                adUser.addAdApprovalUser(adApprovalUser);
            }

            if (approvalDocumentCode != null) {
                LocalAdApprovalDocument adApprovalDocument =
                        adApprovalDocumentHome.findByPrimaryKey(approvalDocumentCode, companyShortName);
                adApprovalDocument.addAdAmountLimit(adAmountLimit);
            } else {
                LocalAdApprovalCoaLine adApprovalCoaLine =
                        adApprovalCoaLineHome.findByPrimaryKey(approvalCoaLineCode, companyShortName);
                adApprovalCoaLine.addAdAmountLimit(adAmountLimit);
            }
        }
        catch (GlobalRecordAlreadyExistException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }

    @Override
    public OfsApiResponse saveAmountLimit(AmountLimitRequest request) {

        Debug.print("AdAmountLimitApiControllerBean saveAmountLimit");

        OfsApiResponse apiResponse = new OfsApiResponse();

        LocalAdCompany adCompany = null;
        LocalAdApprovalDocument adApprovalDocument = null;
        LocalAdApprovalCoaLine adApprovalCoaLine = null;
        LocalAdUser adUser = null;
        Integer approvalDocumentCode = null;
        Integer approvalCoaLineCode = null;


        try {

            AdAmountLimitDetails details = new AdAmountLimitDetails();
            details.setCalDept(request.getDepartment()); //TODO: The value passed here is from AD_LK_UP_VL table under AD DEPARTMENT
            details.setCalAndOr(request.getAndOr());
            details.setCalAmountLimit(request.getAmountLimit());

            // Company Code
            try {
                if (request.getCompanyCode() == null || request.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    details.setCalAdCompany(adCompany.getCmpCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Validate Users exists
            try {
                if (request.getApprovalUsers() == null || request.getApprovalUsers().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }

                int requesterCounter = 0;
                for (ApprovalUser approvalUser : request.getApprovalUsers()) {
                    OfsApiResponse validationResponse = validateApprovalUserType(apiResponse, approvalUser);
                    if (validationResponse != null) {
                        return validationResponse;
                    }
                    adUser = adUserHome.findByUsrName(approvalUser.getUsername(),
                            adCompany.getCmpCode(), adCompany.getCmpShortName());

                    if (approvalUser.getType().equalsIgnoreCase(EJBCommon.REQUESTER)) {
                        requesterCounter++;
                    }
                }
                if (requesterCounter == 0) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_079);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_079_MSG);
                    return apiResponse;
                }
                details.setUsers(request.getApprovalUsers());
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            // Approval Document Code
            try {
                if (request.getDocumentCode() != null) {
                    if (!request.getDocumentCode().isEmpty()) {
                        adApprovalDocument = adApprovalDocumentHome.findByAdcType(request.getDocumentCode(),
                                adCompany.getCmpCode(), adCompany.getCmpShortName());
                        if (adApprovalDocument != null) {
                            approvalDocumentCode = adApprovalDocument.getAdcCode();
                        }
                    }
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Approval COA Line Code
            try {
                if (request.getCoaLineCode() != null) {
                    if (!request.getCoaLineCode().isEmpty()) {
                        adApprovalCoaLine = adApprovalCoaLineHome.findByCoaAccountNumber(request.getCoaLineCode(),
                                adCompany.getCmpCode(), adCompany.getCmpShortName());
                        if (adApprovalDocument != null) {
                            approvalCoaLineCode = adApprovalCoaLine.getAclCode();
                        }
                    }
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            this.addAdCalEntry(details, approvalDocumentCode, approvalCoaLineCode,
                    details.getCalAdCompany(), adCompany.getCmpShortName());

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setStatus("Added amount limit successfully.");

        }
        catch (GlobalRecordAlreadyExistException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_074);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_074_MSG);
            return apiResponse;
        }
        catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;

    }

    private static OfsApiResponse validateApprovalUserType(OfsApiResponse apiResponse, ApprovalUser approvalUser) {

        String[] allowedApprovalUserTypes = { EJBCommon.APPROVER, EJBCommon.REQUESTER };

        if (approvalUser.getType() == null) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_077);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_077_MSG);
            return apiResponse;
        }

        // Verify if APPROVER and REQUESTER user type
        int index = Arrays.asList(allowedApprovalUserTypes).indexOf(approvalUser.getType().toUpperCase());
        if (index >= 0) {
            if (approvalUser.getType().equalsIgnoreCase(EJBCommon.APPROVER) && approvalUser.getLevel() == null) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_075);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_075_MSG);
                return apiResponse;
            }
            if (approvalUser.getType().equalsIgnoreCase(EJBCommon.REQUESTER) && approvalUser.getLevel() != null) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_076);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_076_MSG);
                return apiResponse;
            }
        } else {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_078);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_078_MSG);
            return apiResponse;
        }
        return null;
    }

    private void IsRecordExists(AdAmountLimitDetails details, Integer approvalDocumentCode,
                                Integer approvalCoaLineCode, Integer companyCode, String companyShortName)
            throws GlobalRecordAlreadyExistException {

        LocalAdAmountLimit adAmountLimit;
        try {
            if (approvalDocumentCode != null) {
                adAmountLimit = adAmountLimitHome.findByCalDeptAndCalAmountLimitAndAdcCode(
                        details.getCalDept(), details.getCalAmountLimit(), approvalDocumentCode,
                        companyCode, companyShortName);
            } else {
                adAmountLimit = adAmountLimitHome.findByCalAmountLimitAndAclCode(
                        details.getCalAmountLimit(), approvalCoaLineCode, companyCode, companyShortName);
            }
            if (adAmountLimit != null) {
                throw new GlobalRecordAlreadyExistException();
            }
        }
        catch (GlobalRecordAlreadyExistException ex) {
            throw ex;
        }
        catch (FinderException ex) {
        }
    }

}