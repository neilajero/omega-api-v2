package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class LocalInvBuildOrderLineHome {

    public static final String JNDI_NAME = "LocalInvBuildOrderLineHome!com.ejb.inv.LocalInvBuildOrderLineHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalInvBuildOrderLineHome() {
    }
}