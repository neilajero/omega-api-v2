package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gen.GenModSegmentDetails;
import com.util.reports.gl.GlRepChartOfAccountListDetails;

@Stateless(name = "GlRepChartOfAccountListControllerEJB")
public class GlRepChartOfAccountListControllerBean extends EJBContextClass implements GlRepChartOfAccountListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;

    public ArrayList executeGlRepChartOfAccountList(String COA_ACCNT_NMBR_FRM, String COA_ACCNT_NMBR_TO, boolean COA_ENBL, boolean COA_DSBL, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException, GlobalAccountNumberInvalidException {

        Debug.print("GlRepChartOfAccountListControllerBean executeGlRepChartOfAccountList");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            String strSeparator = String.valueOf(genField.getFlSegmentSeparator());
            int genNumberOfSegment = genField.getFlNumberOfSegment();

            // get coa selected

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(coa) FROM GlChartOfAccount coa ");

            // add branch criteria

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(", in (coa.adBranchCoas) bcoa WHERE bcoa.adBranch.brCode in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") AND ");
            }

            StringTokenizer stAccountFrom = new StringTokenizer(COA_ACCNT_NMBR_FRM, strSeparator);
            StringTokenizer stAccountTo = new StringTokenizer(COA_ACCNT_NMBR_TO, strSeparator);

            // validate if account number is valid

            if (stAccountFrom.countTokens() != genNumberOfSegment || stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlobalAccountNumberInvalidException();
            }

            try {

                StringBuilder var = new StringBuilder("1");

                if (genNumberOfSegment > 1) {

                    for (int i = 0; i < genNumberOfSegment; i++) {

                        if (i == 0 && i < genNumberOfSegment - 1) {

                            // for first segment

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                            var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, 1)+1 ");

                        } else if (i != 0 && i < genNumberOfSegment - 1) {

                            // for middle segments

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                            var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");

                        } else if (i != 0 && i == genNumberOfSegment - 1) {

                            // for last segment

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' ");
                        }
                    }

                } else if (genNumberOfSegment == 1) {

                    String accountFrom = stAccountFrom.nextToken();
                    String accountTo = stAccountTo.nextToken();

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' OR ").append("coa.coaAccountNumber BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' ");
                }

                if (COA_ENBL && !COA_DSBL) {

                    jbossQl.append("AND coa.coaEnable=1 ");

                } else if (!COA_ENBL && COA_DSBL) {

                    jbossQl.append("AND coa.coaEnable=0 ");
                }

                jbossQl.append("AND coa.coaAdCompany=").append(AD_CMPNY).append(" ");

            } catch (NumberFormatException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            // generate order by coa natural account

            short accountSegmentNumber = 0;

            try {

                LocalGenSegment genSegment = genSegmentHome.findByFlCodeAndSegmentType(genField.getFlCode(), 'N', AD_CMPNY);
                accountSegmentNumber = genSegment.getSgSegmentNumber();

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            jbossQl.append("ORDER BY coa.coaSegment").append(accountSegmentNumber).append(", coa.coaAccountNumber ");

            Object[] obj = new Object[0];

            Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria(jbossQl.toString(), obj, 0, 0);

            for (Object chartOfAccount : glChartOfAccounts) {

                LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

                GlRepChartOfAccountListDetails details = new GlRepChartOfAccountListDetails();

                details.setCoaAccountNumber(glChartOfAccount.getCoaAccountNumber());
                details.setCoaDescription(glChartOfAccount.getCoaAccountDescription());
                details.setCoaAccountType(glChartOfAccount.getCoaAccountType());
                details.setCoaEnable(glChartOfAccount.getCoaEnable() == EJBCommon.TRUE);

                list.add(details);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalAccountNumberInvalidException | GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepChartOfAccountListControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenSgAll(Integer AD_CMPNY) {

        Debug.print("GlRepChartOfAccountListControllerBean getGenSgAll");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            Collection genSegments = genSegmentHome.findByFlCode(adCompany.getGenField().getFlCode(), AD_CMPNY);

            for (Object segment : genSegments) {

                LocalGenSegment genSegment = (LocalGenSegment) segment;

                GenModSegmentDetails mdetails = new GenModSegmentDetails();
                mdetails.setSgMaxSize(genSegment.getSgMaxSize());
                mdetails.setSgFlSegmentSeparator(genSegment.getGenField().getFlSegmentSeparator());

                list.add(mdetails);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("AdRepBankAccountListControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepChartOfAccountListControllerBean ejbCreate");
    }
}