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

import com.ejb.exception.gl.GlFrgCALRowAlreadyHasAccountAssignmentException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.entities.gl.LocalGlFrgCalculation;
import com.ejb.dao.gl.LocalGlFrgCalculationHome;
import com.ejb.entities.gl.LocalGlFrgColumn;
import com.ejb.dao.gl.LocalGlFrgColumnHome;
import com.ejb.entities.gl.LocalGlFrgRow;
import com.ejb.dao.gl.LocalGlFrgRowHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlFrgCalculationDetails;
import com.util.gl.GlFrgColumnDetails;
import com.util.gl.GlFrgRowDetails;

@Stateless(name = "GlFrgCalculationControllerEJB")
public class GlFrgCalculationControllerBean extends EJBContextClass implements GlFrgCalculationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFrgCalculationHome glFrgCalculationHome;
    @EJB
    private LocalGlFrgColumnHome glFrgColumnHome;
    @EJB
    private LocalGlFrgRowHome glFrgRowHome;

    public ArrayList getGlFrgCalByRowCode(Integer ROW_CODE, String CAL_TYP, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgCalculationControllerBean getGlFrgCalByRowCode");

        Collection glFrgCalculations = null;

        LocalGlFrgCalculation glFrgCalculation = null;

        ArrayList list = new ArrayList();

        try {

            glFrgCalculations = glFrgCalculationHome.findByRowCodeAndCalType(ROW_CODE, CAL_TYP, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgCalculations.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }

        for (Object frgCalculation : glFrgCalculations) {

            glFrgCalculation = (LocalGlFrgCalculation) frgCalculation;

            GlFrgCalculationDetails details = new GlFrgCalculationDetails();
            details.setCalCode(glFrgCalculation.getCalCode());
            details.setCalSequenceNumber(glFrgCalculation.getCalSequenceNumber());
            details.setCalOperator(glFrgCalculation.getCalOperator());
            details.setCalConstant(glFrgCalculation.getCalConstant());
            details.setCalRowColName(glFrgCalculation.getCalRowColName());
            details.setCalSequenceLow(glFrgCalculation.getCalSequenceLow());
            details.setCalSequenceHigh(glFrgCalculation.getCalSequenceHigh());
            details.setCalType(glFrgCalculation.getCalType());

            list.add(details);
        }

        return list;

    }


    public ArrayList getGlFrgCalByColCode(Integer COL_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgCalculationControllerBean getGlFrgCalByColCode");

        Collection glFrgCalculations = null;

        LocalGlFrgCalculation glFrgCalculation = null;

        ArrayList list = new ArrayList();

        try {

            glFrgCalculations = glFrgCalculationHome.findByColCode(COL_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgCalculations.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }

        for (Object frgCalculation : glFrgCalculations) {

            glFrgCalculation = (LocalGlFrgCalculation) frgCalculation;

            GlFrgCalculationDetails details = new GlFrgCalculationDetails();
            details.setCalCode(glFrgCalculation.getCalCode());
            details.setCalSequenceNumber(glFrgCalculation.getCalSequenceNumber());
            details.setCalOperator(glFrgCalculation.getCalOperator());
            details.setCalConstant(glFrgCalculation.getCalConstant());
            details.setCalRowColName(glFrgCalculation.getCalRowColName());
            details.setCalSequenceLow(glFrgCalculation.getCalSequenceLow());
            details.setCalSequenceHigh(glFrgCalculation.getCalSequenceHigh());

            list.add(details);
        }

        return list;

    }


    public GlFrgRowDetails getGlFrgRowByRowCode(Integer ROW_CODE, Integer AD_CMPNY) {

        Debug.print("GlFrgCalculationControllerBean getGlFrgRowByRowCode");

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


    public GlFrgColumnDetails getGlFrgColByColCode(Integer COL_CODE, Integer AD_CMPNY) {

        Debug.print("GlFrgCalculationControllerBean getGlFrgColByColCode");

        try {

            LocalGlFrgColumn glFrgColumn = glFrgColumnHome.findByPrimaryKey(COL_CODE);

            GlFrgColumnDetails details = new GlFrgColumnDetails();
            details.setColCode(glFrgColumn.getColCode());
            details.setColName(glFrgColumn.getColName());
            details.setColSequenceNumber(glFrgColumn.getColSequenceNumber());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void addGlFrgCalEntry(GlFrgCalculationDetails details, Integer ROW_CODE, Integer COL_CODE, Integer AD_CMPNY) throws GlFrgCALRowAlreadyHasAccountAssignmentException, GlobalRecordAlreadyExistException {

        Debug.print("GlFrgCalculationControllerBean addGlFrgCalEntry");

        LocalGlFrgCalculation glFrgCalculation = null;
        LocalGlFrgRow glFrgRow = null;
        LocalGlFrgColumn glFrgColumn = null;


        try {

            // Validate if row already has account assignment

            if (ROW_CODE != null) {

                try {

                    glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);

                    Collection glFrgAccountAssignments = glFrgRow.getGlFrgAccountAssignments();

                    if (!glFrgAccountAssignments.isEmpty()) {

                        if (details.getCalType().equals("C1"))
                            throw new GlFrgCALRowAlreadyHasAccountAssignmentException();

                    }

                } catch (GlFrgCALRowAlreadyHasAccountAssignmentException ex) {

                    throw ex;

                } catch (FinderException ex) {

                }

            }

            try {

                if (ROW_CODE != null && COL_CODE == null) {

                    glFrgCalculation = glFrgCalculationHome.findByRowCodeAndCalSequenceNumberAndCalType(ROW_CODE, details.getCalSequenceNumber(), details.getCalType(), AD_CMPNY);

                } else {

                    glFrgCalculation = glFrgCalculationHome.findByColCodeAndCalSequenceNumber(COL_CODE, details.getCalSequenceNumber(), AD_CMPNY);

                }

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // Create New Calculation

            String CAL_RW_NM = null;

            if (details.getCalRowColName() != null && details.getCalRowColName().length() > 0) {

                CAL_RW_NM = details.getCalRowColName();

            }

            glFrgCalculation = glFrgCalculationHome.create(details.getCalSequenceNumber(), details.getCalOperator(), details.getCalType(), details.getCalConstant(), CAL_RW_NM, details.getCalSequenceLow(), details.getCalSequenceHigh(), AD_CMPNY);


            try {

                if (ROW_CODE != null && COL_CODE == null) {

                    glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);
                    glFrgRow.addGlFrgCalculation(glFrgCalculation);

                } else {

                    glFrgColumn = glFrgColumnHome.findByPrimaryKey(COL_CODE);
                    glFrgColumn.addGlFrgCalculation(glFrgCalculation);

                }

            } catch (FinderException ex) {

            }

        } catch (GlobalRecordAlreadyExistException | GlFrgCALRowAlreadyHasAccountAssignmentException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlFrgCalEntry(GlFrgCalculationDetails details, Integer ROW_CODE, Integer COL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("GlFrgCalculationControllerBean updateGlFrgCalEntry");

        LocalGlFrgCalculation glFrgCalculation = null;
        LocalGlFrgRow glFrgRow = null;
        LocalGlFrgColumn glFrgColumn = null;

        // Find and Update Calculation

        try {

            try {

                LocalGlFrgCalculation glFrgExistingCalculation = null;

                if (ROW_CODE != null && COL_CODE == null) {

                    glFrgExistingCalculation = glFrgCalculationHome.findByRowCodeAndCalSequenceNumberAndCalType(ROW_CODE, details.getCalSequenceNumber(), details.getCalType(), AD_CMPNY);

                    if (!glFrgExistingCalculation.getCalCode().equals(details.getCalCode())) {

                        throw new GlobalRecordAlreadyExistException();

                    }

                } else {

                    glFrgExistingCalculation = glFrgCalculationHome.findByColCodeAndCalSequenceNumber(COL_CODE, details.getCalSequenceNumber(), AD_CMPNY);

                    if (!glFrgExistingCalculation.getCalCode().equals(details.getCalCode())) {

                        throw new GlobalRecordAlreadyExistException();

                    }

                }

            } catch (FinderException ex) {

            }

            glFrgCalculation = glFrgCalculationHome.findByPrimaryKey(details.getCalCode());
            glFrgCalculation.setCalSequenceNumber(details.getCalSequenceNumber());
            glFrgCalculation.setCalOperator(details.getCalOperator());
            glFrgCalculation.setCalConstant(details.getCalConstant());

            String CAL_RW_NM = null;

            if (details.getCalRowColName() != null && details.getCalRowColName().length() > 0) {

                CAL_RW_NM = details.getCalRowColName();

            }
            glFrgCalculation.setCalRowColName(CAL_RW_NM);
            glFrgCalculation.setCalSequenceLow(details.getCalSequenceLow());
            glFrgCalculation.setCalSequenceHigh(details.getCalSequenceHigh());

            try {

                if (ROW_CODE != null && COL_CODE == null) {

                    glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);
                    glFrgRow.addGlFrgCalculation(glFrgCalculation);

                } else {

                    glFrgColumn = glFrgColumnHome.findByPrimaryKey(COL_CODE);
                    glFrgColumn.addGlFrgCalculation(glFrgCalculation);

                }

            } catch (FinderException ex) {

            }

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlFrgCalEntry(Integer CAL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgCalculationControllerBean deleteGlFrgCalEntry");

        LocalGlFrgCalculation glFrgCalculation = null;

        try {

            try {

                glFrgCalculation = glFrgCalculationHome.findByPrimaryKey(CAL_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();

            }

            em.remove(glFrgCalculation);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public ArrayList getGlFrgRowByRsCodeAndLessThanRowLineNumber(Integer RS_CODE, int RW_LN_NMBR, String CAL_TYP, Integer AD_CMPNY) {

        Debug.print("GlFrgCalculationControllerBean getGlFrgRowByRsCodeAndLessThanRowLineNumber");

        Collection glFrgRows = null;

        LocalGlFrgRow glFrgRow = null;

        ArrayList list = new ArrayList();


        try {

            try {

                if (CAL_TYP.equals("C1"))
                    glFrgRows = glFrgRowHome.findByRowSetAndRowLineNumber(RS_CODE, RW_LN_NMBR, AD_CMPNY);
                else if (CAL_TYP.equals("C2")) glFrgRows = glFrgRowHome.findByRsCode(RS_CODE, AD_CMPNY);

            } catch (FinderException ex) {

            }

            if (glFrgRows.isEmpty()) {

                return null;

            }

            for (Object frgRow : glFrgRows) {

                glFrgRow = (LocalGlFrgRow) frgRow;

                list.add(glFrgRow.getRowName());

            }

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

    }


    public ArrayList getGlFrgColByCsCodeAndLessThanColumnSequenceNumber(Integer CS_CODE, int COL_SQNC_NMBR, Integer AD_CMPNY) {

        Debug.print("GlFrgCalculationControllerBean getGlFrgColByCsCodeAndLessThanColumnSequenceNumber");

        Collection glFrgColumns = null;

        LocalGlFrgColumn glFrgColumn = null;

        ArrayList list = new ArrayList();

        try {

            try {


                glFrgColumns = glFrgColumnHome.findByColumnSetAndColSequenceNumber(CS_CODE, COL_SQNC_NMBR, AD_CMPNY);

            } catch (FinderException ex) {

            }

            if (glFrgColumns.isEmpty()) {

                return null;

            }

            for (Object frgColumn : glFrgColumns) {

                glFrgColumn = (LocalGlFrgColumn) frgColumn;

                list.add(glFrgColumn.getColName());

            }

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgCalculationControllerBean ejbCreate");

    }

}