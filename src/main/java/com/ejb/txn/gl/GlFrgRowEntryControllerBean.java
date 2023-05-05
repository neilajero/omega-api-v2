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

import com.ejb.exception.gl.GlFrgROWLineNumberAlreadyExistException;
import com.ejb.exception.gl.GlFrgROWRowNameAlreadyExistException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.entities.gl.LocalGlFrgRow;
import com.ejb.dao.gl.LocalGlFrgRowHome;
import com.ejb.entities.gl.LocalGlFrgRowSet;
import com.ejb.dao.gl.LocalGlFrgRowSetHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlFrgRowDetails;
import com.util.gl.GlFrgRowSetDetails;

@Stateless(name = "GlFrgRowEntryControllerEJB")
public class GlFrgRowEntryControllerBean extends EJBContextClass implements GlFrgRowEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFrgRowHome glFrgRowHome;
    @EJB
    private LocalGlFrgRowSetHome glFrgRowSetHome;

    public ArrayList getGlFrgRowByRowSetCode(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgRowEntryControllerBean getGlFrgRowByRowSetCode");

        Collection glFrgRows = null;

        LocalGlFrgRow glFrgRow = null;

        ArrayList list = new ArrayList();


        try {

            glFrgRows = glFrgRowHome.findByRsCode(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgRows.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }

        for (Object frgRow : glFrgRows) {

            glFrgRow = (LocalGlFrgRow) frgRow;

            GlFrgRowDetails details = new GlFrgRowDetails();
            details.setRowCode(glFrgRow.getRowCode());
            details.setRowLineNumber(glFrgRow.getRowLineNumber());
            details.setRowName(glFrgRow.getRowName());
            details.setRowIndent(glFrgRow.getRowIndent());
            details.setRowLineToSkipBefore(glFrgRow.getRowLineToSkipBefore());
            details.setRowLineToSkipAfter(glFrgRow.getRowLineToSkipAfter());
            details.setRowUnderlineCharacterBefore(glFrgRow.getRowUnderlineCharacterBefore());
            details.setRowUnderlineCharacterAfter(glFrgRow.getRowUnderlineCharacterAfter());
            details.setRowPageBreakBefore(glFrgRow.getRowPageBreakBefore());
            details.setRowPageBreakAfter(glFrgRow.getRowPageBreakAfter());
            details.setRowOverrideColumnCalculation(glFrgRow.getRowOverrideColumnCalculation());
            details.setRowHideRow(glFrgRow.getRowHideRow());
            details.setRowFontStyle(glFrgRow.getRowFontStyle());
            details.setRowHorizontalAlign(glFrgRow.getRowHorizontalAlign());

            list.add(details);
        }

        return list;

    }


    public GlFrgRowSetDetails getGlFrgRowSetByRowSetCode(Integer RS_CODE, Integer AD_CMPNY) {

        Debug.print("GlFrgRowEntryControllerBean getGlFrgRowSetByRowSetCode");

        try {

            LocalGlFrgRowSet glFrgRowSet = glFrgRowSetHome.findByPrimaryKey(RS_CODE);

            GlFrgRowSetDetails details = new GlFrgRowSetDetails();
            details.setRsCode(glFrgRowSet.getRsCode());
            details.setRsName(glFrgRowSet.getRsName());
            details.setRsDescription(glFrgRowSet.getRsDescription());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void addGlFrgRowEntry(GlFrgRowDetails details, Integer RS_CODE, Integer AD_CMPNY) throws GlFrgROWRowNameAlreadyExistException, GlFrgROWLineNumberAlreadyExistException {

        Debug.print("GlFrgRowEntryControllerBean addGlFrgRowEntry");

        LocalGlFrgRow glFrgRow = null;
        LocalGlFrgRowSet glFrgRowSet = null;

        try {

            glFrgRow = glFrgRowHome.findByRowNameAndRsCode(details.getRowName(), RS_CODE, AD_CMPNY);

            throw new GlFrgROWRowNameAlreadyExistException();

        } catch (GlFrgROWRowNameAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            glFrgRow = glFrgRowHome.findByRowLineNumberAndRsCode(details.getRowLineNumber(), RS_CODE, AD_CMPNY);

            throw new GlFrgROWLineNumberAlreadyExistException();

        } catch (GlFrgROWLineNumberAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            // create new rows

            glFrgRow = glFrgRowHome.create(details.getRowLineNumber(), details.getRowName(), details.getRowIndent(), details.getRowLineToSkipBefore(), details.getRowLineToSkipAfter(), details.getRowUnderlineCharacterBefore(), details.getRowUnderlineCharacterAfter(), details.getRowPageBreakBefore(), details.getRowPageBreakAfter(), details.getRowOverrideColumnCalculation(), details.getRowHideRow(), details.getRowFontStyle(), details.getRowHorizontalAlign(), AD_CMPNY);

            glFrgRowSet = glFrgRowSetHome.findByPrimaryKey(RS_CODE);
            glFrgRowSet.addGlFrgRow(glFrgRow);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    public void updateGlFrgRowEntry(GlFrgRowDetails details, Integer RS_CODE, Integer AD_CMPNY) throws GlFrgROWRowNameAlreadyExistException, GlFrgROWLineNumberAlreadyExistException {

        Debug.print("GlFrgRowEntryControllerBean updateGlFrgRowEntry");

        LocalGlFrgRow glFrgRow = null;
        LocalGlFrgRowSet glFrgRowSet = null;

        try {

            glFrgRow = glFrgRowHome.findByRowNameAndRsCode(details.getRowName(), RS_CODE, AD_CMPNY);

            if (glFrgRow != null && !glFrgRow.getRowCode().equals(details.getRowCode())) {

                throw new GlFrgROWRowNameAlreadyExistException();

            }

        } catch (GlFrgROWRowNameAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            glFrgRow = glFrgRowHome.findByRowLineNumberAndRsCode(details.getRowLineNumber(), RS_CODE, AD_CMPNY);

            if (glFrgRow != null && !glFrgRow.getRowCode().equals(details.getRowCode())) {

                throw new GlFrgROWLineNumberAlreadyExistException();

            }

        } catch (GlFrgROWLineNumberAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        // Find and Update Rows

        try {

            glFrgRow = glFrgRowHome.findByPrimaryKey(details.getRowCode());

            glFrgRow.setRowLineNumber(details.getRowLineNumber());
            glFrgRow.setRowName(details.getRowName());
            glFrgRow.setRowIndent(details.getRowIndent());
            glFrgRow.setRowLineToSkipBefore(details.getRowLineToSkipBefore());
            glFrgRow.setRowLineToSkipAfter(details.getRowLineToSkipAfter());
            glFrgRow.setRowUnderlineCharacterBefore(details.getRowUnderlineCharacterBefore());
            glFrgRow.setRowUnderlineCharacterAfter(details.getRowUnderlineCharacterAfter());
            glFrgRow.setRowPageBreakBefore(details.getRowPageBreakBefore());
            glFrgRow.setRowPageBreakAfter(details.getRowPageBreakAfter());
            glFrgRow.setRowOverrideColumnCalculation(details.getRowOverrideColumnCalculation());
            glFrgRow.setRowHideRow(details.getRowHideRow());
            glFrgRow.setRowFontStyle(details.getRowFontStyle());
            glFrgRow.setRowHorizontalAlign(details.getRowHorizontalAlign());

            glFrgRowSet = glFrgRowSetHome.findByPrimaryKey(RS_CODE);
            glFrgRowSet.addGlFrgRow(glFrgRow);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public void deleteGlFrgRowEntry(Integer ROW_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgRowEntryControllerBean deleteGlFrgRowEntry");

        LocalGlFrgRow glFrgRow = null;

        try {

            glFrgRow = glFrgRowHome.findByPrimaryKey(ROW_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {

            em.remove(glFrgRow);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgRowEntryControllerBean ejbCreate");

    }
}