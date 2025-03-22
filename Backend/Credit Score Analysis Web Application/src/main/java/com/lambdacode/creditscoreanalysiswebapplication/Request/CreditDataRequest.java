package com.lambdacode.creditscoreanalysiswebapplication.Request;


public class CreditDataRequest {
    private float creditUtilizationRatio;
    private float numOfDelayedPayment;
    private float creditHistoryAgeMonths;
    private float numCreditInquiries;
    private float creditMix;

    public float getCreditUtilizationRatio() {
        return creditUtilizationRatio;
    }

    public void setCreditUtilizationRatio(float creditUtilizationRatio) {
        this.creditUtilizationRatio = creditUtilizationRatio;
    }

    public float getNumOfDelayedPayment() {
        return numOfDelayedPayment;
    }

    public void setNumOfDelayedPayment(float numOfDelayedPayment) {
        this.numOfDelayedPayment = numOfDelayedPayment;
    }

    public float getCreditHistoryAgeMonths() {
        return creditHistoryAgeMonths;
    }

    public void setCreditHistoryAgeMonths(float creditHistoryAgeMonths) {
        this.creditHistoryAgeMonths = creditHistoryAgeMonths;
    }

    public float getNumCreditInquiries() {
        return numCreditInquiries;
    }

    public void setNumCreditInquiries(float numCreditInquiries) {
        this.numCreditInquiries = numCreditInquiries;
    }

    public float getCreditMix() {
        return creditMix;
    }

    public void setCreditMix(float creditMix) {
        this.creditMix = creditMix;
    }
}
