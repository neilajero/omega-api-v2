package com.ejb.txnsync.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.restfulapi.sync.ad.models.BankAccountSyncResponse;
import com.ejb.restfulapi.sync.ad.models.BranchSyncRequest;
import com.ejb.restfulapi.sync.ad.models.BranchSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.util.*;

@Stateless(name = "AdBranchSyncControllerBeanEJB")
public class AdBranchSyncControllerBean extends EJBContextClass implements AdBranchSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public String[] getAdBranchAll(Integer companyCode, String companyShortName) {
        Debug.print("AdBranchSyncControllerBean getAdBranchAll");

        LocalAdBranch adBranch;

        try {
            Collection adBranches = adBranchHome.findBrAll(companyCode, companyShortName);
            String[] results = new String[adBranches.size()];
            Iterator i = adBranches.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                adBranch = (LocalAdBranch)i.next();
                results[ctr] = branchRowEncode(adBranch);
                ctr++;
            }
            return results;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getAdBranchAllWithBranchName(Integer companyCode, String companyShortName) {
        Debug.print("AdBranchSyncControllerBean getAdBranchAllWithBranchName");

        LocalAdBranch adBranch;

        try {
            Collection adBranches = adBranchHome.findBrAll(companyCode, companyShortName);
            String[] results = new String[adBranches.size()];
            Iterator i = adBranches.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                adBranch = (LocalAdBranch)i.next();
                results[ctr] = branchRowEncodeWithBranchName(adBranch);
                ctr++;
            }
            return results;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void setAdBranchAllSuccessConfirmation(Integer companyCode, String companyShortName) {
        Debug.print("AdBranchSyncControllerBean setAdBranchAllSuccessConfirmation");

        LocalAdBranch adBranch;

        try {
            Collection adBranches = adBranchHome.findBrAll(companyCode, companyShortName);
            Iterator i = adBranches.iterator();
            while (i.hasNext()) {
                adBranch = (LocalAdBranch)i.next();
                adBranch.setBrDownloadStatus('D');
            }
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public BranchSyncResponse getAdBranchAll(BranchSyncRequest request) {
        Debug.print("AdBranchSyncControllerBean getAdBranchAll");

        BranchSyncResponse response = new BranchSyncResponse();

        LocalAdCompany adCompany;
        Integer companyCode = null;
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

            String[] result = this.getAdBranchAll(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get all branch data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public BranchSyncResponse getAdBranchAllWithBranchName(BranchSyncRequest request) {
        Debug.print("AdBranchSyncControllerBean getAdBranchAllWithBranchName");

        BranchSyncResponse response = new BranchSyncResponse();

        LocalAdCompany adCompany;
        Integer companyCode = null;
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

            String[] result = this.getAdBranchAllWithBranchName(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get all branch data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public BranchSyncResponse setAdBranchAllSuccessConfirmation(BranchSyncRequest request) {
        Debug.print("AdBranchSyncControllerBean setAdBranchAllSuccessConfirmation");

        BranchSyncResponse response = new BranchSyncResponse();

        LocalAdCompany adCompany;
        Integer companyCode = null;
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

            this.setAdBranchAllSuccessConfirmation(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setStatus("Updated branch download status successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private String branchRowEncode(LocalAdBranch adBranch){

        char separator = EJBCommon.SEPARATOR;

        // Start separator
        return String.valueOf(separator)

                // Primary Key / OPOS: Branch Code
                + adBranch.getBrCode()
                + separator

                // Branch Code / OPOS: Br Branch Code
                + adBranch.getBrBranchCode()
                + separator

                // End separator
                + separator;
    }

    private String branchRowEncodeWithBranchName(LocalAdBranch adBranch){

        char separator = EJBCommon.SEPARATOR;

        // Start separator
        return String.valueOf(separator)

                // Primary Key / OPOS: Branch Code
                + adBranch.getBrCode()
                + separator

                // Branch Code / OPOS: Br Branch Code
                + adBranch.getBrBranchCode()
                + separator

                // Branch Name / OPOS: Br Branch Name
                + adBranch.getBrName()
                + separator

                // End separator
                + separator;
    }

}