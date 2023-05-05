/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlCoaGeneratorControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.dao.gen.LocalGenFieldHome;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetHome;
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
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.gen.GenModValueSetDetails;
import com.util.gen.GenValueSetValueDetails;
import com.util.gl.GlChartOfAccountDetails;
import com.util.mod.gl.GlModChartOfAccountDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "GlCoaGeneratorControllerEJB")
public class GlCoaGeneratorControllerBean extends EJBContextClass implements GlCoaGeneratorController {

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
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdResponsibilityHome adResponsibilityHome;
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
    private LocalGenValueSetHome genValueSetHome;
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

    public ArrayList getGenVsAll(Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean getGenVsAll");

        ArrayList list = new ArrayList();

        try {

            Collection genValueSets = genValueSetHome.findVsAll(AD_CMPNY);

            for (Object valueSet : genValueSets) {

                LocalGenValueSet genValueSet = (LocalGenValueSet) valueSet;
                LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSet.getVsCode(), AD_CMPNY);

                GenModValueSetDetails mdetails = new GenModValueSetDetails();
                mdetails.setVsName(genValueSet.getVsName());
                mdetails.setVsSegmentNumber(genSegment.getSgSegmentNumber());

                if (genSegment.getSgSegmentType() == 'N') {

                    mdetails.setVsSgNatural(EJBCommon.TRUE);

                } else {

                    mdetails.setVsSgNatural(EJBCommon.FALSE);
                }

                list.add(mdetails);
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenVsvAllByVsName(String VS_NM, Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean getGenVsvByVsName");

        ArrayList list = new ArrayList();

        try {

            Collection genValueSetValues = genValueSetValueHome.findByVsName(VS_NM, AD_CMPNY);

            for (Object valueSetValue : genValueSetValues) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) valueSetValue;

                if (genValueSetValue.getVsvParent() == EJBCommon.FALSE) {

                    GenValueSetValueDetails details = new GenValueSetValueDetails();
                    details.setVsvValue(genValueSetValue.getVsvValue());
                    details.setVsvDescription(genValueSetValue.getVsvDescription());

                    list.add(details);
                }
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public char getGenFlSgmntSeparator(Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean getGenFlSgmntSeparator");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGenField().getFlSegmentSeparator();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void generateGlCoa(
            GlChartOfAccountDetails details, Integer RS_CODE, ArrayList branchList,
            String GL_FUNCTIONAL_CURRENCY, Integer AD_CMPNY)
            throws GlCOAAccountNumberAlreadyAssignedException,
            GlFCNoFunctionalCurrencyFoundException, GlFCFunctionalCurrencyAlreadyAssignedException {

        Debug.print("GlCoaGeneratorControllerBean generateGlCoa");

        // check if coa is invalid
        LocalGenField genField = null;
        ArrayList genValueSetValueList = new ArrayList();

        LocalAdBranchCoa adBranchCoa = null;
        LocalAdBranch adBranch = null;
        LocalAdCompany adCompany = null;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            genField = adCompany.getGenField();

            genValueSetValueList = this.getGenValueSetValueWithValue(genField, details.getCoaAccountNumber(), AD_CMPNY);

        }
        catch (Exception ex) {

            return;

        }

        if (genValueSetValueList.size() != genField.getFlNumberOfSegment()) {
            return;
        }

        LocalGlChartOfAccount glChartOfAccount = null;
        Collection adBranchCoas = null;
        byte glCoaChanged = 0;

        try {

            glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getCoaAccountNumber(), AD_CMPNY);

            // check if coa already has existing transactions

            if (hasRelation(glChartOfAccount, AD_CMPNY)) {

                if (!glChartOfAccount.getCoaAccountNumber().equals(details.getCoaAccountNumber()) || glChartOfAccount.getCoaDateFrom().getTime() != details.getCoaDateFrom().getTime()) {

                    throw new GlCOAAccountNumberAlreadyAssignedException("Account, Effective Date From or Currency cannot be updated");
                }

                if (!glChartOfAccount.getCoaAccountType().equalsIgnoreCase("REVENUE") && !glChartOfAccount.getCoaAccountType().equalsIgnoreCase("EXPENSE")) {

                    if ((glChartOfAccount.getGlFunctionalCurrency() != null && !glChartOfAccount.getGlFunctionalCurrency().getFcName().equals(GL_FUNCTIONAL_CURRENCY)) || (glChartOfAccount.getGlFunctionalCurrency() == null && !(GL_FUNCTIONAL_CURRENCY.equals("") || GL_FUNCTIONAL_CURRENCY.length() < 0 || GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND")))) {

                        throw new GlCOAAccountNumberAlreadyAssignedException("Account, Effective Date From or Currency cannot be updated");
                    }
                }
            }

            // find existing COA Branch Mapping
            try {

                adBranchCoas = adBranchCoaHome.findBcoaByCoaCode(glChartOfAccount.getCoaCode(), AD_CMPNY);

            }
            catch (FinderException ex) {

            }

            if (!(glChartOfAccount.getCoaDateFrom().equals(details.getCoaDateFrom())) || glChartOfAccount.getCoaEnable() != details.getCoaEnable() || !(adBranchCoas.isEmpty() && branchList.isEmpty())) {

                glCoaChanged = 1;
            }

        }
        catch (FinderException ex) {

        }
        catch (GlCOAAccountNumberAlreadyAssignedException e) {

            getSessionContext().setRollbackOnly();
            throw e;
        }

        // get description and account type
        GlModChartOfAccountDetails mdetails = null;

        try {

            mdetails = this.getGlCoaDescAndAccountTypeByCoaAccountNumber(details.getCoaAccountNumber(), AD_CMPNY);

        }
        catch (Exception ex) {

            return;

        }

        // create coa OR update coa

        if (glCoaChanged == 0) {

            try {

                glChartOfAccount = glChartOfAccountHome.create(details.getCoaAccountNumber(), mdetails.getMcoaDescription(), mdetails.getMcoaAccountType(), mdetails.getMcoaTaxType(), details.getCoaCitCategory(), details.getCoaSawCategory(), details.getCoaIitCategory(), details.getCoaDateFrom(), details.getCoaDateTo(), null, null, null, null, null, null, null, null, null, null, details.getCoaEnable(), EJBCommon.TRUE, AD_CMPNY);

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

        } else if (glCoaChanged == 1) {

            // update COA
            glChartOfAccount.setCoaDateFrom(details.getCoaDateFrom());
            glChartOfAccount.setCoaDateTo(details.getCoaDateTo());
            glChartOfAccount.setCoaEnable(details.getCoaEnable());
            glChartOfAccount.setCoaForRevaluation(details.getCoaForRevaluation());
        }

        LocalAdResponsibility adResponsibility = null;

        // get responsibility
        try {

            adResponsibility = adResponsibilityHome.findByPrimaryKey(RS_CODE);

        }
        catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {

            adBranchCoas = adBranchCoaHome.findBcoaByCoaCodeAndRsName(glChartOfAccount.getCoaCode(), adResponsibility.getRsName(), AD_CMPNY);

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
                    if (hasRelation(adBranchCoa, AD_CMPNY)) {
                        throw new GlCOAAccountNumberAlreadyAssignedException("Branch mapping cannot be deleted for " + adBranchCoa.getAdBranch().getBrName());
                    }
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
                // adBranch.addAdBranchCoa(adBranchCoa);
                adBranchCoa.setAdBranch(adBranch);
            }

            // mapping to functional currency

            if (GL_FUNCTIONAL_CURRENCY.length() > 0 && GL_FUNCTIONAL_CURRENCY != null && (!GL_FUNCTIONAL_CURRENCY.equalsIgnoreCase("NO RECORD FOUND"))) {

                LocalGlFunctionalCurrency glFunctionalCurrency = null;

                try {

                    glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(GL_FUNCTIONAL_CURRENCY, AD_CMPNY);

                }
                catch (FinderException ex) {

                    throw new GlFCNoFunctionalCurrencyFoundException();
                }

                // check if FC selected is default company FC
                if (Objects.equals(adCompany.getGlFunctionalCurrency().getFcCode(), glFunctionalCurrency.getFcCode())) {
                    throw new GlFCFunctionalCurrencyAlreadyAssignedException();
                }

                if (!glChartOfAccount.getCoaAccountType().equalsIgnoreCase("REVENUE") && !glChartOfAccount.getCoaAccountType().equalsIgnoreCase("EXPENSE"))
                // glFunctionalCurrency.addGlChartOfAccount(glChartOfAccount);
                {
                    glChartOfAccount.setGlFunctionalCurrency(glFunctionalCurrency);
                }
            }

        }
        catch (GlCOAAccountNumberAlreadyAssignedException | GlFCFunctionalCurrencyAlreadyAssignedException |
               GlFCNoFunctionalCurrencyFoundException e) {

            getSessionContext().setRollbackOnly();
            throw e;

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlCoa(String COA_ACCT_NMBR, Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException {

        Debug.print("GlCoaGeneratorControllerBean deleteGlCoa");

        LocalGlChartOfAccount glChartOfAccount;

        try {

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(COA_ACCT_NMBR, AD_CMPNY);

            }
            catch (FinderException ex) {

                return;
            }

            if (hasRelation(glChartOfAccount, AD_CMPNY)) {

                throw new GlobalRecordAlreadyAssignedException("Account cannot be deleted");

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
        catch (GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenNaturalAccountAll(String ACCT_FRM, String ACCT_TO, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("GlCoaGeneratorControllerBean getGenNaturalAccountAll");

        ArrayList list = new ArrayList();


        try {

            LocalGenValueSet genValueSet = genValueSetHome.findBySegmentType('N', AD_CMPNY);
            LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSet.getVsCode(), AD_CMPNY);

            LocalGenValueSetValue genVsvFrom = null;
            LocalGenValueSetValue genVsvTo = null;

            try {

                genVsvFrom = genValueSetValueHome.findByVsCodeAndVsvValue(genValueSet.getVsCode(), ACCT_FRM, AD_CMPNY);
                genVsvTo = genValueSetValueHome.findByVsCodeAndVsvValue(genValueSet.getVsCode(), ACCT_TO, AD_CMPNY);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            Collection genValueSetValues = genValueSetValueHome.findByVsvValueRangeAndVsName(genVsvFrom.getVsvValue(), genVsvTo.getVsvValue(), genValueSet.getVsName(), AD_CMPNY);

            for (Object valueSetValue : genValueSetValues) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) valueSetValue;

                GenValueSetValueDetails details = new GenValueSetValueDetails();
                details.setVsvValue(genValueSetValue.getVsvValue());
                details.setVsvDescription(genValueSetValue.getVsvDescription());
                details.setVsvSegmentNumber(genSegment.getSgSegmentNumber());

                list.add(details);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlCoaGeneratorControllerBean getAdBrResAll");


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

    public short getGenFlNumberOfSegment(Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean getGenFlSgmntSeparator");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGenField().getFlNumberOfSegment();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean getGlFcAllWithDefault");

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

    // private methods

    private GlModChartOfAccountDetails getGlCoaDescAndAccountTypeByCoaAccountNumber(
            String COA_ACCNT_NMBR, Integer AD_CMPNY)
            throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberHasParentValueException {

        Debug.print("GlCoaGeneratorControllerBean getGlCoaDescAndAccountTypeByCoaAccountNumber");

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

    private ArrayList getGenValueSetValueWithValue(LocalGenField genField, String COA_ACCNT_NMBR, Integer AD_CMPNY)
            throws GlCOAAccountNumberIsInvalidException, GlCOAAccountNumberHasParentValueException {

        Debug.print("GlCoaGeneratorControllerBean getGenValueSetValueWithValue");

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

            LocalGenSegment genSegment = (LocalGenSegment) j.next();
            LocalGenValueSet genValueSet = genSegment.getGenValueSet();

            LocalGenValueSetValue genValueSetValue = null;

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

    private boolean hasRelation(LocalGlChartOfAccount glChartOfAccount, Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean hasRelation");

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
                    || !adBankAccount2.isEmpty() || !adBankAccount3.isEmpty() || !adBankAccount4.isEmpty()
                    || !adBankAccount5.isEmpty() || !adBankAccount6.isEmpty() || !adBankAccount7.isEmpty()
                    || !adBankAccount8.isEmpty() || !adBankAccount9.isEmpty() || !adBankAccount10.isEmpty()
                    || !apSupplier1.isEmpty() || !apSupplier2.isEmpty() || !arCustomer1.isEmpty() || !arCustomer2.isEmpty()
                    || !invItemLocation1.isEmpty() || !invItemLocation2.isEmpty() || !invItemLocation3.isEmpty()
                    || !invItemLocation4.isEmpty() || !invItemLocation5.isEmpty() || !adBranchSuppliers1.isEmpty()
                    || !adBranchSuppliers2.isEmpty() || !adBranchCustomers1.isEmpty() || !adBranchCustomers2.isEmpty()
                    || !adBranchItemLocations1.isEmpty() || !adBranchItemLocations2.isEmpty() || !adBranchItemLocations3.isEmpty()
                    || !adBranchItemLocations4.isEmpty() || !adBranchItemLocations5.isEmpty() || !adBranchStandardMemoLines1.isEmpty();

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    private boolean hasRelation(LocalAdBranchCoa adBranchCoa, Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean hasRelation");

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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("GlCoaGeneratorControllerBean ejbCreate");
    }

}