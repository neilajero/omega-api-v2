package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import javax.naming.NamingException;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.ejb.dao.gl.LocalGlJournalInterfaceHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBHomeFactory;
import com.util.gl.GlJournalInterfaceDetails;

@Stateless(name = "GlFindJournalInterfaceControllerEJB")
public class GlFindJournalInterfaceControllerBean extends EJBContextClass implements GlFindJournalInterfaceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlJournalInterfaceHome glJournalInterfaceHome;

    public ArrayList getGlJriByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalInterfaceControllerBean getGlJriByCriteria");

        LocalGlJournalInterface glJournalInterface = null;
        Collection glJournalInterfaces = null;

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(jri) FROM GlJournalInterface jri ");

            boolean firstArgument = true;
            short ctr = 0;
            int x = 0;

            if (criteria.containsKey("jriName")) {

                x++;

            }

            if (criteria.containsKey("jriDescription")) {

                x++;

            }

            if (criteria.containsKey("jriJournalCategory")) {

                x++;

            }

            if (criteria.containsKey("jriJournalSource")) {

                x++;

            }

            if (criteria.containsKey("jriFunctionalCurrency")) {

                x++;

            }

            Object[] obj = new Object[(criteria.size() - x)];

            if (criteria.containsKey("jriName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriName LIKE '%").append(criteria.get("jriName")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriDescription")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jri.jriDescription LIKE '%").append(criteria.get("jriDescription")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriEffectiveDate")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;

                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriEffectiveDate=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriEffectiveDate");
                ctr++;

            }

            if (criteria.containsKey("jriJournalCategory")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriJournalCategory LIKE '%").append(criteria.get("jriJournalCategory")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriJournalSource")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriJournalSource LIKE '%").append(criteria.get("jriJournalSource")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriFunctionalCurrency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriFunctionalCurrency LIKE '%").append(criteria.get("jriFunctionalCurrency")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriDateReversal")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriDateReversal=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriDateReversal");
                ctr++;

            }

            if (criteria.containsKey("jriDocumentNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriDocumentNumber=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriDocumentNumber");
                ctr++;

            }

            if (criteria.containsKey("jriConversionDate")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriConversionDate=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriConversionDate");
                ctr++;

            }

            if (criteria.containsKey("jriConversionRate")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriConversionRate=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriConversionRate");
                ctr++;

            }

            if (criteria.containsKey("jriReversed")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriReversed=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriReversed");
                ctr++;

            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("jri.jriAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("NAME")) {

                orderBy = "jri.jriName";

                jbossQl.append("ORDER BY ").append(orderBy).append(", jri.jriEffectiveDate");

            } else {

                jbossQl.append("ORDER BY jri.jriEffectiveDate");

            }


            glJournalInterfaces = glJournalInterfaceHome.getJriByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (glJournalInterfaces.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object journalInterface : glJournalInterfaces) {

                glJournalInterface = (LocalGlJournalInterface) journalInterface;

                GlJournalInterfaceDetails details = new GlJournalInterfaceDetails();
                details.setJriCode(glJournalInterface.getJriCode());
                details.setJriName(glJournalInterface.getJriName());
                details.setJriDescription(glJournalInterface.getJriDescription());
                details.setJriDocumentNumber(glJournalInterface.getJriDocumentNumber());
                details.setJriEffectiveDate(glJournalInterface.getJriEffectiveDate());
                details.setJriJournalCategory(glJournalInterface.getJriJournalCategory());
                details.setJriJournalSource(glJournalInterface.getJriJournalSource());

                list.add(details);

            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }


    public Integer getGlJriSizeByCriteria(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindJournalInterfaceControllerBean getGlJriSizeByCriteria");

        LocalGlJournalInterface glJournalInterface = null;
        Collection glJournalInterfaces = null;

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(jri) FROM GlJournalInterface jri ");

            boolean firstArgument = true;
            short ctr = 0;
            int x = 0;

            if (criteria.containsKey("jriName")) {

                x++;

            }

            if (criteria.containsKey("jriDescription")) {

                x++;

            }

            if (criteria.containsKey("jriJournalCategory")) {

                x++;

            }

            if (criteria.containsKey("jriJournalSource")) {

                x++;

            }

            if (criteria.containsKey("jriFunctionalCurrency")) {

                x++;

            }

            Object[] obj = new Object[criteria.size() - x];

            if (criteria.containsKey("jriName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriName LIKE '%").append(criteria.get("jriName")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriDescription")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jri.jriDescription LIKE '%").append(criteria.get("jriDescription")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriEffectiveDate")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;

                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriEffectiveDate=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriEffectiveDate");
                ctr++;

            }

            if (criteria.containsKey("jriJournalCategory")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriJournalCategory LIKE '%").append(criteria.get("jriJournalCategory")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriJournalSource")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriJournalSource LIKE '%").append(criteria.get("jriJournalSource")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriFunctionalCurrency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriFunctionalCurrency LIKE '%").append(criteria.get("jriFunctionalCurrency")).append("%' ");
                firstArgument = false;

            }

            if (criteria.containsKey("jriDateReversal")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriDateReversal=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriDateReversal");
                ctr++;

            }

            if (criteria.containsKey("jriDocumentNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriDocumentNumber=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriDocumentNumber");
                ctr++;

            }

            if (criteria.containsKey("jriConversionDate")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriConversionDate=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriConversionDate");
                ctr++;

            }

            if (criteria.containsKey("jriConversionRate")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriConversionRate=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriConversionRate");
                ctr++;

            }

            if (criteria.containsKey("jriReversed")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("jri.jriReversed=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("jriReversed");
                ctr++;

            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");

            }

            jbossQl.append("jri.jriAdCompany=").append(AD_CMPNY).append(" ");


            glJournalInterfaces = glJournalInterfaceHome.getJriByCriteria(jbossQl.toString(), obj, 0, 0);

            if (glJournalInterfaces.size() == 0) throw new GlobalNoRecordFoundException();

            return glJournalInterfaces.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

    }

    // private methods


    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFindJournalInterfaceControllerBean ejbCreate");

    }
}