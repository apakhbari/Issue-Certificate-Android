package com.ada.forcert.utilities;

public class EnvironmentalVariables {
    String caName = "08";
    String caNameTest = "01";
    String customerCode = "";
    String profileName = "گواهی موبایل برنز شخص حقیقی مستقل";
    String trackingCode;
    String url = "http://api.raahbartrust.ir/api";

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public void setCaNameTest(String caNameTest) {
        this.caNameTest = caNameTest;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaName() {
        return caName;
    }
    public String getCaNameTest() {
        return caNameTest;
    }
    public String getCustomerCode() {
        return customerCode;
    }
    public String getProfileName() {
        return profileName;
    }
    public String getTrackingCode() {return trackingCode;}

    public String getUrl() {
        return url;
    }
}
