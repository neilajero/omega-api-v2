/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindChartOfAccountControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;
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
import com.ejb.exception.gl.GlCOAAccountNumberHasParentValueException;
import com.ejb.exception.gl.GlCOAAccountNumberIsInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gen.LocalGenFieldHome;
import com.ejb.entities.gen.LocalGenQualifier;
import com.ejb.dao.gen.LocalGenQualifierHome;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.dao.gen.LocalGenValueSetHome;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.ad.AdBranchDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.gen.GenModValueSetValueDetails;
import com.util.mod.gl.GlModChartOfAccountDetails;

@Stateless(name = "GlFindChartOfAccountControllerEJB")
public class GlFindChartOfAccountControllerBean extends EJBContextClass implements GlFindChartOfAccountController {

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
    private LocalGenFieldHome genFieldHome;
    @EJB
    private LocalGenQualifierHome genQualifierHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;

    public ArrayList getGlCoaByCriteria(HashMap criteria, String COA_ACCNT_FRM, String COA_ACCNT_TO, ArrayList vsvDescList, String COA_ACCNT_TYP, Integer OFFSET, Integer LIMIT, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindChartOfAccountControllerBean getGlCoaByCriteria");

        LocalAdCompany adCompany = null;
        LocalGenField genField = null;

        ArrayList list = new ArrayList();

        Collection genSegments = null;
        char chrSeparator;
        String strSeparator = null;
        int genNumberOfSegment = 0;


        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            genField = adCompany.getGenField();
            genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);
            genNumberOfSegment = genField.getFlNumberOfSegment();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT DISTINCT OBJECT(coa) FROM GlChartOfAccount coa");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj = new Object[criteria.size()];

        if (adBrnchList.isEmpty()) {

            jbossQl.append(" WHERE ");

        } else {

            jbossQl.append(", in (coa.adBranchCoas) bcoa WHERE bcoa.adBranch.brCode in (");

            boolean firstLoop = true;

            for (Object o : adBrnchList) {

                if (!firstLoop) {
                    jbossQl.append(", ");
                } else {
                    firstLoop = false;
                }

                AdBranchDetails mdetails = (AdBranchDetails) o;

                jbossQl.append(mdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;
        }

        if (COA_ACCNT_FRM != null && COA_ACCNT_TO != null) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
            }

            StringTokenizer stAccountFrom = new StringTokenizer(COA_ACCNT_FRM, strSeparator);
            StringTokenizer stAccountTo = new StringTokenizer(COA_ACCNT_TO, strSeparator);

            if (stAccountFrom.countTokens() != genNumberOfSegment || stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlobalNoRecordFoundException();
            }

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

            firstArgument = false;
        }

        if (!vsvDescList.isEmpty()) {

            for (Object o : vsvDescList) {

                GenModValueSetValueDetails mdetails = (GenModValueSetValueDetails) o;

                Collection genValueSetValues = null;

                try {

                    genValueSetValues = genValueSetValueHome.findByVsvDescriptionAndVsName(mdetails.getVsvDescription(), mdetails.getVsvVsName(), AD_CMPNY);

                } catch (Exception ex) {

                    throw new EJBException(ex.getMessage());
                }

                if (genValueSetValues.isEmpty()) {

                    throw new GlobalNoRecordFoundException();
                }

                if (!firstArgument) {
                    jbossQl.append("AND (");
                } else {
                    jbossQl.append("(");
                    firstArgument = false;
                }

                Iterator j = genValueSetValues.iterator();

                while (j.hasNext()) {

                    LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) j.next();

                    int genSegmentNumber = 0;

                    try {

                        LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSetValue.getGenValueSet().getVsCode(), AD_CMPNY);
                        genSegmentNumber = genSegment.getSgSegmentNumber();

                    } catch (Exception ex) {

                        throw new EJBException(ex.getMessage());
                    }

                    StringBuilder var = new StringBuilder("1");

                    for (int k = 0; k < genSegmentNumber - 1; k++) {

                        var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");
                    }

                    if (genNumberOfSegment != genSegmentNumber) {

                        jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");

                    } else if (genNumberOfSegment == genSegmentNumber) {

                        jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");
                    }

                    if (j.hasNext()) {

                        jbossQl.append("OR ");
                    }
                }

                jbossQl.append(") ");
            }
        }

        if (COA_ACCNT_TYP != null) {

            Collection genValueSetValues = null;

            try {

                genValueSetValues = genValueSetValueHome.findByQlAccountType(COA_ACCNT_TYP, AD_CMPNY);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (genValueSetValues.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            if (!firstArgument) {
                jbossQl.append("AND (");
            } else {
                jbossQl.append("(");
                firstArgument = false;
            }

            Iterator j = genValueSetValues.iterator();

            while (j.hasNext()) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) j.next();

                int genSegmentNumber = 0;

                try {

                    LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSetValue.getGenValueSet().getVsCode(), AD_CMPNY);
                    genSegmentNumber = genSegment.getSgSegmentNumber();

                } catch (Exception ex) {

                    throw new EJBException(ex.getMessage());
                }

                StringBuilder var = new StringBuilder("1");

                for (int k = 0; k < genSegmentNumber - 1; k++) {

                    var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");
                }

                if (genNumberOfSegment != genSegmentNumber) {

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");

                } else if (genNumberOfSegment == genSegmentNumber) {

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");
                }

                if (j.hasNext()) {

                    jbossQl.append("OR ");
                }
            }

            jbossQl.append(") ");
        }

        if (criteria.containsKey("coaDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }
            jbossQl.append("coa.coaDateFrom=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("coaDateFrom");
            ctr++;
        }

        if (criteria.containsKey("coaDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }
            jbossQl.append("coa.coaDateTo=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("coaDateTo");
            ctr++;
        }

        if (criteria.containsKey("coaEnable")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }
            jbossQl.append("coa.coaEnable=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("coaEnable");
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
        }

        jbossQl.append("coa.coaAdCompany=").append(AD_CMPNY).append(" ");

        // generate order by coa natural account

        short accountSegmentNumber = 0;

        try {

            LocalGenSegment genSegment = genSegmentHome.findByFlCodeAndSegmentType(genField.getFlCode(), 'N', AD_CMPNY);
            accountSegmentNumber = genSegment.getSgSegmentNumber();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        jbossQl.append("ORDER BY coa.coaSegment").append(accountSegmentNumber).append(", coa.coaAccountNumber ");

        Collection glChartOfAccounts = null;

        try {

            glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glChartOfAccounts.size() == 0) throw new GlobalNoRecordFoundException();

        for (Object chartOfAccount : glChartOfAccounts) {

            LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

            GlModChartOfAccountDetails modCoaDetails = new GlModChartOfAccountDetails(glChartOfAccount.getCoaCode(), glChartOfAccount.getCoaAccountNumber(), glChartOfAccount.getCoaCitCategory(), glChartOfAccount.getCoaSawCategory(), glChartOfAccount.getCoaIitCategory(), glChartOfAccount.getCoaDateFrom(), glChartOfAccount.getCoaDateTo(), glChartOfAccount.getCoaEnable(), glChartOfAccount.getCoaAccountDescription(), glChartOfAccount.getCoaAccountType(), glChartOfAccount.getCoaTaxType(), glChartOfAccount.getGlFunctionalCurrency() == null ? null : glChartOfAccount.getGlFunctionalCurrency().getFcName(), glChartOfAccount.getCoaForRevaluation());

            list.add(modCoaDetails);
        }

        return list;
    }

    public Integer getGlCoaSizeByCriteria(HashMap criteria, String COA_ACCNT_FRM, String COA_ACCNT_TO, ArrayList vsvDescList, String COA_ACCNT_TYP, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindChartOfAccountControllerBean getGlCoaSizeByCriteria");

        LocalAdCompany adCompany = null;
        LocalGenField genField = null;

        ArrayList list = new ArrayList();

        Collection genSegments = null;
        char chrSeparator;
        String strSeparator = null;
        int genNumberOfSegment = 0;


        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            genField = adCompany.getGenField();
            genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);
            genNumberOfSegment = genField.getFlNumberOfSegment();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT DISTINCT OBJECT(coa) FROM GlChartOfAccount coa");

        boolean firstArgument = true;
        short ctr = 0;
        Object[] obj = new Object[criteria.size()];

        if (adBrnchList.isEmpty()) {

            jbossQl.append(" WHERE ");

        } else {

            jbossQl.append(", in (coa.adBranchCoas) bcoa WHERE bcoa.adBranch.brCode in (");

            boolean firstLoop = true;

            for (Object o : adBrnchList) {

                if (!firstLoop) {
                    jbossQl.append(", ");
                } else {
                    firstLoop = false;
                }

                AdBranchDetails mdetails = (AdBranchDetails) o;

                jbossQl.append(mdetails.getBrCode());
            }

            jbossQl.append(") ");

            firstArgument = false;
        }

        if (COA_ACCNT_FRM != null && COA_ACCNT_TO != null) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }

            StringTokenizer stAccountFrom = new StringTokenizer(COA_ACCNT_FRM, strSeparator);
            StringTokenizer stAccountTo = new StringTokenizer(COA_ACCNT_TO, strSeparator);

            if (stAccountFrom.countTokens() != genNumberOfSegment || stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlobalNoRecordFoundException();
            }

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

            firstArgument = false;
        }

        if (!vsvDescList.isEmpty()) {

            for (Object o : vsvDescList) {

                GenModValueSetValueDetails mdetails = (GenModValueSetValueDetails) o;

                Collection genValueSetValues = null;

                try {

                    genValueSetValues = genValueSetValueHome.findByVsvDescriptionAndVsName(mdetails.getVsvDescription(), mdetails.getVsvVsName(), AD_CMPNY);

                } catch (Exception ex) {

                    throw new EJBException(ex.getMessage());
                }

                if (genValueSetValues.isEmpty()) {

                    throw new GlobalNoRecordFoundException();
                }

                if (!firstArgument) {
                    jbossQl.append("AND (");
                } else {
                    jbossQl.append("(");
                    firstArgument = false;
                }

                Iterator j = genValueSetValues.iterator();

                while (j.hasNext()) {

                    LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) j.next();

                    int genSegmentNumber = 0;

                    try {

                        LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSetValue.getGenValueSet().getVsCode(), AD_CMPNY);
                        genSegmentNumber = genSegment.getSgSegmentNumber();

                    } catch (Exception ex) {

                        throw new EJBException(ex.getMessage());
                    }

                    StringBuilder var = new StringBuilder("1");

                    for (int k = 0; k < genSegmentNumber - 1; k++) {

                        var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");
                    }

                    if (genNumberOfSegment != genSegmentNumber) {

                        jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");

                    } else if (genNumberOfSegment == genSegmentNumber) {

                        jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");
                    }

                    if (j.hasNext()) {

                        jbossQl.append("OR ");
                    }
                }

                jbossQl.append(") ");
            }
        }

        if (COA_ACCNT_TYP != null) {

            Collection genValueSetValues = null;

            try {

                genValueSetValues = genValueSetValueHome.findByQlAccountType(COA_ACCNT_TYP, AD_CMPNY);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (genValueSetValues.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            if (!firstArgument) {
                jbossQl.append("AND (");
            } else {
                jbossQl.append("(");
                firstArgument = false;
            }

            Iterator j = genValueSetValues.iterator();

            while (j.hasNext()) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) j.next();

                int genSegmentNumber = 0;

                try {

                    LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSetValue.getGenValueSet().getVsCode(), AD_CMPNY);
                    genSegmentNumber = genSegment.getSgSegmentNumber();

                } catch (Exception ex) {

                    throw new EJBException(ex.getMessage());
                }

                StringBuilder var = new StringBuilder("1");

                for (int k = 0; k < genSegmentNumber - 1; k++) {

                    var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");
                }

                if (genNumberOfSegment != genSegmentNumber) {

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");

                } else if (genNumberOfSegment == genSegmentNumber) {

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) = '").append(genValueSetValue.getVsvValue()).append("' ");
                }

                if (j.hasNext()) {

                    jbossQl.append("OR ");
                }
            }

            jbossQl.append(") ");
        }

        if (criteria.containsKey("coaDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }
            jbossQl.append("coa.coaDateFrom=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("coaDateFrom");
            ctr++;
        }

        if (criteria.containsKey("coaDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }
            jbossQl.append("coa.coaDateTo=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("coaDateTo");
            ctr++;
        }

        if (criteria.containsKey("coaEnable")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
            }
            jbossQl.append("coa.coaEnable=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("coaEnable");
        }

        if (!firstArgument) {

            jbossQl.append("AND ");

        } else {

            firstArgument = false;
        }

        jbossQl.append("coa.coaAdCompany=").append(AD_CMPNY).append(" ");

        Collection glChartOfAccounts = null;

        try {

            glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glChartOfAccounts.size() == 0) throw new GlobalNoRecordFoundException();

        return glChartOfAccounts.size();
    }

    public ArrayList getGenQlAll(Integer AD_CMPNY) {

        Debug.print("GlFindChartOfAccountController getGenQlAll");

        ArrayList list = new ArrayList();
        Collection genQualifiers = null;


        try {

            genQualifiers = genQualifierHome.findQlfrAll(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        for (Object qualifier : genQualifiers) {

            LocalGenQualifier genQualifier = (LocalGenQualifier) qualifier;

            list.add(genQualifier.getQlAccountType());
        }

        return list;
    }

    public ArrayList getGenVsAll(Integer AD_CMPNY) {

        Debug.print("GlFindChartOfAccountController getGenVsAll");

        ArrayList list = new ArrayList();
        Collection genValueSets = null;

        try {

            genValueSets = genValueSetHome.findVsAll(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        for (Object valueSet : genValueSets) {

            LocalGenValueSet genValueSet = (LocalGenValueSet) valueSet;

            list.add(genValueSet.getVsName());
        }

        return list;
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindChartOfAccountControllerBean getAdBrResAll");

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
                details.setBrName(adBranch.getBrName());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // private methods

    private ArrayList getGenValueSetValueWithValue(LocalGenField genField, String COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberHasParentValueException {

        Debug.print("GlFindChartOfAccountControllerBean getGenValueSetValueWithValue");

        LocalGenSegment genSegment = null;
        LocalGenValueSetValue genValueSetValue = null;

        Collection genSegments = null;
        ArrayList genValueSetValueList = new ArrayList();

        char chrSeparator;
        String strSeparator = "";

        try {
            genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        StringTokenizer st = new StringTokenizer(COA_ACCNT_NMBR, strSeparator);
        Iterator j = genSegments.iterator();

        while (st.hasMoreTokens()) {

            LocalGenValueSet genValueSet = null;
            genSegment = (LocalGenSegment) j.next();
            genValueSet = genSegment.getGenValueSet();

            try {
                genValueSetValue = genValueSetValueHome.findByVsCodeAndVsvValue(genValueSet.getVsCode(), st.nextToken(), AD_CMPNY);
            } catch (NoSuchElementException | FinderException ex) {
                throw new GlCOAAccountNumberIsInvalidException();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            if (genValueSetValue.getVsvParent() == EJBCommon.TRUE) {
                throw new GlCOAAccountNumberHasParentValueException();
            }

            genValueSetValueList.add(genValueSetValue);
        }

        return genValueSetValueList;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalBatchControllerBean ejbCreate");
    }
}