/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApSupplierEntryControllerBean
 * @created February 16, 2004, 10:44 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchSupplier;
import com.ejb.dao.ad.LocalAdBranchSupplierHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.dao.ad.LocalAdPaymentTermHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdResponsibility;
import com.ejb.dao.ad.LocalAdResponsibilityHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.exception.ap.ApSCCoaGlExpenseAccountNotFoundException;
import com.ejb.exception.ap.ApSCCoaGlPayableAccountNotFoundException;
import com.ejb.exception.global.GlobalNameAndAddressAlreadyExistsException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlInvestorAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.ejb.entities.inv.LocalInvLineItemTemplate;
import com.ejb.dao.inv.LocalInvLineItemTemplateHome;
import com.util.ad.AdBranchDetails;
import com.util.mod.ad.AdModBranchSupplierDetails;
import com.util.ad.AdResponsibilityDetails;
import com.util.mod.ap.ApModSupplierClassDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ap.ApModSupplierTypeDetails;
import com.util.ap.ApSupplierDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApSupplierEntryControllerEJB")
public class ApSupplierEntryControllerBean extends EJBContextClass implements ApSupplierEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private ILocalGlInvestorAccountBalanceHome glInvestorAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchSupplierHome adBranchSupplierHome;
    @EJB
    private LocalAdBranchSupplierHome adBrSplHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdResponsibilityHome adResHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;


    private void generateGlInvtrBalance(LocalApSupplier apSupplier, Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean generateGlInvtrBalance");

        try {

            // create balances to all existing customer

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glSetOfBook.getGlAccountingCalendar().getGlAccountingCalendarValues();

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    LocalGlInvestorAccountBalance glInvestorAccountBalance = null;
                    try {

                        glInvestorAccountBalance = glInvestorAccountBalanceHome.create(0d, 0d, (byte) 0, (byte) 0, 0d, 0d, 0d, 0d, 0d, 0d, AD_CMPNY);
                        glInvestorAccountBalance.setGlAccountingCalendarValue(glAccountingCalendarValue);

                        glInvestorAccountBalance.setApSupplier(apSupplier);
                    } catch (Exception ex) {

                        getSessionContext().setRollbackOnly();
                        throw new EJBException(ex.getMessage());
                    }
                }
            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApStAll");

        LocalApSupplierType apSupplierType = null;

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierTypes = apSupplierTypeHome.findEnabledStAll(AD_CMPNY);

            for (Object supplierType : apSupplierTypes) {

                apSupplierType = (LocalApSupplierType) supplierType;

                list.add(apSupplierType.getStName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArCstAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getArCstAll");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAll(AD_BRNCH, AD_CMPNY);

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                list.add(arCustomer.getCstCustomerCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApPytAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApPytAll");

        LocalAdPaymentTerm adPaymentTerm = null;

        ArrayList list = new ArrayList();

        try {

            Collection adPaymentTerms = adPaymentTermHome.findEnabledPytAll(AD_CMPNY);

            for (Object paymentTerm : adPaymentTerms) {

                adPaymentTerm = (LocalAdPaymentTerm) paymentTerm;

                list.add(adPaymentTerm.getPytName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApScAll");

        LocalApSupplierClass apSupplierClass = null;

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierClasses = apSupplierClassHome.findEnabledScAll(AD_CMPNY);

            for (Object supplierClass : apSupplierClasses) {

                apSupplierClass = (LocalApSupplierClass) supplierClass;

                list.add(apSupplierClass.getScName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getPrfApAutoGenerateSupplierCode(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getPrfApAutoGenerateSupplierCode");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApAutoGenerateSupplierCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getPrfApNextSupplierCode(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getPrfApNextSupplierCode");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfApNextSupplierCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveApSplEntry(ApModSupplierDetails details, String ST_NM, String PYT_NM, String SC_NM, String SPL_COA_GL_PYBL_ACCNT, String SPL_COA_GL_EXPNS_ACCNT, String BA_NM, String RS_NM, ArrayList branchList, String LIT_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalNameAndAddressAlreadyExistsException, ApSCCoaGlPayableAccountNotFoundException, ApSCCoaGlExpenseAccountNotFoundException {

        Debug.print("ApSupplierEntryControllerBean saveApSplEntry");

        LocalApSupplier apSupplier = null;
        LocalAdPreference adPreference = null;
        LocalAdBranch adBranch = null;
        LocalAdBranchSupplier adBranchSupplier = null;

        try {

            adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        try {

            LocalGlChartOfAccount glPayableChartOfAccount = null;
            LocalGlChartOfAccount glExpenseChartOfAccount = null;

            // autoGenerate
            if (adPreference.getPrfApAutoGenerateSupplierCode() == EJBCommon.TRUE && (details.getSplSupplierCode() == null || details.getSplSupplierCode().length() == 0)) {

                while (true) {

                    try {

                        apSupplierHome.findBySplSupplierCode(adPreference.getPrfApNextSupplierCode(), AD_CMPNY);
                        adPreference.setPrfApNextSupplierCode(EJBCommon.incrementStringNumber(adPreference.getPrfApNextSupplierCode()));

                    } catch (FinderException ex) {

                        details.setSplSupplierCode(adPreference.getPrfApNextSupplierCode());
                        adPreference.setPrfApNextSupplierCode(EJBCommon.incrementStringNumber(adPreference.getPrfApNextSupplierCode()));
                        break;
                    }
                }
            }

            // validate if supplier code already exists

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(details.getSplSupplierCode(), AD_CMPNY);

                if (details.getSplCode() == null || details.getSplCode() != null && !apSupplier.getSplCode().equals(details.getSplCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }
            Debug.print("---------------------------------->");
            // validate if supplier with this name and address already exists

            try {

                apSupplier = apSupplierHome.findBySplNameAndAddress(details.getSplName(), details.getSplAddress(), AD_CMPNY);

                if (apSupplier != null && !apSupplier.getSplCode().equals(details.getSplCode())) {

                    throw new GlobalNameAndAddressAlreadyExistsException();
                }

            } catch (GlobalNameAndAddressAlreadyExistsException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            try {

                glPayableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SPL_COA_GL_PYBL_ACCNT, AD_CMPNY);

            } catch (FinderException ex) {

                throw new ApSCCoaGlPayableAccountNotFoundException();
            }

            try {

                glExpenseChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SPL_COA_GL_EXPNS_ACCNT, AD_CMPNY);

            } catch (FinderException ex) {

                throw new ApSCCoaGlExpenseAccountNotFoundException();
            }

            // create new supplier

            if (details.getSplCode() == null) {
                Debug.print("------------------------------------------------>NEW SUPPLIER MODE");
                apSupplier = apSupplierHome.create(details.getSplSupplierCode(), details.getSplAccountNumber(), details.getSplName(), details.getSplAddress(), details.getSplCity(), details.getSplStateProvince(), details.getSplPostalCode(), details.getSplCountry(), details.getSplContact(), details.getSplPhone(), details.getSplFax(), details.getSplAlternatePhone(), details.getSplAlternateContact(), details.getSplEmail(), details.getSplTin(), glPayableChartOfAccount.getCoaCode(), glExpenseChartOfAccount.getCoaCode(), details.getSplEnable(), details.getSplRemarks(), AD_CMPNY);

                if (ST_NM != null && ST_NM.length() > 0) {

                    LocalApSupplierType apSupplierType = apSupplierTypeHome.findByStName(ST_NM, AD_CMPNY);
                    apSupplier.setApSupplierType(apSupplierType);
                }

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
                apSupplier.setAdPaymentTerm(adPaymentTerm);

                LocalApSupplierClass apSupplierClass = apSupplierClassHome.findByScName(SC_NM, AD_CMPNY);
                apSupplier.setApSupplierClass(apSupplierClass);

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, AD_CMPNY);
                apSupplier.setAdBankAccount(adBankAccount);

                // create new BranchSupplier

                for (Object o : branchList) {

                    AdModBranchSupplierDetails mdetails = (AdModBranchSupplierDetails) o;
                    try {
                        glPayableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getBSplPayableAccountNumber(), AD_CMPNY);
                    } catch (FinderException ex) {
                        throw new ApSCCoaGlPayableAccountNotFoundException();
                    }

                    try {
                        glExpenseChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getBSplExpenseAccountNumber(), AD_CMPNY);
                    } catch (FinderException ex) {
                        throw new ApSCCoaGlExpenseAccountNotFoundException();
                    }
                    adBranchSupplier = adBranchSupplierHome.create(glPayableChartOfAccount.getCoaCode(), glExpenseChartOfAccount.getCoaCode(), 'N', AD_CMPNY);
                    adBranchSupplier.setApSupplier(apSupplier);

                    adBranch = adBranchHome.findByPrimaryKey(mdetails.getBSplBranchCode());
                    adBranchSupplier.setAdBranch(adBranch);
                }

                if (LIT_NM != null && LIT_NM.length() > 0) {

                    LocalInvLineItemTemplate invLineItemTemplate = invLineItemTemplateHome.findByLitName(LIT_NM, AD_CMPNY);
                    apSupplier.setInvLineItemTemplate(invLineItemTemplate);
                }

                // generate GL Investor Account Balance default value
                if (apSupplier.getApSupplierClass().getScLedger() == EJBCommon.TRUE) {
                    this.generateGlInvtrBalance(apSupplier, AD_CMPNY);
                }

            } else {

                // update supplier
                apSupplier = apSupplierHome.findByPrimaryKey(details.getSplCode());

                apSupplier.setSplSupplierCode(details.getSplSupplierCode());
                apSupplier.setSplAccountNumber(details.getSplAccountNumber());
                apSupplier.setSplName(details.getSplName());
                apSupplier.setSplAddress(details.getSplAddress());
                apSupplier.setSplCity(details.getSplCity());
                apSupplier.setSplStateProvince(details.getSplStateProvince());
                apSupplier.setSplPostalCode(details.getSplPostalCode());
                apSupplier.setSplCountry(details.getSplCountry());
                apSupplier.setSplContact(details.getSplContact());
                apSupplier.setSplPhone(details.getSplPhone());
                apSupplier.setSplFax(details.getSplFax());
                apSupplier.setSplAlternatePhone(details.getSplAlternatePhone());
                apSupplier.setSplAlternateContact(details.getSplAlternateContact());
                apSupplier.setSplEmail(details.getSplEmail());
                apSupplier.setSplTin(details.getSplTin());
                apSupplier.setSplEnable(details.getSplEnable());
                apSupplier.setSplCoaGlPayableAccount(glPayableChartOfAccount.getCoaCode());
                apSupplier.setSplCoaGlExpenseAccount(glExpenseChartOfAccount.getCoaCode());
                apSupplier.setSplRemarks(details.getSplRemarks());

                if (ST_NM != null && ST_NM.length() > 0) {

                    LocalApSupplierType apSupplierType = apSupplierTypeHome.findByStName(ST_NM, AD_CMPNY);
                    apSupplier.setApSupplierType(apSupplierType);
                }

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
                apSupplier.setAdPaymentTerm(adPaymentTerm);

                LocalApSupplierClass apSupplierClass = apSupplierClassHome.findByScName(SC_NM, AD_CMPNY);
                apSupplier.setApSupplierClass(apSupplierClass);

                // generate GL Investor Account Balance default value
                if (apSupplier.getApSupplierClass().getScLedger() == EJBCommon.TRUE) {
                    if (apSupplier.getGlInvestorAccountBalances().size() == 0)
                        this.generateGlInvtrBalance(apSupplier, AD_CMPNY);
                }

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, AD_CMPNY);
                apSupplier.setAdBankAccount(adBankAccount);

                // remove all BranchSupplier
                Collection adBranchSuppliers = adBranchSupplierHome.findBSplBySplCodeAndRsName(apSupplier.getSplCode(), RS_NM, AD_CMPNY);

                for (Object branchSupplier : adBranchSuppliers) {

                    adBranchSupplier = (LocalAdBranchSupplier) branchSupplier;
                    apSupplier.dropAdBranchSupplier(adBranchSupplier);

                    adBranch = adBranchHome.findByPrimaryKey(adBranchSupplier.getAdBranch().getBrCode());
                    adBranch.dropAdBranchSupplier(adBranchSupplier);
                    em.remove(adBranchSupplier);
                }

                // create new BranchSupplier

                for (Object o : branchList) {

                    AdModBranchSupplierDetails mdetails = (AdModBranchSupplierDetails) o;
                    try {
                        glPayableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getBSplPayableAccountNumber(), AD_CMPNY);
                    } catch (FinderException ex) {
                        throw new ApSCCoaGlPayableAccountNotFoundException();
                    }

                    try {
                        glExpenseChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getBSplExpenseAccountNumber(), AD_CMPNY);
                    } catch (FinderException ex) {
                        throw new ApSCCoaGlExpenseAccountNotFoundException();
                    }
                    adBranchSupplier = adBranchSupplierHome.create(glPayableChartOfAccount.getCoaCode(), glExpenseChartOfAccount.getCoaCode(), 'N', AD_CMPNY);
                    adBranchSupplier.setApSupplier(apSupplier);

                    adBranch = adBranchHome.findByPrimaryKey(mdetails.getBSplBranchCode());
                    adBranchSupplier.setAdBranch(adBranch);
                }

                LocalInvLineItemTemplate invLineItemTemplate = null;

                if (LIT_NM != null && LIT_NM.length() > 0 && !LIT_NM.equalsIgnoreCase("NO RECORD FOUND")) {

                    invLineItemTemplate = invLineItemTemplateHome.findByLitName(LIT_NM, AD_CMPNY);
                    apSupplier.setInvLineItemTemplate(invLineItemTemplate);

                } else {

                    if (apSupplier.getInvLineItemTemplate() != null) {

                        invLineItemTemplate = invLineItemTemplateHome.findByLitName(apSupplier.getInvLineItemTemplate().getLitName(), AD_CMPNY);
                        invLineItemTemplate.dropApSupplier(apSupplier);
                    }
                }
            }

        } catch (GlobalRecordAlreadyExistException | ApSCCoaGlExpenseAccountNotFoundException |
                 ApSCCoaGlPayableAccountNotFoundException | GlobalNameAndAddressAlreadyExistsException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplCode(Integer SPL_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApSupplierEntryControllerBean getApSplBySplCode");

        LocalApSupplier apSupplier = null;

        try {

            try {

                apSupplier = apSupplierHome.findByPrimaryKey(SPL_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails splDetails = new ApModSupplierDetails();
            splDetails.setSplCode(apSupplier.getSplCode());
            splDetails.setSplSupplierCode(apSupplier.getSplSupplierCode());
            splDetails.setSplAccountNumber(apSupplier.getSplAccountNumber());
            splDetails.setSplName(apSupplier.getSplName());
            splDetails.setSplAddress(apSupplier.getSplAddress());
            splDetails.setSplCity(apSupplier.getSplCity());
            splDetails.setSplStateProvince(apSupplier.getSplStateProvince());
            splDetails.setSplPostalCode(apSupplier.getSplPostalCode());
            splDetails.setSplCountry(apSupplier.getSplCountry());
            splDetails.setSplContact(apSupplier.getSplContact());
            splDetails.setSplPhone(apSupplier.getSplPhone());
            splDetails.setSplFax(apSupplier.getSplFax());
            splDetails.setSplAlternatePhone(apSupplier.getSplAlternatePhone());
            splDetails.setSplAlternateContact(apSupplier.getSplAlternateContact());
            splDetails.setSplEmail(apSupplier.getSplEmail());
            splDetails.setSplTin(apSupplier.getSplTin());
            splDetails.setSplEnable(apSupplier.getSplEnable());

            splDetails.setSplStName(apSupplier.getApSupplierType() != null ? apSupplier.getApSupplierType().getStName() : null);

            splDetails.setSplPytName(apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);

            splDetails.setSplScName(apSupplier.getApSupplierClass() != null ? apSupplier.getApSupplierClass().getScName() : null);

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplier.getSplCoaGlPayableAccount());

            splDetails.setSplCoaGlPayableAccountNumber(glChartOfAccount.getCoaAccountNumber());
            splDetails.setSplCoaGlPayableAccountDescription(glChartOfAccount.getCoaAccountDescription());

            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplier.getSplCoaGlExpenseAccount());

            splDetails.setSplCoaGlExpenseAccountNumber(glChartOfAccount.getCoaAccountNumber());
            splDetails.setSplCoaGlExpenseAccountDescription(glChartOfAccount.getCoaAccountDescription());

            splDetails.setSplBaName(apSupplier.getAdBankAccount().getBaName());

            splDetails.setSplLitName(apSupplier.getInvLineItemTemplate() != null ? apSupplier.getInvLineItemTemplate().getLitName() : null);

            splDetails.setSplRemarks(apSupplier.getSplRemarks());

            return splDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_CMPNY);

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierClassDetails getApScByScName(String SC_NM, Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApScByScName");

        ArrayList list = new ArrayList();

        try {

            LocalApSupplierClass apSupplierClass = apSupplierClassHome.findByScName(SC_NM, AD_CMPNY);
            ApModSupplierClassDetails mdetails = new ApModSupplierClassDetails();

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplierClass.getScGlCoaPayableAccount());
            mdetails.setScCoaGlPayableAccountNumber(glChartOfAccount.getCoaAccountNumber());
            mdetails.setScCoaGlPayableAccountDescription(glChartOfAccount.getCoaAccountDescription());

            if (apSupplierClass.getScGlCoaExpenseAccount() != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(apSupplierClass.getScGlCoaExpenseAccount());
                mdetails.setScCoaGlExpenseAccountNumber(glChartOfAccount.getCoaAccountNumber());
                mdetails.setScCoaGlExpenseAccountDescription(glChartOfAccount.getCoaAccountDescription());
            }

            return mdetails;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierTypeDetails getApStByStName(String ST_NM, Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApScByScName");

        ArrayList list = new ArrayList();

        try {

            LocalApSupplierType apSupplierType = apSupplierTypeHome.findByStName(ST_NM, AD_CMPNY);
            ApModSupplierTypeDetails mdetails = new ApModSupplierTypeDetails();

            mdetails.setStBaName(apSupplierType.getAdBankAccount().getBaName());

            return mdetails;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApSupplierEntryControllerBean getAdBrAll");

        LocalAdBranch adBranch = null;

        Collection adBranches = null;

        ArrayList list = new ArrayList();

        try {

            adBranches = adBranchHome.findBrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranches.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        for (Object branch : adBranches) {

            adBranch = (LocalAdBranch) branch;

            AdBranchDetails details = new AdBranchDetails();
            details.setBrBranchCode(adBranch.getBrBranchCode());
            details.setBrCode(adBranch.getBrCode());
            details.setBrName(adBranch.getBrName());

            list.add(details);
        }

        return list;
    }

    public ArrayList getAdBrSplAll(Integer BSPL_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApSupplierEntryControllerBean getBrSplAll");

        LocalAdBranchSupplier adBranchSupplier = null;
        LocalAdBranch adBranch = null;
        LocalGlChartOfAccount glChartOfAccount = null;

        Collection adBranchSuppliers = null;

        ArrayList branchList = new ArrayList();
        ArrayList glCoaAccntList = new ArrayList();


        try {

            adBranchSuppliers = adBranchSupplierHome.findBSplBySpl(BSPL_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchSuppliers.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchSupplier : adBranchSuppliers) {

                adBranchSupplier = (LocalAdBranchSupplier) branchSupplier;

                adBranch = adBranchHome.findByPrimaryKey(adBranchSupplier.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();

                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrCode(adBranch.getBrCode());
                details.setBrName(adBranch.getBrName());

                // get the gl chart of account for this bspl_code
                ApSupplierDetails apSplDetails = new ApSupplierDetails();

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchSupplier.getBsplGlCoaPayableAccount());
                apSplDetails.setSplCoaGlPayableAccount(glChartOfAccount.getCoaCode());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchSupplier.getBsplGlCoaExpenseAccount());
                apSplDetails.setSplCoaGlExpenseAccount(glChartOfAccount.getCoaCode());

                branchList.add(details);
                glCoaAccntList.add(apSplDetails);
            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        ArrayList branchAndGlAccnts = new ArrayList();
        branchAndGlAccnts.add(branchList);
        branchAndGlAccnts.add(glCoaAccntList);

        return branchAndGlAccnts;
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer RS_CODE) throws GlobalNoRecordFoundException {

        LocalAdResponsibility adRes = null;

        try {
            adRes = adResHome.findByPrimaryKey(RS_CODE);
        } catch (FinderException ex) {

        }

        AdResponsibilityDetails details = new AdResponsibilityDetails();
        details.setRsName(adRes.getRsName());

        return details;
    }

    // SessionBean methods

    public ArrayList getAdBrSplBySplCode(Integer SPL_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        LocalAdBranchSupplier adBrSpl = null;
        LocalGlChartOfAccount glPayableCoa = null;
        LocalGlChartOfAccount glExpenseCoa = null;

        Collection adBrSpls = null;
        ArrayList branchSuppliers = new ArrayList();

        try {
            adBrSpls = adBrSplHome.findBSplBySpl(SPL_CODE, AD_CMPNY);
        } catch (FinderException ex) {

        }

        for (Object brSpl : adBrSpls) {

            adBrSpl = (LocalAdBranchSupplier) brSpl;

            try {
                glPayableCoa = glChartOfAccountHome.findByPrimaryKey(adBrSpl.getBsplGlCoaPayableAccount());
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            try {
                glExpenseCoa = glChartOfAccountHome.findByPrimaryKey(adBrSpl.getBsplGlCoaExpenseAccount());
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            AdModBranchSupplierDetails mdetails = new AdModBranchSupplierDetails();
            mdetails.setBSplBranchCode(adBrSpl.getAdBranch().getBrCode());
            mdetails.setBSplBranchName(adBrSpl.getAdBranch().getBrName());
            mdetails.setBSplPayableAccountNumber(glPayableCoa.getCoaAccountNumber());
            mdetails.setBSplPayableAccountDesc(glPayableCoa.getCoaAccountDescription());
            mdetails.setBSplExpenseAccountNumber(glExpenseCoa.getCoaAccountNumber());
            mdetails.setBSplExpenseAccountDesc(glExpenseCoa.getCoaAccountDescription());
            branchSuppliers.add(mdetails);
        }

        return branchSuppliers;
    }

    public ArrayList getInvLitAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getInvLitAll");

        LocalInvLineItemTemplate invLineItemTemplate = null;

        ArrayList list = new ArrayList();

        try {

            Collection invLineItemTemplates = invLineItemTemplateHome.findLitAll(AD_CMPNY);

            for (Object lineItemTemplate : invLineItemTemplates) {

                invLineItemTemplate = (LocalInvLineItemTemplate) lineItemTemplate;

                list.add(invLineItemTemplate.getLitName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApSupplierEntryControllerBean ejbCreate");
    }
}