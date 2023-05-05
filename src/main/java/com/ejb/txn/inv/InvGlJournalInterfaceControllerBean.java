/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvGlJournalInterfaceControllerBean
 * @created Aug 12, 2004, 12:59 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.gl.LocalGlJournalInterfaceHome;
import com.ejb.dao.gl.LocalGlJournalLineInterfaceHome;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.dao.inv.LocalInvDistributionRecordHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.entities.inv.LocalInvDistributionRecord;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvGlJournalInterfaceControllerEJB")
public class InvGlJournalInterfaceControllerBean extends EJBContextClass
        implements InvGlJournalInterfaceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;

    public long executeInvGlJriRun(Date JRI_DT_FRM, Date JRI_DT_TO, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvGlJournalInterfaceControllerBean executeInvGlJriRun");

        java.text.SimpleDateFormat formatter =
                new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
        long IMPORTED_JOURNALS = 0L;
        short lineNumber = 0;
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlFunctionalCurrency glFunctionalCurrency = adCompany.getGlFunctionalCurrency();

            // inventory adjustments

            Collection invAdjustments =
                    invAdjustmentHome.findPostedAdjByAdjDateRange(JRI_DT_FRM, JRI_DT_TO, AD_CMPNY);

            Iterator i = invAdjustments.iterator();

            while (i.hasNext()) {

                LocalInvAdjustment invAdjustment = (LocalInvAdjustment) i.next();

                Collection invDistributionRecords =
                        invDistributionRecordHome.findImportableDrByAdjCode(
                                invAdjustment.getAdjCode(), AD_CMPNY);

                if (!invDistributionRecords.isEmpty()) {

                    LocalGlJournalInterface glJournalInterface =
                            glJournalInterfaceHome.create(
                                    invAdjustment.getAdjReferenceNumber(),
                                    invAdjustment.getAdjDescription(),
                                    invAdjustment.getAdjDate(),
                                    "INVENTORY ADJUSTMENTS",
                                    "INVENTORY",
                                    glFunctionalCurrency.getFcName(),
                                    null,
                                    invAdjustment.getAdjDocumentNumber(),
                                    null,
                                    1d,
                                    'N',
                                    EJBCommon.FALSE,
                                    null,
                                    null,
                                    null,
                                    AD_BRNCH,
                                    AD_CMPNY);

                    IMPORTED_JOURNALS++;
                    lineNumber = 0;

                    for (Object distributionRecord : invDistributionRecords) {

                        LocalInvDistributionRecord invDistributionRecord =
                                (LocalInvDistributionRecord) distributionRecord;

                        LocalGlJournalLineInterface glJournalLineInterface =
                                glJournalLineInterfaceHome.create(
                                        ++lineNumber,
                                        invDistributionRecord.getDrDebit(),
                                        invDistributionRecord.getDrAmount(),
                                        invDistributionRecord.getInvChartOfAccount().getCoaAccountNumber(),
                                        AD_CMPNY);

                        glJournalInterface.addGlJournalLineInterface(glJournalLineInterface);

                        invDistributionRecord.setDrImported(EJBCommon.TRUE);
                    }
                }
            }

            return IMPORTED_JOURNALS;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvGlJournalInterfaceControllerBean ejbCreate");
    }

}