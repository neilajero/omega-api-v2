package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class LocalInvBuildOrderHome {

    public static final String JNDI_NAME = "LocalInvBuildOrderHome!com.ejb.inv.LocalInvBuildOrderHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalInvBuildOrderHome() {
    }

    public java.util.Collection getBorByCriteria(java.lang.String jbossQl, java.lang.Object[] args) throws FinderException {

        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }
}