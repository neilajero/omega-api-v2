/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFrgRowSetControllerBean
 * @created July 31, 2003, 9:13 AM
 * @author Neil Andrew M. Ajero
 **/


package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.entities.gl.LocalGlFrgRow;
import com.ejb.dao.gl.LocalGlFrgRowHome;
import com.ejb.entities.gl.LocalGlFrgRowSet;
import com.ejb.dao.gl.LocalGlFrgRowSetHome;
import com.ejb.entities.gl.LocalGlFrgAccountAssignment;
import com.ejb.dao.gl.LocalGlFrgAccountAssignmentHome;
import com.ejb.entities.gl.LocalGlFrgCalculation;
import com.ejb.dao.gl.LocalGlFrgCalculationHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlFrgRowSetDetails;

@Stateless(name = "GlFrgRowSetControllerEJB")
public class GlFrgRowSetControllerBean extends EJBContextClass implements GlFrgRowSetController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFrgAccountAssignmentHome glFrgAccountAssignmentHome;
    @EJB
    private LocalGlFrgCalculationHome glFrgCalculationHome;
    @EJB
    private LocalGlFrgRowHome glFrgRowHome;
    @EJB
    private LocalGlFrgRowSetHome glFrgRowSetHome;


    public ArrayList getGlFrgRsAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgRowSetControllerBean getGlFrgRsAll");

        Collection glFrgRows = null;

        LocalGlFrgRowSet glFrgRowSet = null;

        ArrayList list = new ArrayList();

        try {

            glFrgRows = glFrgRowSetHome.findRsAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgRows.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }

        for (Object glFrgRow : glFrgRows) {

            glFrgRowSet = (LocalGlFrgRowSet) glFrgRow;

            GlFrgRowSetDetails mdetails = new GlFrgRowSetDetails();
            mdetails.setRsCode(glFrgRowSet.getRsCode());
            mdetails.setRsName(glFrgRowSet.getRsName());
            mdetails.setRsDescription(glFrgRowSet.getRsDescription());

            list.add(mdetails);
        }

        return list;

    }


    public void addGlFrgRsEntry(GlFrgRowSetDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgRowSetControllerBean addGlFrgRsEntry");

        LocalGlFrgRowSet glFrgRowSet = null;

        ArrayList list = new ArrayList();

        try {

            glFrgRowSet = glFrgRowSetHome.findByRsName(details.getRsName(), AD_CMPNY);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // set new row

            glFrgRowSet = glFrgRowSetHome.create(details.getRsName(), details.getRsDescription(), AD_CMPNY);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void copyGlFrgRsEntry(GlFrgRowSetDetails details, Integer AD_CMPNY) throws Exception {

        Debug.print("GlFrgRowSetControllerBean copyGlFrgRsEntry");

        LocalGlFrgRowSet glFrgRowSetNew = null;
        LocalGlFrgRow glFrgRowNew = null;
        LocalGlFrgAccountAssignment glFrgAccountAssignmentNew = null;
        LocalGlFrgCalculation glFrgCalculationNew = null;

        try {

            LocalGlFrgRowSet glFrgRowSetSource = glFrgRowSetHome.findByRsName(details.getRsName(), AD_CMPNY);

            //set new row set copy
            glFrgRowSetNew = glFrgRowSetHome.create(("Copy of " + details.getRsName()), details.getRsDescription(), AD_CMPNY);
            Debug.print("ROW SET COPY SUCCESS");

            //set new row copy
            Collection glFrgRowSources = glFrgRowSetSource.getGlFrgRows();

            for (Object frgRowSource : glFrgRowSources) {

                LocalGlFrgRow glFrgRowSource = (LocalGlFrgRow) frgRowSource;

                glFrgRowNew = glFrgRowHome.create(glFrgRowSource.getRowLineNumber(), (glFrgRowSource.getRowName()), glFrgRowSource.getRowIndent(), glFrgRowSource.getRowLineToSkipBefore(), glFrgRowSource.getRowLineToSkipAfter(), glFrgRowSource.getRowUnderlineCharacterBefore(), glFrgRowSource.getRowUnderlineCharacterAfter(), glFrgRowSource.getRowPageBreakBefore(), glFrgRowSource.getRowPageBreakAfter(), glFrgRowSource.getRowOverrideColumnCalculation(), glFrgRowSource.getRowHideRow(), glFrgRowSource.getRowFontStyle(), glFrgRowSource.getRowHorizontalAlign(), AD_CMPNY);

                glFrgRowSetNew.addGlFrgRow(glFrgRowNew);
                Debug.print("ROW COPY SUCCESS");


                //set new account assignment copy
                Collection glFrgAccountAssignmentSources = glFrgRowSource.getGlFrgAccountAssignments();

                if (glFrgAccountAssignmentSources.size() > 0) {

                    for (Object frgAccountAssignmentSource : glFrgAccountAssignmentSources) {

                        LocalGlFrgAccountAssignment glFrgAccountAssignmentSource = (LocalGlFrgAccountAssignment) frgAccountAssignmentSource;

                        glFrgAccountAssignmentNew = glFrgAccountAssignmentHome.create(glFrgAccountAssignmentSource.getAaAccountLow(), glFrgAccountAssignmentSource.getAaAccountHigh(), glFrgAccountAssignmentSource.getAaActivityType(), AD_CMPNY);

                        glFrgRowNew.addGlFrgAccountAssignment(glFrgAccountAssignmentNew);

                    }
                    Debug.print("ACCOUNT ASSIGNMENT COPY SUCCESS");
                }


                //set new calculation copy
                Collection glFrgcalculationSources = glFrgRowSource.getGlFrgCalculations();

                if (glFrgcalculationSources.size() > 0) {

                    for (Object glFrgcalculationSource : glFrgcalculationSources) {

                        LocalGlFrgCalculation glFrgCalculationSource = (LocalGlFrgCalculation) glFrgcalculationSource;

                        glFrgCalculationNew = glFrgCalculationHome.create(glFrgCalculationSource.getCalSequenceNumber(), glFrgCalculationSource.getCalOperator(), glFrgCalculationSource.getCalType(), glFrgCalculationSource.getCalConstant(), (glFrgCalculationSource.getCalRowColName()), glFrgCalculationSource.getCalSequenceLow(), glFrgCalculationSource.getCalSequenceHigh(), AD_CMPNY);

                        glFrgRowNew.addGlFrgCalculation(glFrgCalculationNew);

                    }
                    Debug.print("CALCULATION COPY SUCCESS");
                }
            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlFrgRsEntry(GlFrgRowSetDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgRowSetControllerBean updateGlFrgFrEntry");

        LocalGlFrgRowSet glFrgRowSet = null;

        ArrayList list = new ArrayList();

        try {

            LocalGlFrgRowSet glExistingRowSet = glFrgRowSetHome.findByRsName(details.getRsName(), AD_CMPNY);

            if (!glExistingRowSet.getRsCode().equals(details.getRsCode())) {

                throw new GlobalRecordAlreadyExistException();

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // find and update row set

            glFrgRowSet = glFrgRowSetHome.findByPrimaryKey(details.getRsCode());

            glFrgRowSet.setRsName(details.getRsName());
            glFrgRowSet.setRsDescription(details.getRsDescription());

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlFrgRsEntry(Integer RS_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgRowSetControllerBean deleteGlFrgRsEntry");

        LocalGlFrgRowSet glFrgRowSet = null;

        try {

            glFrgRowSet = glFrgRowSetHome.findByPrimaryKey(RS_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }

        try {

            if (!glFrgRowSet.getGlFrgFinancialReports().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();

            }

            em.remove(glFrgRowSet);

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgRowSetControllerBean ejbCreate");

    }
}