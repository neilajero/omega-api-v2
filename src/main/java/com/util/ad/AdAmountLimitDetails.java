package com.util.ad;

import com.ejb.entities.ad.LocalAdAmountLimit;
import com.ejb.restfulapi.ad.models.ApprovalUser;

import java.util.List;

public class AdAmountLimitDetails extends LocalAdAmountLimit implements java.io.Serializable {

    private List<ApprovalUser> users = null;

    public AdAmountLimitDetails() {

    }

    public List<ApprovalUser> getUsers() {

        return users;
    }

    public void setUsers(List<ApprovalUser> users) {

        this.users = users;
    }

}