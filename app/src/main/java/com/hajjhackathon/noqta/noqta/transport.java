package com.hajjhackathon.noqta.noqta;

public class transport {
    String arafatentriesno;
    String cartype;
    String makkahentrydate;
    String minaentriesno;

    String order;
    String drivername;
    String driverid;

    public String getOrder() {
        return order;
    }

    public transport(String arafatentriesno, String cartype, String makkahentrydate, String minaentriesno, String order, String drivername, String driverid, String permissionno) {
        this.arafatentriesno = arafatentriesno;
        this.cartype = cartype;
        this.makkahentrydate = makkahentrydate;
        this.minaentriesno = minaentriesno;
        this.order = order;
        this.drivername = drivername;
        this.driverid = driverid;
        this.permissionno = permissionno;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public transport() {
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
