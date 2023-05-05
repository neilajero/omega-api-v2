/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFrgAccountAssignmentControllerBean
 * @created Aug 8, 2003, 10:29 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlFrgAAAccountHighInvalidException;
import com.ejb.exception.gl.GlFrgAAAccountLowInvalidException;
import com.ejb.exception.gl.GlFrgAARowAlreadyHasCalculationException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gen.LocalGenFieldHome;
import com.ejb.entities.gl.LocalGlFrgAccountAssignment;
import com.ejb.dao.gl.LocalGlFrgAccountAssignmentHome;
import com.ejb.entities.gl.LocalGlFrgCalculation;
import com.ejb.dao.gl.LocalGlFrgCalculationHome;
import com.ejb.entities.gl.LocalGlFrgRow;
import com.ejb.dao.gl.LocalGlFrgRowHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlFrgAccountAssignmentDetails;
import com.util.gl.GlFrgRowDetails;

@Stateless(name = "GlFrgAccountAssignmentControllerEJB")
public class GlFrgAccountAssignmentControllerBean extends EJBContextClass implements GlFrgAccountAssignmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGenFieldHome genFieldHome;
    @EJB
    private LocalGlFrgAccountAssignmentHome glFrgAccountAssignmentHome;
    @EJB
    private LocalGlFrgCalculationHome glFrgCalculationHome;
    @EJB
    private LocalGlFrgRowHome glFrgRowHome;


    public ArrayList getGlFrgAaByRowCode(Integer ROW_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgAccountAssignmentControllerBean getGlFrgAaByRowCode");

        Collection glFrgAccountAssignments = null;

        LocalGlFrgAccountAssignment glFrgAccountAssignment = null;

        ArrayList list = new ArrayList();

        try {

            glFrgAccountAssignments = glFrgAccountAssignmentHome.findByRowCode(ROW_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgAccountAssignments.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        for (Object frgAccountAssignment : glFrgAccountAssignments) {

            glFrgAccountAssignment = (LocalGlFrgAccountAssignment) frgAccountAssignment;

            GlFrgAccountAssignmentDetails details = new GlFrgAccountAssignmentDetails();
            details.setAaCode(glFrgAccountAssignment.getAaCode());
            details.setAaAccountLow(glFrgAccountAssignment.getAaAccountLow());
            details.setAaAccountHigh(glFrgAccountAssignment.getAaAccountHigh());
            details.setAaActivityType(glFrgAccountAssignment.getAaActivityType());

            list.add(details);
        }

        return list;
    }

    public GlFrgRowDetails getGlFrgRowByRowCode(Integer ROW_CODE, Integer AD_CMPNY) {

        Debug.print("GlFrgAccountAssignmentControllerBean getGlFrgRowByRowCode");

        try {

            LocalGlFrgRow glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);

            GlFrgRowDetails details = new GlFrgRowDetails();
            details.setRowCode(glFrgRow.getRowCode());
            details.setRowLineNumber(glFrgRow.getRowLineNumber());
            details.setRowName(glFrgRow.getRowName());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void addGlFrgAaEntry(GlFrgAccountAssignmentDetails details, Integer ROW_CODE, Integer AD_CMPNY) throws GlFrgAARowAlreadyHasCalculationException, GlFrgAAAccountHighInvalidException, GlFrgAAAccountLowInvalidException {

        Debug.print("GlFrgAccountAssignmentControllerBean addGlFrgAaEntry");

        LocalGlFrgAccountAssignment glFrgAccountAssignment = null;
        LocalGlFrgRow glFrgRow = null;
        LocalGenField genField = null;
        LocalAdCompany adCompany = null;

        Collection glFrgAccountAssignments = null;

        String strSeparator = null;

        // Validate if row already has calculation

        try {

            glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);

            Collection glFrgCalculations = glFrgRow.getGlFrgCalculations();

            if (!glFrgCalculations.isEmpty()) {

                for (Object frgCalculation : glFrgCalculations) {

                    LocalGlFrgCalculation glFrgCalculation = (LocalGlFrgCalculation) frgCalculation;

                    if (glFrgCalculation.getCalType().equals("C1"))
                        throw new GlFrgAARowAlreadyHasCalculationException();
                }
            }

        } catch (GlFrgAARowAlreadyHasCalculationException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Get GenField's separator and number of segments

        short genNumberOfSegment = 0;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            genField = adCompany.getGenField();
            char chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);
            genNumberOfSegment = genField.getFlNumberOfSegment();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Validate Account High

        StringTokenizer stAccountHigh = new StringTokenizer(details.getAaAccountHigh(), strSeparator);

        if (stAccountHigh.countTokens() != genNumberOfSegment) {

            throw new GlFrgAAAccountHighInvalidException();
        }

        // Validate Account Low

        StringTokenizer stAccountLow = new StringTokenizer(details.getAaAccountLow(), strSeparator);

        if (stAccountLow.countTokens() != genNumberOfSegment) {

            throw new GlFrgAAAccountLowInvalidException();
        }

        try {

            // Create New Account Assignment

            glFrgAccountAssignment = glFrgAccountAssignmentHome.create(details.getAaAccountLow(), details.getAaAccountHigh(), details.getAaActivityType(), AD_CMPNY);

            glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);
            glFrgRow.addGlFrgAccountAssignment(glFrgAccountAssignment);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlFrgAaEntry(GlFrgAccountAssignmentDetails details, Integer ROW_CODE, Integer AD_CMPNY) throws GlFrgAAAccountHighInvalidException, GlFrgAAAccountLowInvalidException {

        Debug.print("GlFrgAccountAssignmentControllerBean updateGlFrgAaEntry");

        LocalGlFrgAccountAssignment glFrgAccountAssignment = null;
        LocalGlFrgRow glFrgRow = null;
        LocalGenField genField = null;
        LocalAdCompany adCompany = null;

        Collection glFrgAccountAssignments = null;

        String strSeparator = null;

        // Get GenField's separator and number of segments

        short genNumberOfSegment = 0;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            genField = adCompany.getGenField();
            char chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);
            genNumberOfSegment = genField.getFlNumberOfSegment();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Validate Account High

        StringTokenizer stAccountHigh = new StringTokenizer(details.getAaAccountHigh(), strSeparator);

        if (stAccountHigh.countTokens() != genNumberOfSegment) {

            throw new GlFrgAAAccountHighInvalidException();
        }

        // Validate Account Low

        StringTokenizer stAccountLow = new StringTokenizer(details.getAaAccountLow(), strSeparator);

        if (stAccountLow.countTokens() != genNumberOfSegment) {

            throw new GlFrgAAAccountLowInvalidException();
        }

        // Find and Update Account Assignments

        try {

            glFrgAccountAssignment = glFrgAccountAssignmentHome.findByPrimaryKey(details.getAaCode());

            glFrgAccountAssignment.setAaAccountLow(details.getAaAccountLow());
            glFrgAccountAssignment.setAaAccountHigh(details.getAaAccountHigh());
            glFrgAccountAssignment.setAaActivityType(details.getAaActivityType());

            glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);
            glFrgRow.addGlFrgAccountAssignment(glFrgAccountAssignment);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlFrgAaEntry(Integer AA_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgAccountAssignmentControllerBean deleteGlFrgAaEntry");

        LocalGlFrgAccountAssignment glFrgAccountAssignment = null;

        try {

            glFrgAccountAssignment = glFrgAccountAssignmentHome.findByPrimaryKey(AA_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            //	      glFrgAccountAssignment.entityRemove();
            em.remove(glFrgAccountAssignment);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgAccountAssignmentControllerBean ejbCreate");
    }
}