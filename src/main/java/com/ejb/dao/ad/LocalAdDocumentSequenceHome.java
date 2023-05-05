package com.ejb.dao.ad;


import com.ejb.PersistenceBeanClass;

import com.ejb.entities.ad.LocalAdDocumentSequence;
import jakarta.ejb.EJB;

import com.util.Debug;

import java.util.Date;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.NoResultException;

@Stateless
public class LocalAdDocumentSequenceHome {

    public static final String JNDI_NAME="LocalAdDocumentSequenceHome!com.ejb.ad.LocalAdDocumentSequenceHome";
    
    @EJB
    public PersistenceBeanClass em;
    
    public LocalAdDocumentSequenceHome(){}

    //FINDER METHODS

    public LocalAdDocumentSequence findByPrimaryKey(java.lang.Integer pk)
    throws FinderException {
        
        try{
            
            LocalAdDocumentSequence entity = (LocalAdDocumentSequence)em.find(new LocalAdDocumentSequence(), pk);
            if(entity==null){ throw new FinderException();}
            return entity;
        }
        catch(FinderException ex){
            throw new FinderException(ex.getMessage());
        }
        catch(Exception ex){
            throw ex;
        }
    }


    public java.util.Collection findDsAll(java.lang.Integer DS_AD_CMPNY)
    throws FinderException {
        
        try{
            Query query = em.createQuery("SELECT OBJECT(ds) FROM AdDocumentSequence ds WHERE ds.dsAdCompany = ?1");
            query.setParameter(1,DS_AD_CMPNY);
            return query.getResultList();
        }catch(Exception ex){
            Debug.print("EXCEPTION: Exception com.ejb.gl.LocalAdDocumentSequenceHome.findDsAll(java.lang.Integer DS_AD_CMPNY)");
            throw ex;
        }
    }




    public LocalAdDocumentSequence findByDsName(java.lang.String DS_NM, java.lang.Integer DS_AD_CMPNY)
    throws FinderException {
        
        try{
            Query query = em.createQuery("SELECT OBJECT(ds) FROM AdDocumentSequence ds WHERE ds.dsSequenceName=?1 AND ds.dsAdCompany = ?2");
            query.setParameter(1,DS_NM);
            query.setParameter(2,DS_AD_CMPNY);
            return (LocalAdDocumentSequence)query.getSingleResult();
        }catch(NoResultException ex){
            Debug.print("EXCEPTION: NoResultException com.ejb.gl.LocalAdDocumentSequenceHome.findByDsName(java.lang.String DS_NM, java.lang.Integer DS_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }catch(Exception ex){
            Debug.print("EXCEPTION: Exception com.ejb.gl.LocalAdDocumentSequenceHome.findByDsName(java.lang.String DS_NM, java.lang.Integer DS_AD_CMPNY)");
            throw ex;
        }
    }




    public java.util.Collection findDsEnabled(java.util.Date CRRNT_DT, java.lang.Integer DS_AD_CMPNY)
    throws FinderException {
        
        try{
            Query query = em.createQuery("SELECT OBJECT(ds) FROM AdDocumentSequence ds WHERE ((ds.dsDateFrom <= ?1 AND ds.dsDateTo >= ?1) OR (ds.dsDateFrom <= ?1 AND ds.dsDateTo IS NULL)) AND ds.dsAdCompany = ?2");
            query.setParameter(1,CRRNT_DT);
            query.setParameter(2,DS_AD_CMPNY);
            return query.getResultList();
        }catch(Exception ex){
            Debug.print("EXCEPTION: Exception com.ejb.gl.LocalAdDocumentSequenceHome.findDsEnabled(java.com.util.Date CRRNT_DT, java.lang.Integer DS_AD_CMPNY)");
            throw ex;
        }
    }




    //OTHER METHODS



    //CREATE METHODS

	public LocalAdDocumentSequence create (java.lang.Integer DS_CODE, java.lang.String DS_SQNC_NM,
                                           Date DS_DT_FRM, Date DS_DT_TO, char DS_NMBRNG_TYP,
                                           String DS_INTL_VL, Integer DS_AD_CMPNY)
		throws CreateException {
		try{

			LocalAdDocumentSequence entity = new LocalAdDocumentSequence();


			Debug.print("AdDocumentSequenceBean create");
			entity.setDsCode(DS_CODE);
			entity.setDsSequenceName(DS_SQNC_NM);
			entity.setDsDateFrom(DS_DT_FRM);
			entity.setDsDateTo(DS_DT_TO);
			entity.setDsNumberingType(DS_NMBRNG_TYP);
			entity.setDsInitialValue(DS_INTL_VL);
			entity.setDsAdCompany(DS_AD_CMPNY);

			em.persist(entity);
			return entity;

		}catch(Exception ex){throw new CreateException(ex.getMessage());}
	}
	public LocalAdDocumentSequence create (java.lang.String DS_SQNC_NM,
                                           Date DS_DT_FRM, Date DS_DT_TO, char DS_NMBRNG_TYP,
                                           String DS_INTL_VL, Integer DS_AD_CMPNY)
		throws CreateException {
		try{

			LocalAdDocumentSequence entity = new LocalAdDocumentSequence();


			Debug.print("AdDocumentSequenceBean create");
			entity.setDsSequenceName(DS_SQNC_NM);
			entity.setDsDateFrom(DS_DT_FRM);
			entity.setDsDateTo(DS_DT_TO);
			entity.setDsNumberingType(DS_NMBRNG_TYP);
			entity.setDsInitialValue(DS_INTL_VL);
			entity.setDsAdCompany(DS_AD_CMPNY);

			em.persist(entity);
			return entity;

		}catch(Exception ex){throw new CreateException(ex.getMessage());}
	}


}