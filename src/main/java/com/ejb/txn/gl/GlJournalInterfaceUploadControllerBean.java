/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlJournalInterfaceUploadControllerBean
 * @created Januray 09, 2004, 10:20 AM
 * @author Dennis M. Hilario
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.exception.gl.GlJRIJournalNotBalanceException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
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
import com.util.gl.GlJournalLineInterfaceDetails;
import com.util.gl.GlJournalInterfaceDetails;

@Stateless(name = "GlJournalInterfaceUploadControllerEJB")
public class GlJournalInterfaceUploadControllerBean extends EJBContextClass implements GlJournalInterfaceUploadController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;

    public void saveGlJriEntry(GlJournalInterfaceDetails details, ArrayList glJournalLineInterfaceList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlJRIJournalNotBalanceException {

        Debug.print("GlJournalInterfaceUploadControllerBean saveGlJriEntry");

        try {

            // check if journal exists

            LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.findByJriName(details.getJriName(), AD_CMPNY);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // check if total debits and total credits are balance
            // check account number if valid

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            for (Object o : glJournalLineInterfaceList) {

                GlJournalLineInterfaceDetails mdetails = (GlJournalLineInterfaceDetails) o;

                try {

                    LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getJliCoaAccountNumber(), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalAccountNumberInvalidException();
                }

                if (mdetails.getJliDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT = TOTAL_DEBIT + mdetails.getJliAmount();

                } else {

                    TOTAL_CREDIT = TOTAL_CREDIT + mdetails.getJliAmount();
                }
            }

            if (TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlJRIJournalNotBalanceException();
            }

        } catch (GlJRIJournalNotBalanceException | GlobalAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalGlJournalInterface glJournalInterface = glJournalInterfaceHome.create(details.getJriName(), details.getJriDescription(), details.getJriEffectiveDate(), "GENERAL", "MANUAL", "PHP", null, null, null, 1.0, 'N', EJBCommon.FALSE, null, null, null, AD_BRNCH, AD_CMPNY);

            Iterator i = glJournalLineInterfaceList.iterator();

            short ctr = 0;

            while (i.hasNext()) {

                GlJournalLineInterfaceDetails mdetails = (GlJournalLineInterfaceDetails) i.next();

                ctr++;

                LocalGlJournalLineInterface glJournalLineInterface = glJournalLineInterfaceHome.create(ctr, mdetails.getJliDebit(), mdetails.getJliAmount(), mdetails.getJliCoaAccountNumber(), AD_CMPNY);

                glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);
            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlJournalInterfaceUploadControllerBean ejbCreate");
    }
}