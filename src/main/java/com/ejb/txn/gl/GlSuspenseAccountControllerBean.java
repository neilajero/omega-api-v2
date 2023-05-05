package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.gl.GlCOANoChartOfAccountFoundException;
import com.ejb.exception.gl.GlJCNoJournalCategoryFoundException;
import com.ejb.exception.gl.GlJSNoJournalSourceFoundException;
import com.ejb.exception.gl.GlSANoSuspenseAccountFoundException;
import com.ejb.exception.gl.GlSASourceCategoryCombinationAlreadyExistException;
import com.ejb.exception.gl.GlSASuspenseAccountAlreadyDeletedException;
import com.ejb.exception.gl.GlSASuspenseAccountAlreadyExistException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlJournalCategory;
import com.ejb.dao.gl.LocalGlJournalCategoryHome;
import com.ejb.entities.gl.LocalGlJournalSource;
import com.ejb.dao.gl.LocalGlJournalSourceHome;
import com.ejb.entities.gl.LocalGlSuspenseAccount;
import com.ejb.dao.gl.LocalGlSuspenseAccountHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlChartOfAccountDetails;
import com.util.gl.GlJournalCategoryDetails;
import com.util.gl.GlJournalSourceDetails;
import com.util.mod.gl.GlModSuspenseAccountDetails;
import com.util.gl.GlSuspenseAccountDetails;

@Stateless(name = "GlSuspenseAccountControllerEJB")
public class GlSuspenseAccountControllerBean extends EJBContextClass implements GlSuspenseAccountController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;


    public ArrayList getGlJcAll(Integer AD_CMPNY) throws GlJCNoJournalCategoryFoundException {

        Debug.print("GlJCNoJournalCategoryFoundException getJcAll");

        ArrayList jcAllList = new ArrayList();
        Collection glJournalCategories = null;

        try {
            glJournalCategories = glJournalCategoryHome.findJcAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glJournalCategories.size() == 0) throw new GlJCNoJournalCategoryFoundException();

        for (Object journalCategory : glJournalCategories) {
            LocalGlJournalCategory glJournalCategory = (LocalGlJournalCategory) journalCategory;

            GlJournalCategoryDetails details = new GlJournalCategoryDetails(glJournalCategory.getJcCode(), glJournalCategory.getJcName(), glJournalCategory.getJcDescription(), glJournalCategory.getJcReversalMethod());

            jcAllList.add(details);
        }

        return jcAllList;
    }

    public ArrayList getGlJsAll(Integer AD_CMPNY) throws GlJSNoJournalSourceFoundException {

        Debug.print("GlSuspenseAccountControllerBean getJsAll");

        ArrayList jsAllList = new ArrayList();
        Collection glJournalSources = null;

        try {
            glJournalSources = glJournalSourceHome.findJsAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glJournalSources.size() == 0) throw new GlJSNoJournalSourceFoundException();

        for (Object journalSource : glJournalSources) {
            LocalGlJournalSource glJournalSource = (LocalGlJournalSource) journalSource;

            GlJournalSourceDetails details = new GlJournalSourceDetails(glJournalSource.getJsCode(), glJournalSource.getJsName(), glJournalSource.getJsDescription(), glJournalSource.getJsFreezeJournal(), glJournalSource.getJsJournalApproval(), glJournalSource.getJsEffectiveDateRule());

            jsAllList.add(details);
        }
        return jsAllList;
    }

    public ArrayList getGlCoaAll(Integer AD_CMPNY) throws GlCOANoChartOfAccountFoundException {

        Debug.print("GlSuspenseAccountControllerBean getCoaAll");

        ArrayList coaAllList = new ArrayList();
        Collection glChartOfAccounts = null;

        try {
            glChartOfAccounts = glChartOfAccountHome.findCoaAllEnabled(new Date(EJBCommon.getGcCurrentDateWoTime().getTime().getTime()), AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glChartOfAccounts.size() == 0) throw new GlCOANoChartOfAccountFoundException();

        for (Object chartOfAccount : glChartOfAccounts) {
            LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

            GlChartOfAccountDetails details = new GlChartOfAccountDetails(glChartOfAccount.getCoaCode(), glChartOfAccount.getCoaAccountNumber(), glChartOfAccount.getCoaAccountNumber(), glChartOfAccount.getCoaCitCategory(), glChartOfAccount.getCoaSawCategory(), glChartOfAccount.getCoaIitCategory(), glChartOfAccount.getCoaAccountType(), glChartOfAccount.getCoaTaxType(), glChartOfAccount.getCoaDateFrom(), glChartOfAccount.getCoaDateTo(), glChartOfAccount.getCoaEnable(), EJBCommon.TRUE);

            coaAllList.add(details);
        }

        return coaAllList;
    }

    public ArrayList getGlSaAll(Integer AD_CMPNY) throws GlSANoSuspenseAccountFoundException {

        Debug.print("GlSuspenseAccountControllerBean getGlSaAll");

        Collection glSuspenseAccounts = null;

        ArrayList saAllList = new ArrayList();

        try {
            glSuspenseAccounts = glSuspenseAccountHome.findSaAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glSuspenseAccounts.size() == 0) throw new GlSANoSuspenseAccountFoundException();

        Iterator i = glSuspenseAccounts.iterator();
        while (i.hasNext()) {

            try {

                LocalGlSuspenseAccount glSuspenseAccount = (LocalGlSuspenseAccount) i.next();

                GlModSuspenseAccountDetails details = new GlModSuspenseAccountDetails(glSuspenseAccount.getSaCode(), glSuspenseAccount.getSaName(), glSuspenseAccount.getSaDescription(), glSuspenseAccount.getGlJournalCategory().getJcName(), glSuspenseAccount.getGlJournalSource().getJsName(), glSuspenseAccount.getGlChartOfAccount().getCoaAccountNumber(), glSuspenseAccount.getGlChartOfAccount().getCoaAccountDescription());

                saAllList.add(details);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return saAllList;
    }

    public void deleteGlSaEntry(Integer SA_CODE, Integer AD_CMPNY) throws GlSASuspenseAccountAlreadyDeletedException {

        Debug.print("GlSuspenseAccountControllerBead deleteGlSaEntry");

        LocalGlSuspenseAccount glSuspenseAccount = null;

        try {
            glSuspenseAccount = glSuspenseAccountHome.findByPrimaryKey(SA_CODE);
        } catch (FinderException ex) {
            throw new GlSASuspenseAccountAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            //         glSuspenseAccount.entityRemove();
            em.remove(glSuspenseAccount);
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void addGlSaEntry(GlSuspenseAccountDetails details, String SA_JC_NM, String SA_JS_NM, String SA_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlSASuspenseAccountAlreadyExistException, GlJSNoJournalSourceFoundException, GlJCNoJournalCategoryFoundException, GlCOANoChartOfAccountFoundException, GlSASourceCategoryCombinationAlreadyExistException {

        Debug.print("GlSuspenseAccountControllerBean addGlSaEntry");

        LocalGlJournalCategory glJournalCategory = null;
        LocalGlJournalSource glJournalSource = null;
        LocalGlChartOfAccount glChartOfAccount = null;

        Collection glSuspenseAccounts = null;

        try {
            glJournalCategory = glJournalCategoryHome.findByJcName(SA_JC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlJCNoJournalCategoryFoundException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glJournalSource = glJournalSourceHome.findByJsName(SA_JS_NM, AD_CMPNY);
        } catch (FinderException ex) {
            ex.printStackTrace();
            throw new GlJSNoJournalSourceFoundException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SA_COA_ACCNT_NMBR, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlCOANoChartOfAccountFoundException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glSuspenseAccounts = glSuspenseAccountHome.findSaAll(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {

            glSuspenseAccountHome.findBySaName(details.getSaName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlSASuspenseAccountAlreadyExistException();

        } catch (FinderException ex) {

        }

        if (glSuspenseAccounts.size() == 0) {

            try {
                LocalGlSuspenseAccount glSuspenseAccount = glSuspenseAccountHome.create(details.getSaName(), details.getSaDescription(), AD_CMPNY);

                glJournalSource.addGlSuspenseAccount(glSuspenseAccount);
                glJournalCategory.addGlSuspenseAccount(glSuspenseAccount);
                glChartOfAccount.addGlSuspenseAccount(glSuspenseAccount);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

        } else {
            for (Object suspenseAccount : glSuspenseAccounts) {
                LocalGlSuspenseAccount glSuspenseAccount = (LocalGlSuspenseAccount) suspenseAccount;

                String JC_NM = glSuspenseAccount.getGlJournalCategory().getJcName();
                String JS_NM = glSuspenseAccount.getGlJournalSource().getJsName();

                if (JC_NM.equals(SA_JC_NM) && JS_NM.equals(SA_JS_NM))
                    throw new GlSASourceCategoryCombinationAlreadyExistException();
            }

            try {
                LocalGlSuspenseAccount glSuspenseAccount = glSuspenseAccountHome.create(details.getSaName(), details.getSaDescription(), AD_CMPNY);

                glJournalCategory.addGlSuspenseAccount(glSuspenseAccount);
                glJournalSource.addGlSuspenseAccount(glSuspenseAccount);
                glChartOfAccount.addGlSuspenseAccount(glSuspenseAccount);
            } catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException();
            }
        }
    }

    public void updateGlSaEntry(GlSuspenseAccountDetails details, String SA_JC_NM, String SA_JS_NM, String SA_COA_ACCNT_NMBR, Integer AD_CMPNY) throws GlSASuspenseAccountAlreadyExistException, GlJSNoJournalSourceFoundException, GlJCNoJournalCategoryFoundException, GlCOANoChartOfAccountFoundException, GlSASourceCategoryCombinationAlreadyExistException, GlSASuspenseAccountAlreadyDeletedException {

        Debug.print("GlSuspenseAccountController updateGlSaEntry");

        LocalGlJournalCategory glJournalCategory = null;
        LocalGlJournalSource glJournalSource = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalGlSuspenseAccount glSuspenseAccount = null;
        LocalGlSuspenseAccount glSuspenseAccountByName = null;

        Collection glSuspenseAccounts = null;

        try {
            glJournalCategory = glJournalCategoryHome.findByJcName(SA_JC_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlJCNoJournalCategoryFoundException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glJournalSource = glJournalSourceHome.findByJsName(SA_JS_NM, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlJSNoJournalSourceFoundException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(SA_COA_ACCNT_NMBR, AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlCOANoChartOfAccountFoundException();
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        try {
            glSuspenseAccounts = glSuspenseAccountHome.findSaAll(AD_CMPNY);
        } catch (FinderException ex) {
            throw new GlSASuspenseAccountAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        for (Object suspenseAccount : glSuspenseAccounts) {
            LocalGlSuspenseAccount glSuspenseAccount2 = (LocalGlSuspenseAccount) suspenseAccount;

            String JC_NM = glSuspenseAccount2.getGlJournalCategory().getJcName();
            String JS_NM = glSuspenseAccount2.getGlJournalSource().getJsName();
            Integer SA_CODE = glSuspenseAccount2.getSaCode();
            boolean isCombinationExist = false;

            Debug.print(JC_NM + ", " + JS_NM + ", " + SA_CODE + " :: " + SA_JC_NM + ", " + SA_JS_NM + ", " + details.getSaCode());

            isCombinationExist = JC_NM.equals(SA_JC_NM) && JS_NM.equals(SA_JS_NM);

            if (isCombinationExist) {
                if (!SA_CODE.equals(details.getSaCode()))
                    throw new GlSASourceCategoryCombinationAlreadyExistException();
            }

            if (glSuspenseAccount2.getSaName().equals(details.getSaName()) && !glSuspenseAccount2.getSaCode().equals(details.getSaCode()))
                throw new GlSASuspenseAccountAlreadyExistException();
        }

        try {
            glSuspenseAccount = glSuspenseAccountHome.findByPrimaryKey(details.getSaCode());
        } catch (FinderException ex) {
            throw new GlSASuspenseAccountAlreadyDeletedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        try {
            glSuspenseAccount.setSaName(details.getSaName());
            glSuspenseAccount.setSaDescription(details.getSaDescription());

            glJournalCategory.addGlSuspenseAccount(glSuspenseAccount);
            glJournalSource.addGlSuspenseAccount(glSuspenseAccount);
            glChartOfAccount.addGlSuspenseAccount(glSuspenseAccount);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // Session Methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlSuspenseAccountControllerBean ejbCreate");
    }
}