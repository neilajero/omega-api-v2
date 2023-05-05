package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.dao.gen.LocalGenFieldHome;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.dao.inv.LocalInvDistributionRecordHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.gen.*;
import com.ejb.entities.gl.*;
import com.ejb.exception.gl.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.gl.GlChartOfAccountDetails;
import com.util.mod.gl.GlModChartOfAccountDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "GlChartOfAccountControllerEJB")
public class GlChartOfAccountControllerBean extends EJBContextClass implements GlChartOfAccountController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdApprovalCoaLineHome adApprovalCoaLineHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchCoaHome adBranchCoaHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private LocalAdBranchSupplierHome adBranchSupplierHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalGenFieldHome genFieldHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlRecurringJournalHome glRecurringJournalHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;

    public ArrayList getAdLvCitCategoryAll(Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean getAdLvAccountCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL COA CATEGORY - CIT", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvSAWCategoryAll(Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean getAdLvSAWCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL COA CATEGORY - SAW", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                Debug.print("adLookUpValue.getLvName()=" + adLookUpValue.getLvName());
                list.add(adLookUpValue.getLvName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvIiTCategoryAll(Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean getAdLvIITCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL COA CATEGORY - IIT", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;
                Debug.print("adLookUpValue.getLvName()=" + adLookUpValue.getLvName());
                list.add(adLookUpValue.getLvName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlModChartOfAccountDetails getGlCoaDescAndAccountTypeByCoaAccountNumber(String COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberHasParentValueException {

        Debug.print("GlChartOfAccountControllerBean getGlCoaDescAndAccountTypeByCoaAccountNumber");

        LocalAdCompany adCompany = null;
        LocalGenField genField = null;

        Collection genSegments = null;
        char chrSeparator;
        String strSeparator = "";

        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
        }
        catch (FinderException ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            genField = adCompany.getGenField();
            genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);
            chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        ArrayList genValueSetValueList = null;

        GlModChartOfAccountDetails modCoaDetails = null;
        StringBuilder MCOA_DESC = new StringBuilder();
        String MCOA_ACCNT_TYP = null;

        try {

            genValueSetValueList = getGenValueSetValueWithValue(genField, COA_ACCNT_NMBR, AD_CMPNY);

            if (genValueSetValueList.size() != genField.getFlNumberOfSegment()) {

                throw new GlCOAAccountNumberIsInvalidException();
            }

        }
        catch (GlCOAAccountNumberIsInvalidException | GlCOAAccountNumberHasParentValueException ex) {

            throw ex;

        }

        Iterator j = genValueSetValueList.iterator();
        Iterator k = genSegments.iterator();

        while (j.hasNext()) {

            LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) j.next();
            LocalGenSegment genSegment = (LocalGenSegment) k.next();

            if (genSegment.getSgSegmentType() == 'N') {

                LocalGenQualifier genQualifier = genValueSetValue.getGenQualifier();
                MCOA_ACCNT_TYP = genQualifier.getQlAccountType();
            }

            MCOA_DESC.append(genValueSetValue.getVsvDescription());

            if (j.hasNext()) {

                MCOA_DESC.append(strSeparator);
            }
        }

        modCoaDetails = new GlModChartOfAccountDetails(MCOA_DESC.toString(), MCOA_ACCNT_TYP);

        return modCoaDetails;
    }

    public GlModChartOfAccountDetails getGlCoaByCoaCode(Integer COA_CODE, Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean getGlCoaByCoaCode");

        try {

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

            return new GlModChartOfAccountDetails(glChartOfAccount.getCoaCode(), glChartOfAccount.getCoaAccountNumber(), glChartOfAccount.getCoaCitCategory(), glChartOfAccount.getCoaSawCategory(), glChartOfAccount.getCoaIitCategory(), glChartOfAccount.getCoaDateFrom(), glChartOfAccount.getCoaDateTo(), glChartOfAccount.getCoaEnable(), glChartOfAccount.getCoaAccountDescription(), glChartOfAccount.getCoaAccountType(), glChartOfAccount.getCoaTaxType(), glChartOfAccount.getGlFunctionalCurrency() == null ? null : glChartOfAccount.getGlFunctionalCurrency().getFcName(), glChartOfAccount.getCoaForRevaluation());

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void addGlCoaEntry(GlChartOfAccountDetails details, ArrayList branchList, String GL_FUNCTIONAL_CURRENCY, Integer AD_CMPNY) throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberAlreadyExistException, GlCOAAccountNumberHasParentValueException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException {

        Debug.print("GlChartOfAccountControllerBean addGlCoaEntry");

        LocalGenField genField = null;
        ArrayList genValueSetValueList = new ArrayList();
        LocalAdBranchCoa adBranchCoa = null;
        LocalAdBranch adBranch = null;

        // check if account number is valid

        LocalAdCompany adCompany = null;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            genField = adCompany.getGenField();

            genValueSetValueList = getGenValueSetValueWithValue(genField, details.getCoaAccountNumber(), AD_CMPNY);

        }
        catch (GlCOAAccountNumberHasParentValueException ex) {

            throw ex;

        }
        catch (Exception ex) {

            throw new GlCOAAccountNumberIsInvalidException();
        }

        if (genValueSetValueList.size() != genField.getFlNumberOfSegment())
            throw new GlCOAAccountNumberIsInvalidException();

        try {

            glChartOfAccountHome.findByCoaAccountNumber(details.getCoaAccountNumber(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlCOAAccountNumberAlreadyExistException();

        }
        catch (FinderException ex) {

        }

        // create coa

        LocalGlChartOfAccount glChartOfAccount = null;

        try {

            glChartOfAccount = glChartOfAccountHome.create(details.getCoaAccountNumber(), details.getCoaAccountDescription(), details.getCoaAccountType(), details.getCoaTaxType(), details.getCoaCitCategory(), details.getCoaSawCategory(), details.getCoaIitCategory(), details.getCoaDateFrom(), details.getCoaDateTo(), null, null, null, null, null, null, null, null, null, null, details.getCoaEnable(), details.getCoaForRevaluation(), AD_CMPNY);

            StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), Character.toString(genField.getFlSegmentSeparator()));

            int i = 0;

            while (st.hasMoreTokens()) {
                i++;
                String value = st.nextToken();

                switch (i) {
                    case 1:
                        glChartOfAccount.setCoaSegment1(value);
                        break;
                    case 2:
                        glChartOfAccount.setCoaSegment2(value);
                        break;
                    case 3:
                        glChartOfAccount.setCoaSegment3(value);
                        break;
                    case 4:
                        glChartOfAccount.setCoaSegment4(value);
                        break;
                    case 5:
                        glChartOfAccount.setCoaSegment5(value);
                        break;
                    case 6:
                        glChartOfAccount.setCoaSegment6(value);
                        break;
                    case 7:
                        glChartOfAccount.setCoaSegment7(value);
                        break;
                    case 8:
                        glChartOfAccount.setCoaSegment8(value);
                        break;
                    case 9:
                        glChartOfAccount.setCoaSegment9(value);
                        break;
                    case 10:
                        glChartOfAccount.setCoaSegment10(value);
                        break;
                    default:
                        break;
                }
            }

            // mapping to functional currency

            if ((GL_FUNCTIONAL_CURRENCY.length() < 0) && (GL_FUNCTIONAL_CURRENCY == null) && (GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND"))) {

                LocalGlFunctionalCurrency glFunctionalCurrency = null;

                try {

                    glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(GL_FUNCTIONAL_CURRENCY, AD_CMPNY);

                    // check if FC selected is default company FC
                    if (Objects.equals(adCompany.getGlFunctionalCurrency().getFcCode(), glFunctionalCurrency.getFcCode()))
                        throw new GlFCFunctionalCurrencyAlreadyAssignedException();

                    glFunctionalCurrency.addGlChartOfAccount(glChartOfAccount);

                }
                catch (FinderException ex) {

                    throw new GlFCNoFunctionalCurrencyFoundException();
                }
            }

        }
        catch (GlFCNoFunctionalCurrencyFoundException | GlFCFunctionalCurrencyAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {

            // create balances to all existing set of books

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSetOfBook.getGlAccountingCalendar().getAcCode(), AD_CMPNY);

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    LocalGlChartOfAccountBalance glChartOfAccountBalance = null;

                    try {

                        glChartOfAccountBalance = glChartOfAccountBalanceHome.create(0d, 0d, 0d, 0d, AD_CMPNY);
                        glChartOfAccountBalance.setGlAccountingCalendarValue(glAccountingCalendarValue);
                        glChartOfAccountBalance.setGlChartOfAccount(glChartOfAccount);

                    }
                    catch (Exception ex) {

                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());
                    }
                }
            }

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // add branch coa

        try {

            for (Object o : branchList) {

                AdBranchDetails brDetails = (AdBranchDetails) o;
                adBranchCoa = adBranchCoaHome.create(AD_CMPNY);

                glChartOfAccount.addAdBranchCoa(adBranchCoa);

                adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranch.addAdBranchCoa(adBranchCoa);
            }

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlCoaEntry(GlChartOfAccountDetails details, String RS_NM, ArrayList branchList, String GL_FUNCTIONAL_CURRENCY, Integer AD_CMPNY) throws GlCOAAccountNumberAlreadyAssignedException, GlCOAAccountNumberHasParentValueException, GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberAlreadyExistException, GlCOAAccountNumberAlreadyDeletedException, GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException {

        Debug.print("GlChartOfAccountControllerBean updateGlCoaEntry");

        LocalGlChartOfAccount glChartOfAccount = null;

        LocalAdBranchCoa adBranchCoa = null;
        LocalAdBranch adBranch = null;

        try {

            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(details.getCoaCode());

        }
        catch (FinderException ex) {

            throw new GlCOAAccountNumberAlreadyDeletedException();
        }

        if (hasRelation(glChartOfAccount, AD_CMPNY)) {

            updateGlCoaEntryWithRelation(glChartOfAccount, details, GL_FUNCTIONAL_CURRENCY, AD_CMPNY);

        } else {

            LocalAdCompany adCompany = null;
            LocalGenField genField = null;
            LocalGlChartOfAccount glChartOfAccount2 = null;
            ArrayList genValueSetValueList = new ArrayList();

            try {

                adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                genField = adCompany.getGenField();

            }
            catch (FinderException ex) {

                throw new EJBException(ex.getMessage());
            }

            try {

                genValueSetValueList = getGenValueSetValueWithValue(genField, details.getCoaAccountNumber(), AD_CMPNY);

            }
            catch (GlCOAAccountNumberHasParentValueException ex) {

                throw ex;

            }
            catch (Exception ex) {

                throw new GlCOAAccountNumberIsInvalidException();
            }

            if (genValueSetValueList.size() != genField.getFlNumberOfSegment())
                throw new GlCOAAccountNumberIsInvalidException();

            try {

                glChartOfAccount2 = glChartOfAccountHome.findByCoaAccountNumber(details.getCoaAccountNumber(), AD_CMPNY);

            }
            catch (FinderException ex) {

                glChartOfAccount.setCoaAccountNumber(details.getCoaAccountNumber());
                glChartOfAccount.setCoaCitCategory(details.getCoaCitCategory());
                glChartOfAccount.setCoaSawCategory(details.getCoaSawCategory());
                glChartOfAccount.setCoaIitCategory(details.getCoaIitCategory());
                glChartOfAccount.setCoaAccountDescription(details.getCoaAccountDescription());
                glChartOfAccount.setCoaAccountType(details.getCoaAccountType());
                glChartOfAccount.setCoaTaxType(details.getCoaTaxType());
                glChartOfAccount.setCoaDateFrom(details.getCoaDateFrom());
                glChartOfAccount.setCoaDateTo(details.getCoaDateTo());
                glChartOfAccount.setCoaEnable(details.getCoaEnable());
                glChartOfAccount.setCoaForRevaluation(details.getCoaForRevaluation());

                StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), Character.toString(genField.getFlSegmentSeparator()));

                int i = 0;

                while (st.hasMoreTokens()) {
                    i++;
                    String value = st.nextToken();

                    switch (i) {
                        case 1:
                            glChartOfAccount.setCoaSegment1(value);
                            break;
                        case 2:
                            glChartOfAccount.setCoaSegment2(value);
                            break;
                        case 3:
                            glChartOfAccount.setCoaSegment3(value);
                            break;
                        case 4:
                            glChartOfAccount.setCoaSegment4(value);
                            break;
                        case 5:
                            glChartOfAccount.setCoaSegment5(value);
                            break;
                        case 6:
                            glChartOfAccount.setCoaSegment6(value);
                            break;
                        case 7:
                            glChartOfAccount.setCoaSegment7(value);
                            break;
                        case 8:
                            glChartOfAccount.setCoaSegment8(value);
                            break;
                        case 9:
                            glChartOfAccount.setCoaSegment9(value);
                            break;
                        case 10:
                            glChartOfAccount.setCoaSegment10(value);
                            break;
                        default:
                            break;
                    }
                }

                // mapping to functional currency

                try {

                    if (GL_FUNCTIONAL_CURRENCY.length() > 0 && GL_FUNCTIONAL_CURRENCY != null && (!GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND"))) {

                        LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(GL_FUNCTIONAL_CURRENCY, AD_CMPNY);

                        // check if FC selected is default company FC
                        if (Objects.equals(adCompany.getGlFunctionalCurrency().getFcCode(), glFunctionalCurrency.getFcCode()))
                            throw new GlFCFunctionalCurrencyAlreadyAssignedException();

                        glFunctionalCurrency.addGlChartOfAccount(glChartOfAccount);
                    }

                }
                catch (FinderException e) {

                    getSessionContext().setRollbackOnly();
                    throw new GlFCNoFunctionalCurrencyFoundException();

                }
                catch (GlFCFunctionalCurrencyAlreadyAssignedException e) {

                    getSessionContext().setRollbackOnly();
                    throw e;
                }
            }

            if (glChartOfAccount2 != null && !glChartOfAccount.getCoaCode().equals(glChartOfAccount2.getCoaCode())) {

                getSessionContext().setRollbackOnly();
                throw new GlCOAAccountNumberAlreadyExistException();

            } else {

                if (glChartOfAccount2 != null && glChartOfAccount.getCoaCode().equals(glChartOfAccount2.getCoaCode())) {
                    // glChartOfAccount.setCoaAccountCategory(details.getCoaAccountCategory());
                    glChartOfAccount.setCoaCitCategory(details.getCoaCitCategory());
                    glChartOfAccount.setCoaSawCategory(details.getCoaSawCategory());
                    glChartOfAccount.setCoaIitCategory(details.getCoaIitCategory());
                    glChartOfAccount.setCoaAccountDescription(details.getCoaAccountDescription());
                    glChartOfAccount.setCoaAccountType(details.getCoaAccountType());
                    glChartOfAccount.setCoaTaxType(details.getCoaTaxType());
                    glChartOfAccount.setCoaDateFrom(details.getCoaDateFrom());
                    glChartOfAccount.setCoaDateTo(details.getCoaDateTo());
                    glChartOfAccount.setCoaEnable(details.getCoaEnable());
                    glChartOfAccount.setCoaForRevaluation(details.getCoaForRevaluation());

                    // remove mapping to previous functional currency

                    LocalGlFunctionalCurrency glFunctionalCurrency = glChartOfAccount.getGlFunctionalCurrency() == null ? null : glChartOfAccount.getGlFunctionalCurrency();

                    // checks if there is no previous FC mapping
                    if (glFunctionalCurrency != null) glFunctionalCurrency.dropGlChartOfAccount(glChartOfAccount);

                    // mapping to new functional currency

                    if (GL_FUNCTIONAL_CURRENCY.length() > 0 && GL_FUNCTIONAL_CURRENCY != null && (!GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND"))) {

                        try {

                            glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(GL_FUNCTIONAL_CURRENCY, AD_CMPNY);

                            // check if FC selected is default company FC
                            if (Objects.equals(adCompany.getGlFunctionalCurrency().getFcCode(), glFunctionalCurrency.getFcCode()))
                                throw new GlFCFunctionalCurrencyAlreadyAssignedException();

                            glFunctionalCurrency.addGlChartOfAccount(glChartOfAccount);

                        }
                        catch (FinderException e) {

                            getSessionContext().setRollbackOnly();
                            throw new GlFCNoFunctionalCurrencyFoundException();

                        }
                        catch (GlFCFunctionalCurrencyAlreadyAssignedException e) {

                            getSessionContext().setRollbackOnly();
                            throw e;
                        }
                    }
                }
            }
        }

        // update branch coa

        try {

            Collection adBranchCoas = adBranchCoaHome.findBcoaByCoaCodeAndRsName(glChartOfAccount.getCoaCode(), RS_NM, AD_CMPNY);

            for (Object branchCoa : adBranchCoas) {

                adBranchCoa = (LocalAdBranchCoa) branchCoa;

                // check if existing mapping is included in the list

                boolean brIncluded = false;

                for (Object o : branchList) {

                    AdBranchDetails brDetails = (AdBranchDetails) o;

                    adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                    if (Objects.equals(adBranchCoa.getAdBranch().getBrCode(), adBranch.getBrCode())) {
                        brIncluded = true;
                        break;
                    }
                }

                if (!brIncluded) {
                    if (hasRelation(adBranchCoa, AD_CMPNY))
                        throw new GlCOAAccountNumberAlreadyAssignedException("Branch mapping cannot be deleted for " + adBranchCoa.getAdBranch().getBrName());
                }

                // remove ad branch coa line
                // if it is included in the list or
                // if it is not included & has no existing unposted transaction

                glChartOfAccount.dropAdBranchCoa(adBranchCoa);
                adBranch = adBranchHome.findByPrimaryKey(adBranchCoa.getAdBranch().getBrCode());
                adBranch.dropAdBranchCoa(adBranchCoa);
                // adBranchCoa.entityRemove();
                em.remove(adBranchCoa);
            }

            // add adBranch coa lines

            for (Object o : branchList) {

                AdBranchDetails brDetails = (AdBranchDetails) o;
                adBranchCoa = adBranchCoaHome.create(AD_CMPNY);

                glChartOfAccount.addAdBranchCoa(adBranchCoa);

                adBranch = adBranchHome.findByPrimaryKey(brDetails.getBrCode());
                adBranch.addAdBranchCoa(adBranchCoa);
            }

        }
        catch (GlCOAAccountNumberAlreadyAssignedException e) {

            getSessionContext().setRollbackOnly();
            throw e;

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlCoaEntry(Integer COA_CODE, Integer AD_CMPNY) throws GlCOAAccountNumberAlreadyAssignedException, GlCOAAccountNumberAlreadyDeletedException {

        Debug.print("GlChartOfAccountControllerBean deleteGlCoaEntry");

        LocalGlChartOfAccount glChartOfAccount;

        try {

            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(COA_CODE);

        }
        catch (FinderException ex) {

            throw new GlCOAAccountNumberAlreadyDeletedException();
        }

        if (hasRelation(glChartOfAccount, AD_CMPNY)) {

            throw new GlCOAAccountNumberAlreadyAssignedException();

        } else {

            try {

                // glChartOfAccount.entityRemove();
                em.remove(glChartOfAccount);

            }
            catch (Exception ex) {

                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlChartOfAccountControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

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

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList getAdBrCoaAll(Integer COA_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlChartOfAccountControllerBean getAdBrCoaAll");

        LocalAdBranchCoa adBranchCoa = null;
        LocalAdBranch adBranch = null;

        Collection adBranchCoas = null;

        ArrayList list = new ArrayList();

        try {

            adBranchCoas = adBranchCoaHome.findBcoaByCoaCode(COA_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchCoas.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchCoa : adBranchCoas) {

                adBranchCoa = (LocalAdBranchCoa) branchCoa;

                adBranch = adBranchHome.findByPrimaryKey(adBranchCoa.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());

                list.add(details);
            }

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {

        Debug.print("GlChartOfAccountControllerBean getAdRsByRsCode");

        LocalAdResponsibility adRes = null;

        try {
            adRes = adResHome.findByPrimaryKey(RS_CODE);
        }
        catch (FinderException ex) {

        }

        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());

        return details;
    }

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    @Override
    public LocalGlChartOfAccount findByCoaAccountNumberAndBranchCode(String COA_ACCNT_NMBR, Integer COA_AD_BRNCH, Integer COA_AD_CMPNY) throws FinderException {

        return glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(COA_ACCNT_NMBR, COA_AD_BRNCH, COA_AD_CMPNY);
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlChartOfAccountControllerBean ejbCreate");
    }

    // private methods

    private ArrayList getGenValueSetValueWithValue(LocalGenField genField, String COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberHasParentValueException {

        Debug.print("GlChartOfAccountControllerBean getGenValueSetValueWithValue");

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

        }
        catch (Exception ex) {

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

            }
            catch (NoSuchElementException | FinderException ex) {

                throw new GlCOAAccountNumberIsInvalidException();

            }
            catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (genValueSetValue.getVsvParent() == EJBCommon.TRUE) {

                throw new GlCOAAccountNumberHasParentValueException();
            }

            genValueSetValueList.add(genValueSetValue);
        }

        return genValueSetValueList;
    }

    private void updateGlCoaEntryWithRelation(LocalGlChartOfAccount glChartOfAccount, GlChartOfAccountDetails details, String GL_FUNCTIONAL_CURRENCY, Integer AD_CMPNY) throws GlCOAAccountNumberAlreadyAssignedException {

        if (!glChartOfAccount.getCoaAccountNumber().equals(details.getCoaAccountNumber())) {
            System.out.print("cause");
            // account number or date from or functional currency is modified then update

            throw new GlCOAAccountNumberAlreadyAssignedException("Account, Effective Date From or Currency cannot be updated");

        } else {

            try {

                // glChartOfAccount.setCoaAccountDescription(details.getCoaAccountDescription());
                // // to be
                // deleted
                // glChartOfAccount.setCoaAccountType(details.getCoaAccountType()); // to be
                // deleted
                glChartOfAccount.setCoaCitCategory(details.getCoaCitCategory());
                glChartOfAccount.setCoaSawCategory(details.getCoaSawCategory());
                glChartOfAccount.setCoaIitCategory(details.getCoaIitCategory());
                glChartOfAccount.setCoaDateTo(details.getCoaDateTo());
                glChartOfAccount.setCoaEnable(details.getCoaEnable());
                glChartOfAccount.setCoaTaxType(details.getCoaTaxType());
                glChartOfAccount.setCoaForRevaluation(details.getCoaForRevaluation());

                if (GL_FUNCTIONAL_CURRENCY != null && GL_FUNCTIONAL_CURRENCY.length() > 0) {
                    LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(GL_FUNCTIONAL_CURRENCY, AD_CMPNY);
                    glFunctionalCurrency.addGlChartOfAccount(glChartOfAccount);
                } else {
                    if (glChartOfAccount.getGlFunctionalCurrency() != null) {
                        glChartOfAccount.getGlFunctionalCurrency().dropGlChartOfAccount(glChartOfAccount);
                    }
                }

            }
            catch (Exception ex) {

                ex.printStackTrace();

                getSessionContext().setRollbackOnly();
                throw new EJBException();
            }
        }
    }

    private boolean hasRelation(LocalGlChartOfAccount glChartOfAccount, Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean hasRelation");

        try {

            // supplier class

            Collection apSupplierClasses1 = apSupplierClassHome.findByScGlCoaPayableAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection apSupplierClasses2 = apSupplierClassHome.findByScGlCoaExpenseAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            // customer class

            Collection arCustomerClasses1 = arCustomerClassHome.findByCcGlCoaChargeAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection arCustomerClasses2 = arCustomerClassHome.findByCcGlCoaReceivableAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection arCustomerClasses3 = arCustomerClassHome.findByCcGlCoaRevenueAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            // bank account

            Collection adBankAccount1 = adBankAccountHome.findByBaGlCoaCashAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount2 = adBankAccountHome.findByBaGlCoaOnAccountReceipt(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount3 = adBankAccountHome.findByBaGlCoaUnappliedReceipt(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount4 = adBankAccountHome.findByBaGlCoaBankChargeAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount5 = adBankAccountHome.findByBaGlCoaClearingAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount6 = adBankAccountHome.findByBaGlCoaInterestAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount7 = adBankAccountHome.findByBaGlCoaAdjustmentAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount8 = adBankAccountHome.findByBaGlCoaCashDiscount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount9 = adBankAccountHome.findByBaGlCoaSalesDiscount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBankAccount10 = adBankAccountHome.findByBaGlCoaUnappliedCheck(glChartOfAccount.getCoaCode(), AD_CMPNY);

            // supplier

            Collection apSupplier1 = apSupplierHome.findBySplCoaGlPayableAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection apSupplier2 = apSupplierHome.findBySplCoaGlExpenseAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            Collection adBranchSuppliers1 = adBranchSupplierHome.findByBsplGlCoaPayableAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBranchSuppliers2 = adBranchSupplierHome.findByBsplGlCoaExpenseAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            // customer

            Collection arCustomer1 = arCustomerHome.findByCstGlCoaReceivableAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection arCustomer2 = arCustomerHome.findByCstGlCoaRevenueAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            Collection adBranchCustomers1 = adBranchCustomerHome.findByBcstGlCoaReceivableAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBranchCustomers2 = adBranchCustomerHome.findByBcstGlCoaRevenueAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            // inventory item location
            Collection invItemLocation1 = invItemLocationHome.findByIlGlCoaSalesAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection invItemLocation2 = invItemLocationHome.findByIlGlCoaInventoryAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection invItemLocation3 = invItemLocationHome.findByIlGlCoaCostOfSalesAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection invItemLocation4 = invItemLocationHome.findByIlGlCoaWipAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection invItemLocation5 = invItemLocationHome.findByIlGlCoaAccruedInventoryAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            Collection adBranchItemLocations1 = adBranchItemLocationHome.findByBilGlCoaSalesAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBranchItemLocations2 = adBranchItemLocationHome.findByBilGlCoaInventoryAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBranchItemLocations3 = adBranchItemLocationHome.findByBilGlCoaCostOfSalesAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBranchItemLocations4 = adBranchItemLocationHome.findByBilGlCoaWipAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);
            Collection adBranchItemLocations5 = adBranchItemLocationHome.findByBilGlCoaAccruedInventoryAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            // branch standard memo lines
            Collection adBranchStandardMemoLines1 = adBranchStandardMemoLineHome.findByBSMLGlAccount(glChartOfAccount.getCoaCode(), AD_CMPNY);

            return !glChartOfAccount.getGlSuspenseAccounts().isEmpty() || !glChartOfAccount.getGlRecurringJournalLines().isEmpty()
                    || !glChartOfAccount.getGlJournalLines().isEmpty() || !glChartOfAccount.getApDistributionRecords().isEmpty()
                    || !glChartOfAccount.getApTaxCodes().isEmpty() || !glChartOfAccount.getApWithholdingTaxCodes().isEmpty()
                    || !glChartOfAccount.getArTaxCodes().isEmpty() || !glChartOfAccount.getArWithholdingTaxCodes().isEmpty()
                    || !glChartOfAccount.getArDistributionRecords().isEmpty() || !glChartOfAccount.getArStandardMemoLines().isEmpty()
                    || !glChartOfAccount.getAdApprovalCoaLines().isEmpty() || !glChartOfAccount.getCmDistributionRecords().isEmpty()
                    || !glChartOfAccount.getGlBudgetAmountCoas().isEmpty() || !glChartOfAccount.getInvAdjustments().isEmpty()
                    || !glChartOfAccount.getInvDistributionRecords().isEmpty() || !glChartOfAccount.getAdPaymentTerms().isEmpty()
                    || !apSupplierClasses1.isEmpty() || !apSupplierClasses2.isEmpty() || !arCustomerClasses1.isEmpty()
                    || !arCustomerClasses2.isEmpty() || !arCustomerClasses3.isEmpty() || !adBankAccount1.isEmpty()
                    || !adBankAccount2.isEmpty() || !adBankAccount3.isEmpty() || !adBankAccount4.isEmpty() || !adBankAccount5.isEmpty()
                    || !adBankAccount6.isEmpty() || !adBankAccount7.isEmpty() || !adBankAccount8.isEmpty() || !adBankAccount9.isEmpty()
                    || !adBankAccount10.isEmpty() || !apSupplier1.isEmpty() || !apSupplier2.isEmpty() || !arCustomer1.isEmpty()
                    || !arCustomer2.isEmpty() || !invItemLocation1.isEmpty() || !invItemLocation2.isEmpty() || !invItemLocation3.isEmpty()
                    || !invItemLocation4.isEmpty() || !invItemLocation5.isEmpty()
                    || !adBranchSuppliers1.isEmpty() || !adBranchSuppliers2.isEmpty() || !adBranchCustomers1.isEmpty() || !adBranchCustomers2.isEmpty()
                    || !adBranchItemLocations1.isEmpty() || !adBranchItemLocations2.isEmpty() || !adBranchItemLocations3.isEmpty() || !adBranchItemLocations4.isEmpty()
                    || !adBranchItemLocations5.isEmpty() || !adBranchStandardMemoLines1.isEmpty();

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    private boolean hasRelation(LocalAdBranchCoa adBranchCoa, Integer AD_CMPNY) {

        Debug.print("GlChartOfAccountControllerBean hasRelation");

        try {

            // check if coa has existing unposted transactions

            Collection glJournalLines = glJournalLineHome.findUnpostedJrByJlCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);

            Collection apDistributionRecords1 = apDistributionRecordHome.findUnpostedVouByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection apDistributionRecords2 = apDistributionRecordHome.findUnpostedChkByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection apDistributionRecords3 = apDistributionRecordHome.findUnpostedPoByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);

            Collection arDistributionRercords1 = arDistributionRecordHome.findUnpostedInvByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection arDistributionRercords2 = arDistributionRecordHome.findUnpostedRctByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);

            Collection cmDistributionRercords1 = cmDistributionRecordHome.findUnpostedAdjByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection cmDistributionRercords2 = cmDistributionRecordHome.findUnpostedFtByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);

            Collection invAdjustments = invAdjustmentHome.findUnpostedAdjByAdjCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords1 = invDistributionRecordHome.findUnpostedAdjByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords2 = invDistributionRecordHome.findUnpostedBuaByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords3 = invDistributionRecordHome.findUnpostedSiByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords4 = invDistributionRecordHome.findUnpostedAtrByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords5 = invDistributionRecordHome.findUnpostedStByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords6 = invDistributionRecordHome.findUnpostedBstByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);
            Collection invDistributionRecords7 = invDistributionRecordHome.findUnpostedOhByDrCoaAccountNumberAndBrCode(adBranchCoa.getGlChartOfAccount().getCoaCode(), adBranchCoa.getAdBranch().getBrCode(), AD_CMPNY);

            return !glJournalLines.isEmpty() || !apDistributionRecords1.isEmpty() || !apDistributionRecords2.isEmpty() || !apDistributionRecords3.isEmpty() || !arDistributionRercords1.isEmpty() || !arDistributionRercords2.isEmpty() || !cmDistributionRercords1.isEmpty() || !cmDistributionRercords2.isEmpty() || !invAdjustments.isEmpty() || !invDistributionRecords1.isEmpty() || !invDistributionRecords2.isEmpty() || !invDistributionRecords3.isEmpty() || !invDistributionRecords4.isEmpty() || !invDistributionRecords5.isEmpty() || !invDistributionRecords6.isEmpty() || !invDistributionRecords7.isEmpty();

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

}