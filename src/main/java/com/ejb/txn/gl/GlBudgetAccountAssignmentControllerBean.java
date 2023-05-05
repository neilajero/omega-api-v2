/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlBudgetAccountAssignmentControllerBean
 * @created
 * @author
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
import com.ejb.exception.gl.GlBgtAAAccountFromInvalidException;
import com.ejb.exception.gl.GlBgtAAAccountOverlappedException;
import com.ejb.exception.gl.GlBgtAAAccountToInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.dao.gen.LocalGenFieldHome;
import com.ejb.entities.gl.LocalGlBudgetAccountAssignment;
import com.ejb.dao.gl.LocalGlBudgetAccountAssignmentHome;
import com.ejb.entities.gl.LocalGlBudgetOrganization;
import com.ejb.dao.gl.LocalGlBudgetOrganizationHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlBudgetAccountAssignmentDetails;
import com.util.gl.GlBudgetOrganizationDetails;

@Stateless(name = "GlBudgetAccountAssignmentControllerEJB")
public class GlBudgetAccountAssignmentControllerBean extends EJBContextClass implements GlBudgetAccountAssignmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGenFieldHome genFieldHome;
    @EJB
    private LocalGlBudgetAccountAssignmentHome glBudgetAccountAssignmentHome;
    @EJB
    private LocalGlBudgetOrganizationHome glBudgetOrganizationHome;

    public ArrayList getGlBaaByBoCode(Integer BO_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlBudgetAccountAssignmentControllerBean getGlBaaByBoCode");

        ArrayList list = new ArrayList();

        try {

            Collection glBudgetAccountAssignments = glBudgetAccountAssignmentHome.findByBoCode(BO_CODE, AD_CMPNY);

            if (glBudgetAccountAssignments.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object budgetAccountAssignment : glBudgetAccountAssignments) {

                LocalGlBudgetAccountAssignment glBudgetAccountAssignment = (LocalGlBudgetAccountAssignment) budgetAccountAssignment;

                GlBudgetAccountAssignmentDetails mdetails = new GlBudgetAccountAssignmentDetails();
                mdetails.setBaaCode(glBudgetAccountAssignment.getBaaCode());
                mdetails.setBaaAccountFrom(glBudgetAccountAssignment.getBaaAccountFrom());
                mdetails.setBaaAccountTo(glBudgetAccountAssignment.getBaaAccountTo());
                mdetails.setBaaType(glBudgetAccountAssignment.getBaaType());

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

    public GlBudgetOrganizationDetails getGlBoByBoCode(Integer BO_CODE, Integer AD_CMPNY) {

        Debug.print("GlBudgetAccountAssignmentControllerBean getGlBoByBoCode");

        ArrayList list = new ArrayList();

        try {

            LocalGlBudgetOrganization glBudgetOrganization = glBudgetOrganizationHome.findByPrimaryKey(BO_CODE);

            GlBudgetOrganizationDetails details = new GlBudgetOrganizationDetails();
            details.setBoName(glBudgetOrganization.getBoName());
            details.setBoSegmentOrder(glBudgetOrganization.getBoSegmentOrder());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addGlBaaEntry(GlBudgetAccountAssignmentDetails details, Integer BO_CODE, Integer AD_CMPNY) throws GlBgtAAAccountFromInvalidException, GlBgtAAAccountToInvalidException, GlBgtAAAccountOverlappedException {

        Debug.print("GlBudgetAccountAssignmentControllerBean addGlBaaEntry");

        try {

            // Get GenField's separator and number of segments

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            char chrSeparator = genField.getFlSegmentSeparator();
            String strSeparator = String.valueOf(chrSeparator);
            short genNumberOfSegment = genField.getFlNumberOfSegment();

            // Validate Account From

            StringTokenizer stAccountFrom = new StringTokenizer(details.getBaaAccountFrom(), strSeparator);

            if (stAccountFrom.countTokens() != genNumberOfSegment) {

                throw new GlBgtAAAccountFromInvalidException();
            }

            // Validate Account To

            StringTokenizer stAccountTo = new StringTokenizer(details.getBaaAccountTo(), strSeparator);

            if (stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlBgtAAAccountToInvalidException();
            }

            // validate if account range entered does not overlap other budget organizations or self

            String[] newAccountFromSegmentValue = new String[genNumberOfSegment];
            String[] newAccountToSegmentValue = new String[genNumberOfSegment];

            StringTokenizer st = new StringTokenizer(details.getBaaAccountFrom(), strSeparator);
            int stIndex = 0;

            while (st.hasMoreTokens()) {
                newAccountFromSegmentValue[stIndex] = st.nextToken();
                stIndex++;
            }

            st = new StringTokenizer(details.getBaaAccountTo(), strSeparator);
            stIndex = 0;

            while (st.hasMoreTokens()) {
                newAccountToSegmentValue[stIndex] = st.nextToken();
                stIndex++;
            }

            Collection glBudgetOrganizations = glBudgetOrganizationHome.findBoAll(AD_CMPNY);

            for (Object budgetOrganization : glBudgetOrganizations) {

                LocalGlBudgetOrganization glBudgetOrganization = (LocalGlBudgetOrganization) budgetOrganization;

                Collection glBudgetAccountAssignments = glBudgetOrganization.getGlBudgetAccountAssignments();

                for (Object budgetAccountAssignment : glBudgetAccountAssignments) {

                    LocalGlBudgetAccountAssignment glBudgetAccountAssignment = (LocalGlBudgetAccountAssignment) budgetAccountAssignment;

                    String[] accountFromSegmentValue = new String[genNumberOfSegment];
                    String[] accountToSegmentValue = new String[genNumberOfSegment];

                    st = new StringTokenizer(glBudgetAccountAssignment.getBaaAccountFrom(), strSeparator);
                    stIndex = 0;

                    while (st.hasMoreTokens()) {
                        accountFromSegmentValue[stIndex] = st.nextToken();
                        stIndex++;
                    }

                    st = new StringTokenizer(glBudgetAccountAssignment.getBaaAccountTo(), strSeparator);
                    stIndex = 0;

                    while (st.hasMoreTokens()) {
                        accountToSegmentValue[stIndex] = st.nextToken();
                        stIndex++;
                    }

                    boolean isOverlapped = false;

                    if (isOverlapped) {

                        throw new GlBgtAAAccountOverlappedException();
                    }
                }
            }

            // create new budget account assignment

            LocalGlBudgetAccountAssignment glBudgetAccountAssignment = glBudgetAccountAssignmentHome.create(details.getBaaAccountFrom(), details.getBaaAccountTo(), details.getBaaType(), AD_CMPNY);

            LocalGlBudgetOrganization glBudgetOrganization = glBudgetOrganizationHome.findByPrimaryKey(BO_CODE);
            glBudgetOrganization.addGlBudgetAccountAssignment(glBudgetAccountAssignment);

        } catch (GlBgtAAAccountFromInvalidException | GlBgtAAAccountOverlappedException |
                 GlBgtAAAccountToInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlBaaEntry(GlBudgetAccountAssignmentDetails details, Integer BO_CODE, Integer AD_CMPNY) throws GlBgtAAAccountFromInvalidException, GlBgtAAAccountToInvalidException, GlBgtAAAccountOverlappedException {

        Debug.print("GlBudgetAccountAssignmentControllerBean updateGlBaaEntry");

        try {

            // Get GenField's separator and number of segments

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            char chrSeparator = genField.getFlSegmentSeparator();
            String strSeparator = String.valueOf(chrSeparator);
            short genNumberOfSegment = genField.getFlNumberOfSegment();

            // Validate Account From

            StringTokenizer stAccountFrom = new StringTokenizer(details.getBaaAccountFrom(), strSeparator);

            if (stAccountFrom.countTokens() != genNumberOfSegment) {

                throw new GlBgtAAAccountFromInvalidException();
            }

            // Validate Account To

            StringTokenizer stAccountTo = new StringTokenizer(details.getBaaAccountTo(), strSeparator);

            if (stAccountTo.countTokens() != genNumberOfSegment) {

                throw new GlBgtAAAccountToInvalidException();
            }

            // validate if account range entered does not overlap other budget organizations or self

            String[] newAccountFromSegmentValue = new String[genNumberOfSegment];
            String[] newAccountToSegmentValue = new String[genNumberOfSegment];

            StringTokenizer st = new StringTokenizer(details.getBaaAccountFrom(), strSeparator);
            int stIndex = 0;

            while (st.hasMoreTokens()) {
                newAccountFromSegmentValue[stIndex] = st.nextToken();
                stIndex++;
            }

            st = new StringTokenizer(details.getBaaAccountTo(), strSeparator);
            stIndex = 0;

            while (st.hasMoreTokens()) {
                newAccountToSegmentValue[stIndex] = st.nextToken();
                stIndex++;
            }

            Collection glBudgetOrganizations = glBudgetOrganizationHome.findBoAll(AD_CMPNY);

            for (Object budgetOrganization : glBudgetOrganizations) {

                LocalGlBudgetOrganization glBudgetOrganization = (LocalGlBudgetOrganization) budgetOrganization;

                Collection glBudgetAccountAssignments = glBudgetOrganization.getGlBudgetAccountAssignments();

                for (Object budgetAccountAssignment : glBudgetAccountAssignments) {

                    LocalGlBudgetAccountAssignment glBudgetAccountAssignment = (LocalGlBudgetAccountAssignment) budgetAccountAssignment;

                    if (!glBudgetAccountAssignment.getBaaCode().equals(details.getBaaCode())) {

                        String[] accountFromSegmentValue = new String[genNumberOfSegment];
                        String[] accountToSegmentValue = new String[genNumberOfSegment];

                        st = new StringTokenizer(glBudgetAccountAssignment.getBaaAccountFrom(), strSeparator);
                        stIndex = 0;

                        while (st.hasMoreTokens()) {
                            accountFromSegmentValue[stIndex] = st.nextToken();
                            stIndex++;
                        }

                        st = new StringTokenizer(glBudgetAccountAssignment.getBaaAccountTo(), strSeparator);
                        stIndex = 0;

                        while (st.hasMoreTokens()) {
                            accountToSegmentValue[stIndex] = st.nextToken();
                            stIndex++;
                        }

                        boolean isOverlapped = false;

                        if (isOverlapped) {

                            throw new GlBgtAAAccountOverlappedException();
                        }
                    }
                }
            }

            LocalGlBudgetAccountAssignment glBudgetAccountAssignment = glBudgetAccountAssignmentHome.findByPrimaryKey(details.getBaaCode());

            // Find and Update Budget Account Assignment

            glBudgetAccountAssignment.setBaaAccountFrom(details.getBaaAccountFrom());
            glBudgetAccountAssignment.setBaaAccountTo(details.getBaaAccountTo());
            glBudgetAccountAssignment.setBaaType(details.getBaaType());

            LocalGlBudgetOrganization glBudgetOrganization = glBudgetOrganizationHome.findByPrimaryKey(BO_CODE);
            glBudgetOrganization.addGlBudgetAccountAssignment(glBudgetAccountAssignment);

        } catch (GlBgtAAAccountFromInvalidException | GlBgtAAAccountOverlappedException |
                 GlBgtAAAccountToInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlBaaEntry(Integer BAA_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlBudgetAccountAssignmentControllerBean deleteGlBaaEntry");

        LocalGlBudgetAccountAssignment glBudgetAccountAssignment = null;

        try {

            try {

                glBudgetAccountAssignment = glBudgetAccountAssignmentHome.findByPrimaryKey(BAA_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            //    		glBudgetAccountAssignment.entityRemove();
            em.remove(glBudgetAccountAssignment);

        } catch (GlobalRecordAlreadyDeletedException ex) {

            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlBudgetAccountAssignmentControllerBean ejbCreate");
    }

    // private methods

}