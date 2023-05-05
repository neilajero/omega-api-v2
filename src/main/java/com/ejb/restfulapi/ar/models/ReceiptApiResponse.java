package com.ejb.restfulapi.ar.models;

import com.ejb.restfulapi.OfsApiResponse;

public class ReceiptApiResponse extends OfsApiResponse {

    private boolean isOverApplyAmount = false;

    public boolean isOverApplyAmount() {
        return isOverApplyAmount;
    }

    public void setOverApplyAmount(boolean isOverApplyAmount) {
        this.isOverApplyAmount = isOverApplyAmount;
    }
}
