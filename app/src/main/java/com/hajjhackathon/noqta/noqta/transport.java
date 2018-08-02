package com.hajjhackathon.noqta.noqta;

public class transport {
    String arafatentriesno;
    String cartype;
    String makkahentrydate;
    String minaentriesno;

    public transport() {
    }

    public transport(String arafatentriesno, String cartype, String makkahentrydate, String minaentriesno, String permissionno) {
        this.arafatentriesno = arafatentriesno;
        this.cartype = cartype;
        this.makkahentrydate = makkahentrydate;
        this.minaentriesno = minaentriesno;
        this.permissionno = permissionno;
    }

    public String getArafatentriesno() {
        return arafatentriesno;
    }

    public void setArafatentriesno(String arafatentriesno) {
        this.arafatentriesno = arafatentriesno;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getMakkahentrydate() {
        return makkahentrydate;
    }

    public void setMakkahentrydate(String makkahentrydate) {
        this.makkahentrydate = makkahentrydate;
    }

    public String getMinaentriesno() {
        return minaentriesno;
    }

    public void setMinaentriesno(String minaentriesno) {
        this.minaentriesno = minaentriesno;
    }

    public String getPermissionno() {
        return permissionno;
    }

    public void setPermissionno(String permissionno) {
        this.permissionno = permissionno;
    }

    String permissionno;
}
