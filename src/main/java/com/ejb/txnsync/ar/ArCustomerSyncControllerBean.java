package com.ejb.txnsync.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchCustomerHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ar.*;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchCustomer;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.*;
import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArCustomerSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArCustomerSyncControllerBeanEJB")
public class ArCustomerSyncControllerBean extends EJBContextClass implements ArCustomerSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalArCustomerBalanceHome arCustomerBalanceHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;

    @Override
    public String[] getArCSTAreaAll(Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCSTAreaAll");

        String[] results;
        Collection cstArea = null;
        try {
            cstArea = arCustomerHome.findCstArea(companyCode, companyShortName);
        }
        catch (FinderException ex) {
        }

        ArrayList cstAreaList = new ArrayList();
        Iterator i = cstArea.iterator();
        int ctr = 0;
        while (i.hasNext()) {
            LocalArCustomer arCst = (LocalArCustomer) i.next();
            if (arCst.getCstArea() == null || arCst.getCstArea().equals("")) {
                continue;
            }
            if (!cstAreaList.contains(arCst.getCstArea().toUpperCase())) {
                cstAreaList.add(arCst.getCstArea().toUpperCase());
            }
        }

        results = new String[cstAreaList.size()];
        Iterator j = cstAreaList.iterator();
        ctr = 0;
        while (j.hasNext()) {
            results[ctr] = j.next().toString();
            ctr++;
        }
        return results;
    }

    @Override
    public String[] getArSoPostedAllByCstArea(String customerArea, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArSoPostedAllByCstArea");

        String[] results;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        calendar.add(java.util.Calendar.MONTH, -3);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        String previousDate = EJBCommon.convertSQLDateToString(calendar.getTime());

        calendar = java.util.Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        String nextDate = EJBCommon.convertSQLDateToString(calendar.getTime());

        Collection soPostedColl;
        soPostedColl = arSalesOrderHome.findSoPostedAndApprovedByDateRangeAndAdCompany(
                EJBCommon.convertStringToSQLDate(previousDate),
                EJBCommon.convertStringToSQLDate(nextDate), companyCode, companyShortName);

        ArrayList soList = new ArrayList();

        Iterator i = soPostedColl.iterator();
        while (i.hasNext()) {
            LocalArSalesOrder salesOrder = (LocalArSalesOrder) i.next();
            if (salesOrder.getArCustomer().getCstArea().equals(customerArea)) {
                String str = "";

                Collection arSOLineColl = salesOrder.getArSalesOrderLines();
                Iterator j = arSOLineColl.iterator();

                double ctr = 0;
                while (j.hasNext()) {
                    LocalArSalesOrderLine arSOLine = (LocalArSalesOrderLine) j.next();
                    if (arSOLine.getInvUnitOfMeasure().getUomName().equals("CTN")) {
                        ctr += arSOLine.getSolQuantity();
                    }
                }

                if (salesOrder.getSoLock() == (byte) 1) {
                    str += EJBCommon.convertSQLDateToString(salesOrder.getSoDateApprovedRejected())
                            + "$" + salesOrder.getArCustomer().getCstName() + "$" + salesOrder.getSoDocumentNumber() + "$" + "1" + "$" + ctr;
                    soList.add(str);
                } else {
                    str += EJBCommon.convertSQLDateToString(salesOrder.getSoDateApprovedRejected())
                            + "$" + salesOrder.getArCustomer().getCstName() + "$" + salesOrder.getSoDocumentNumber() + "$" + "0" + "$" + ctr;
                    soList.add(str);
                }
            }
        }

        results = new String[soList.size()];
        Iterator j = soList.iterator();
        int ctr = 0;
        while (j.hasNext()) {
            results[ctr++] = j.next().toString();
        }

        return results;
    }

    @Override
    public int getArCustomersAllNewLength(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewLength");

        try {
            Collection arCustomers = arCustomerHome.findCstByCstNewAndUpdated(
                    branchCode, companyCode, 'N', 'N', 'N', companyShortName);
            return arCustomers.size();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public int getArCustomersAllUpdatedLength(Integer branchCode, Integer companyCode, String companyShortName) {

        return 0;
    }

    @Override
    public String[] getArSalespersonAll(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArSalespersonAll");

        try {
            Collection arSalespersons = arSalespersonHome.findSlpByBrCode(branchCode, companyCode, companyShortName);
            String[] results = new String[arSalespersons.size()];
            Iterator i = arSalespersons.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalArSalesperson arSalesperson = (LocalArSalesperson) i.next();
                results[ctr] = salespersonRowEncode(arSalesperson);
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
    public String[] getArCustomerDraftBalances(String[] customerCodes, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomerDraftBalances");

        try {
            String[] results = new String[customerCodes.length];
            for (int i = 0; i < customerCodes.length; i++) {
                Collection arInvoices = arInvoiceHome.findUnpostedInvByCstCustomerCode(
                        customerCodes[i], companyCode, companyShortName);
                double totalDraftBalance = 0;
                Iterator iter = arInvoices.iterator();
                while (iter.hasNext()) {
                    totalDraftBalance += ((LocalArInvoice) iter.next()).getInvAmountDue();
                }
                results[i] = "" + totalDraftBalance;
            }
            return results;

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getArCustomersNameCodeAddressSlp(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersNameCodeAddressSlp");

        try {
            Collection arCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, branchCode, 'N', 'N', 'N', companyShortName);
            Collection arUpdatedCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, branchCode, 'U', 'U', 'X', companyShortName);

            String[] results = new String[arCustomers.size() + arUpdatedCustomers.size()];
            Iterator i = arCustomers.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer) i.next();
                results[ctr] = customerNewRowEncode(arCustomer);
                ctr++;
            }

            i = arUpdatedCustomers.iterator();
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer) i.next();
                results[ctr] = customerNewRowEncode(arCustomer);
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
    public String[] getArCustomersAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewAndUpdated");

        try {
            Collection arCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'N', 'N', 'N', companyShortName);
            Collection arUpdatedCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'U', 'U', 'X', companyShortName);
            String[] results = new String[arCustomers.size() + arUpdatedCustomers.size()];
            Iterator i = arCustomers.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer)i.next();
                results[ctr] = customerRowEncode(arCustomer);
                ctr++;
            }

            i = arUpdatedCustomers.iterator();
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer)i.next();
                results[ctr] = customerRowEncode(arCustomer);
                ctr++;
            }
            return results;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getArCustomersAllNewAndUpdatedWithSalesperson(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewAndUpdatedWithSalesperson");

        try {
            Collection arCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'N', 'N', 'N', companyShortName);
            Collection arUpdatedCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'U', 'U', 'X', companyShortName);
            String[] results = new String[arCustomers.size() + arUpdatedCustomers.size()];
            Iterator i = arCustomers.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer)i.next();
                results[ctr] = customerRowEncodeWithSalesPerson(arCustomer);
                ctr++;
            }

            i = arUpdatedCustomers.iterator();
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer)i.next();
                results[ctr] = customerRowEncodeWithSalesPerson(arCustomer);
                ctr++;
            }
            return results;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getArCustomersAllNewAndUpdatedWithCustomerClass(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewAndUpdatedWithClass");

        try {
            Collection arCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'N', 'N', 'N', companyShortName);
            Collection arUpdatedCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'U', 'U', 'X', companyShortName);

            String[] results = new String[arCustomers.size() + arUpdatedCustomers.size()];

            Iterator i = arCustomers.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer)i.next();
                results[ctr] = customerRowEncodeWithClass(arCustomer);
                ctr++;
            }

            i = arUpdatedCustomers.iterator();
            while (i.hasNext()) {
                LocalArCustomer arCustomer = (LocalArCustomer)i.next();
                results[ctr] = customerRowEncodeWithClass(arCustomer);
                ctr++;
            }
            return results;

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String getArCustomersBalanceAllDownloaded(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersBalanceAllDownloaded");

        char separator = EJBCommon.SEPARATOR;
        StringBuilder CustomerBalances = new StringBuilder();

        try {
            Collection arCustomers = arCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'D', 'D', 'D', companyShortName);

            Iterator i = arCustomers.iterator();
            int ctr = 0;
            while (i.hasNext()) {

                LocalArCustomer arCustomer = (LocalArCustomer) i.next();
                double currentBalance = 0;
                // Current Balance

                Collection arCustomerBalances = arCustomerBalanceHome
                        .findByBeforeOrEqualCbDateAndCstCode(
                                EJBCommon.getGcCurrentDateWoTime().getTime(),
                                arCustomer.getCstCode(), arCustomer.getCstAdCompany(), companyShortName);

                if (!arCustomerBalances.isEmpty()) {
                    ArrayList arCustomerBalanceList = new ArrayList(arCustomerBalances);
                    LocalArCustomerBalance arCustomerBalance = (LocalArCustomerBalance) arCustomerBalanceList.get(arCustomerBalanceList.size() - 1);
                    currentBalance = arCustomerBalance.getCbBalance();
                }
                CustomerBalances.append(separator);
                CustomerBalances.append(arCustomer.getCstCode().toString());
                CustomerBalances.append(separator);
                CustomerBalances.append(currentBalance);
                ctr++;
            }
            CustomerBalances.append(separator);
            return CustomerBalances.toString();

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public void setArCustomersAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print("ArCustomerSyncControllerBean setArCustomersAllNewAndUpdatedSuccessConfirmation");

        LocalAdBranchCustomer adBranchCustomer;

        try {
            Collection adBranchCustomers = adBranchCustomerHome
                    .findCstByCstNewAndUpdated(branchCode, companyCode, 'N', 'U', 'X', companyShortName);
            Iterator i = adBranchCustomers.iterator();
            while (i.hasNext()) {
                adBranchCustomer = (LocalAdBranchCustomer)i.next();
                adBranchCustomer.setBcstCustomerDownloadStatus('D');
            }
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public ArCustomerSyncResponse getArCSTAreaAll(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCSTAreaAll");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArCSTAreaAll(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get all customer data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArSoPostedAllByCstArea(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArSoPostedAllByCstArea");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArSoPostedAllByCstArea(request.getCustomerArea(), companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get all posted SO data successfully.");

        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersAllNewLength(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewLength");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            int count = this.getArCustomersAllNewLength(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Get customer count data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersAllUpdatedLength(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllUpdatedLength");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            int count = this.getArCustomersAllUpdatedLength(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Get customer count updated data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArSalespersonAll(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArSalespersonAll");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArSalespersonAll(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get sales person data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomerDraftBalances(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomerDraftBalances");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] customerCodes = request.getCustomerCodes();
            String[] result = this.getArCustomerDraftBalances(customerCodes, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get customer draft balances successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersNameCodeAddressSlp(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersNameCodeAddressSlp");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArCustomersNameCodeAddressSlp(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get customer details successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersAllNewAndUpdated(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewAndUpdated");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArCustomersAllNewAndUpdated(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get customer details successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersAllNewAndUpdatedWithSalesperson(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewAndUpdatedWithSalesperson");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArCustomersAllNewAndUpdatedWithSalesperson(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get customer details with salesperson data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersAllNewAndUpdatedWithCustomerClass(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersAllNewAndUpdatedWithCustomerClass");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String[] result = this.getArCustomersAllNewAndUpdatedWithCustomerClass(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Get customer details with class data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse getArCustomersBalanceAllDownloaded(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean getArCustomersBalanceAllDownloaded");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            String result = this.getArCustomersBalanceAllDownloaded(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            //response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setMessage(result);
            response.setStatus("Get customer balances all downloaded data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArCustomerSyncResponse setArCustomersAllNewAndUpdatedSuccessConfirmation(ArCustomerSyncRequest request) {

        Debug.print("ArCustomerSyncControllerBean setArCustomersAllNewAndUpdatedSuccessConfirmation");

        ArCustomerSyncResponse response = new ArCustomerSyncResponse();

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

            this.setArCustomersAllNewAndUpdatedSuccessConfirmation(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setStatus("Get customer balances all downloaded data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private String customerNewRowEncode(LocalArCustomer arCustomer) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(arCustomer.getCstCode());
        tempResult.append(separator);

        // Customer Code
        tempResult.append(arCustomer.getCstCustomerCode());
        tempResult.append(separator);

        // Name
        tempResult.append(arCustomer.getCstName());
        tempResult.append(separator);

        // Address
        if (arCustomer.getCstAddress().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstAddress());
            tempResult.append(separator);
        }

        //Credit Limit
        tempResult.append(arCustomer.getCstCreditLimit());
        tempResult.append(separator);

        // Sales Person
        try {
            tempResult.append(arCustomer.getArSalesperson().getSlpCode());
            tempResult.append(separator);
        }
        catch (Exception ex) {
            tempResult.append(" ");
            tempResult.append(separator);
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

    private String customerRowEncode(LocalArCustomer arCustomer) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(arCustomer.getCstCode());
        tempResult.append(separator);

        // Customer Code
        tempResult.append(arCustomer.getCstCustomerCode());
        tempResult.append(separator);

        // Name
        tempResult.append(arCustomer.getCstName());
        tempResult.append(separator);

        // Credit Limit
        tempResult.append(arCustomer.getCstCreditLimit());
        tempResult.append(separator);

        // Current Balance
        try {
            Collection arCustomerBalances = arCustomerBalanceHome
                    .findByBeforeOrEqualCbDateAndCstCode(
                            EJBCommon.getGcCurrentDateWoTime().getTime(),
                            arCustomer.getCstCode(), arCustomer.getCstAdCompany());
            LocalArCustomerBalance arCustomerBalance = null;
            Iterator i = arCustomerBalances.iterator();
            while (i.hasNext()) {
                arCustomerBalance = (LocalArCustomerBalance) i.next();
            }
            if (arCustomerBalance != null) {
                tempResult.append(arCustomerBalance.getCbBalance());
            } else {
                tempResult.append("0");
            }
            tempResult.append(separator);

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // Area / State Province
        if (arCustomer.getCstStateProvince().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstStateProvince());
            tempResult.append(separator);
        }

        // Contact
        if (arCustomer.getCstContact().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstContact());
            tempResult.append(separator);
        }

        // Phone
        if (arCustomer.getCstPhone().length() < 1) {
            tempResult.append("0");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstPhone());
            tempResult.append(separator);
        }

        // Fax
        if (arCustomer.getCstFax().length() < 1) {
            tempResult.append("0");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstFax());
            tempResult.append(separator);
        }

        // Email
        if (arCustomer.getCstEmail().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstEmail());
            tempResult.append(separator);
        }

        // Address
        if (arCustomer.getCstAddress().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstAddress());
            tempResult.append(separator);
        }

        // Payment Term
        tempResult.append(arCustomer.getAdPaymentTerm().getPytName());
        tempResult.append(separator);

        // Deal
        if (arCustomer.getCstDealPrice() == null || arCustomer.getCstDealPrice().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstDealPrice());
            tempResult.append(separator);
        }

        // Enable
        tempResult.append(arCustomer.getCstEnable());
        tempResult.append(separator);

        // Salesperson
        if (arCustomer.getArSalesperson() == null) {
            tempResult.append("null");
            tempResult.append(separator);
        } else {
            if (arCustomer.getArSalesperson().getSlpName() != null) {
                tempResult.append(arCustomer.getArSalesperson().getSlpName());
                tempResult.append(separator);
            } else {
                tempResult.append("null");
                tempResult.append(separator);
            }
        }

        // TIN Number
        if (arCustomer.getCstTin().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstTin());
            tempResult.append(separator);
        }

        // Ship To Address
        if (arCustomer.getCstShipToAddress() != null) {
            if (arCustomer.getCstShipToAddress().length() < 1) {
                tempResult.append("not specified");
                tempResult.append(separator);
            } else {
                tempResult.append(arCustomer.getCstShipToAddress());
                tempResult.append(separator);
            }
        } else {
            tempResult.append(arCustomer.getCstShipToAddress());
            tempResult.append(separator);
        }

        // Salesperson 2
        String slName = null;
        Integer slId = null;

        if (arCustomer.getCstArSalesperson2() != null) {
            try {
                LocalArSalesperson arSalesperson = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());
                slName = arSalesperson.getSlpName();
                slId = arSalesperson.getSlpCode();
            }
            catch (Exception ex) {
                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage());
            }
        }

        // Remove unwanted CHARS from encoded result
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");
        return encodedResult;
    }

    private String customerRowEncodeWithSalesPerson(LocalArCustomer arCustomer) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(arCustomer.getCstCode());
        tempResult.append(separator);

        // Customer Code
        tempResult.append(arCustomer.getCstCustomerCode());
        tempResult.append(separator);

        // Name
        tempResult.append(arCustomer.getCstName());
        tempResult.append(separator);

        // Credit Limit
        tempResult.append(arCustomer.getCstCreditLimit());
        tempResult.append(separator);

        // Current Balance
        try {
            Collection arCustomerBalances = arCustomerBalanceHome
                    .findByBeforeOrEqualCbDateAndCstCode(
                            EJBCommon.getGcCurrentDateWoTime().getTime(),
                            arCustomer.getCstCode(), arCustomer.getCstAdCompany());
            LocalArCustomerBalance arCustomerBalance = null;
            Iterator i = arCustomerBalances.iterator();
            while (i.hasNext()) {
                arCustomerBalance = (LocalArCustomerBalance) i.next();
            }
            if (arCustomerBalance != null) {
                tempResult.append(arCustomerBalance.getCbBalance());
            } else {
                tempResult.append("0");
            }
            tempResult.append(separator);
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // Area / State Province
        if (arCustomer.getCstStateProvince().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstStateProvince());
            tempResult.append(separator);
        }

        // Contact
        if (arCustomer.getCstContact().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstContact());
            tempResult.append(separator);
        }

        // Phone
        if (arCustomer.getCstPhone().length() < 1) {
            tempResult.append("0");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstPhone());
            tempResult.append(separator);
        }

        // Fax
        if (arCustomer.getCstFax().length() < 1) {
            tempResult.append("0");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstFax());
            tempResult.append(separator);
        }

        // Email
        if (arCustomer.getCstEmail().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstEmail());
            tempResult.append(separator);
        }

        // Address
        if (arCustomer.getCstAddress().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstAddress());
            tempResult.append(separator);
        }

        // Payment Term
        tempResult.append(arCustomer.getAdPaymentTerm().getPytName());
        tempResult.append(separator);

        // Deal
        if (arCustomer.getCstDealPrice() == null || arCustomer.getCstDealPrice().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstDealPrice());
            tempResult.append(separator);
        }

        // Salesperson
        if (arCustomer.getArSalesperson() == null) {
            tempResult.append("null");
            tempResult.append(separator);
        } else {
            if (arCustomer.getArSalesperson().getSlpName() != null) {
                tempResult.append(arCustomer.getArSalesperson().getSlpName());
                tempResult.append(separator);
            } else {
                tempResult.append("null");
                tempResult.append(separator);
            }
        }

        // Salesperson 2
        String slName = null;
        Integer slId = null;
        if (arCustomer.getCstArSalesperson2() != null) {
            try {
                LocalArSalesperson arSalesperson = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());
                slName = arSalesperson.getSlpName();
                slId = arSalesperson.getSlpCode();
            }
            catch (Exception ex) {
                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage());
            }
        }

        // Start
        if (slName == null) {
            tempResult.append("null");
            tempResult.append(separator);
        } else {
            tempResult.append(slName);
            tempResult.append(separator);
        }

        // Salesman ID
        if (arCustomer.getArSalesperson() == null) {
            tempResult.append("null");
            tempResult.append(separator);
        } else {
            if (arCustomer.getArSalesperson().getSlpCode() != null) {
                tempResult.append(arCustomer.getArSalesperson().getSlpCode());
                tempResult.append(separator);
            } else {
                tempResult.append("null");
                tempResult.append(separator);
            }
        }

        // Salesman ID 2
        if (slName == null) {
            tempResult.append("null");
            tempResult.append(separator);
        } else {
            tempResult.append(slId);
            tempResult.append(separator);
        }

        // Remove unwanted CHARS from encoded result
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");
        return encodedResult;
    }

    private String customerRowEncodeWithClass(LocalArCustomer arCustomer) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(arCustomer.getCstCode());
        tempResult.append(separator);

        // Customer Code
        tempResult.append(arCustomer.getCstCustomerCode());
        tempResult.append(separator);

        // Name
        tempResult.append(arCustomer.getCstName());
        tempResult.append(separator);

        // Credit Limit
        tempResult.append(arCustomer.getCstCreditLimit());
        tempResult.append(separator);

        // Current Balance
        try {
            Collection arCustomerBalances = arCustomerBalanceHome
                    .findByBeforeOrEqualCbDateAndCstCode(
                            EJBCommon.getGcCurrentDateWoTime().getTime(),
                            arCustomer.getCstCode(), arCustomer.getCstAdCompany());
            LocalArCustomerBalance arCustomerBalance = null;
            Iterator i = arCustomerBalances.iterator();
            while (i.hasNext()) {
                arCustomerBalance = (LocalArCustomerBalance) i.next();
                ;
            }
            if (arCustomerBalance != null) {
                tempResult.append(arCustomerBalance.getCbBalance());
            } else {
                tempResult.append("0");
            }
            tempResult.append(separator);
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        // Area / State Province
        if (arCustomer.getCstStateProvince().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstStateProvince());
            tempResult.append(separator);
        }

        // Contact
        if (arCustomer.getCstContact().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstContact());
            tempResult.append(separator);
        }

        // Phone
        if (arCustomer.getCstPhone().length() < 1) {
            tempResult.append("0");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstPhone());
            tempResult.append(separator);
        }

        // Fax
        if (arCustomer.getCstFax().length() < 1) {
            tempResult.append("0");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstFax());
            tempResult.append(separator);
        }

        // Email
        if (arCustomer.getCstEmail().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstEmail());
            tempResult.append(separator);
        }

        // Address
        if (arCustomer.getCstAddress().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstAddress());
            tempResult.append(separator);
        }

        // Payment Term
        tempResult.append(arCustomer.getAdPaymentTerm().getPytName());
        tempResult.append(separator);

        // Deal
        if (arCustomer.getCstDealPrice() == null || arCustomer.getCstDealPrice().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getCstDealPrice());
            tempResult.append(separator);
        }

        // Class
        if (arCustomer.getArCustomerClass().getCcName() == null) {
            tempResult.append("null Class");
            tempResult.append(separator);
        } else {
            tempResult.append(arCustomer.getArCustomerClass().getCcName());
            tempResult.append(separator);
        }

        // Remove unwanted CHARS from encoded result
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");
        return encodedResult;
    }

    private String salespersonRowEncode(LocalArSalesperson arSalesPerson) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(arSalesPerson.getSlpCode());
        tempResult.append(separator);

        // Salesperson Code
        tempResult.append(arSalesPerson.getSlpSalespersonCode());
        tempResult.append(separator);

        // Salesperson Name
        tempResult.append(arSalesPerson.getSlpName());
        tempResult.append(separator);

        // Address
        if (arSalesPerson.getSlpAddress().length() < 1) {
            tempResult.append("not specified");
            tempResult.append(separator);
        } else {
            tempResult.append(arSalesPerson.getSlpAddress());
            tempResult.append(separator);
        }

        // Remove unwanted CHARS from encoded result
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");
        return encodedResult;
    }

}