/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArCustomerClassControllerBean
 * @created March 08, 2004, 9:18 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ar.LocalArAutoAccountingSegmentHome;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.mod.ar.ArModCustomerClassDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArCustomerClassControllerEJB")
public class ArCustomerClassControllerBean extends EJBContextClass implements ArCustomerClassController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalArAutoAccountingSegmentHome arAutoAccountingSegmentHome;

    public ArrayList getArCcAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArCustomerClassControllerBean getArCcAll");
        ArrayList list = new ArrayList();
        try {

            Collection arCustomerClasses = arCustomerClassHome.findCcAll(AD_CMPNY);

            if (arCustomerClasses.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object customerClass : arCustomerClasses) {

                LocalArCustomerClass arCustomerClass = (LocalArCustomerClass) customerClass;

                LocalGlChartOfAccount glReceivableAccount = glChartOfAccountHome.findByPrimaryKey(arCustomerClass.getCcGlCoaReceivableAccount());

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

                ArModCustomerClassDetails mdetails = new ArModCustomerClassDetails();
                mdetails.setCcCode(arCustomerClass.getCcCode());
                mdetails.setCcName(arCustomerClass.getCcName());
                mdetails.setCcDescription(arCustomerClass.getCcDescription());
                mdetails.setCcNextCustomerCode(arCustomerClass.getCcNextCustomerCode());
                mdetails.setCcCustomerBatch(arCustomerClass.getCcCustomerBatch());
                mdetails.setCcDealPrice(arCustomerClass.getCcDealPrice());
                mdetails.setCcMonthlyInterestRate(arCustomerClass.getCcMonthlyInterestRate());
                mdetails.setCcMinimumFinanceCharge(arCustomerClass.getCcMinimumFinanceCharge());
                mdetails.setCcGracePeriodDay(arCustomerClass.getCcGracePeriodDay());
                mdetails.setCcDaysInPeriod(arCustomerClass.getCcDaysInPeriod());
                mdetails.setCcChargeByDueDate(arCustomerClass.getCcChargeByDueDate());

                mdetails.setCcTcName(arCustomerClass.getArTaxCode().getTcName());
                mdetails.setCcWtcName(arCustomerClass.getArWithholdingTaxCode().getWtcName());
                mdetails.setCcGlCoaFinanceChargeAccountNumber(null);
                mdetails.setCcGlCoaReceivableAccountNumber(glReceivableAccount.getCoaAccountNumber());
                mdetails.setCcGlCoaRevenueAccountNumber(glRevenueAccount != null ? glRevenueAccount.getCoaAccountNumber() : null);

                mdetails.setCcGlCoaUnEarnedInterestAccountNumber(glUnEarnedInterestAccount != null ? glUnEarnedInterestAccount.getCoaAccountNumber() : null);
                mdetails.setCcGlCoaEarnedInterestAccountNumber(glEarnedInterestAccount != null ? glEarnedInterestAccount.getCoaAccountNumber() : null);
                mdetails.setCcGlCoaUnEarnedPenaltyAccountNumber(glUnEarnedPenaltyAccount != null ? glUnEarnedPenaltyAccount.getCoaAccountNumber() : null);
                mdetails.setCcGlCoaEarnedPenaltyAccountNumber(glEarnedPenaltyAccount != null ? glEarnedPenaltyAccount.getCoaAccountNumber() : null);

                mdetails.setCcGlCoaReceivableAccountDescription(glReceivableAccount.getCoaAccountDescription());
                mdetails.setCcGlCoaRevenueAccountDescription(glRevenueAccount != null ? glRevenueAccount.getCoaAccountDescription() : null);

                mdetails.setCcGlCoaUnEarnedInterestAccountDescription(glUnEarnedInterestAccount != null ? glUnEarnedInterestAccount.getCoaAccountDescription() : null);
                mdetails.setCcGlCoaEarnedInterestAccountDescription(glEarnedInterestAccount != null ? glEarnedInterestAccount.getCoaAccountDescription() : null);
                mdetails.setCcGlCoaUnEarnedPenaltyAccountDescription(glUnEarnedPenaltyAccount != null ? glUnEarnedPenaltyAccount.getCoaAccountDescription() : null);
                mdetails.setCcGlCoaEarnedPenaltyAccountDescription(glEarnedPenaltyAccount != null ? glEarnedPenaltyAccount.getCoaAccountDescription() : null);

                mdetails.setCcEnable(arCustomerClass.getCcEnable());
                mdetails.setCcEnableRebate(arCustomerClass.getCcEnableRebate());
                mdetails.setCcAutoComputeInterest(arCustomerClass.getCcAutoComputeInterest());
                mdetails.setCcAutoComputePenalty(arCustomerClass.getCcAutoComputePenalty());

                mdetails.setCcCreditLimit(arCustomerClass.getCcCreditLimit());
                // get revenue account description

                if (glRevenueAccount != null) {

                    mdetails.setCcGlCoaRevenueAccountDescription(glReceivableAccount.getCoaAccountDescription());
                }

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addArCcEntry(ArModCustomerClassDetails mdetails, String TC_NM, String WTC_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ArCCCoaGlReceivableAccountNotFoundException, ArCCCoaGlRevenueAccountNotFoundException, ArCCCoaGlUnEarnedInterestAccountNotFoundException, ArCCCoaGlEarnedInterestAccountNotFoundException, ArCCCoaGlUnEarnedPenaltyAccountNotFoundException, ArCCCoaGlEarnedPenaltyAccountNotFoundException {
        Debug.print("ArCustomerClassControllerBean addArCcEntry");
        try {

            LocalArCustomerClass arCustomerClass = null;
            LocalGlChartOfAccount glReceivableAccount = null;
            LocalGlChartOfAccount glRevenueAccount = null;

            LocalGlChartOfAccount glUnEarnedInterestAccount = null;
            LocalGlChartOfAccount glEarnedInterestAccount = null;
            LocalGlChartOfAccount glUnEarnedPenaltyAccount = null;
            LocalGlChartOfAccount glEarnedPenaltyAccount = null;

            try {

                arCustomerClass = arCustomerClassHome.findByCcName(mdetails.getCcName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // get receivable and revenue account to validate accounts

            try {

                glReceivableAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaReceivableAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                throw new ArCCCoaGlReceivableAccountNotFoundException();
            }

            try {

                glRevenueAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaRevenueAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                if (this.getArCcGlCoaRevenueAccountEnable(AD_CMPNY)) {

                    throw new ArCCCoaGlRevenueAccountNotFoundException();
                }
            }

            if (mdetails.getCcAutoComputeInterest() == EJBCommon.TRUE) {

                try {

                    glUnEarnedInterestAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaUnEarnedInterestAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlUnEarnedInterestAccountNotFoundException();
                }

                try {

                    glEarnedInterestAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaEarnedInterestAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlEarnedInterestAccountNotFoundException();
                }
            }

            if (mdetails.getCcAutoComputePenalty() == EJBCommon.TRUE) {

                try {

                    glUnEarnedPenaltyAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaUnEarnedPenaltyAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlUnEarnedPenaltyAccountNotFoundException();
                }

                try {

                    glEarnedPenaltyAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaEarnedPenaltyAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlEarnedPenaltyAccountNotFoundException();
                }
            }

            // create new customer class

            arCustomerClass = arCustomerClassHome.create(mdetails.getCcName(), mdetails.getCcDescription(), mdetails.getCcNextCustomerCode(), mdetails.getCcCustomerBatch(), mdetails.getCcDealPrice(), mdetails.getCcMonthlyInterestRate(), mdetails.getCcMinimumFinanceCharge(), mdetails.getCcGracePeriodDay(), mdetails.getCcDaysInPeriod(), null, mdetails.getCcChargeByDueDate(), glReceivableAccount.getCoaCode(), glRevenueAccount != null ? glRevenueAccount.getCoaCode() : null, glUnEarnedInterestAccount != null ? glUnEarnedInterestAccount.getCoaCode() : null, glEarnedInterestAccount != null ? glEarnedInterestAccount.getCoaCode() : null, glUnEarnedPenaltyAccount != null ? glUnEarnedPenaltyAccount.getCoaCode() : null, glEarnedPenaltyAccount != null ? glEarnedPenaltyAccount.getCoaCode() : null, mdetails.getCcEnable(), mdetails.getCcEnableRebate(), mdetails.getCcAutoComputeInterest(), mdetails.getCcAutoComputePenalty(), mdetails.getCcCreditLimit(), AD_CMPNY);

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            arTaxCode.addArCustomerClass(arCustomerClass);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
            arWithholdingTaxCode.addArCustomerClass(arCustomerClass);

        } catch (GlobalRecordAlreadyExistException | ArCCCoaGlEarnedPenaltyAccountNotFoundException |
                 ArCCCoaGlUnEarnedPenaltyAccountNotFoundException | ArCCCoaGlEarnedInterestAccountNotFoundException |
                 ArCCCoaGlUnEarnedInterestAccountNotFoundException | ArCCCoaGlRevenueAccountNotFoundException |
                 ArCCCoaGlReceivableAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArCcEntry(ArModCustomerClassDetails mdetails, String TC_NM, String WTC_NM, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, ArCCCoaGlReceivableAccountNotFoundException, ArCCCoaGlRevenueAccountNotFoundException, ArCCCoaGlUnEarnedInterestAccountNotFoundException, ArCCCoaGlEarnedInterestAccountNotFoundException, ArCCCoaGlUnEarnedPenaltyAccountNotFoundException, ArCCCoaGlEarnedPenaltyAccountNotFoundException {
        Debug.print("ArCustomerClassControllerBean updateArCcEntry");
        try {

            LocalArCustomerClass arCustomerClass = null;
            LocalGlChartOfAccount glFinanceChargeAccount = null;
            LocalGlChartOfAccount glReceivableAccount = null;
            LocalGlChartOfAccount glRevenueAccount = null;
            LocalGlChartOfAccount glUnEarnedInterestAccount = null;
            LocalGlChartOfAccount glEarnedInterestAccount = null;
            LocalGlChartOfAccount glUnEarnedPenaltyAccount = null;
            LocalGlChartOfAccount glEarnedPenaltyAccount = null;

            try {

                LocalArCustomerClass arExistingCustomerClass = arCustomerClassHome.findByCcName(mdetails.getCcName(), AD_CMPNY);

                if (!arExistingCustomerClass.getCcCode().equals(mdetails.getCcCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // get receivable and revenue account to validate accounts

            try {

                glReceivableAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaReceivableAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                throw new ArCCCoaGlReceivableAccountNotFoundException();
            }

            try {

                glRevenueAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaRevenueAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                if (this.getArCcGlCoaRevenueAccountEnable(AD_CMPNY)) {

                    throw new ArCCCoaGlRevenueAccountNotFoundException();
                }
            }

            if (mdetails.getCcAutoComputeInterest() == EJBCommon.TRUE) {

                try {

                    glUnEarnedInterestAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaUnEarnedInterestAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlUnEarnedInterestAccountNotFoundException();
                }

                try {

                    glEarnedInterestAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaEarnedInterestAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlEarnedInterestAccountNotFoundException();
                }
            }

            if (mdetails.getCcAutoComputePenalty() == EJBCommon.TRUE) {

                try {

                    glUnEarnedPenaltyAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaUnEarnedPenaltyAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlUnEarnedPenaltyAccountNotFoundException();
                }

                try {

                    glEarnedPenaltyAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getCcGlCoaEarnedPenaltyAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new ArCCCoaGlEarnedPenaltyAccountNotFoundException();
                }
            }

            // find and update customer class
            Debug.print("--------------------------->");
            arCustomerClass = arCustomerClassHome.findByPrimaryKey(mdetails.getCcCode());

            arCustomerClass.setCcName(mdetails.getCcName());
            arCustomerClass.setCcDescription(mdetails.getCcDescription());
            arCustomerClass.setCcNextCustomerCode(mdetails.getCcNextCustomerCode());
            arCustomerClass.setCcCustomerBatch(mdetails.getCcCustomerBatch());
            arCustomerClass.setCcDealPrice(mdetails.getCcDealPrice());
            arCustomerClass.setCcMonthlyInterestRate(mdetails.getCcMonthlyInterestRate());
            arCustomerClass.setCcMinimumFinanceCharge(mdetails.getCcMinimumFinanceCharge());
            arCustomerClass.setCcGracePeriodDay(mdetails.getCcGracePeriodDay());
            arCustomerClass.setCcDaysInPeriod(mdetails.getCcDaysInPeriod());
            arCustomerClass.setCcGlCoaChargeAccount(null);
            arCustomerClass.setCcChargeByDueDate(mdetails.getCcChargeByDueDate());
            arCustomerClass.setCcGlCoaReceivableAccount(glReceivableAccount.getCoaCode());
            arCustomerClass.setCcGlCoaRevenueAccount(glRevenueAccount != null ? glRevenueAccount.getCoaCode() : null);

            arCustomerClass.setCcGlCoaUnEarnedInterestAccount(glUnEarnedInterestAccount != null ? glUnEarnedInterestAccount.getCoaCode() : null);
            arCustomerClass.setCcGlCoaEarnedInterestAccount(glEarnedInterestAccount != null ? glEarnedInterestAccount.getCoaCode() : null);
            arCustomerClass.setCcGlCoaUnEarnedPenaltyAccount(glUnEarnedPenaltyAccount != null ? glUnEarnedPenaltyAccount.getCoaCode() : null);
            arCustomerClass.setCcGlCoaEarnedPenaltyAccount(glEarnedPenaltyAccount != null ? glEarnedPenaltyAccount.getCoaCode() : null);

            Debug.print("mdetails.getCcAutoComputeInterest()=" + mdetails.getCcAutoComputeInterest());
            Debug.print("mdetails.getCcAutoComputePenalty()=" + mdetails.getCcAutoComputePenalty());
            arCustomerClass.setCcEnable(mdetails.getCcEnable());
            arCustomerClass.setCcEnableRebate(mdetails.getCcEnableRebate());
            arCustomerClass.setCcAutoComputeInterest(mdetails.getCcAutoComputeInterest());
            arCustomerClass.setCcAutoComputePenalty(mdetails.getCcAutoComputePenalty());
            arCustomerClass.setCcCreditLimit(mdetails.getCcCreditLimit());

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            arTaxCode.addArCustomerClass(arCustomerClass);

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);
            arWithholdingTaxCode.addArCustomerClass(arCustomerClass);

        } catch (GlobalRecordAlreadyExistException | ArCCCoaGlEarnedPenaltyAccountNotFoundException |
                 ArCCCoaGlUnEarnedPenaltyAccountNotFoundException | ArCCCoaGlEarnedInterestAccountNotFoundException |
                 ArCCCoaGlUnEarnedInterestAccountNotFoundException | ArCCCoaGlRevenueAccountNotFoundException |
                 ArCCCoaGlReceivableAccountNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArCcEntry(Integer CC_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {
        Debug.print("ArCustomerClassControllerBean deleteArCcEntry");
        try {

            LocalArCustomerClass arCustomerClass = null;

            try {

                arCustomerClass = arCustomerClassHome.findByPrimaryKey(CC_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!arCustomerClass.getArCustomers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    arCustomerClass.entityRemove();
            em.remove(arCustomerClass);

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArCustomerClassControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getArCcGlCoaRevenueAccountEnable(Integer AD_CMPNY) {
        Debug.print("ArCustomerClassControllerBean getArCcGlCoaRevenueAccountEnable");
        try {

            Collection arAutoAccountingSegments = arAutoAccountingSegmentHome.findByAasClassTypeAndAaAccountType("AR CUSTOMER", "REVENUE", AD_CMPNY);

            return !arAutoAccountingSegments.isEmpty();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArCustomerClassControllerBean ejbCreate");
    }

}