/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArCustomerEntryControllerBean
 * @created March 04, 2004, 9:36 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.LocalInvLineItemTemplateHome;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.entities.ar.LocalArSalesperson;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.LocalInvLineItemTemplate;
import com.ejb.exception.ar.*;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArCustomerDetails;
import com.util.mod.ad.AdModBranchCustomerDetails;
import com.util.mod.ar.ArModCustomerClassDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModCustomerTypeDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArCustomerEntryControllerEJB")
public class ArCustomerEntryControllerBean extends EJBContextClass implements ArCustomerEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalArCustomerTypeHome arCustomerTypeHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchCustomerHome adBranchCustomerHome;
    @EJB
    private LocalAdResponsibilityHome adResponsibiltyHome;
    @EJB
    private LocalArSalespersonHome arSalespersonHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdResponsibilityHome adResponsibilityHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBrResHome;

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getAdPrfApUseSupplierPulldown");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByCstCode(Integer CST_CODE, Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getAdApprovalNotifiedUsersByCstCode");
        ArrayList list = new ArrayList();
        try {

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);
            LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(CST_CODE);

            if (arCustomer.getCstPosted() == EJBCommon.TRUE && adApproval.getAprEnableArCustomer() == EJBCommon.TRUE) {
                arCustomer.setCstPosted(EJBCommon.FALSE);
                arCustomer.setCstEnable(EJBCommon.FALSE);
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AR CUSTOMER", CST_CODE, AD_CMPNY);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add("User: " + adApprovalQueue.getAdUser().getUsrName() + " - " + adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getPrfValidateCustomerEmail(Integer AD_CMPNY) {
        Debug.print("ApCustomerEntryControllerBean getPrfArNextCustomerCode");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfArValidateCustomerEmail();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveArCstEntry(ArModCustomerDetails details, String CT_NM, String PYT_NM, String SPL_SPPLR_CODE, String CC_NM, String CST_GL_COA_RCVBL_ACCNT, String CST_GL_COA_RVNUE_ACCNT, String CST_GL_COA_UNERND_INT_ACCNT, String CST_GL_COA_ERND_INT_ACCNT, String CST_GL_COA_UNERND_PNT_ACCNT, String CST_GL_COA_ERND_PNT_ACCNT, String BA_NM, Integer RS_CODE, String SLP_SLSPRSN_CODE, String SLP_SLSPRSN_CODE2, String LIT_NM, String HR_DB_NM, String HR_EMP_BIO_NMBR, String PM_USR_EMP_NMBR, boolean isDraft, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalNameAndAddressAlreadyExistsException, ArCCCoaGlReceivableAccountNotFoundException, ArCCCoaGlRevenueAccountNotFoundException, ArCCCoaGlUnEarnedInterestAccountNotFoundException, ArCCCoaGlEarnedInterestAccountNotFoundException, ArCCCoaGlUnEarnedPenaltyAccountNotFoundException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, ArCCCoaGlEarnedPenaltyAccountNotFoundException {
        Debug.print("ArCustomerEntryControllerBean saveArCstEntry");
        LocalArCustomer arCustomer = null;
        LocalAdPreference adPreference = null;
        try {
            adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        try {

            LocalGlChartOfAccount glReceivableChartOfAccount = null;
            LocalGlChartOfAccount glRevenueChartOfAccount = null;
            LocalGlChartOfAccount glUnEarnedInterestChartOfAccount = null;
            LocalGlChartOfAccount glEarnedInterestChartOfAccount = null;
            LocalGlChartOfAccount glUnEarnedPenaltyChartOfAccount = null;
            LocalGlChartOfAccount glEarnedPenaltyChartOfAccount = null;

            LocalArCustomerClass arCustomerClass = arCustomerClassHome.findByCcName(CC_NM, AD_CMPNY);

            // autoGenerate
			/*try {

				arCustomerHome.findByCstCustomerCode(details.getCstCustomerCode(), AD_CMPNY);

				arCustomerClass.setCcNextCustomerCode(
						EJBCommon.incrementStringNumber(arCustomerClass.getCcNextCustomerCode()));

			} catch (FinderException ex) {

				details.setCstCustomerCode(arCustomerClass.getCcNextCustomerCode());

				arCustomerClass.setCcNextCustomerCode(
						EJBCommon.incrementStringNumber(arCustomerClass.getCcNextCustomerCode()));

			}*/

            // validate if customer already exists
            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(details.getCstCustomerCode(), AD_CMPNY);

                if (details.getCstCode() == null || details.getCstCode() != null && !arCustomer.getCstCode().equals(details.getCstCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            // validate if customer with this name and address already exists

            try {

                arCustomer = arCustomerHome.findByCstNameAndAddress(details.getCstName(), details.getCstAddress(), AD_CMPNY);

                if (arCustomer != null && !arCustomer.getCstCode().equals(details.getCstCode())) {

                    throw new GlobalNameAndAddressAlreadyExistsException();
                }

            } catch (GlobalNameAndAddressAlreadyExistsException ex) {

                throw ex;

            } catch (FinderException ex) {

            }

            try {

                glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_RCVBL_ACCNT, AD_CMPNY);

            } catch (FinderException ex) {

                throw new ArCCCoaGlReceivableAccountNotFoundException();
            }

            if (CST_GL_COA_RVNUE_ACCNT != null && CST_GL_COA_RVNUE_ACCNT.length() > 0) {

                try {

                    glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_RVNUE_ACCNT, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlRevenueAccountNotFoundException();
                }
            }

            if (CST_GL_COA_UNERND_INT_ACCNT != null && CST_GL_COA_UNERND_INT_ACCNT.length() > 0) {

                try {

                    glUnEarnedInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_UNERND_INT_ACCNT, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlUnEarnedInterestAccountNotFoundException();
                }
            }

            if (CST_GL_COA_ERND_INT_ACCNT != null && CST_GL_COA_ERND_INT_ACCNT.length() > 0) {

                try {

                    glEarnedInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_ERND_INT_ACCNT, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlEarnedInterestAccountNotFoundException();
                }
            }

            if (CST_GL_COA_UNERND_PNT_ACCNT != null && CST_GL_COA_UNERND_PNT_ACCNT.length() > 0) {

                try {

                    glUnEarnedPenaltyChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_UNERND_PNT_ACCNT, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlUnEarnedPenaltyAccountNotFoundException();
                }
            }

            if (CST_GL_COA_ERND_PNT_ACCNT != null && CST_GL_COA_ERND_PNT_ACCNT.length() > 0) {

                try {

                    glEarnedPenaltyChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(CST_GL_COA_ERND_PNT_ACCNT, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlEarnedPenaltyAccountNotFoundException();
                }
            }

            // create new customer

            if (details.getCstCode() == null) {
                Debug.print("------------------------------------------------>NEW CUSTOMER MODE");
                arCustomer = arCustomerHome.create(details.getCstCustomerCode(), details.getCstName(), details.getCstDescription(), details.getCstPaymentMethod(), details.getCstCreditLimit(), details.getCstAddress(), details.getCstCity(), details.getCstStateProvince(), details.getCstPostalCode(), details.getCstCountry(), details.getCstContact(), details.getCstEmployeeID(), details.getCstAccountNumber(), details.getCstPhone(), details.getCstMobilePhone(), details.getCstFax(), details.getCstAlternatePhone(), details.getCstAlternateMobilePhone(), details.getCstAlternateContact(), details.getCstEmail(), details.getCstBillToAddress(), details.getCstBillToContact(), details.getCstBillToAltContact(), details.getCstBillToPhone(), details.getCstBillingHeader(), details.getCstBillingFooter(), details.getCstBillingHeader2(), details.getCstBillingFooter2(), details.getCstBillingHeader3(), details.getCstBillingFooter3(), details.getCstBillingSignatory(), details.getCstSignatoryTitle(), details.getCstShipToAddress(), details.getCstShipToContact(), details.getCstShipToAltContact(), details.getCstShipToPhone(), details.getCstTin(), details.getCstNumbersParking(), details.getCstMonthlyInterestRate(), details.getCstParkingID(), details.getCstAssociationDuesRate(), details.getCstRealPropertyTaxRate(), details.getCstWordPressCustomerID(), glReceivableChartOfAccount.getCoaCode(), glRevenueChartOfAccount != null ? glRevenueChartOfAccount.getCoaCode() : null, glUnEarnedInterestChartOfAccount != null ? glUnEarnedInterestChartOfAccount.getCoaCode() : null, glEarnedInterestChartOfAccount != null ? glEarnedInterestChartOfAccount.getCoaCode() : null, glUnEarnedPenaltyChartOfAccount != null ? glUnEarnedPenaltyChartOfAccount.getCoaCode() : null, glEarnedPenaltyChartOfAccount != null ? glEarnedPenaltyChartOfAccount.getCoaCode() : null, details.getCstEnable(), details.getCstEnablePayroll(), details.getCstEnableRetailCashier(), details.getCstEnableRebate(), details.getCstAutoComputeInterest(), details.getCstAutoComputePenalty(), details.getCstBirthday(), details.getCstDealPrice(), details.getCstArea(), details.getCstSquareMeter(), details.getCstEntryDate(), details.getCstEffectivityDays(), details.getCstAdLvRegion(), details.getCstMemo(), details.getCstCustomerBatch(), details.getCstCustomerDepartment(), null, null, EJBCommon.FALSE, details.getCstCreatedBy(), details.getCstDateCreated(), details.getCstLastModifiedBy(), details.getCstDateLastModified(), null, null, null, null, AD_BRNCH, AD_CMPNY);

                arCustomer.setCstHrEnableCashBond(EJBCommon.TRUE);
                arCustomer.setCstHrCashBondAmount(100);
                arCustomer.setCstHrEnableInsMisc(EJBCommon.TRUE);
                arCustomer.setCstHrInsMiscAmount(100);

                if (CT_NM != null && CT_NM.length() > 0) {

                    LocalArCustomerType arCustomerType = arCustomerTypeHome.findByCtName(CT_NM, AD_CMPNY);
                    arCustomer.setArCustomerType(arCustomerType);
                }

                // Link to AP Investor
                if (SPL_SPPLR_CODE != null && SPL_SPPLR_CODE.length() > 0) {

                    LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);
                    arCustomer.setApSupplier(apSupplier);
                }

                // create new BranchCustomer

                ArrayList bCstlist = details.getBcstList();

                for (Object o : bCstlist) {

                    AdModBranchCustomerDetails bCstDetails = (AdModBranchCustomerDetails) o;

                    try {

                        glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstReceivableAccountNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new ArCCCoaGlReceivableAccountNotFoundException();
                    }

                    Integer glRevChrtOfAccntCode = null;

                    if (bCstDetails.getBcstRevenueAccountNumber() != null && bCstDetails.getBcstRevenueAccountNumber().length() > 0) {

                        try {

                            glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstRevenueAccountNumber(), AD_CMPNY);

                            glRevChrtOfAccntCode = glRevenueChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlRevenueAccountNotFoundException();
                        }
                    }

                    Integer glUnEarnedInterestChrtOfAccntCode = null;

                    if (bCstDetails.getBcstUnEarnedInterestAccountNumber() != null && bCstDetails.getBcstUnEarnedInterestAccountNumber().length() > 0) {

                        try {

                            glUnEarnedInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstUnEarnedInterestAccountNumber(), AD_CMPNY);

                            glUnEarnedInterestChrtOfAccntCode = glUnEarnedInterestChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlUnEarnedInterestAccountNotFoundException();
                        }
                    }

                    Integer glEarnedInterestChrtOfAccntCode = null;

                    if (bCstDetails.getBcstEarnedInterestAccountNumber() != null && bCstDetails.getBcstEarnedInterestAccountNumber().length() > 0) {

                        try {

                            glEarnedInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstEarnedInterestAccountNumber(), AD_CMPNY);

                            glEarnedInterestChrtOfAccntCode = glEarnedInterestChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlEarnedInterestAccountNotFoundException();
                        }
                    }

                    Integer glUnEarnedPenaltyChrtOfAccntCode = null;

                    if (bCstDetails.getBcstUnEarnedPenaltyAccountNumber() != null && bCstDetails.getBcstUnEarnedPenaltyAccountNumber().length() > 0) {

                        try {

                            glUnEarnedPenaltyChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstUnEarnedPenaltyAccountNumber(), AD_CMPNY);

                            glUnEarnedPenaltyChrtOfAccntCode = glUnEarnedPenaltyChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlUnEarnedPenaltyAccountNotFoundException();
                        }
                    }

                    Integer glEarnedPenaltyChrtOfAccntCode = null;

                    if (bCstDetails.getBcstEarnedPenaltyAccountNumber() != null && bCstDetails.getBcstEarnedPenaltyAccountNumber().length() > 0) {

                        try {

                            glEarnedPenaltyChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstEarnedPenaltyAccountNumber(), AD_CMPNY);

                            glEarnedPenaltyChrtOfAccntCode = glEarnedPenaltyChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlEarnedPenaltyAccountNotFoundException();
                        }
                    }

                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.create(glReceivableChartOfAccount.getCoaCode(), glRevChrtOfAccntCode, glUnEarnedInterestChrtOfAccntCode, glEarnedInterestChrtOfAccntCode, glUnEarnedPenaltyChrtOfAccntCode, glEarnedPenaltyChrtOfAccntCode, 'N', AD_CMPNY);
                    // arCustomer.addAdBranchCustomer(adBranchCustomer);
                    adBranchCustomer.setArCustomer(arCustomer);

                    LocalAdBranch adBranch = adBranchHome.findByBrName(bCstDetails.getBcstBranchName(), AD_CMPNY);
                    // adBranch.addAdBranchCustomer(adBranchCustomer);
                    adBranchCustomer.setAdBranch(adBranch);
                }

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
                arCustomer.setAdPaymentTerm(adPaymentTerm);

                arCustomer.setArCustomerClass(arCustomerClass);

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, AD_CMPNY);
                arCustomer.setAdBankAccount(adBankAccount);

                LocalArSalesperson arSalesperson = null;

                if (SLP_SLSPRSN_CODE != null && SLP_SLSPRSN_CODE.length() > 0 && !SLP_SLSPRSN_CODE.equalsIgnoreCase("NO RECORD FOUND")) {

                    // if he tagged a salesperson for this customer
                    arSalesperson = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE, AD_CMPNY);
                    arCustomer.setArSalesperson(arSalesperson);

                } else {

                    // if he untagged a salesperson for this customer
                    if (arCustomer.getArSalesperson() != null) {

                        arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode(), AD_CMPNY);
                        arSalesperson.dropArCustomer(arCustomer);
                    }
                }

                LocalArSalesperson arSalesperson2 = null;
                if (SLP_SLSPRSN_CODE2 != null && SLP_SLSPRSN_CODE2.length() > 0 && !SLP_SLSPRSN_CODE2.equalsIgnoreCase("NO RECORD FOUND")) {

                    // if he tagged a salesperson for this customer
                    arSalesperson2 = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE2, AD_CMPNY);
                    arCustomer.setCstArSalesperson2(arSalesperson2.getSlpCode());

                } else {

                    // if he untagged a salesperson for this customer
                    arCustomer.setCstArSalesperson2(null);
                }

                if (LIT_NM != null && LIT_NM.length() > 0) {

                    LocalInvLineItemTemplate invLineItemTemplate = invLineItemTemplateHome.findByLitName(LIT_NM, AD_CMPNY);
                    arCustomer.setInvLineItemTemplate(invLineItemTemplate);
                }

            } else {

                Debug.print("------------------------------------------------>UPDATE CUSTOMER MODE");
                arCustomer = arCustomerHome.findByPrimaryKey(details.getCstCode());

                arCustomer.setCstCustomerCode(details.getCstCustomerCode());
                arCustomer.setCstName(details.getCstName());
                arCustomer.setCstDescription(details.getCstDescription());
                arCustomer.setCstPaymentMethod(details.getCstPaymentMethod());
                arCustomer.setCstCreditLimit(details.getCstCreditLimit());
                arCustomer.setCstAddress(details.getCstAddress());
                arCustomer.setCstCity(details.getCstCity());
                arCustomer.setCstStateProvince(details.getCstStateProvince());
                arCustomer.setCstPostalCode(details.getCstPostalCode());
                arCustomer.setCstCountry(details.getCstCountry());
                arCustomer.setCstEmployeeID(details.getCstEmployeeID());
                arCustomer.setCstAccountNumber(details.getCstAccountNumber());
                arCustomer.setCstContact(details.getCstContact());
                arCustomer.setCstPhone(details.getCstPhone());
                arCustomer.setCstMobilePhone(details.getCstMobilePhone());
                arCustomer.setCstFax(details.getCstFax());
                arCustomer.setCstAlternatePhone(details.getCstAlternatePhone());
                arCustomer.setCstAlternateMobilePhone(details.getCstAlternateMobilePhone());
                arCustomer.setCstAlternateContact(details.getCstAlternateContact());
                arCustomer.setCstEmail(details.getCstEmail());
                arCustomer.setCstBillToAddress(details.getCstBillToAddress());
                arCustomer.setCstBillToContact(details.getCstBillToContact());
                arCustomer.setCstBillToAltContact(details.getCstBillToAltContact());
                arCustomer.setCstBillToPhone(details.getCstBillToPhone());
                arCustomer.setCstBillingHeader(details.getCstBillingHeader());
                arCustomer.setCstBillingFooter(details.getCstBillingFooter());
                arCustomer.setCstBillingHeader2(details.getCstBillingHeader2());
                arCustomer.setCstBillingFooter2(details.getCstBillingFooter2());
                arCustomer.setCstBillingHeader3(details.getCstBillingHeader3());
                arCustomer.setCstBillingFooter3(details.getCstBillingFooter3());
                arCustomer.setCstBillingSignatory(details.getCstBillingSignatory());
                arCustomer.setCstSignatoryTitle(details.getCstSignatoryTitle());
                arCustomer.setCstShipToAddress(details.getCstShipToAddress());
                arCustomer.setCstShipToContact(details.getCstShipToContact());
                arCustomer.setCstShipToAltContact(details.getCstShipToAltContact());
                arCustomer.setCstShipToPhone(details.getCstShipToPhone());
                arCustomer.setCstTin(details.getCstTin());
                arCustomer.setCstNumbersParking(details.getCstNumbersParking());
                arCustomer.setCstMonthlyInterestRate(details.getCstMonthlyInterestRate());
                arCustomer.setCstParkingID(details.getCstParkingID());
                arCustomer.setCstRealPropertyTaxRate(details.getCstRealPropertyTaxRate());
                arCustomer.setCstAssociationDuesRate(details.getCstAssociationDuesRate());
                arCustomer.setCstWordPressCustomerID(details.getCstWordPressCustomerID());
                arCustomer.setCstGlCoaReceivableAccount(glReceivableChartOfAccount.getCoaCode());
                arCustomer.setCstGlCoaRevenueAccount(glRevenueChartOfAccount != null ? glRevenueChartOfAccount.getCoaCode() : null);

                arCustomer.setCstGlCoaUnEarnedInterestAccount(glUnEarnedInterestChartOfAccount != null ? glUnEarnedInterestChartOfAccount.getCoaCode() : null);
                arCustomer.setCstGlCoaEarnedInterestAccount(glEarnedInterestChartOfAccount != null ? glEarnedInterestChartOfAccount.getCoaCode() : null);
                arCustomer.setCstGlCoaUnEarnedPenaltyAccount(glUnEarnedPenaltyChartOfAccount != null ? glUnEarnedPenaltyChartOfAccount.getCoaCode() : null);
                arCustomer.setCstGlCoaEarnedPenaltyAccount(glEarnedPenaltyChartOfAccount != null ? glEarnedPenaltyChartOfAccount.getCoaCode() : null);

                arCustomer.setCstEnable(details.getCstEnable());
                arCustomer.setCstEnableRetailCashier(details.getCstEnableRetailCashier());
                arCustomer.setCstEnableRebate(details.getCstEnableRebate());
                arCustomer.setCstAutoComputeInterest(details.getCstAutoComputeInterest());
                arCustomer.setCstAutoComputePenalty(details.getCstAutoComputePenalty());
                arCustomer.setCstBirthday(details.getCstBirthday());
                arCustomer.setCstDealPrice(details.getCstDealPrice());
                arCustomer.setCstArea(details.getCstArea());
                arCustomer.setCstSquareMeter(details.getCstSquareMeter());
                arCustomer.setCstEntryDate(details.getCstEntryDate());
                arCustomer.setCstEffectivityDays(details.getCstEffectivityDays());
                arCustomer.setCstAdLvRegion(details.getCstAdLvRegion());
                arCustomer.setCstMemo(details.getCstMemo());
                arCustomer.setCstCustomerBatch(details.getCstCustomerBatch());
                arCustomer.setCstCustomerDepartment(details.getCstCustomerDepartment());

                arCustomer.setCstLastModifiedBy(details.getCstLastModifiedBy());
                arCustomer.setCstDateLastModified(details.getCstDateLastModified());
                arCustomer.setCstReasonForRejection(null);
                arCustomer.setCstAdBranch(AD_BRNCH);

                arCustomer.setCstHrEnableCashBond(EJBCommon.TRUE);
                arCustomer.setCstHrCashBondAmount(100);
                arCustomer.setCstHrEnableInsMisc(EJBCommon.TRUE);
                arCustomer.setCstHrInsMiscAmount(100);

                if (CT_NM != null && CT_NM.length() > 0) {

                    LocalArCustomerType arCustomerType = arCustomerTypeHome.findByCtName(CT_NM, AD_CMPNY);
                    arCustomer.setArCustomerType(arCustomerType);
                }

                // Link to AP Investor
                if (SPL_SPPLR_CODE != null && SPL_SPPLR_CODE.length() > 0) {

                    LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);
                    arCustomer.setApSupplier(apSupplier);
                }

                Debug.print("PANK ELSE");

                LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
                arCustomer.setAdPaymentTerm(adPaymentTerm);
                Debug.print("adPaymentTerm:" + adPaymentTerm);
                Debug.print("CC_NM--" + CC_NM);
                arCustomer.setArCustomerClass(arCustomerClass);
                Debug.print("arCustomerClass:" + arCustomerClass);
                LocalAdBankAccount adBankAccount = adBankAccountHome.findByBaName(BA_NM, AD_CMPNY);
                arCustomer.setAdBankAccount(adBankAccount);
                Debug.print("adBankAccount:" + adBankAccount);
                LocalArSalesperson arSalesperson = null;

                if (SLP_SLSPRSN_CODE != null && SLP_SLSPRSN_CODE.length() > 0 && !SLP_SLSPRSN_CODE.equalsIgnoreCase("NO RECORD FOUND")) {

                    // if he tagged a salesperson for this customer
                    arSalesperson = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE, AD_CMPNY);
                    arCustomer.setArSalesperson(arSalesperson);

                } else {

                    // if he untagged a salesperson for this customer
                    if (arCustomer.getArSalesperson() != null) {

                        arSalesperson = arSalespersonHome.findBySlpSalespersonCode(arCustomer.getArSalesperson().getSlpSalespersonCode(), AD_CMPNY);
                        arSalesperson.dropArCustomer(arCustomer);
                    }
                }

                LocalArSalesperson arSalesperson2 = null;
                if (SLP_SLSPRSN_CODE2 != null && SLP_SLSPRSN_CODE2.length() > 0 && !SLP_SLSPRSN_CODE2.equalsIgnoreCase("NO RECORD FOUND")) {

                    // if he tagged a salesperson for this customer
                    arSalesperson2 = arSalespersonHome.findBySlpSalespersonCode(SLP_SLSPRSN_CODE2, AD_CMPNY);
                    arCustomer.setCstArSalesperson2(arSalesperson2.getSlpCode());

                } else {

                    // if he untagged a salesperson for this customer
                    arCustomer.setCstArSalesperson2(null);
                }

                // Set download status
                AdModBranchCustomerDetails bCstDetails = null;
                ArrayList newbcstList = new ArrayList();
                ArrayList bcstList = details.getBcstList();
                Iterator iterBcst = bcstList.iterator();

                int ctr = 0;

                while (iterBcst.hasNext()) {

                    bCstDetails = (AdModBranchCustomerDetails) iterBcst.next();

                    LocalAdBranch adBranch = adBranchHome.findByBrName(bCstDetails.getBcstBranchName(), AD_CMPNY);

                    LocalAdBranchCustomer adBranchCustomer = null;

                    try {
                        adBranchCustomer = adBranchCustomerHome.findBcstByCstCodeAndBrCode(arCustomer.getCstCode(), adBranch.getBrCode(), AD_CMPNY);
                    } catch (FinderException ex) {

                    }

                    bCstDetails.setBcstCustomerDownloadStatus('N');
                    if (adBranchCustomer != null) {
                        if (adBranchCustomer.getBcstCustomerDownloadStatus() == 'N') {
                            bCstDetails.setBcstCustomerDownloadStatus('N');
                        } else if (adBranchCustomer.getBcstCustomerDownloadStatus() == 'D') {
                            bCstDetails.setBcstCustomerDownloadStatus('X');
                        } else if (adBranchCustomer.getBcstCustomerDownloadStatus() == 'U') {
                            bCstDetails.setBcstCustomerDownloadStatus('U');
                        } else if (adBranchCustomer.getBcstCustomerDownloadStatus() == 'X') {
                            bCstDetails.setBcstCustomerDownloadStatus('X');
                        }
                    }

                    newbcstList.add(bCstDetails);
                }

                details.setBcstList(newbcstList);

                // remove all BranchCustomer

                LocalAdResponsibility adResponsibility = adResponsibiltyHome.findByPrimaryKey(RS_CODE);

                Collection adBranchCustomers = adBranchCustomerHome.findBcstByCstCodeAndRsName(arCustomer.getCstCode(), adResponsibility.getRsName(), AD_CMPNY);

                Iterator i = adBranchCustomers.iterator();

                while (i.hasNext()) {

                    LocalAdBranchCustomer adBranchCustomer = (LocalAdBranchCustomer) i.next();
                    LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(adBranchCustomer.getAdBranch().getBrCode());

                    arCustomer.dropAdBranchCustomer(adBranchCustomer);
                    adBranch.dropAdBranchCustomer(adBranchCustomer);
                    // adBranchCustomer.entityRemove();
                    em.remove(adBranchCustomer);
                }

                // create new BranchCustomer
                ArrayList bCstlist = details.getBcstList();
                i = bCstlist.iterator();

                while (i.hasNext()) {

                    bCstDetails = (AdModBranchCustomerDetails) i.next();

                    try {

                        glReceivableChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstReceivableAccountNumber(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new ArCCCoaGlReceivableAccountNotFoundException();
                    }

                    Integer glRevChrtOfAccntCode = null;

                    if (bCstDetails.getBcstRevenueAccountNumber() != null && bCstDetails.getBcstRevenueAccountNumber().length() > 0) {

                        try {

                            glRevenueChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstRevenueAccountNumber(), AD_CMPNY);

                            glRevChrtOfAccntCode = glRevenueChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlRevenueAccountNotFoundException();
                        }
                    }

                    Integer glUnEarnedInterestChrtOfAccntCode = null;

                    if (bCstDetails.getBcstUnEarnedInterestAccountNumber() != null && bCstDetails.getBcstUnEarnedInterestAccountNumber().length() > 0) {

                        try {

                            glUnEarnedInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstUnEarnedInterestAccountNumber(), AD_CMPNY);

                            glUnEarnedInterestChrtOfAccntCode = glUnEarnedInterestChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlUnEarnedInterestAccountNotFoundException();
                        }
                    }

                    Integer glEarnedInterestChrtOfAccntCode = null;

                    if (bCstDetails.getBcstEarnedInterestAccountNumber() != null && bCstDetails.getBcstEarnedInterestAccountNumber().length() > 0) {

                        try {

                            glEarnedInterestChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstEarnedInterestAccountNumber(), AD_CMPNY);

                            glEarnedInterestChrtOfAccntCode = glEarnedInterestChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlEarnedInterestAccountNotFoundException();
                        }
                    }

                    Integer glUnEarnedPenaltyChrtOfAccntCode = null;

                    if (bCstDetails.getBcstUnEarnedPenaltyAccountNumber() != null && bCstDetails.getBcstUnEarnedPenaltyAccountNumber().length() > 0) {

                        try {

                            glUnEarnedPenaltyChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstUnEarnedPenaltyAccountNumber(), AD_CMPNY);

                            glUnEarnedPenaltyChrtOfAccntCode = glUnEarnedPenaltyChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlUnEarnedPenaltyAccountNotFoundException();
                        }
                    }

                    Integer glEarnedPenaltyChrtOfAccntCode = null;

                    if (bCstDetails.getBcstEarnedPenaltyAccountNumber() != null && bCstDetails.getBcstEarnedPenaltyAccountNumber().length() > 0) {

                        try {

                            glEarnedPenaltyChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(bCstDetails.getBcstEarnedPenaltyAccountNumber(), AD_CMPNY);

                            glEarnedPenaltyChrtOfAccntCode = glEarnedPenaltyChartOfAccount.getCoaCode();

                        } catch (FinderException ex) {

                            throw new ArCCCoaGlEarnedPenaltyAccountNotFoundException();
                        }
                    }

                    Debug.print("Customer DL Stat: " + bCstDetails.getBcstCustomerDownloadStatus());

                    LocalAdBranchCustomer adBranchCustomer = adBranchCustomerHome.create(glReceivableChartOfAccount.getCoaCode(), glRevChrtOfAccntCode, glUnEarnedInterestChrtOfAccntCode, glEarnedInterestChrtOfAccntCode, glUnEarnedPenaltyChrtOfAccntCode, glEarnedPenaltyChrtOfAccntCode, bCstDetails.getBcstCustomerDownloadStatus(), AD_CMPNY);
                    // arCustomer.addAdBranchCustomer(adBranchCustomer);
                    adBranchCustomer.setArCustomer(arCustomer);

                    LocalAdBranch adBranch = adBranchHome.findByBrName(bCstDetails.getBcstBranchName(), AD_CMPNY);
                    // adBranch.addAdBranchCustomer(adBranchCustomer);
                    adBranchCustomer.setAdBranch(adBranch);
                }

                LocalInvLineItemTemplate invLineItemTemplate = null;

                if (LIT_NM != null && LIT_NM.length() > 0 && !LIT_NM.equalsIgnoreCase("NO RECORD FOUND")) {

                    invLineItemTemplate = invLineItemTemplateHome.findByLitName(LIT_NM, AD_CMPNY);
                    arCustomer.setInvLineItemTemplate(invLineItemTemplate);

                } else {

                    if (arCustomer.getInvLineItemTemplate() != null) {

                        invLineItemTemplate = invLineItemTemplateHome.findByLitName(arCustomer.getInvLineItemTemplate().getLitName(), AD_CMPNY);
                        invLineItemTemplate.dropArCustomer(arCustomer);
                    }
                }
            }
            Debug.print("trace 5");
            // set purchase requisition approval status

            String CST_APPRVL_STATUS = null;

            // if(!isDraft) {

            LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

            // check if ap voucher approval is enabled
            if (adApproval.getAprEnableArCustomer() == EJBCommon.FALSE) {
                CST_APPRVL_STATUS = "N/A";

            } else {
                // for approval, create approval queue

                // get user who is requesting details.getPrLastModifiedBy() adUser

                LocalAdUser adUser = adUserHome.findByUsrName(details.getCstLastModifiedBy(), AD_CMPNY);

                Collection adUsers;

                try {

                    adUsers = adUserHome.findUsrByDepartmentHead(adUser.getUsrDept(), (byte) 0, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalNoApprovalRequesterFoundException();
                }

                try {

                    adUsers = adUserHome.findUsrByDepartmentHead(adUser.getUsrDept(), (byte) 1, AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalNoApprovalApproverFoundException();
                }
                if (adUsers.isEmpty()) {

                    throw new GlobalNoApprovalApproverFoundException();

                } else {

                    for (Object user : adUsers) {
                        LocalAdUser adUserHead = (LocalAdUser) user;
                        LocalAdApprovalQueue adApprovalQueue = adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("AR CUSTOMER").AqDocumentCode(arCustomer.getCstCode()).AqDocumentNumber(arCustomer.getCstCustomerCode()).AqDate(arCustomer.getCstEntryDate()).AqAndOr("OR").AqUserOr(EJBCommon.TRUE).AqAdBranch(AD_BRNCH).AqAdCompany(AD_CMPNY).buildApprovalQueue();
                        adUserHead.addAdApprovalQueue(adApprovalQueue);
                    }
                }
                CST_APPRVL_STATUS = "PENDING";
            }

            arCustomer.setCstApprovalStatus(CST_APPRVL_STATUS);

            // set post purchase order

            if (CST_APPRVL_STATUS.equals("N/A")) {

                arCustomer.setCstPosted(EJBCommon.TRUE);
                // arCustomer.setCstEnable(EJBCommon.TRUE);
                arCustomer.setCstPostedBy(arCustomer.getCstLastModifiedBy());
                arCustomer.setCstDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());
            }

            // }

        } catch (GlobalRecordAlreadyExistException | ArCCCoaGlEarnedInterestAccountNotFoundException |
                 ArCCCoaGlUnEarnedInterestAccountNotFoundException | ArCCCoaGlRevenueAccountNotFoundException |
                 ArCCCoaGlReceivableAccountNotFoundException | GlobalNameAndAddressAlreadyExistsException |
                 GlobalNoApprovalRequesterFoundException | GlobalNoApprovalApproverFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
        return arCustomer.getCstCode();
    }

    public ArModCustomerDetails getArCstByCstCode(Integer CST_CODE, Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArCustomerEntryControllerBean getArCstByCstCode");
        LocalArCustomer arCustomer = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalArSalesperson arSalesperson = null;
        try {

            try {

                arCustomer = arCustomerHome.findByPrimaryKey(CST_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            LocalAdResponsibility adResponsibility = null;

            try {

                adResponsibility = adResponsibilityHome.findByPrimaryKey(RS_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get Branch Customers lines if any

            Collection adBranchCustomers = adBranchCustomerHome.findBcstByCstCodeAndRsName(arCustomer.getCstCode(), adResponsibility.getRsName(), AD_CMPNY);

            for (Object branchCustomer : adBranchCustomers) {

                LocalAdBranchCustomer adBranchCustomer = (LocalAdBranchCustomer) branchCustomer;
                AdModBranchCustomerDetails bCstDetails = new AdModBranchCustomerDetails();

                try {

                    glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaReceivableAccount());

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }

                bCstDetails.setBcstReceivableAccountDescription(glChartOfAccount.getCoaAccountDescription());
                bCstDetails.setBcstReceivableAccountNumber(glChartOfAccount.getCoaAccountNumber());

                if (adBranchCustomer.getBcstGlCoaRevenueAccount() != null) {

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaRevenueAccount());

                        bCstDetails.setBcstRevenueAccountDescription(glChartOfAccount.getCoaAccountDescription());
                        bCstDetails.setBcstRevenueAccountNumber(glChartOfAccount.getCoaAccountNumber());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                }

                if (adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount() != null) {

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount());

                        bCstDetails.setBcstUnEarnedInterestAccountDescription(glChartOfAccount.getCoaAccountDescription());
                        bCstDetails.setBcstUnEarnedInterestAccountNumber(glChartOfAccount.getCoaAccountNumber());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                }

                if (adBranchCustomer.getBcstGlCoaEarnedInterestAccount() != null) {

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaEarnedInterestAccount());

                        bCstDetails.setBcstEarnedInterestAccountDescription(glChartOfAccount.getCoaAccountDescription());
                        bCstDetails.setBcstEarnedInterestAccountNumber(glChartOfAccount.getCoaAccountNumber());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                }

                if (adBranchCustomer.getBcstGlCoaUnEarnedPenaltyAccount() != null) {

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaUnEarnedPenaltyAccount());

                        bCstDetails.setBcstUnEarnedPenaltyAccountDescription(glChartOfAccount.getCoaAccountDescription());
                        bCstDetails.setBcstUnEarnedPenaltyAccountNumber(glChartOfAccount.getCoaAccountNumber());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                }

                if (adBranchCustomer.getBcstGlCoaEarnedPenaltyAccount() != null) {

                    try {

                        glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaEarnedPenaltyAccount());

                        bCstDetails.setBcstEarnedPenaltyAccountDescription(glChartOfAccount.getCoaAccountDescription());
                        bCstDetails.setBcstEarnedPenaltyAccountNumber(glChartOfAccount.getCoaAccountNumber());

                    } catch (FinderException ex) {

                        throw new GlobalNoRecordFoundException();
                    }
                }

                bCstDetails.setBcstBranchName(adBranchCustomer.getAdBranch().getBrName());
                bCstDetails.setBcstBranchCode(adBranchCustomer.getAdBranch().getBrBranchCode());

                list.add(bCstDetails);
            }

            ArModCustomerDetails cstDetails = new ArModCustomerDetails();
            cstDetails.setCstCode(arCustomer.getCstCode());
            cstDetails.setCstCustomerCode(arCustomer.getCstCustomerCode());
            cstDetails.setCstRefCustomerCode(arCustomer.getCstRefCustomerCode());
            cstDetails.setCstName(arCustomer.getCstName());
            cstDetails.setCstDescription(arCustomer.getCstDescription());
            cstDetails.setCstPaymentMethod(arCustomer.getCstPaymentMethod());
            cstDetails.setCstCreditLimit(arCustomer.getCstCreditLimit());
            cstDetails.setCstAddress(arCustomer.getCstAddress());
            cstDetails.setCstCity(arCustomer.getCstCity());
            cstDetails.setCstStateProvince(arCustomer.getCstStateProvince());
            cstDetails.setCstPostalCode(arCustomer.getCstPostalCode());
            cstDetails.setCstCountry(arCustomer.getCstCountry());
            cstDetails.setCstEmployeeID(arCustomer.getCstEmployeeID());
            cstDetails.setCstAccountNumber(arCustomer.getCstAccountNumber());
            cstDetails.setCstContact(arCustomer.getCstContact());
            cstDetails.setCstPhone(arCustomer.getCstPhone());
            cstDetails.setCstFax(arCustomer.getCstFax());
            cstDetails.setCstAlternatePhone(arCustomer.getCstAlternatePhone());
            cstDetails.setCstAlternateContact(arCustomer.getCstAlternateContact());
            cstDetails.setCstEmail(arCustomer.getCstEmail());
            cstDetails.setCstBillToAddress(arCustomer.getCstBillToAddress());
            cstDetails.setCstBillToContact(arCustomer.getCstBillToContact());
            cstDetails.setCstBillToAltContact(arCustomer.getCstBillToAltContact());
            cstDetails.setCstBillToPhone(arCustomer.getCstBillToPhone());
            cstDetails.setCstBillingHeader(arCustomer.getCstBillingHeader());
            cstDetails.setCstBillingFooter(arCustomer.getCstBillingFooter());
            cstDetails.setCstBillingHeader2(arCustomer.getCstBillingHeader2());
            cstDetails.setCstBillingFooter2(arCustomer.getCstBillingFooter2());
            cstDetails.setCstBillingHeader3(arCustomer.getCstBillingHeader3());
            cstDetails.setCstBillingFooter3(arCustomer.getCstBillingFooter3());
            cstDetails.setCstBillingSignatory(arCustomer.getCstBillingSignatory());
            cstDetails.setCstSignatoryTitle(arCustomer.getCstSignatoryTitle());
            cstDetails.setCstShipToAddress(arCustomer.getCstShipToAddress());
            cstDetails.setCstShipToContact(arCustomer.getCstShipToContact());
            cstDetails.setCstShipToAltContact(arCustomer.getCstShipToAltContact());
            cstDetails.setCstShipToPhone(arCustomer.getCstShipToPhone());
            cstDetails.setCstTin(arCustomer.getCstTin());
            cstDetails.setCstEnable(arCustomer.getCstEnable());
            cstDetails.setCstEnableRetailCashier(arCustomer.getCstEnableRetailCashier());
            cstDetails.setCstEnableRebate(arCustomer.getCstEnableRebate());
            cstDetails.setCstAutoComputeInterest(arCustomer.getCstAutoComputeInterest());
            cstDetails.setCstAutoComputePenalty(arCustomer.getCstAutoComputePenalty());
            cstDetails.setCstMobilePhone(arCustomer.getCstMobilePhone());
            cstDetails.setCstAlternateMobilePhone(arCustomer.getCstAlternateMobilePhone());
            cstDetails.setCstBirthday(arCustomer.getCstBirthday());
            cstDetails.setCstDealPrice(arCustomer.getCstDealPrice());
            cstDetails.setCstArea(arCustomer.getCstArea());
            cstDetails.setCstSquareMeter(arCustomer.getCstSquareMeter());
            cstDetails.setCstNumbersParking(arCustomer.getCstNumbersParking());
            cstDetails.setCstMonthlyInterestRate(arCustomer.getCstMonthlyInterestRate());
            cstDetails.setCstParkingID(arCustomer.getCstParkingID());
            cstDetails.setCstAssociationDuesRate(arCustomer.getCstAssociationDuesRate());
            cstDetails.setCstRealPropertyTaxRate(arCustomer.getCstRealPropertyTaxRate());
            cstDetails.setCstWordPressCustomerID(arCustomer.getCstWordPressCustomerID());
            cstDetails.setCstEntryDate(arCustomer.getCstEntryDate());
            cstDetails.setCstEffectivityDays(arCustomer.getCstEffectivityDays());
            cstDetails.setCstAdLvRegion(arCustomer.getCstAdLvRegion());
            cstDetails.setCstMemo(arCustomer.getCstMemo());
            cstDetails.setCstCustomerBatch(arCustomer.getCstCustomerBatch());
            cstDetails.setCstCustomerDepartment(arCustomer.getCstCustomerDepartment());
            cstDetails.setCstSlpSalespersonCode(arCustomer.getArSalesperson() != null ? arCustomer.getArSalesperson().getSlpSalespersonCode() : null);
            cstDetails.setCstApprovalStatus(arCustomer.getCstApprovalStatus());
            cstDetails.setCstReasonForRejection(arCustomer.getCstReasonForRejection());
            cstDetails.setCstPosted(arCustomer.getCstPosted());

            cstDetails.setCstCreatedBy(arCustomer.getCstCreatedBy());
            cstDetails.setCstDateCreated(arCustomer.getCstDateCreated());
            cstDetails.setCstLastModifiedBy(arCustomer.getCstLastModifiedBy());
            cstDetails.setCstDateLastModified(arCustomer.getCstDateLastModified());
            cstDetails.setCstApprovedRejectedBy(arCustomer.getCstApprovedRejectedBy());
            cstDetails.setCstDateApprovedRejected(arCustomer.getCstDateApprovedRejected());
            cstDetails.setCstPostedBy(arCustomer.getCstPostedBy());
            cstDetails.setCstDatePosted(arCustomer.getCstDatePosted());

            LocalArSalesperson arSalesperson2 = null;

            if (arCustomer.getCstArSalesperson2() != null) {

                try {
                    arSalesperson2 = arSalespersonHome.findByPrimaryKey(arCustomer.getCstArSalesperson2());

                } catch (Exception ex) {

                }

                cstDetails.setCstSlpSalespersonCode2(arSalesperson2.getSlpSalespersonCode());

            } else {

                cstDetails.setCstSlpSalespersonCode2(null);
            }

            cstDetails.setCstCtName(arCustomer.getArCustomerType() != null ? arCustomer.getArCustomerType().getCtName() : null);

            cstDetails.setCstPytName(arCustomer.getAdPaymentTerm() != null ? arCustomer.getAdPaymentTerm().getPytName() : null);

            cstDetails.setCstCcName(arCustomer.getArCustomerClass() != null ? arCustomer.getArCustomerClass().getCcName() : null);

            cstDetails.setCstCcName(arCustomer.getArCustomerClass() != null ? arCustomer.getArCustomerClass().getCcName() : null);

            cstDetails.setCstSupplierCode(arCustomer.getApSupplier() != null ? arCustomer.getApSupplier().getSplSupplierCode() : null);

            glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomer.getCstGlCoaReceivableAccount());

            cstDetails.setCstGlCoaReceivableAccountNumber(glChartOfAccount.getCoaAccountNumber());
            cstDetails.setCstGlCoaReceivableAccountDescription(glChartOfAccount.getCoaAccountDescription());

            if (arCustomer.getCstGlCoaRevenueAccount() != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomer.getCstGlCoaRevenueAccount());

                cstDetails.setCstGlCoaRevenueAccountNumber(glChartOfAccount.getCoaAccountNumber());
                cstDetails.setCstGlCoaRevenueAccountDescription(glChartOfAccount.getCoaAccountDescription());
            }

            if (arCustomer.getCstGlCoaUnEarnedInterestAccount() != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomer.getCstGlCoaUnEarnedInterestAccount());

                cstDetails.setCstGlCoaUnEarnedInterestAccountNumber(glChartOfAccount.getCoaAccountNumber());
                cstDetails.setCstGlCoaUnEarnedInterestAccountDescription(glChartOfAccount.getCoaAccountDescription());
            }

            if (arCustomer.getCstGlCoaEarnedInterestAccount() != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomer.getCstGlCoaEarnedInterestAccount());

                cstDetails.setCstGlCoaEarnedInterestAccountNumber(glChartOfAccount.getCoaAccountNumber());
                cstDetails.setCstGlCoaEarnedInterestAccountDescription(glChartOfAccount.getCoaAccountDescription());
            }

            if (arCustomer.getCstGlCoaUnEarnedPenaltyAccount() != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomer.getCstGlCoaUnEarnedPenaltyAccount());

                cstDetails.setCstGlCoaUnEarnedPenaltyAccountNumber(glChartOfAccount.getCoaAccountNumber());
                cstDetails.setCstGlCoaUnEarnedPenaltyAccountDescription(glChartOfAccount.getCoaAccountDescription());
            }

            if (arCustomer.getCstGlCoaEarnedPenaltyAccount() != null) {

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomer.getCstGlCoaEarnedPenaltyAccount());

                cstDetails.setCstGlCoaEarnedPenaltyAccountNumber(glChartOfAccount.getCoaAccountNumber());
                cstDetails.setCstGlCoaEarnedPenaltyAccountDescription(glChartOfAccount.getCoaAccountDescription());
            }

            cstDetails.setCstBaName(arCustomer.getAdBankAccount().getBaName());

            cstDetails.setCstLitName(arCustomer.getInvLineItemTemplate() != null ? arCustomer.getInvLineItemTemplate().getLitName() : null);

            cstDetails.setBcstList(list);

            return cstDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getArCstGlCoaRevenueAccountEnable(Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getArCstGlCoaRevenueAccountEnable");
        try {

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAasClassTypeAndAaAccountType("AR CUSTOMER", "REVENUE", AD_CMPNY);
            return !arAutoAccountingSegments.isEmpty();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerClassDetails getArCcByCcName(String CC_NM, Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getArCcByCcName");
        try {

            LocalArCustomerClass arCustomerClass = arCustomerClassHome.findByCcName(CC_NM, AD_CMPNY);
            ArModCustomerClassDetails mdetails = new ArModCustomerClassDetails();

            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaReceivableAccount());

            LocalGlChartOfAccount glRevenueAccount = null;
            LocalGlChartOfAccount glUnEarnedInterestAccount = null;
            LocalGlChartOfAccount glEarnedInterestAccount = null;
            LocalGlChartOfAccount glUnEarnedPenaltyAccount = null;
            LocalGlChartOfAccount glEarnedPenaltyAccount = null;

            if (arCustomerClass.getCcGlCoaRevenueAccount() != null) {

                glRevenueAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaRevenueAccount());
            }

            if (arCustomerClass.getCcGlCoaUnEarnedInterestAccount() != null) {

                glUnEarnedInterestAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaUnEarnedInterestAccount());
            }

            if (arCustomerClass.getCcGlCoaEarnedInterestAccount() != null) {

                glEarnedInterestAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaEarnedInterestAccount());
            }

            if (arCustomerClass.getCcGlCoaUnEarnedPenaltyAccount() != null) {

                glUnEarnedPenaltyAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaUnEarnedPenaltyAccount());
            }

            if (arCustomerClass.getCcGlCoaEarnedPenaltyAccount() != null) {

                glEarnedPenaltyAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaEarnedPenaltyAccount());
            }

            mdetails.setCcGlCoaReceivableAccountNumber(glChartOfAccount != null ? glChartOfAccount.getCoaAccountNumber() : null);
            mdetails.setCcGlCoaRevenueAccountNumber(glRevenueAccount != null ? glRevenueAccount.getCoaAccountNumber() : null);

            mdetails.setCcGlCoaUnEarnedInterestAccountNumber(glUnEarnedInterestAccount != null ? glUnEarnedInterestAccount.getCoaAccountNumber() : null);
            mdetails.setCcGlCoaEarnedInterestAccountNumber(glEarnedInterestAccount != null ? glEarnedInterestAccount.getCoaAccountNumber() : null);

            mdetails.setCcGlCoaUnEarnedPenaltyAccountNumber(glUnEarnedPenaltyAccount != null ? glUnEarnedPenaltyAccount.getCoaAccountNumber() : null);
            mdetails.setCcGlCoaEarnedPenaltyAccountNumber(glEarnedPenaltyAccount != null ? glEarnedPenaltyAccount.getCoaAccountNumber() : null);

            mdetails.setCcGlCoaReceivableAccountDescription(glChartOfAccount != null ? glChartOfAccount.getCoaAccountDescription() : null);
            mdetails.setCcGlCoaRevenueAccountDescription(glRevenueAccount != null ? glRevenueAccount.getCoaAccountDescription() : null);

            mdetails.setCcGlCoaUnEarnedInterestAccountDescription(glUnEarnedInterestAccount != null ? glUnEarnedInterestAccount.getCoaAccountDescription() : null);
            mdetails.setCcGlCoaEarnedInterestAccountDescription(glEarnedInterestAccount != null ? glEarnedInterestAccount.getCoaAccountDescription() : null);

            mdetails.setCcGlCoaUnEarnedPenaltyAccountDescription(glUnEarnedPenaltyAccount != null ? glUnEarnedPenaltyAccount.getCoaAccountDescription() : null);
            mdetails.setCcGlCoaEarnedPenaltyAccountDescription(glEarnedPenaltyAccount != null ? glEarnedPenaltyAccount.getCoaAccountDescription() : null);

            mdetails.setCcNextCustomerCode(arCustomerClass.getCcNextCustomerCode());
            mdetails.setCcCustomerBatch(arCustomerClass.getCcCustomerBatch());
            mdetails.setCcDealPrice(arCustomerClass.getCcDealPrice());
            mdetails.setCcMonthlyInterestRate(arCustomerClass.getCcMonthlyInterestRate());
            mdetails.setCcCreditLimit(arCustomerClass.getCcCreditLimit());

            mdetails.setCcEnableRebate(arCustomerClass.getCcEnableRebate());
            mdetails.setCcAutoComputeInterest(arCustomerClass.getCcAutoComputeInterest());
            mdetails.setCcAutoComputePenalty(arCustomerClass.getCcAutoComputePenalty());

            return mdetails;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModCustomerTypeDetails getArCtByCtName(String CT_NM, Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getArCtByCtName");
        try {

            LocalArCustomerType arCustomerType = arCustomerTypeHome.findByCtName(CT_NM, AD_CMPNY);
            ArModCustomerTypeDetails mdetails = new ArModCustomerTypeDetails();
            mdetails.setCtBaName(arCustomerType.getAdBankAccount().getBaName());

            return mdetails;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrByRspnsblty(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArCustomerEntryControllerBean getAdBrByRspnsblty");
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

    public ArrayList getAdBrCstAll(Integer BCST_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArCustomerEntryControllerBean getBrCstAll");
        LocalAdBranchCustomer adBranchCustomer = null;
        LocalAdBranch adBranch = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        Collection adBranchCustomers = null;
        ArrayList branchList = new ArrayList();
        ArrayList glCoaAccntList = new ArrayList();
        try {

            adBranchCustomers = adBranchCustomerHome.findBcstByCstCode(BCST_CODE, AD_CMPNY);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchCustomers.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchCustomer : adBranchCustomers) {

                adBranchCustomer = (LocalAdBranchCustomer) branchCustomer;

                adBranch = adBranchHome.findByPrimaryKey(adBranchCustomer.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();

                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrCode(adBranch.getBrCode());
                details.setBrName(adBranch.getBrName());

                // get the gl chart of account for this bcst_code

                ArCustomerDetails arCstDetails = new ArCustomerDetails();

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaReceivableAccount());
                arCstDetails.setCstGlCoaReceivableAccount(glChartOfAccount.getCoaCode());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaRevenueAccount());
                arCstDetails.setCstGlCoaRevenueAccount(glChartOfAccount.getCoaCode());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaUnEarnedInterestAccount());
                arCstDetails.setCstGlCoaUnEarnedInterestAccount(glChartOfAccount.getCoaCode());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaEarnedInterestAccount());
                arCstDetails.setCstGlCoaEarnedInterestAccount(glChartOfAccount.getCoaCode());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaUnEarnedPenaltyAccount());
                arCstDetails.setCstGlCoaUnEarnedPenaltyAccount(glChartOfAccount.getCoaCode());

                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchCustomer.getBcstGlCoaEarnedPenaltyAccount());
                arCstDetails.setCstGlCoaEarnedPenaltyAccount(glChartOfAccount.getCoaCode());

                branchList.add(details);
                glCoaAccntList.add(arCstDetails);
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

    public ArrayList getAdBrResAll(int resCode, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArCustomerEntryControllerBean getAdBrResAll");
        LocalAdBranchResponsibility adBrRes = null;
        Collection adBranches = null;
        ArrayList list = new ArrayList();

        try {

            adBranches = adBrResHome.findByAdResponsibility(resCode, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (adBranches.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }

        for (Object adBranch : adBranches) {

            adBrRes = (LocalAdBranchResponsibility) adBranch;
            AdBranchDetails details = new AdBranchDetails();

            details.setBrBranchCode(adBrRes.getAdBranch().getBrBranchCode());
            details.setBrName(adBrRes.getAdBranch().getBrName());
            list.add(details);
        }

        return list;
    }

    public ArrayList getInvLitAll(Integer AD_CMPNY) {
        Debug.print("ArCustomerEntryControllerBean getInvLitAll");
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

        Debug.print("ArCustomerEntryControllerBean ejbCreate");
    }

}