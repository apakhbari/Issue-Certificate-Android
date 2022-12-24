package com.ada.forcert.utilities;

import java.io.Serializable;

public class CertificateCredentials  implements Serializable
{

    String IsSuccess;
    String Certificate;
    String PaymentStatus;
    String Description;
    String CN;
    String IssuerName;
    String ValidFrom;
    String ValidTo;

    public void setCertificate(String certificate) {
        Certificate = certificate;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public void setIssuerName(String issuerName) {
        IssuerName = issuerName;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public void setValidFrom(String validFrom) {
        ValidFrom = validFrom;
    }

    public void setValidTo(String validTo) {
        ValidTo = validTo;
    }

    public String getCertificate() {
        return Certificate;
    }

    public String getCN() {
        return CN;
    }

    public String getDescription() {
        return Description;
    }

    public String getIsSuccess() {
        return IsSuccess;
    }

    public String getIssuerName() {
        return IssuerName;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public String getValidTo() {
        return ValidTo;
    }
}
