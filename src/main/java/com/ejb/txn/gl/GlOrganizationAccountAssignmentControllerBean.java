package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlARAccountNumberOfSegmentInvalidException;
import com.ejb.exception.gl.GlARAccountRangeAlreadyDeletedException;
import com.ejb.exception.gl.GlARAccountRangeInvalidException;
import com.ejb.exception.gl.GlARAccountRangeNoAccountFoundException;
import com.ejb.exception.gl.GlARAccountRangeOverlappedException;
import com.ejb.exception.gl.GlARNoAccountRangeFoundException;
import com.ejb.exception.gl.GlARResponsibilityNotAllowedException;
import com.ejb.exception.gl.GlORGNoOrganizationFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlAccountRange;
import com.ejb.dao.gl.LocalGlAccountRangeHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlOrganization;
import com.ejb.dao.gl.LocalGlOrganizationHome;
import com.ejb.entities.gl.LocalGlResponsibility;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlAccountRangeDetails;
import com.util.gl.GlOrganizationDetails;

@Stateless(name = "GlOrganizationAccountAssignmentControllerEJB")
public class GlOrganizationAccountAssignmentControllerBean extends EJBContextClass implements GlOrganizationAccountAssignmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalGlAccountRangeHome glAccountRangeHome;
    @EJB
    private LocalGlOrganizationHome glOrganizationHome;


    public ArrayList getGlOrgAll(Integer AD_CMPNY) throws GlORGNoOrganizationFoundException {

        Debug.print("GlOrganizationControllerBean getGlOrgAll");

        ArrayList orgAllList = new ArrayList();
        Collection glOrganizations;

        try {
            glOrganizations = glOrganizationHome.findOrgAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glOrganizations.size() == 0) throw new GlORGNoOrganizationFoundException();

        for (Object organization : glOrganizations) {
            LocalGlOrganization glOrganization = (LocalGlOrganization) organization;

            GlOrganizationDetails details = new GlOrganizationDetails(glOrganization.getOrgCode(), glOrganization.getOrgName(), glOrganization.getOrgDescription(), glOrganization.getOrgMasterCode());
            orgAllList.add(details);
        }

        return orgAllList;
    }

    public GlOrganizationDetails getGlOrgDescriptionByGlOrgName(String ORG_NM, Integer AD_CMPNY) throws GlORGNoOrganizationFoundException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean getGlOrgDescriptionByGlOrgName");

        LocalGlOrganization glOrganization = null;

        try {
            glOrganization = glOrganizationHome.findByOrgName(ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (EJBException ex) {
            throw new EJBException(ex.getMessage());
        }

        return new GlOrganizationDetails(glOrganization.getOrgName(), glOrganization.getOrgDescription(), glOrganization.getOrgMasterCode());
    }

    public ArrayList getGlArByGlOrgName(String ORG_NM, Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlARNoAccountRangeFoundException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean getGlArByGlOrgName");

        LocalGlOrganization glOrganization = null;

        ArrayList arAllList = new ArrayList();
        Collection glAccountRanges = null;

        try {
            glOrganization = glOrganizationHome.findByOrgName(ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (EJBException ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glAccountRanges = glOrganization.getGlAccountRanges();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glAccountRanges.size() == 0) throw new GlARNoAccountRangeFoundException();

        for (Object accountRange : glAccountRanges) {
            LocalGlAccountRange glAccountRange = (LocalGlAccountRange) accountRange;

            GlAccountRangeDetails details = new GlAccountRangeDetails(glAccountRange.getArCode(), glAccountRange.getArLine(), glAccountRange.getArAccountLow(), glAccountRange.getArAccountHigh());

            arAllList.add(details);
        }

        return arAllList;
    }

    public void addGlArEntry(GlAccountRangeDetails details, String ORG_NM, Integer RES_CODE, Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlARAccountRangeOverlappedException, GlARAccountNumberOfSegmentInvalidException, GlARAccountRangeNoAccountFoundException, GlARResponsibilityNotAllowedException, GlARAccountRangeInvalidException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean addGlArEntry");

        LocalGlOrganization glOrganization = null;
        LocalAdCompany adCompany = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalGlAccountRange glAccountRange = null;
        LocalGenField genField = null;
        LocalGlOrganization glMasterOrganization = null;
        Collection glResponsibilities = null;
        boolean responsibilityFound = false;

        try {
            glOrganization = glOrganizationHome.findByOrgName(ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (EJBException ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            genField = adCompany.getGenField();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glOrganization.getOrgMasterCode() != null && glOrganization.getOrgMasterCode() != 0) {

            try {
                glMasterOrganization = glOrganizationHome.findByPrimaryKey(glOrganization.getOrgMasterCode());
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            try {
                glResponsibilities = glMasterOrganization.getGlResponsibilities();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            if (glResponsibilities != null) {
                for (Object glResponsibility : glResponsibilities) {
                    if (RES_CODE.equals(((LocalGlResponsibility) glResponsibility).getResCode())) {
                        responsibilityFound = true;
                        break;
                    }
                }
                if (!responsibilityFound) throw new GlARResponsibilityNotAllowedException();
            }

            if (isEnteredAccountWithinMasterCodeRange(glMasterOrganization, details, genField, AD_CMPNY))
                throw new GlARAccountRangeInvalidException();
        }

        ArrayList coaWithinRangeList = getCoaWithinRange(details, glOrganization, genField, AD_CMPNY);

        if (coaWithinRangeList.size() == 0) {
            throw new GlARAccountRangeNoAccountFoundException();
        }

        try {
            glAccountRange = glAccountRangeHome.create(details.getArLine(), details.getArAccountLow(), details.getArAccountHigh(), AD_CMPNY);

            glOrganization.addGlAccountRange(glAccountRange);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlArEntry(GlAccountRangeDetails details, String ORG_NM, Integer RES_CODE, Integer AD_CMPNY) throws GlORGNoOrganizationFoundException, GlARAccountRangeOverlappedException, GlARAccountNumberOfSegmentInvalidException, GlARAccountRangeNoAccountFoundException, GlARAccountRangeAlreadyDeletedException, GlARResponsibilityNotAllowedException, GlARAccountRangeInvalidException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean updateGlArEntry");

        LocalGlOrganization glOrganization = null;
        LocalAdCompany adCompany = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalGlAccountRange glAccountRange = null;
        LocalGenField genField = null;
        Collection glAccountRanges = null;
        boolean isAccountRangeChanged = false;
        LocalGlOrganization glMasterOrganization = null;
        Collection glResponsibilities = null;
        boolean responsibilityFound = false;

        try {
            glOrganization = glOrganizationHome.findByOrgName(ORG_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlORGNoOrganizationFoundException();
        } catch (EJBException ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glAccountRange = glAccountRangeHome.findByPrimaryKey(details.getArCode());
        } catch (FinderException ex) {
            throw new GlARAccountRangeAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        isAccountRangeChanged = !glAccountRange.getArAccountLow().equals(details.getArAccountLow()) || !glAccountRange.getArAccountHigh().equals(details.getArAccountHigh());

        if (isAccountRangeChanged) {

            try {
                adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                genField = adCompany.getGenField();
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
            if (glOrganization.getOrgMasterCode() != null && glOrganization.getOrgMasterCode() != 0) {

                try {
                    glMasterOrganization = glOrganizationHome.findByPrimaryKey(glOrganization.getOrgMasterCode());
                } catch (Exception ex) {
                    throw new EJBException(ex.getMessage());
                }

                try {
                    glResponsibilities = glMasterOrganization.getGlResponsibilities();
                } catch (Exception ex) {
                    throw new EJBException(ex.getMessage());
                }

                if (glResponsibilities != null) {
                    for (Object glResponsibility : glResponsibilities) {
                        if (RES_CODE.equals(((LocalGlResponsibility) glResponsibility).getResCode())) {
                            responsibilityFound = true;
                            break;
                        }
                    }
                    if (!responsibilityFound) throw new GlARResponsibilityNotAllowedException();
                }

                if (isEnteredAccountWithinMasterCodeRange(glMasterOrganization, details, genField, AD_CMPNY))
                    throw new GlARAccountRangeInvalidException();
            }

            ArrayList coaWithinRangeList = getCoaWithinRange(details, glOrganization, genField, AD_CMPNY);

            if (coaWithinRangeList.size() == 0) {
                getSessionContext().setRollbackOnly();
                throw new GlARAccountRangeNoAccountFoundException();
            }

            try {
                glAccountRange.setArAccountLow(details.getArAccountLow());
                glAccountRange.setArAccountHigh(details.getArAccountHigh());
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    public void deleteGlArEntry(Integer AR_CODE, Integer RES_CODE, Integer AD_CMPNY) throws GlARAccountRangeAlreadyDeletedException, GlARResponsibilityNotAllowedException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean deleteGlArEntry");

        LocalGlOrganization glOrganization = null;
        LocalGlAccountRange glAccountRange = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalAdCompany adCompany = null;
        LocalGenField genField = null;
        LocalGlOrganization glMasterOrganization = null;
        Collection glResponsibilities = null;
        boolean responsibilityFound = false;

        try {
            glAccountRange = glAccountRangeHome.findByPrimaryKey(AR_CODE);
        } catch (FinderException ex) {
            throw new GlARAccountRangeAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glOrganization = glAccountRange.getGlOrganization();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            genField = adCompany.getGenField();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glOrganization.getOrgMasterCode() != null) {

            try {
                glMasterOrganization = glOrganizationHome.findByPrimaryKey(glOrganization.getOrgMasterCode());
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            try {
                glResponsibilities = glMasterOrganization.getGlResponsibilities();
            } catch (Exception ex) {
                throw new EJBException(ex.getMessage());
            }

            if (glResponsibilities != null) {
                for (Object glResponsibility : glResponsibilities) {
                    if (RES_CODE.equals(((LocalGlResponsibility) glResponsibility).getResCode())) {
                        responsibilityFound = true;
                        break;
                    }
                }

                if (!responsibilityFound) throw new GlARResponsibilityNotAllowedException();
            }
        }

        try {
            em.remove(glAccountRange);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean ejbCreate");
    }

    private boolean isEnteredAccountWithinMasterCodeRange(LocalGlOrganization glMasterOrganization, GlAccountRangeDetails details, LocalGenField genField, Integer AD_CMPNY) {

        Debug.print("GlOrganizationAccountAssignmentBean isEnteredAccountWithinMasterCodeRange");

        Collection glAccountRanges = null;
        LocalGlAccountRange glAccountRange = null;
        String strSeparator = null;
        short genNumberOfSegment = 0;
        boolean isWithinRange = false;

        try {
            char chrSeparator = genField.getFlSegmentSeparator();
            genNumberOfSegment = genField.getFlNumberOfSegment();
            strSeparator = String.valueOf(chrSeparator);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glAccountRanges = glMasterOrganization.getGlAccountRanges();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glAccountRanges != null) {
            for (Object accountRange : glAccountRanges) {
                glAccountRange = (LocalGlAccountRange) accountRange;
                StringTokenizer stMasterAccountLow = new StringTokenizer(glAccountRange.getArAccountLow(), strSeparator);
                StringTokenizer stMasterAccountHigh = new StringTokenizer(glAccountRange.getArAccountHigh(), strSeparator);
                StringTokenizer stAccountLow = new StringTokenizer(details.getArAccountLow(), strSeparator);
                StringTokenizer stAccountHigh = new StringTokenizer(details.getArAccountHigh(), strSeparator);
                String[] masterAccountLowSegmentValue = new String[genNumberOfSegment];
                String[] masterAccountHighSegmentValue = new String[genNumberOfSegment];
                String[] accountLowSegmentValue = new String[genNumberOfSegment];
                String[] accountHighSegmentValue = new String[genNumberOfSegment];

                int j = 0;
                while (stAccountLow.hasMoreTokens()) {
                    if (j == genNumberOfSegment) {
                        return false;
                    }
                    try {
                        accountLowSegmentValue[j] = stAccountLow.nextToken();
                    } catch (Exception ex) {
                        return false;
                    }
                    j++;
                }
                if (j < genNumberOfSegment) {
                    return false;
                }
                j = 0;
                while (stAccountHigh.hasMoreTokens()) {
                    if (j == genNumberOfSegment) {
                        return false;
                    }
                    try {
                        accountHighSegmentValue[j] = stAccountHigh.nextToken();
                    } catch (Exception ex) {
                        return false;
                    }
                    j++;
                }

                if (j < genNumberOfSegment) {
                    return false;
                }
                j = 0;
                while (stMasterAccountLow.hasMoreTokens()) {
                    masterAccountLowSegmentValue[j] = stMasterAccountLow.nextToken();
                    masterAccountHighSegmentValue[j] = stMasterAccountHigh.nextToken();
                    j++;
                }

                for (int k = 0; k < genNumberOfSegment; k++) {
                    if ((accountLowSegmentValue[k].compareTo(masterAccountLowSegmentValue[k]) >= 0 && accountLowSegmentValue[k].compareTo(masterAccountHighSegmentValue[k]) <= 0) || (accountHighSegmentValue[k].compareTo(masterAccountLowSegmentValue[k]) <= 0 && accountHighSegmentValue[k].compareTo(masterAccountHighSegmentValue[k]) >= 0)) {

                        isWithinRange = true;
                    } else {

                        isWithinRange = false;
                        break;
                    }
                }
                if (isWithinRange) {
                    return false;
                }
            }
        }
        return !isWithinRange;
    }

    // private methods
    private boolean isRegisteredAccountWithinRange(LocalGlAccountRange glAccountRange, LocalGlChartOfAccount glChartOfAccount, LocalGenField genField, Integer AD_CMPNY) {

        Debug.print("GlOrganizationAccountAssignmentControllerBean isRegisteredAccountWithinRange");

        String strSeparator = null;
        short genNumberOfSegment = 0;

        try {
            char chrSeparator = genField.getFlSegmentSeparator();
            genNumberOfSegment = genField.getFlNumberOfSegment();
            strSeparator = String.valueOf(chrSeparator);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        StringTokenizer stCoa = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), strSeparator);
        StringTokenizer stArAccountLow = new StringTokenizer(glAccountRange.getArAccountLow(), strSeparator);
        StringTokenizer stArAccountHigh = new StringTokenizer(glAccountRange.getArAccountHigh(), strSeparator);

        String[] coaSegmentValue = new String[genNumberOfSegment];
        String[] arAccountLowSegmentValue = new String[genNumberOfSegment];
        String[] arAccountHighSegmentValue = new String[genNumberOfSegment];

        int j = 0;

        while (stCoa.hasMoreTokens()) {
            coaSegmentValue[j] = stCoa.nextToken();
            arAccountLowSegmentValue[j] = stArAccountLow.nextToken();
            arAccountHighSegmentValue[j] = stArAccountHigh.nextToken();
            j++;
        }

        boolean isOverlapped = false;

        for (int k = 0; k < genNumberOfSegment; k++) {
            if (coaSegmentValue[k].compareTo(arAccountLowSegmentValue[k]) >= 0 && coaSegmentValue[k].compareTo(arAccountHighSegmentValue[k]) <= 0) {
                isOverlapped = true;
            } else {
                isOverlapped = false;
                break;
            }
        }

        return isOverlapped;
    }

    private ArrayList getCoaWithinRange(GlAccountRangeDetails details, LocalGlOrganization glOrganization, LocalGenField genField, Integer AD_CMPNY) throws GlARAccountNumberOfSegmentInvalidException, GlARAccountRangeOverlappedException, GlARAccountRangeInvalidException {

        Debug.print("GlOrganizationAccountAssignmentControllerBean getCoaWithinRange");

        Collection glAccountRanges = null;
        ArrayList coaWithinRangeList = new ArrayList();

        String strSeparator = null;
        short numberOfSegment = 0;
        short genNumberOfSegment = 0;

        try {
            char chrSeparator = genField.getFlSegmentSeparator();
            genNumberOfSegment = genField.getFlNumberOfSegment();
            strSeparator = String.valueOf(chrSeparator);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        StringTokenizer st = new StringTokenizer(details.getArAccountLow(), strSeparator);
        String[] newAccountLowSegmentValue = new String[genNumberOfSegment];
        String[] newAccountHighSegmentValue = new String[genNumberOfSegment];
        String[] accountLowSegmentValue = new String[genNumberOfSegment];
        String[] accountHighSegmentValue = new String[genNumberOfSegment];

        while (st.hasMoreTokens()) {
            if (numberOfSegment == genNumberOfSegment) {
                throw new GlARAccountNumberOfSegmentInvalidException();
            } else {
                try {
                    newAccountLowSegmentValue[numberOfSegment] = st.nextToken();
                } catch (Exception ex) {
                    getSessionContext().setRollbackOnly();
                    throw new GlARAccountRangeInvalidException();
                }
            }
            numberOfSegment = (short) (numberOfSegment + 1);
        }

        if (numberOfSegment < genNumberOfSegment) {
            getSessionContext().setRollbackOnly();
            throw new GlARAccountNumberOfSegmentInvalidException();
        }

        st = new StringTokenizer(details.getArAccountHigh(), strSeparator);

        numberOfSegment = 0;

        while (st.hasMoreTokens()) {
            if (numberOfSegment == genNumberOfSegment) {
                getSessionContext().setRollbackOnly();
                throw new GlARAccountNumberOfSegmentInvalidException();
            } else {
                try {
                    newAccountHighSegmentValue[numberOfSegment] = st.nextToken();
                } catch (Exception ex) {
                    getSessionContext().setRollbackOnly();
                    throw new GlARAccountRangeInvalidException();
                }
            }
            numberOfSegment = (short) (numberOfSegment + 1);
        }

        if (numberOfSegment < genNumberOfSegment) {
            getSessionContext().setRollbackOnly();
            throw new GlARAccountNumberOfSegmentInvalidException();
        }

        try {
            glAccountRanges = glOrganization.getGlAccountRanges();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        if (!glAccountRanges.isEmpty()) {

            for (Object accountRange : glAccountRanges) {
                LocalGlAccountRange glAccountRange = (LocalGlAccountRange) accountRange;

                if (details.getArCode() == null || (!details.getArCode().equals(glAccountRange.getArCode()))) {
                    StringTokenizer stAccountLow = new StringTokenizer(glAccountRange.getArAccountLow(), strSeparator);
                    StringTokenizer stAccountHigh = new StringTokenizer(glAccountRange.getArAccountHigh(), strSeparator);

                    int j = 0;

                    while (stAccountLow.hasMoreTokens()) {
                        accountLowSegmentValue[j] = stAccountLow.nextToken();
                        j++;
                    }

                    j = 0;
                    while (stAccountHigh.hasMoreTokens()) {
                        accountHighSegmentValue[j] = stAccountHigh.nextToken();
                        j++;
                    }

                    boolean isOverlapped = false;

                    for (int k = 0; k < genNumberOfSegment; k++) {
                        if (((newAccountLowSegmentValue[k].compareTo(accountLowSegmentValue[k]) >= 0 && newAccountLowSegmentValue[k].compareTo(accountHighSegmentValue[k]) <= 0) || (newAccountHighSegmentValue[k].compareTo(accountLowSegmentValue[k]) <= 0 && newAccountHighSegmentValue[k].compareTo(accountHighSegmentValue[k]) >= 0)) || ((accountLowSegmentValue[k].compareTo(newAccountLowSegmentValue[k]) >= 0 && accountLowSegmentValue[k].compareTo(newAccountHighSegmentValue[k]) <= 0) || (accountHighSegmentValue[k].compareTo(newAccountLowSegmentValue[k]) <= 0 && accountHighSegmentValue[k].compareTo(newAccountHighSegmentValue[k]) >= 0))) {

                            isOverlapped = true;
                        } else {

                            isOverlapped = false;
                            break;
                        }
                    }

                    if (isOverlapped) {
                        getSessionContext().setRollbackOnly();
                        throw new GlARAccountRangeOverlappedException();
                    }
                }
            }
        }

        Collection glChartOfAccounts = null;
        try {
            glChartOfAccounts = glChartOfAccountHome.findCoaAllEnabled(new Date(EJBCommon.getGcCurrentDateWoTime().getTime().getTime()), AD_CMPNY);
        } catch (FinderException ex) {
        } catch (EJBException ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        if (glChartOfAccounts != null) {

            for (Object chartOfAccount : glChartOfAccounts) {
                LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;
                StringTokenizer stCoa = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), strSeparator);

                String[] coaSegmentValue = new String[genNumberOfSegment];

                int j = 0;

                while (stCoa.hasMoreTokens()) {
                    coaSegmentValue[j] = stCoa.nextToken();
                    j++;
                }

                boolean isOverlapped = false;

                for (int k = 0; k < genNumberOfSegment; k++) {
                    if (coaSegmentValue[k].compareTo(newAccountLowSegmentValue[k]) >= 0 && coaSegmentValue[k].compareTo(newAccountHighSegmentValue[k]) <= 0) {
                        isOverlapped = true;
                    } else {
                        isOverlapped = false;
                        break;
                    }
                }

                if (isOverlapped) {
                    coaWithinRangeList.add(glChartOfAccount.getCoaCode());
                }
            }
        }

        return coaWithinRangeList;
    }
}