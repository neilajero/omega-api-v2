package com.ejb.txnsync.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.restfulapi.sync.inv.models.InvUnitOfMeasureSyncRequest;
import com.ejb.restfulapi.sync.inv.models.InvUnitOfMeasureSyncResponse;
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

@Stateless(name = "InvUnitOfMeasureSyncControllerBeanEJB")
public class InvUnitOfMeasureSyncControllerBean extends EJBContextClass implements InvUnitOfMeasureSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public String[] getInvUnitOfMeasuresAll(Integer companyCode, String companyShortName) {

        Debug.print("InvUnitOfMeasureSyncControllerBean getInvUnitOfMeasuresAll");

        LocalInvUnitOfMeasure invUnitOfMeasure;

        try {
            Collection invUnitOfMasures = invUnitOfMeasureHome.findEnabledUomAll(companyCode, companyShortName);
            String[] results = new String[invUnitOfMasures.size()];
            Iterator i = invUnitOfMasures.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                invUnitOfMeasure = (LocalInvUnitOfMeasure) i.next();
                results[ctr] = uomRowEncode(invUnitOfMeasure);
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
    public void setInvUnitOfMeasuresAllSuccessConfirmation(Integer companyCode, String companyShortName) {

        Debug.print("InvUnitOfMeasureSyncControllerBean setInvUnitOfMeasuresAllSuccessConfirmation");

        LocalInvUnitOfMeasure invUnitOfMeasure;

        try {
            Collection invUnitOfMeasures = invUnitOfMeasureHome.findEnabledUomAll(companyCode, companyShortName);
            Iterator i = invUnitOfMeasures.iterator();
            while (i.hasNext()) {
                invUnitOfMeasure = (LocalInvUnitOfMeasure) i.next();
                invUnitOfMeasure.setUomDownloadStatus('D');
            }
        }
        catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public InvUnitOfMeasureSyncResponse getInvUnitOfMeasuresAll(InvUnitOfMeasureSyncRequest request) {

        Debug.print("InvUnitOfMeasureSyncControllerBean getInvUnitOfMeasuresAll");

        InvUnitOfMeasureSyncResponse response = new InvUnitOfMeasureSyncResponse();

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

            String[] result = this.getInvUnitOfMeasuresAll(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResults(result);
            response.setStatus("Get all unit of measures data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public InvUnitOfMeasureSyncResponse setInvUnitOfMeasuresAllSuccessConfirmation(InvUnitOfMeasureSyncRequest request) {

        Debug.print("InvUnitOfMeasureSyncControllerBean setInvUnitOfMeasuresAllSuccessConfirmation");

        InvUnitOfMeasureSyncResponse response = new InvUnitOfMeasureSyncResponse();

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

            this.setInvUnitOfMeasuresAllSuccessConfirmation(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setStatus("Set unit of measures status successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private String uomRowEncode(LocalInvUnitOfMeasure invUnitOfMeasure) {

        char separator = EJBCommon.SEPARATOR;

        // Start separator
        return String.valueOf(separator)

                // Primary Key
                + invUnitOfMeasure.getUomCode()
                + separator

                // Name / OPOS: UOM Code
                + invUnitOfMeasure.getUomName()
                + separator

                // Description / OPOS: UOM Name
                + invUnitOfMeasure.getUomDescription()
                + separator

                // Short Name / OPOS: Short Name
                + invUnitOfMeasure.getUomShortName()
                + separator

                // UOM Class
                + invUnitOfMeasure.getUomAdLvClass()
                + separator

                // End separator
                + separator;

    }

}