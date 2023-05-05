package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.ejb.dao.gl.LocalGlJournalBatchHome;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.ejb.entities.gl.LocalGlRecurringJournal;
import com.ejb.dao.gl.LocalGlRecurringJournalHome;
import com.ejb.entities.gl.LocalGlRecurringJournalLine;
import com.ejb.dao.gl.LocalGlRecurringJournalLineHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlRecurringJournalDetails;
import com.util.mod.gl.GlModRecurringJournalDetails;
import com.util.mod.gl.GlModRecurringJournalLineDetails;

@Stateless(name = "GlRecurringJournalEntryControllerEJB")
public class GlRecurringJournalEntryControllerBean extends EJBContextClass implements GlRecurringJournalEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlRecurringJournalHome glRecurringJournalHome;
    @EJB
    private LocalGlRecurringJournalLineHome glRecurringJournalLineHome;

    public ArrayList getGlJcAll(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalEntryControllerBean getGlJcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalCategories = glJournalCategoryHome.findJcAll(AD_CMPNY);

            for (Object journalCategory : glJournalCategories) {

                LocalGlJournalCategory glJournalCategory = (LocalGlJournalCategory) journalCategory;

                list.add(glJournalCategory.getJcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdUsrAll(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalEntryControllerBean getAdUsrAll");

        ArrayList list = new ArrayList();

        try {

            Collection adUsers = adUserHome.findUsrAll(AD_CMPNY);

            for (Object user : adUsers) {

                LocalAdUser adUser = (LocalAdUser) user;

                list.add(adUser.getUsrName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfGlJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalEntryControllerBean getAdPrfGlJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfGlJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlModRecurringJournalDetails getGlRjByRjCode(Integer RJ_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlRecurringJournalEntryControllerBean getGlRjByRjCode");

        try {

            LocalGlRecurringJournal glRecurringJournal = null;

            try {

                glRecurringJournal = glRecurringJournalHome.findByPrimaryKey(RJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList rjRjlList = new ArrayList();

            // get recurring journal lines

            Collection glRecurringJournalLines = glRecurringJournalLineHome.findByRjCode(glRecurringJournal.getRjCode(), AD_CMPNY);

            Iterator i = glRecurringJournalLines.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (i.hasNext()) {

                LocalGlRecurringJournalLine glRecurringJournalLine = (LocalGlRecurringJournalLine) i.next();

                GlModRecurringJournalLineDetails mdetails = new GlModRecurringJournalLineDetails();

                mdetails.setRjlCode(glRecurringJournalLine.getRjlCode());
                mdetails.setRjlLineNumber(glRecurringJournalLine.getRjlLineNumber());
                mdetails.setRjlDebit(glRecurringJournalLine.getRjlDebit());
                mdetails.setRjlAmount(glRecurringJournalLine.getRjlAmount());
                mdetails.setRjlCoaAccountNumber(glRecurringJournalLine.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setRjlCoaAccountDescription(glRecurringJournalLine.getGlChartOfAccount().getCoaAccountDescription());

                if (glRecurringJournalLine.getRjlDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += glRecurringJournalLine.getRjlAmount();

                } else {

                    TOTAL_CREDIT += glRecurringJournalLine.getRjlAmount();
                }

                rjRjlList.add(mdetails);
            }

            GlModRecurringJournalDetails mRjDetails = new GlModRecurringJournalDetails();

            mRjDetails.setRjCode(glRecurringJournal.getRjCode());
            mRjDetails.setRjName(glRecurringJournal.getRjName());
            mRjDetails.setRjDescription(glRecurringJournal.getRjDescription());
            mRjDetails.setRjTotalDebit(TOTAL_DEBIT);
            mRjDetails.setRjTotalCredit(TOTAL_CREDIT);
            mRjDetails.setRjUserName1(glRecurringJournal.getRjUserName1());
            mRjDetails.setRjUserName2(glRecurringJournal.getRjUserName2());
            mRjDetails.setRjUserName3(glRecurringJournal.getRjUserName3());
            mRjDetails.setRjUserName4(glRecurringJournal.getRjUserName4());
            mRjDetails.setRjUserName5(glRecurringJournal.getRjUserName5());
            mRjDetails.setRjSchedule(glRecurringJournal.getRjSchedule());
            mRjDetails.setRjNextRunDate(glRecurringJournal.getRjNextRunDate());
            mRjDetails.setRjLastRunDate(glRecurringJournal.getRjLastRunDate());
            mRjDetails.setRjJcName(glRecurringJournal.getGlJournalCategory().getJcName());
            mRjDetails.setRjRjlList(rjRjlList);
            mRjDetails.setRjJbName(glRecurringJournal.getGlJournalBatch() != null ? glRecurringJournal.getGlJournalBatch().getJbName() : null);

            return mRjDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveGlRjEntry(GlRecurringJournalDetails details, String JC_NM, String JB_NM, ArrayList rjlList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyDeletedException, GlobalAccountNumberInvalidException {

        Debug.print("GlRecurringJournalEntryControllerBean saveGlRjEntry");

        LocalGlRecurringJournal glRecurringJournal = null;

        try {

            // validate if recurring journal is already deleted

            try {

                if (details.getRjCode() != null) {

                    glRecurringJournal = glRecurringJournalHome.findByPrimaryKey(details.getRjCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if recurring journal exists

            try {

                LocalGlRecurringJournal glExistingRecurringJournal = glRecurringJournalHome.findByRjName(details.getRjName(), AD_CMPNY);

                if (details.getRjCode() == null || details.getRjCode() != null && !glExistingRecurringJournal.getRjCode().equals(details.getRjCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // create recurring journal

            if (details.getRjCode() == null) {

                glRecurringJournal = glRecurringJournalHome.create(details.getRjName(), details.getRjDescription(), details.getRjUserName1(), details.getRjUserName2(), details.getRjUserName3(), details.getRjUserName4(), details.getRjUserName5(), details.getRjSchedule(), details.getRjNextRunDate(), null, AD_BRNCH, AD_CMPNY);

            } else {

                glRecurringJournal.setRjName(details.getRjName());
                glRecurringJournal.setRjDescription(details.getRjDescription());
                glRecurringJournal.setRjUserName1(details.getRjUserName1());
                glRecurringJournal.setRjUserName2(details.getRjUserName2());
                glRecurringJournal.setRjUserName3(details.getRjUserName3());
                glRecurringJournal.setRjUserName4(details.getRjUserName4());
                glRecurringJournal.setRjUserName5(details.getRjUserName5());
                glRecurringJournal.setRjSchedule(details.getRjSchedule());
                glRecurringJournal.setRjNextRunDate(details.getRjNextRunDate());
            }

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName(JC_NM, AD_CMPNY);
            glJournalCategory.addGlRecurringJournal(glRecurringJournal);

            try {

                LocalGlJournalBatch glJournalBatch = glJournalBatchHome.findByJbName(JB_NM, AD_BRNCH, AD_CMPNY);
                glJournalBatch.addGlRecurringJournal(glRecurringJournal);

            } catch (FinderException ex) {

            }

            // remove all journal lines

            Collection glRecurringJournalLines = glRecurringJournal.getGlRecurringJournalLines();

            Iterator i = glRecurringJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlRecurringJournalLine glRecurringJournalLine = (LocalGlRecurringJournalLine) i.next();

                i.remove();

                //	  	  	    glRecurringJournalLine.entityRemove();
                em.remove(glRecurringJournalLine);
            }

            // add new recurring journal lines

            i = rjlList.iterator();

            while (i.hasNext()) {

                GlModRecurringJournalLineDetails mRjlDetails = (GlModRecurringJournalLineDetails) i.next();

                this.addGlRjlEntry(mRjlDetails, glRecurringJournal, AD_BRNCH, AD_CMPNY);
            }

        } catch (GlobalRecordAlreadyDeletedException | GlobalAccountNumberInvalidException |
                 GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlRjEntry(Integer RJ_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlRecurringJournalEntryControllerBean deleteGlRjEntry");

        try {

            LocalGlRecurringJournal glRecurringJournal = glRecurringJournalHome.findByPrimaryKey(RJ_CODE);

            //        	glRecurringJournal.entityRemove();
            em.remove(glRecurringJournal);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableGlJournalBatch(Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalEntryControllerBean getAdPrfApEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableGlJournalBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlOpenJbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("GlRecurringJournalEntryControllerBean getGlOpenJbAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalBatches = glJournalBatchHome.findOpenJbAll(AD_BRNCH, AD_CMPNY);

            for (Object journalBatch : glJournalBatches) {

                LocalGlJournalBatch glJournalBatch = (LocalGlJournalBatch) journalBatch;

                list.add(glJournalBatch.getJbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addGlRjlEntry(GlModRecurringJournalLineDetails mdetails, LocalGlRecurringJournal glRecurringJournal, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalAccountNumberInvalidException {

        Debug.print("GlRecurringJournalEntryControllerBean addGlRjlEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getRjlCoaAccountNumber(), AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getRjlLineNumber()));

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getRjlLineNumber()));
            }

            // create journal

            LocalGlRecurringJournalLine glRecurringJournalLine = glRecurringJournalLineHome.create(mdetails.getRjlLineNumber(), mdetails.getRjlDebit(), EJBCommon.roundIt(mdetails.getRjlAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), AD_CMPNY);

            glRecurringJournal.addGlRecurringJournalLine(glRecurringJournalLine);
            glChartOfAccount.addGlRecurringJournalLine(glRecurringJournalLine);

        } catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRecurringJournalEntryControllerBean ejbCreate");
    }
}