package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;

public class SRDPPdf {
    ArrayList<Child> childList;
    int[] tfAges;
    SRDPSum sSum;
    SRDPConso sConso;
    SRDPList sList;

    int[][] listData;
    int[] consoData;
    String[] consoPerc;

    int[] dataData;
    int[] dataMother;
    int[] dataCount;

    String[][] masterData = new String[13][22];

    public SRDPPdf(ArrayList<Child> childList){
        this.childList = childList;
        setObjects();
        setArrData();
    }
    public int[] getTfAges(){
        return sList.tfAges(sList.monthFilter());
    }
    public void setTfAges(int[] tfAges) {
        this.tfAges = tfAges;
    }
    public void setObjects(){
        this.sSum = new SRDPSum(childList);
        this.sConso = new SRDPConso(childList);
        this.sList = new SRDPList(childList);
    }
    public void setArrData(){
        this.listData = sList.countNow(sList.monthFilter());
        this.consoData = sConso.countNow(sConso.monthFilter());
        this.consoPerc = sConso.getPercentage(consoData);
        this.dataData = sSum.countNow(sSum.monthFilter());
        this.dataMother = sSum.countNowMother(sSum.monthFilter());
        this.dataCount = sSum.countNowData(childList);

        setMasterData();
    }

     public void setMasterData(){
        for(int i=0; i<4; i++){
            int k=0;
            int l=0;
            for(int j=0; j<6; j++){
                //--WFA
                masterData[i][k+0] = String.valueOf(listData[i + l][0]);
                masterData[i][k+1] = String.valueOf(listData[i + l][1]);
                masterData[i][k+2] = String.valueOf(listData[i+l][0] + listData[i+l][1]);

                //--HFA
                int m = i+4;
                masterData[m][k+0] = String.valueOf(listData[m + l][0]);
                masterData[m][k+1] = String.valueOf(listData[m + l][1]);
                masterData[m][k+2] = String.valueOf(listData[m+l][0] + listData[m+l][1]);

                k = k + 3;
                l = l+4;
            }
        }

         for(int i=8; i<13; i++){
             int k=0;
             int l=40;
             for(int j=0; j<6; j++){
                 //--WFH
                 masterData[i][k+0] = String.valueOf(listData[i + l][0]);
                 masterData[i][k+1] = String.valueOf(listData[i + l][1]);
                 masterData[i][k+2] = String.valueOf(listData[i + l][0] + listData[i+l][1]);
                 k = k + 3;
                 l = l+5;
             }
         }

         //--Consolidated
         for(int i=0; i<13; i++){
             int k=0;
             for(int j=18; j<22; j++){
                 if(j % 2 == 0){
                     masterData[i][j] = String.valueOf(consoData[i]);
                 }else{
                     masterData[i][j] = consoPerc[i];
                 }
                 k = k + 13;
             }
         }


         for(int i= 0; i<13; i++){
             StringBuilder rowStringBuilder = new StringBuilder();
             for(int j=0; j<22; j++){
                 rowStringBuilder.append(masterData[i][j]).append(" ");
             }
             Log.d("2D Array", rowStringBuilder.toString());
         }

     }

}
