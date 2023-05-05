package com.ejb.txnapi.ar;

import com.ejb.dao.ad.*;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.ar.ArCCCoaGlReceivableAccountNotFoundException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.CustomerRequest;
import com.ejb.txn.OmegaDataListController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.mod.ad.AdModBranchCustomerDetails;
import com.util.mod.ar.ArModCustomerDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "ArCustomerEntryApiControllerEJB")
public class ArCustomerEntryApiControllerBean extends EJBContextClass implements ArCustomerEntryApiController {

    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private OmegaDataListController omegaDataList;

    /**
     * This method saves customer information into Omega ERP.
     */
    @Override
    public Integer saveArCstEntry(ArModCustomerDetails modCustomerDetails, String customerType, String dealPrice, String paymentTerm, String customerClass,
                                  String bankAccount, Integer branchCode, Integer companyCode)
            throws ArCCCoaGlReceivableAccountNotFoundException {

        Debug.print("ArCustomerEntryApiControllerBean saveArCstEntry");

        LocalArCustomer arCustomer;
        LocalGlChartOfAccount glReceivableChartOfAccount;
        LocalArCustomerClass arCustomerClass;
        LocalArCustomerType arCustomerType = null;

        String companyShortName = modCustomerDetails.getCompanyShortName();

        try {
            arCustomerClass = arCustomerClassHome.findByCcName(customerClass, companyCode, companyShortName);
            // Auto Generate of Customer Code
            try {
                arCustomer = arCustomerHome.findByCstCustomerCode(arCustomerClass.getCcNextCustomerCode(), companyCode, companyShortName);
                // Regenerate customer code if already in-used
                if (arCustomer != null) {
                    arCustomerClass.setCcNextCustomerCode(
                            EJBCommon.incrementStringNumber(arCustomerClass.getCcNextCustomerCode()));
                }
            }
            catch (FinderException ex) {
            }

            try {
                //TODO: Find solution for the COA_CTGRY and COA_DEFAULT_BRANCH parameter not to be hard-coded
                glReceivableChartOfAccount = glChartOfAccountHome.findHoCoaByCoaCategory(
                        "Retained Earnings", "OGJ", companyCode, companyShortName);
            }
            catch (FinderException ex) {
                throw new ArCCCoaGlReceivableAccountNotFoundException();
            }

            // create new customer
            arCustomer = arCustomerHome
                    .CstCustomerCode(arCustomerClass.getCcNextCustomerCode() == null ? modCustomerDetails.getCstCustomerCode() : arCustomerClass.getCcNextCustomerCode())
                    .CstName(modCustomerDetails.getCstName())
                    .CstDescription(modCustomerDetails.getCstDescription())
                    .CstDealPrice(modCustomerDetails.getCstDealPrice())
                    .CstEnable(EJBCommon.TRUE)
                    .CstCreatedBy(modCustomerDetails.getCstCreatedBy())
                    .CstDateCreated(modCustomerDetails.getCstDateCreated())
                    .CstAdBranch(branchCode)
                    .CstAdCompany(companyCode)
                    .buildCustomer(companyShortName);

            // create customer branch
            List<AdModBranchCustomerDetails> branchCustomerDetails = new ArrayList<>();

            List<String> branchMapList = omegaDataList.getAdBrNameAll(companyCode, companyShortName);
            for (String branchMap : branchMapList) {
                AdModBranchCustomerDetails modBranchCustomerDetails = new AdModBranchCustomerDetails();
                modBranchCustomerDetails.setBcstReceivableAccountNumber(glReceivableChartOfAccount.getCoaAccountNumber());
                modBranchCustomerDetails.setBcstBranchName(branchMap);
                branchCustomerDetails.add(modBranchCustomerDetails);
            }

            for (AdModBranchCustomerDetails bCstDetails : branchCustomerDetails) {
                try {
                    glReceivableChartOfAccount = glChartOfAccountHome
                            .findByCoaAccountNumber(bCstDetails.getBcstReceivableAccountNumber(), companyCode, companyShortName);
                }
                catch (FinderException ex) {
                    throw new ArCCCoaGlReceivableAccountNotFoundException();
                }

                LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome
                        .BcstGlCoaReceivableAccount(glReceivableChartOfAccount.getCoaCode())
                        .BcstAdCompany(companyCode)
                        .buildBranchCustomer(companyShortName);
                adBranchCustomer.setArCustomer(arCustomer);

                LocalAdBranch adBranch = adBranchHome.findByBrName(bCstDetails.getBcstBranchName(), companyCode, companyShortName);
                adBranchCustomer.setAdBranch(adBranch);
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(paymentTerm, companyCode, companyShortName);
            arCustomer.setAdPaymentTerm(adPaymentTerm);

            LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(bankAccount, companyCode, companyShortName);
            arCustomer.setAdBankAccount(adBankAccount);

            //TODO: Verify if the Receivable Account is correct... REVIEW...
            arCustomer.setCstGlCoaReceivableAccount(glReceivableChartOfAccount.getCoaCode());

            arCustomer.setArCustomerType(arCustomerType);
            arCustomer.setCstDealPrice(dealPrice);
            arCustomer.setArCustomerClass(arCustomerClass);
            arCustomer.setCstApprovalStatus("N/A");
            arCustomer.setCstPosted(EJBCommon.TRUE);
            arCustomer.setCstPostedBy(arCustomer.getCstLastModifiedBy());
            arCustomer.setCstDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

        }
        catch (ArCCCoaGlReceivableAccountNotFoundException ex) {
            getSessionContext().setRollbackOnly();
            throw ex;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        return arCustomer.getCstCode();
    }

    /**
     * This method creates a customer object.
     */
    public OfsApiResponse createCustomer(CustomerRequest customerRequest) {

        Debug.print("ArCustomerEntryApiControllerBean createCustomer");

        OfsApiResponse apiResponse = new OfsApiResponse();
        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        LocalAdUser adUser;
        LocalArCustomer arCustomer;

        try {

            if (customerRequest.getCustomerName() == null || customerRequest.getCustomerName().equals("")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_048);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_048_MSG);
                return apiResponse;
            }

            ArModCustomerDetails modCustomerDetails = new ArModCustomerDetails();
            modCustomerDetails.setCstCustomerCode(customerRequest.getCustomerCode());
            modCustomerDetails.setCstName(customerRequest.getCustomerName());
            modCustomerDetails.setCstDescription(customerRequest.getDescription());
            modCustomerDetails.setCstDealPrice(customerRequest.getDealPrice());

            // Company Code
            try {
                if (customerRequest.getCompanyCode() == null || customerRequest.getCompanyCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return apiResponse;
                }
                adCompany = adCompanyHome.findByCmpShortName(customerRequest.getCompanyCode());
                if (adCompany != null) {
                    modCustomerDetails.setCstAdCompany(adCompany.getCmpCode());
                    modCustomerDetails.setCompanyShortName(adCompany.getCmpShortName());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return apiResponse;
            }

            // Branch Code
            try {
                if (customerRequest.getBranchCode() == null || customerRequest.getBranchCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_073);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_073_MSG);
                    return apiResponse;
                }
                adBranch = adBranchHome.findByBrBranchCode(customerRequest.getBranchCode(),
                        adCompany.getCmpCode(), adCompany.getCmpShortName());
                if (adBranch != null) {
                    modCustomerDetails.setCstAdBranch(adBranch.getBrCode());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                apiResponse.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, customerRequest.getBranchCode()));
                return apiResponse;
            }

            // Validate if the customer code exists
            try {
                if (customerRequest.getCustomerCode() == null || customerRequest.getCustomerCode().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_048);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_048_MSG);
                    return apiResponse;
                }
                arCustomer = arCustomerHome.findByCstCustomerCode(customerRequest.getCustomerCode(), adCompany.getCmpCode(), adCompany.getCmpShortName());
                if (arCustomer != null) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_022);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_022_MSG);
                    return apiResponse;
                }
            }
            catch (FinderException ex) {
            }

            // User
            try {
                if (customerRequest.getUsername() == null || customerRequest.getUsername().equals("")) {
                    apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return apiResponse;
                }
                adUser = adUserHome.findByUsrName(customerRequest.getUsername(), adCompany.getCmpCode(), adCompany.getCmpShortName());
                if (adUser != null) {
                    modCustomerDetails.setCstCreatedBy(adUser.getUsrName());
                    modCustomerDetails.setCstLastModifiedBy(adUser.getUsrName());
                }
            }
            catch (FinderException ex) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return apiResponse;
            }

            Integer customerCode;
            String customerType = "DEALER"; //customerRequest.getCustomerType();
            String dealPrice = "RETAIL"; //customerRequest.getDealPrice();
            String paymentTerm = "IMMEDIATE"; // This is a temporary setting
            String customerClassName = "Trade-Acct"; // This is a temporary setting

            if (customerRequest.getBankAccountName() == null || customerRequest.getBankAccountName().equals("")) {
                apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_072);
                apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_072_MSG);
                return apiResponse;
            }
            String bankAccountName = customerRequest.getBankAccountName();

            // Customer Entry
            customerCode = this.saveArCstEntry(modCustomerDetails, customerType, dealPrice, paymentTerm, customerClassName,
                    bankAccountName, adBranch.getBrCode(), adCompany.getCmpCode());

            arCustomer = arCustomerHome.findByPrimaryKey(customerCode, adCompany.getCmpShortName());

            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            apiResponse.setStatus("Added new customer successfully.");
            apiResponse.setErpCustomerCode(arCustomer.getCstCustomerCode());

        }
        catch (ArCCCoaGlReceivableAccountNotFoundException ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_023);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_023_MSG);
            return apiResponse;
        }
        catch (Exception ex) {
            apiResponse.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            apiResponse.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return apiResponse;
        }
        return apiResponse;
    }

}