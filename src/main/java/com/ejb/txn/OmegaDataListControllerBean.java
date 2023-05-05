package com.ejb.txn;

import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gen.LocalGenQualifierHome;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.entities.ar.*;
import com.ejb.entities.gen.LocalGenQualifier;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArSalespersonDetails;
import com.util.gen.GenModSegmentDetails;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.*;

@Stateless(name = "OmegaDataListControllerEJB")
public class OmegaDataListControllerBean extends EJBContextClass implements OmegaDataListController {

    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalArInvoiceBatchHome arInvoiceBatchHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGenQualifierHome genQualifierHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;

    @Override
    public List<GlModFunctionalCurrencyDetails> getGlFcAllWithDefault(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getGlFcAllWithDefault");
        Collection<LocalGlFunctionalCurrency> glFunctionalCurrencies;
        LocalAdCompany adCompany;
        List<GlModFunctionalCurrencyDetails> list = new ArrayList<>();

        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        for (LocalGlFunctionalCurrency functionalCurrency : glFunctionalCurrencies) {
            GlModFunctionalCurrencyDetails details = new GlModFunctionalCurrencyDetails(functionalCurrency.getFcCode(), functionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(functionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);
            list.add(details);
        }

        return list;
    }

    public ArrayList getInvUomByIiName(String itemName, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getInvUomByIiName");

        ArrayList list = new ArrayList();

        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(itemName, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName()))details.setDefault(true);

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    @Override
    public List<String> getAdPytAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getAdPytAll");
        Collection<LocalAdPaymentTerm> paymentTerms;
        List<String> list = new ArrayList<>();
        try {
            paymentTerms = adPaymentTermHome.findEnabledPytAll(companyCode);
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        for (LocalAdPaymentTerm paymentTerm : paymentTerms) {
            list.add(paymentTerm.getPytName());
        }
        return list;
    }

    @Override
    public List<String> getArTcAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArTcAll");
        Collection<LocalArTaxCode> taxCodes;
        List<String> list = new ArrayList<>();
        try {
            taxCodes = arTaxCodeHome.findEnabledTcAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalArTaxCode taxCode : taxCodes) {
            list.add(taxCode.getTcName());
        }
        return list;
    }

    @Override
    public List<String> getApTcAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getApTcAll");
        Collection<LocalApTaxCode> taxCodes;
        List<String> list = new ArrayList<>();
        try {
            taxCodes = apTaxCodeHome.findEnabledTcAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalApTaxCode taxCode : taxCodes) {
            list.add(taxCode.getTcName());
        }
        return list;
    }


    @Override
    public List<String> getArWtcAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArWtcAll");
        Collection<LocalArWithholdingTaxCode> wtCodes;
        List<String> list = new ArrayList<>();
        try {
            wtCodes = arWithholdingTaxCodeHome.findEnabledWtcAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalArWithholdingTaxCode wtCode : wtCodes) {
            list.add(wtCode.getWtcName());
        }
        return list;
    }

    @Override
    public List<String> getApWtcAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getApWtcAll");
        Collection<LocalApWithholdingTaxCode> wtCodes;
        List<String> list = new ArrayList<>();
        try {
            wtCodes = apWithholdingTaxCodeHome.findEnabledWtcAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalApWithholdingTaxCode wtCode : wtCodes) {
            list.add(wtCode.getWtcName());
        }
        return list;
    }

    @Override
    public List<String> getAdUsrAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getAdUsrAll");
        Collection<LocalAdUser> users;
        List<String> list = new ArrayList<>();
        try {
            users = adUserHome.findUsrAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        for (LocalAdUser user : users) {
            list.add(user.getUsrName());
        }
        return list;
    }

    @Override
    public List<ArSalespersonDetails> getArSlpAllByBrCode(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArSlpAllByBrCode");
        Collection<LocalArSalesperson> salesPersons;
        List<ArSalespersonDetails> list = new ArrayList<>();
        try {
            salesPersons = arSalespersonHome.findSlpByBrCode(branchCode, companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalArSalesperson salesperson : salesPersons) {
            ArSalespersonDetails details = new ArSalespersonDetails();
            details.setSlpSalespersonCode(salesperson.getSlpSalespersonCode());
            details.setSlpName(salesperson.getSlpName());
            list.add(details);
        }
        return list;
    }

    @Override
    public List<String> getLookupValuesByType(Integer companyCode, String lookupType) {

        Debug.print("OmegaDataListControllerBean getLookupValuesByType : " + lookupType);
        Collection<LocalAdLookUpValue> lookUpValues;
        List<String> list = new ArrayList<>();
        try {
            lookUpValues = adLookUpValueHome.findByLuName(getLookupName(lookupType).toUpperCase(), companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalAdLookUpValue lookUpValue : lookUpValues) {
            list.add(lookUpValue.getLvName());
        }
        return list;
    }

    private String getLookupName(String lookupType) {

        if (lookupType.equals("ITEM")) {
            return "ARINVOICEITEMPRINTTYPE";
        } else if (lookupType.equals("MEMOLINE")) {
            return "ARINVOICEMEMOLINEPRINTTYPE";
        } else if (lookupType.equals("SOMATCHED")) {
            return "ARINVOICESOMATCHEDPRINTTYPE";
        } else {
            return lookupType;
        }
    }

    @Override
    public List<String> getArSmlAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArSmlAll");
        Collection<LocalArStandardMemoLine> memoLines;
        List<String> list = new ArrayList<>();
        try {
            memoLines = arStandardMemoLineHome.findEnabledSmlAll(branchCode, companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalArStandardMemoLine memoLine : memoLines) {
            list.add(memoLine.getSmlName());
        }
        return list;
    }

    @Override
    public List<String> getArOpenIbAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArOpenIbAll");
        Collection<LocalArInvoiceBatch> invoiceBatches;
        List<String> list = new ArrayList<>();
        try {
            invoiceBatches = arInvoiceBatchHome.findOpenIbByIbType("INVOICE", branchCode, companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalArInvoiceBatch invoiceBatch : invoiceBatches) {
            list.add(invoiceBatch.getIbName());
        }
        return list;
    }

    @Override
    public List<String> getInvLocAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getInvLocAll");
        Collection<LocalInvLocation> invLocations;
        List<String> list = new ArrayList<>();
        try {
            invLocations = invLocationHome.findLocAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalInvLocation location : invLocations) {
            list.add(location.getLocName());
        }
        return list;
    }

    @Override
    public List<String> getAdBrNameAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getAdBrNameAll");
        Collection<LocalAdBranch> branches;
        List<String> list = new ArrayList<>();
        try {
            branches = adBranchHome.findBrAll(companyCode);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalAdBranch branch : branches) {
            list.add(branch.getBrName());
        }
        return list;
    }

    @Override
    public List<String> getAdBrNameAll(Integer companyCode, String companyShortName) {

        Debug.print("OmegaDataListControllerBean getAdBrNameAll");
        Collection<LocalAdBranch> branches;
        List<String> list = new ArrayList<>();
        try {
            branches = adBranchHome.findBrAll(companyCode, companyShortName);
        } catch (FinderException ex) {
            return Collections.emptyList();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        for (LocalAdBranch branch : branches) {
            list.add(branch.getBrName());
        }
        return list;
    }

    @Override
    public List<GlModAccountingCalendarValueDetails> getGlReportableAcvAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getGlReportableAcvAll");
        List<GlModAccountingCalendarValueDetails> list = new ArrayList();

        try {
            Collection glSetOfBooks = glSetOfBookHome.findSobAll(companyCode);
            for (Object setOfBook : glSetOfBooks) {
                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;
                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findReportableAcvByAcCodeAndAcvStatus(glSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', 'C', 'P', companyCode);
                for (Object accountingCalendarValue : glAccountingCalendarValues) {
                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlModAccountingCalendarValueDetails mdetails = new GlModAccountingCalendarValueDetails();
                    mdetails.setAcvPeriodPrefix(glAccountingCalendarValue.getAcvPeriodPrefix());

                    // get year
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glAccountingCalendarValue.getAcvDateTo());
                    mdetails.setAcvYear(gc.get(Calendar.YEAR));

                    // is current
                    gc = EJBCommon.getGcCurrentDateWoTime();
                    if ((glAccountingCalendarValue.getAcvDateFrom().before(gc.getTime()) || glAccountingCalendarValue.getAcvDateFrom().equals(gc.getTime())) && (glAccountingCalendarValue.getAcvDateTo().after(gc.getTime()) || glAccountingCalendarValue.getAcvDateTo().equals(gc.getTime()))) {
                        mdetails.setAcvCurrent(true);
                    }
                    list.add(mdetails);
                }
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<GenModSegmentDetails> getGenSgAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getGenSgAll");
        List<GenModSegmentDetails> list = new ArrayList<>();
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Collection genSegments = genSegmentHome.findByFlCode(adCompany.getGenField().getFlCode(), companyCode);
            for (Object segment : genSegments) {
                LocalGenSegment genSegment = (LocalGenSegment) segment;
                GenModSegmentDetails mdetails = new GenModSegmentDetails();
                mdetails.setSgMaxSize(genSegment.getSgMaxSize());
                mdetails.setSgFlSegmentSeparator(genSegment.getGenField().getFlSegmentSeparator());
                list.add(mdetails);
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<AdBranchDetails> getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("OmegaDataListControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;
        List<AdBranchDetails> list = new ArrayList<>();
        try {
            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(responsibilityCode, companyCode);
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

    @Override
    public List<String> getApSplAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getApSplAll");
        List<String> list = new ArrayList<>();
        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(branchCode, companyCode);
            Iterator i = apSuppliers.iterator();
            while (i.hasNext()) {
                LocalApSupplier apSupplier = (LocalApSupplier) i.next();
                list.add(apSupplier.getSplSupplierCode());
            }
            return list;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getGenVsAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getGenVsAll");
        List<String> list = new ArrayList();
        Collection genValueSets;

        try {
            genValueSets = genValueSetHome.findVsAll(companyCode);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        Iterator i = genValueSets.iterator();
        while (i.hasNext()) {
            LocalGenValueSet genValueSet = (LocalGenValueSet) i.next();
            list.add(genValueSet.getVsName());
        }
        return list;
    }

    @Override
    public List<String> getGenQlfrAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("OmegaDataListControllerBean getGenQlfrAll");
        Collection genQualifiers = null;
        LocalGenQualifier genQualifier;
        List<String> list = new ArrayList();
        try {
            genQualifiers = genQualifierHome.findQlfrAll(companyCode);
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (genQualifiers.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }

        Iterator i = genQualifiers.iterator();
        while (i.hasNext()) {
            genQualifier = (LocalGenQualifier) i.next();
            list.add(genQualifier.getQlAccountType());
        }
        return list;
    }

    @Override
    public ArrayList getGlJsAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getGlJsAll");
        ArrayList list = new ArrayList();
        try {
            Collection glJournalSources = glJournalSourceHome.findJsAll(companyCode);
            Iterator i = glJournalSources.iterator();
            while (i.hasNext()) {
                LocalGlJournalSource glJournalSource = (LocalGlJournalSource) i.next();
                list.add(glJournalSource.getJsName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getApOpenVbByType(int typeIndex, String departmentName, Integer branchCode, Integer companyCode, boolean isDepartment) {

        Debug.print("OmegaDataListControllerBean getApOpenVbByType");
        List<String> list = new ArrayList<>();
        String[] voucherType = {"VOUCHER", "DEBIT MEMO"};
        try {
            Collection apVoucherBatches;
            if (!isDepartment) {
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbType(voucherType[typeIndex], branchCode, companyCode);
            } else {
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbTypeDepartment(voucherType[typeIndex], departmentName, branchCode, companyCode);
            }
            for (Object voucherBatch : apVoucherBatches) {
                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;
                list.add(apVoucherBatch.getVbName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getGlFcAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getGlFcAll");
        List<String> list = new ArrayList<>();
        try {
            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(companyCode);
            for (Object functionalCurrency : glFunctionalCurrencies) {
                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;
                list.add(glFunctionalCurrency.getFcName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getApOpenVbAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getApOpenVbAll");
        List<String> list = new ArrayList<>();
        try {
            Collection apVoucherBatches = apVoucherBatchHome.findOpenVbAll(branchCode, companyCode);
            for (Object voucherBatch : apVoucherBatches) {
                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;
                list.add(apVoucherBatch.getVbName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public List<String> getArCstAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArCstAll");
        List<String> list = new ArrayList<>();
        try {
            Collection arCustomers = arCustomerHome.findEnabledCstAll(branchCode, companyCode);
            for (Object customer : arCustomers) {
                LocalArCustomer arCustomer = (LocalArCustomer) customer;
                list.add(arCustomer.getCstCustomerCode());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getAdBaAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getAdBaAll");
        List<String> list = new ArrayList<>();
        try {
            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(branchCode, companyCode);
            for (Object bankAccount : adBankAccounts) {
                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;
                list.add(adBankAccount.getBaName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getArOpenRbAll(Integer branchCode, Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArOpenRbAll");
        ArrayList list = new ArrayList();
        try {
            Collection arReceiptBatches = arReceiptBatchHome.findOpenRbAll(branchCode, companyCode);
            for (Object receiptBatch : arReceiptBatches) {
                LocalArReceiptBatch arReceiptBatch = (LocalArReceiptBatch) receiptBatch;
                list.add(arReceiptBatch.getRbName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getArCtAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArCtAll");
        LocalArCustomerType arCustomerType;
        List<String> list = new ArrayList<>();
        try {
            Collection arCustomerTypes = arCustomerTypeHome.findEnabledCtAll(companyCode);
            for (Object customerType : arCustomerTypes) {
                arCustomerType = (LocalArCustomerType) customerType;
                list.add(arCustomerType.getCtName());
            }
            return list;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getArSlpAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArSlpAll");
        List<String> list = new ArrayList<>();
        try {
            Collection arSalespersons = arSalespersonHome.findSlpAll(companyCode);
            for (Object salesperson : arSalespersons) {
                LocalArSalesperson arSalesperson = (LocalArSalesperson) salesperson;
                list.add(arSalesperson.getSlpSalespersonCode());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getArCcAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getArCcAll");
        LocalArCustomerClass arCustomerClass;
        List<String> list = new ArrayList<>();
        try {

            Collection arCustomerClasses = arCustomerClassHome.findEnabledCcAll(companyCode);
            for (Object customerClass : arCustomerClasses) {
                arCustomerClass = (LocalArCustomerClass) customerClass;
                list.add(arCustomerClass.getCcName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<ArSalespersonDetails> getArSlpDetailsAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("OmegaDataListControllerBean getArSlpAll");
        List<ArSalespersonDetails> list = new ArrayList<>();

        try {

            Collection arSalespersons = arSalespersonHome.findSlpAll(companyCode);

            if (arSalespersons.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object salesperson : arSalespersons) {

                LocalArSalesperson arSalesperson = (LocalArSalesperson) salesperson;

                ArSalespersonDetails details = new ArSalespersonDetails();

                details.setSlpCode(arSalesperson.getSlpCode());
                details.setSlpSalespersonCode(arSalesperson.getSlpSalespersonCode());
                details.setSlpName(arSalesperson.getSlpName());
                details.setSlpEntryDate(arSalesperson.getSlpEntryDate());
                details.setSlpAddress(arSalesperson.getSlpAddress());
                details.setSlpPhone(arSalesperson.getSlpPhone());
                details.setSlpMobilePhone(arSalesperson.getSlpMobilePhone());
                details.setSlpEmail(arSalesperson.getSlpEmail());
                details.setSlpAdCompany(companyCode);
                list.add(details);
            }
            return list;
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getInvUomAll(Integer companyCode) {

        Debug.print("OmegaDataListControllerBean getInvUomAll");
        LocalInvUnitOfMeasure invUnitOfMeasure;
        List<String> list = new ArrayList<>();
        try {

            Collection invUnitOfMeasures = invUnitOfMeasureHome.findEnabledUomAll(companyCode);
            for (Object unitOfMeasure : invUnitOfMeasures) {
                invUnitOfMeasure = (LocalInvUnitOfMeasure) unitOfMeasure;
                list.add(invUnitOfMeasure.getUomName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public List<String> getApSplAllOrderBySupplierCode(Integer AD_CMPNY) {

        Debug.print("OmegaDataListControllerBean getApSplAllOrderBySupplierCode");
        List<String> list = new ArrayList<>();
        try {
            Collection apSuppliers = apSupplierHome.findEnabledSplAllOrderBySplSupplierCode(AD_CMPNY);
            for (Object supplier : apSuppliers) {
                LocalApSupplier apSupplier = (LocalApSupplier) supplier;
                list.add(apSupplier.getSplSupplierCode());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public List<String> getArCstAllOrderByCstName(Integer AD_CMPNY) {

        Debug.print("OmegaDataListControllerBean getArCstAllOrderByCstName");
        List<String> list = new ArrayList<>();
        try {
            Collection arCustomers = arCustomerHome.findEnabledCstAllOrderByCstName(AD_CMPNY);
            for (Object customer : arCustomers) {
                LocalArCustomer arCustomer = (LocalArCustomer) customer;
                list.add(arCustomer.getCstCustomerCode());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

}