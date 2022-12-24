package com.ada.forcert.utilities;


import java.io.Serializable;

@SuppressWarnings("serial")
public class UserCredentials implements Serializable {

    private String faFirstName;
    private String faLastName;
    private String enFirstName;
    private String enLastName;
    private String nationalCode;
    private String birthCertificateCode;
    private String telephone;
    private String postalCode;
    private String provinceName;
    private String city;
    private String birthDate;

    private String isMan;
    private String isForeign;
    private String isForTest;

    private String TrackingCode;

    private String serverURL;

    public void setBirthCertificateCode(String birthCertificateCode) {
        this.birthCertificateCode = birthCertificateCode;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEnFirstName(String enFirstName) {
        this.enFirstName = enFirstName;
    }

    public void setEnLastName(String enLastName) {
        this.enLastName = enLastName;
    }

    public void setFaFirstName(String faFirstName) {
        this.faFirstName = faFirstName;
    }

    public void setFaLastName(String faLastName) {
        this.faLastName = faLastName;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setIsForeign(String isForeign) {
        this.isForeign = isForeign;
    }

    public void setIsForTest(String isForTest) {
        this.isForTest = isForTest;
    }

    public void setIsMan(String isMan) {
        this.isMan = isMan;
    }

    public void setTrackingCode(String trackingCode) {
        TrackingCode = trackingCode;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getBirthCertificateCode() {
        return birthCertificateCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    public String getEnFirstName() {
        return enFirstName;
    }

    public String getEnLastName() {
        return enLastName;
    }

    public String getFaFirstName() {
        return faFirstName;
    }

    public String getFaLastName() {
        return faLastName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getIsForeign() {
        return isForeign;
    }

    public String getIsForTest() {
        return isForTest;
    }

    public String getIsMan() {
        return isMan;
    }

    public String getTrackingCode() {
        return TrackingCode;
    }

    public String getServerURL() {
        return serverURL;
    }
}

//https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android