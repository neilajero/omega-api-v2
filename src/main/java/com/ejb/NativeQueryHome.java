package com.ejb;

import com.util.Debug;

import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.List;

@Stateless
public class NativeQueryHome {

    @EJB
    public PersistenceBeanClass em;

    public NativeQueryHome() {

    }

    public List<Object[]> getResultList(String strQ) throws FinderException {

        try {
            Query query = em.createNativeQuery(strQ);
            List<Object[]> list = query.getResultList();
            return list;
        }
        catch (NoResultException ex) {
            Debug.print("EXCEPTION: NoResultException com.ejb.ad.LocalAdCompanyHome.findTest()");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdCompanyHome.findTest()");
            throw ex;
        }
    }

    public Object[] getSingleResult(String strQ) throws FinderException {

        try {
            Query query = em.createNativeQuery(strQ);
            Object[] obj = (Object[]) query.getSingleResult();
            return obj;
        }
        catch (NoResultException ex) {
            Debug.print("EXCEPTION: NoResultException com.ejb.ad.LocalAdCompanyHome.findTest()");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdCompanyHome.findTest()");
            throw ex;
        }
    }

}