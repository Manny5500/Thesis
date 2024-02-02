package com.example.myapplication;

public class TempEmail {
    String gmail;
    String parentFirstName;

    String parentMiddleName;
    String parentLastName;

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getParentFirstName() {
        return parentFirstName;
    }

    public void setParentFirstName(String parentFirstName) {
        this.parentFirstName = parentFirstName;
    }

    public String getParentMiddleName() {
        return parentMiddleName;
    }

    public void setParentMiddleName(String parentMiddleName) {
        this.parentMiddleName = parentMiddleName;
    }

    public String getParentLastName() {
        return parentLastName;
    }

    public void setParentLastName(String parentLastName) {
        this.parentLastName = parentLastName;
    }

    public String getFullName(){
        return parentFirstName + " " + parentMiddleName + " " + parentLastName;
    }

}
