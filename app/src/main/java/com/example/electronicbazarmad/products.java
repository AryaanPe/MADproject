package com.example.electronicbazarmad;
public class products {
    private String PID;
    private String PName;
    private String PCost;

    public products(String pid, String pName, String pCost) {
        this.PID = pid;
        this.PName = pName;
        this.PCost = pCost;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getPCost() {
        return PCost;
    }

    public void setPCost(String PCost) {
        this.PCost = PCost;
    }
}
