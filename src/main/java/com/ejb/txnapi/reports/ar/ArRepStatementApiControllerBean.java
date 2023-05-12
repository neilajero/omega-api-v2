package com.ejb.txnapi.reports.ar;

import com.ejb.ConfigurationClass;
import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.reports.ar.models.StatementDetails;
import com.ejb.restfulapi.reports.ar.models.StatementRequest;
import com.ejb.restfulapi.reports.ar.models.StatementResponse;
import com.ejb.txnapi.reports.Report;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless(name = "ArRepStatementApiControllerEJB")
public class ArRepStatementApiControllerBean extends EJBContextClass implements ArRepStatementApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdUserHome adUserHome;

    @Override
    public void executeSpArRepStatementOfAccount(StatementDetails details)
            throws GlobalNoRecordFoundException {

        Debug.print("ArRepStatementApiControllerBean executeSpArRepStatementOfAccount");

        try {
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery(details.getStoredProcedure(), details.getCompanyShortName())
                    .registerStoredProcedureParameter("cutoffDate", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("customerBatch", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("customerCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeUnposted", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeAdvance", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("includeAdvanceOnly", Boolean.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("branchCode", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("adCompany", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("resultCount", Integer.class, ParameterMode.OUT);

            spQuery.setParameter("cutoffDate", details.getCutOfDate());
            spQuery.setParameter("customerBatch", details.getCustomerBatch());
            spQuery.setParameter("customerCode", details.getCustomerCode());
            spQuery.setParameter("includeUnposted", details.isIncludePosted());
            spQuery.setParameter("includeAdvance", details.isIncludeAdvance());
            spQuery.setParameter("includeAdvanceOnly", details.isIncludeAdvanceOnly());
            spQuery.setParameter("branchCode", "27,28,29,30,31,32"); //TODO: Verify if this is required
            spQuery.setParameter("adCompany", details.getCompanyCode());

            spQuery.execute();

            Integer resultCount = (Integer) spQuery.getOutputParameterValue("resultCount");

            if (resultCount <= 0) {
                throw new GlobalNoRecordFoundException();
            }

        }
        catch (GlobalNoRecordFoundException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public StatementResponse generateSoa(StatementRequest request) {

        Debug.print("ArRepStatementApiControllerBean generateSoa");

        StatementResponse response = new StatementResponse();
        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        LocalAdUser adUser;
        LocalArCustomer arCustomer;

        try {

            StatementDetails details = new StatementDetails();

            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurationClass.DEFAULT_DATE_FORMAT);
            Date date = sdf.parse(request.getCutOfDate());

            // Company Code
            try {
                if (request.getCompanyCode() == null || request.getCompanyCode().equals("")) {
                    response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    details.setCompanyCode(adCompany.getCmpCode());
                    details.setCompanyShortName(adCompany.getCmpShortName());
                    details.setCompanyName(adCompany.getCmpName());
                    details.setCompanyAddress(adCompany.getCmpAddress() + " " + adCompany.getCmpCity() + " " + adCompany.getCmpCountry());
                    details.setCompanyPhone(adCompany.getCmpPhone());
                    details.setCompanyFax(adCompany.getCmpFax());
                    details.setCompanyEmail(adCompany.getCmpEmail());
                    details.setCompanyTinNumber(adCompany.getCmpTin());
                }
            }
            catch (FinderException ex) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Codes
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(),
                        details.getCompanyCode(), details.getCompanyShortName());
                if (adBranch != null) {
                    details.setBranchName(adBranch.getBrName());
                }
            }
            catch (FinderException ex) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, "TODO_HERE"));
                return response;
            }

            // User
            try {
                if (request.getUsername() == null || request.getUsername().equals("")) {
                    response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_042);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_042_MSG);
                    return response;
                }
                adUser = adUserHome.findByUsrName(request.getUsername(),
                        details.getCompanyCode(), details.getCompanyShortName());
                if (adUser != null) {
                    details.setPreparedBy(adUser.getUsrName());
                }
            }
            catch (FinderException ex) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_002);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_002_MSG);
                return response;
            }

            // Customer Code
            try {
                if (request.getCustomerCode() == null || request.getCustomerCode().equals("")) {
                    response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                arCustomer = arCustomerHome.findByCstCustomerCode(request.getCustomerCode(),
                        details.getCompanyCode(), details.getCompanyShortName());
                details.setCustomerCode(arCustomer.getCstCustomerCode());
            }
            catch (FinderException ex) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_009);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_009_MSG);
                return response;
            }

            details.setStoredProcedure("sp_ArRepStatement");
            details.setCutOfDate(new java.sql.Date(date.getTime()));
            details.setDateFrom(request.getDateFrom());
            details.setDateTo(request.getDateTo());

            this.executeSpArRepStatementOfAccount(details);
            Report report = this.generateReport(this.setReportParameters(details));
            if (report.getBytes() != null) {
                response.setPdfReport(report.getBytes());
            }

            //this.generateReportV2(this.setReportParameters(details));

            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setStatus("Generated SOA successfully.");

        }
        catch (Exception ex) {
            ex.printStackTrace();
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private Report generateReport(Map<String, Object> parameters)
            throws JRException, SQLException {
        Report report = new Report();
        String filename = "/Users/neoajero/neosolutions/obci-solutions/ofs-source/omega-api-v2/src/main/java/com/ejb/txnapi/reports/jasper/ArRepStatementOfAccount.jasper";
        Connection conn = this.getConnection();
        report.setViewType(EJBCommon.REPORT_VIEW_TYPE_PDF);
        report.setBytes(JasperRunManager.runReportToPdf(filename, parameters, conn));
        conn.close();
        return report;
    }

    private Connection getConnection() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(ConfigurationClass.DATASOURCE_NAME);
            return ds.getConnection();
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }

    }

    private Map<String, Object> setReportParameters(StatementDetails details) throws ParseException {

        Debug.print("ArRepStatementApiControllerBean setReportParameters");

        int agingBucket = 0;

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("branchName", details.getBranchName());
        parameters.put("preparedBy", details.getPreparedBy());
        parameters.put("date", EJBCommon.convertStringToSQLDate(details.getDateTo()));
        parameters.put("date2", EJBCommon.convertStringToSQLDate(details.getDateTo()));

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(EJBCommon.convertStringToSQLDateV2(details.getDateTo()));
        calendar.add(Calendar.DAY_OF_MONTH, 10);

        parameters.put("dueDate", EJBCommon.convertSQLDateToString(calendar.getTime()));
        parameters.put("customerMessage", ""); //TODO: Review where to pull this data
        parameters.put("collectionHead", ""); //TODO: Review where to pull this data
        parameters.put("invoiceBatchName", ""); //TODO: Review where to pull this data
        parameters.put("waterOnly", false);
        parameters.put("viewType", "PDF");

        parameters.put("company", details.getCompanyName());
        parameters.put("dateFrom", EJBCommon.convertStringToSQLDate(details.getDateFrom()));
        parameters.put("dateTo", EJBCommon.convertStringToSQLDate(details.getDateTo()));

        parameters.put("telephoneNumber", details.getCompanyPhone());
        parameters.put("faxNumber", details.getCompanyFax());
        parameters.put("email", details.getCompanyEmail());
        parameters.put("tinNumber", details.getCompanyTinNumber());
        parameters.put("companyAddress", details.getCompanyAddress());

        if (agingBucket > 1) {
            parameters.put("agingBucket1", "1-" + agingBucket + " days");
            parameters.put("agingBucket2", (agingBucket + 1) + "-" + (agingBucket * 2) + " days");
            parameters.put("agingBucket3", ((agingBucket * 2) + 1) + "-" + (agingBucket * 3) + " days");
            parameters.put("agingBucket4", ((agingBucket * 3) + 1) + "-" + (agingBucket * 4) + " days");
            parameters.put("agingBucket5", "Over " + (agingBucket * 4) + " days");
        } else {
            parameters.put("agingBucket1", "1 day");
            parameters.put("agingBucket2", "2 days");
            parameters.put("agingBucket3", "3 days");
            parameters.put("agingBucket4", "4 days");
            parameters.put("agingBucket5", "Over 4 days");
        }

        if (details.isIncludePosted()) {
            parameters.put("includeUnpostedTransaction", "YES");
        } else {
            parameters.put("includeUnpostedTransaction", "NO");
        }

        if (details.isIncludeAdvance()) {
            parameters.put("includeAdvance", "YES");
        } else {
            parameters.put("includeAdvance", "NO");
        }

        return parameters;
    }

}