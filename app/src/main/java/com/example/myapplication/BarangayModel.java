package com.example.myapplication;

public class BarangayModel {

    private double malnutritionRate;
    private int totalAssess;
    private double povertyIndex;
    private int numberOfUnderweight;
    private int numberOfSevereUnderweight;

    private int Overweight;
    private int wasted;
    private int SevereWasted;
    private int Stunted;
    private int SevereStunted;
    private int Normal;

    private String barangay;

    // Constructor
    /*
    public BarangayModel(String barangay, double malnutritionRate, int totalAssess, double povertyIndex,
                         int numberOfUnderweight, int numberOfSevereUnderweight,
                         int Overweight, int wasted, int SevereWasted,
                         int Stunted, int SevereStunted, int Normal) {
        this.malnutritionRate = malnutritionRate;
        this.totalAssess = totalAssess;
        this.povertyIndex = povertyIndex;
        this.numberOfUnderweight = numberOfUnderweight;
        this.numberOfSevereUnderweight = numberOfSevereUnderweight;
        this.Overweight = Overweight;
        this.wasted = wasted;
        this.SevereWasted = SevereWasted;
        this.Stunted =Stunted;
        this.SevereStunted = SevereStunted;
        this.barangay = barangay;
        this.Normal = Normal;
    }**/

    public String getBarangay(){return barangay;}
    public void setBarangay(String barangay){this.barangay = barangay;}

    public int getNormal(){return Normal;}

    public void setNormal(int normal) {
        Normal = normal;
    }

    // Getters and setters
    public double getMalnutritionRate() {
        return malnutritionRate;
    }

    public void setMalnutritionRate(double malnutritionRate) {
        this.malnutritionRate = malnutritionRate;
    }

    public int getTotalAssess() {
        return totalAssess;
    }

    public void setTotalAssess(int totalAssess) {
        this.totalAssess = totalAssess;
    }

    public double getPovertyIndex() {
        return povertyIndex;
    }

    public void setPovertyIndex(double povertyIndex) {
        this.povertyIndex = povertyIndex;
    }

    public int getNumberOfUnderweight() {
        return numberOfUnderweight;
    }

    public void setNumberOfUnderweight(int numberOfUnderweight) {
        this.numberOfUnderweight = numberOfUnderweight;
    }

    public int getNumberOfSevereUnderweight() {
        return numberOfSevereUnderweight;
    }

    public void setNumberOfSevereUnderweight(int numberOfSevereUnderweight) {
        this.numberOfSevereUnderweight = numberOfSevereUnderweight;
    }

    public int getOverweight() {
        return Overweight;
    }

    public void setOverweight(int overweight) {
        Overweight = overweight;
    }

    public int getWasted() {
        return wasted;
    }

    public void setWasted(int wasted) {
        this.wasted = wasted;
    }

    public int getSevereWasted() {
        return SevereWasted;
    }

    public void setSevereWasted(int severeWasted) {
        SevereWasted = severeWasted;
    }

    public int getStunted() {
        return Stunted;
    }

    public void setStunted(int stunted) {
        Stunted = stunted;
    }

    public int getSevereStunted() {
        return SevereStunted;
    }

    public void setSevereStunted(int severeStunted) {
        SevereStunted = severeStunted;
    }

}
