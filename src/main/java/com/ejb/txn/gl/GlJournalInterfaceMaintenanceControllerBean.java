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
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlCOANoChartOfAccountFoundException;
import com.ejb.exception.gl.GlJLINoJournalLineInterfacesFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.ejb.dao.gl.LocalGlJournalInterfaceHome;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.ejb.dao.gl.LocalGlJournalLineInterfaceHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlJournalInterfaceDetails;
import com.util.gl.GlJournalLineInterfaceDetails;
import com.util.mod.gl.GlModJournalLineInterfaceDetails;

@Stateless(name = "GlJournalInterfaceMaintenanceControllerEJB")
public class GlJournalInterfaceMaintenanceControllerBean extends EJBContextClass implements GlJournalInterfaceMaintenanceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;


    public ArrayList getGlJliByJriCode(Integer JRI_CODE, Integer AD_CMPNY) throws GlJLINoJournalLineInterfacesFoundException {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean getGlJliByJriCode");

        ArrayList jliAllOfJriList = new ArrayList();
        LocalGlJournalInterface glJournalInterface = null;
        Collection glJournalLineInterfaces = null;

        try {

            glJournalInterface = glJournalInterfaceHome.findByPrimaryKey(JRI_CODE);

            glJournalLineInterfaces = glJournalInterface.getGlJournalLineInterfaces();

            if (glJournalLineInterfaces.size() == 0) throw new GlJLINoJournalLineInterfacesFoundException();

            for (Object journalLineInterface : glJournalLineInterfaces) {

                LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) journalLineInterface;

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(glJournalLineInterface.getJliCoaAccountNumber(), AD_CMPNY);

                GlModJournalLineInterfaceDetails details = new GlModJournalLineInterfaceDetails(glJournalLineInterface.getJliCode(), glJournalLineInterface.getJliLineNumber(), glJournalLineInterface.getJliDebit(), glJournalLineInterface.getJliAmount(), glJournalLineInterface.getJliCoaAccountNumber(), glChartOfAccount.getCoaAccountDescription());

                jliAllOfJriList.add(details);
            }

            return jliAllOfJriList;

        } catch (GlJLINoJournalLineInterfacesFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlJournalInterfaceDetails getGlJriByJriCode(Integer JRI_CODE, Integer AD_CMPNY) {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean getGlJriByJriCode");

        LocalGlJournalInterface glJournalInterface = null;

        try {

            glJournalInterface = glJournalInterfaceHome.findByPrimaryKey(JRI_CODE);

            GlJournalInterfaceDetails details = new GlJournalInterfaceDetails();
            details.setJriCode(glJournalInterface.getJriCode());
            details.setJriName(glJournalInterface.getJriName());
            details.setJriDescription(glJournalInterface.getJriDescription());
            details.setJriEffectiveDate(glJournalInterface.getJriEffectiveDate());
            details.setJriJournalCategory(glJournalInterface.getJriJournalCategory());
            details.setJriJournalSource(glJournalInterface.getJriJournalSource());
            details.setJriFunctionalCurrency(glJournalInterface.getJriFunctionalCurrency());
            details.setJriDateReversal(glJournalInterface.getJriDateReversal());
            details.setJriDocumentNumber(glJournalInterface.getJriDocumentNumber());
            details.setJriConversionDate(glJournalInterface.getJriConversionDate());
            details.setJriConversionRate(glJournalInterface.getJriConversionRate());
            details.setJriFundStatus(glJournalInterface.getJriFundStatus());
            details.setJriReversed(glJournalInterface.getJriReversed());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void saveGlJriEntry(GlJournalInterfaceDetails details, ArrayList list, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlCOANoChartOfAccountFoundException {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean saveGlJriEntry");

        LocalGlJournalInterface glJournalInterface = null;

        GlJournalLineInterfaceDetails lineDetails = null;

        try {

            glJournalInterface = glJournalInterfaceHome.findByJriName(details.getJriName(), AD_CMPNY);

            if (!glJournalInterface.getJriCode().equals(details.getJriCode())) {

                throw new GlobalRecordAlreadyExistException();
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            glJournalInterface = glJournalInterfaceHome.findByPrimaryKey(details.getJriCode());

            // remove all journal line interfaces

            Collection glJournalLineInterfaces = glJournalInterface.getGlJournalLineInterfaces();

            Iterator i = glJournalLineInterfaces.iterator();

            while (i.hasNext()) {

                LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) i.next();

                i.remove();

                //	  	  	   	glJournalLineInterface.entityRemove();
                em.remove(glJournalLineInterface);
            }

            // add new journal line interfaces

            i = list.iterator();

            while (i.hasNext()) {

                GlJournalLineInterfaceDetails mdetails = (GlJournalLineInterfaceDetails) i.next();

                lineDetails = new GlJournalLineInterfaceDetails();
                lineDetails.setJliLineNumber(mdetails.getJliLineNumber());
                lineDetails.setJliDebit(mdetails.getJliDebit());
                lineDetails.setJliAmount(mdetails.getJliAmount());
                lineDetails.setJliCoaAccountNumber(mdetails.getJliCoaAccountNumber());

                this.addGlJliEntry(glJournalInterface, lineDetails, details.getJriCode(), AD_CMPNY);
            }

            glJournalInterface.setJriName(details.getJriName());
            glJournalInterface.setJriDescription(details.getJriDescription());
            glJournalInterface.setJriEffectiveDate(details.getJriEffectiveDate());
            glJournalInterface.setJriJournalCategory(details.getJriJournalCategory());
            glJournalInterface.setJriJournalSource(details.getJriJournalSource());
            glJournalInterface.setJriFunctionalCurrency(details.getJriFunctionalCurrency());
            glJournalInterface.setJriDateReversal(details.getJriDateReversal());
            glJournalInterface.setJriDocumentNumber(details.getJriDocumentNumber());
            glJournalInterface.setJriConversionDate(details.getJriConversionDate());
            glJournalInterface.setJriConversionRate(details.getJriConversionRate());
            glJournalInterface.setJriFundStatus(details.getJriFundStatus());
            glJournalInterface.setJriReversed(details.getJriReversed());

        } catch (FinderException ex) {

        } catch (GlCOANoChartOfAccountFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlCOANoChartOfAccountFoundException(String.valueOf(lineDetails.getJliLineNumber()));

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlJriEntry(Integer JRI_CODE, Integer AD_CMPNY) {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean deleteGlJriEntry");

        LocalGlJournalInterface glJournalInterface = null;
        GlJournalLineInterfaceDetails lineDetails = null;

        try {

            glJournalInterface = glJournalInterfaceHome.findByPrimaryKey(JRI_CODE);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // remove all journal line interfaces

        Collection glJournalLineInterfaces = glJournalInterface.getGlJournalLineInterfaces();

        Iterator i = glJournalLineInterfaces.iterator();

        while (i.hasNext()) {

            LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) i.next();

            i.remove();

            try {

                em.remove(glJournalLineInterface);

            } catch (Exception ex) {

                getSessionContext().setRollbackOnly();

                throw new EJBException(ex.getMessage());

            }
        }

        // delete journal interface

        try {

            em.remove(glJournalInterface);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();

            throw new EJBException(ex.getMessage());

        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addGlJliEntry(LocalGlJournalInterface glJournalInterface, GlJournalLineInterfaceDetails lineDetails, Integer JRI_CODE, Integer AD_CMPNY) throws GlCOANoChartOfAccountFoundException {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean addGlJliEntry");

        LocalGlJournalLineInterface glJournalLineInterface = null;
        LocalGlChartOfAccount glChartOfAccount = null;
        LocalAdCompany adCompany = null;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // get extended precision

        short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcPrecision();

        // validate if coa exists

        try {

            glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(lineDetails.getJliCoaAccountNumber(), AD_CMPNY);

        } catch (FinderException ex) {

            throw new GlCOANoChartOfAccountFoundException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {

            throw new GlCOANoChartOfAccountFoundException();
        }

        // create journal

        try {

            glJournalLineInterface = glJournalLineInterfaceHome.create(lineDetails.getJliLineNumber(), lineDetails.getJliDebit(), EJBCommon.roundIt(lineDetails.getJliAmount(), FC_EXTNDD_PRCSN), lineDetails.getJliCoaAccountNumber(), AD_CMPNY);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

        // add journal line interface to journal interface

        try {

            glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalInterfaceMaintenanceControllerBean ejbCreate");
    }
}