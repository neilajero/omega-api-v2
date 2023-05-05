package com.util.ad;

import com.ejb.entities.ad.LocalAdApprovalUser;

public class AdApprovalUserDetails extends LocalAdApprovalUser implements java.io.Serializable{

    private String companyShortName;

    public AdApprovalUserDetails(){}

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

}