package com.ejb.restfulapi.ar.models;

public class MemolineDetails {
    private String memoLineName;
    private Double applyAmount;

    public String getMemoLineName() {
        return memoLineName;
    }

    public void setMemoLineName(String memoLineName) {
        this.memoLineName = memoLineName;
    }

    public Double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }
}