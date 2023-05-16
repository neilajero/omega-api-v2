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
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.*;
import java.net.URL;
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

    @Inject
    private ServletContext servletContext;

    @Override
    public void executeSpArRepStatementOfAccount(StatementDetails details)
            throws GlobalNoRecordFoundException {

        Debug.print("ArRepStatementApiControllerBean executeSpArRepStatementOfAccount");

        try {

            String branchCodes = String.join(",", details.getBranchCodes());

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
            spQuery.setParameter("branchCode", branchCodes);
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
                    details.setBranchCodes(adBranchHome.findBrCodeByBrAdCompany(adCompany.getCmpCode(), adCompany.getCmpShortName()));
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
            Report report = this.generateReport(
                    this.setReportParameters(details), details.getCompanyShortName().toLowerCase());
            if (report.getBytes() != null) {
                response.setPdfReport(report.getBytes());
            }

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

    private Report generateReport(Map<String, Object> parameters, String companyShortName)
            throws JRException, SQLException {
        Debug.print("ArRepStatementApiControllerBean generateReport");
        Report report = new Report();
        String filename = servletContext.getRealPath(
                String.format("/WEB-INF/jasper/%s/ArRepStatementOfAccount.jasper", companyShortName));
        if (filename != null) {
            Connection conn = this.getConnection();
            report.setViewType(EJBCommon.REPORT_VIEW_TYPE_PDF);
            report.setBytes(JasperRunManager.runReportToPdf(filename, parameters, conn));
            conn.close();
        } else {
            Debug.print("ERROR : RESOURCE URL PATH NOT FOUND!");
        }
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

    private Map<String, Object> setReportParameters(StatementDetails details) {

        Debug.print("ArRepStatementApiControllerBean setReportParameters");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("preparedBy", details.getPreparedBy());
        parameters.put("dateFrom", EJBCommon.convertStringToSQLDate(details.getDateFrom()));
        parameters.put("dateTo", EJBCommon.convertStringToSQLDate(details.getDateTo()));

        return parameters;
    }

}