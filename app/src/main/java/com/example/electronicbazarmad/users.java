package com.example.electronicbazarmad;

public class users {
    private String uid;
    private String ufname;
    private String ulname;


    public users(String uid, String ufname, String ulname) {
        this.uid = uid;
        this.ufname = ufname;
        this.ulname = ulname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUfname() {
        return ufname;
    }

    public void setUfname(String ufname) {
        this.ufname = ufname;
    }

    public String getUlname() {
        return ulname;
    }

    public void setUlname(String ulname) {
        this.ulname = ulname;
    }


}
