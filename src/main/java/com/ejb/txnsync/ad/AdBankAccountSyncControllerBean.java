package com.ejb.txnsync.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.dao.ad.LocalAdBranchBankAccountHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchBankAccount;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.restfulapi.sync.ad.models.BankAccountSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BankAccountSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "AdBankAccountSyncControllerBeanEJB")
public class AdBankAccountSyncControllerBean extends EJBContextClass implements AdBankAccountSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchBankAccountHome adBranchBankAccountHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public int getAdBankAccountAllNewLength(Integer branchCode, Integer companyCode, String companyShortName) {
        Debug.print("AdBankAccountSyncControllerBean getAdBankAccountAllNewLength");

        try {
            Collection adBankAccounts = adBankAccountHome.findBaByBaNewAndUpdated(
                    branchCode, companyCode, 'N', 'N', 'N', companyShortName);
            return adBankAccounts.size();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public int getAdBankAccountAllUpdatedLength(Integer branchCode, Integer companyCode, String companyShortName) {
        Debug.print("AdBankAccountSyncControllerBean getAdBankAccountAllUpdatedLength");

        try {
            Collection adBankAccounts = adBankAccountHome
                    .findBaByBaNewAndUpdated(branchCode, companyCode, 'U', 'U', 'X', companyShortName);
            return adBankAccounts.size();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getAdBankAccountAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName) {
        Debug.print("AdBankAccountSyncControllerBean getAdBankAccountAllNewAndUpdated");

        try {
            Collection adBankAccounts = adBankAccountHome
                    .findBaByBaNewAndUpdated(branchCode, companyCode, 'N', 'N', 'N', companyShortName);
            Collection adUpdatedBankAccounts = adBankAccountHome
                    .findBaByBaNewAndUpdated(branchCode, companyCode, 'U', 'U', 'X', companyShortName);

            String[] results = new String[adBankAccounts.size() + adUpdatedBankAccounts.size()];

            Iterator i = adBankAccounts.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) i.next();
                results[ctr] = bankAccountRowEncode(adBankAccount, companyShortName);
                ctr++;
            }

            i = adUpdatedBankAccounts.iterator();
            while (i.hasNext()) {
                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) i.next();
                results[ctr] = bankAccountRowEncode(adBankAccount, companyShortName);
                ctr++;
            }
            return results;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void setAdBankAccountsAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName) {
        Debug.print("AdBankAccountSyncControllerBean setAdBankAccountAllNewAndUpdatedSuccessConfirmation");

        LocalAdBranchBankAccount adBranchBankAccount;

        try {
            Collection adBranchBankAccounts = adBranchBankAccountHome
                    .findBBaByBaNewAndUpdated(branchCode, companyCode, 'N', 'U', 'X', companyShortName);
            Iterator i = adBranchBankAccounts.iterator();
            while (i.hasNext()) {
                adBranchBankAccount = (LocalAdBranchBankAccount) i.next();
                adBranchBankAccount.setBbaDownloadStatus('D');
            }
        }
        catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public BankAccountSyncResponse getAllNewLength(BankAccountSyncRequest request) {
        Debug.print("AdBankAccountSyncControllerBean getAllNewLength");

        BankAccountSyncResponse response = new BankAccountSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            int count = this.getAdBankAccountAllNewLength(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Retrieved bank account data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public BankAccountSyncResponse getAllUpdatedLength(BankAccountSyncRequest request) {
        Debug.print("AdBankAccountSyncControllerBean getAllUpdatedLength");

        BankAccountSyncResponse response = new BankAccountSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            int count = this.getAdBankAccountAllUpdatedLength(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Retrieved bank account data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public BankAccountSyncResponse getAllNewAndUpdated(BankAccountSyncRequest request) {
        Debug.print("AdBankAccountSyncControllerBean getAllNewAndUpdated");

        BankAccountSyncResponse response = new BankAccountSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] result = this.getAdBankAccountAllNewAndUpdated(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Retrieved bank account data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public BankAccountSyncResponse setAllNewAndUpdatedSuccessConfirmation(BankAccountSyncRequest request) {
        Debug.print("AdBankAccountSyncControllerBean setAllNewAndUpdatedSuccessConfirmation");

        BankAccountSyncResponse response = new BankAccountSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            this.setAdBankAccountsAllNewAndUpdatedSuccessConfirmation(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setStatus("Updated bank account download status successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private String bankAccountRowEncode(LocalAdBankAccount adBankAccount, String companyShortName) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(adBankAccount.getBaCode());
        tempResult.append(separator);

        // Name
        tempResult.append(adBankAccount.getBaName());
        tempResult.append(separator);

        // Description
        tempResult.append(adBankAccount.getBaDescription());
        tempResult.append(separator);

        // GL CASH ACCOUNT
        try {
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome
                    .findByCoaCode(adBankAccount.getBaCoaGlCashAccount(), adBankAccount.getBaAdCompany(), companyShortName);
            tempResult.append(glChartOfAccount.getCoaAccountNumber());
            tempResult.append(separator);
        }
        catch (Exception ex) {
            tempResult.append("Error");
            tempResult.append(separator);
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // remove unwanted chars from encodedResult;
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");

        return encodedResult;
    }
}