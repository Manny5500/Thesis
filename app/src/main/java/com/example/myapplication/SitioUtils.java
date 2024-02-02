package com.example.myapplication;

public class SitioUtils {

    public static String[] getSitioList(String barangay){
        String[] sitioList;
        if(barangay.equals("Alipit")){

            sitioList = new String[]{"Ali"};

        }else if (barangay.equals("Malaking Ambling")) {

            sitioList = new String[]{"MA"};

        }else if (barangay.equals("Munting Ambling")) {

            sitioList = new String[]{"MUA"};

        }else if (barangay.equals("Baanan")) {

            sitioList = new String[]{"BN"};

        }else if (barangay.equals("Balanac")) {

            sitioList = new String[]{"BL"};

        }else if (barangay.equals("Bucal")) {

            sitioList = new String[]{"Batisan", "Butuanan"};

        }else if (barangay.equals("Buenavista")) {

            sitioList = new String[]{"Buen"};

        }else if (barangay.equals("Bungkol")) {

            sitioList = new String[]{"Bunk"};

        }else if (barangay.equals("Buo")) {
            sitioList = new String[]{"Buo"};

        }else if (barangay.equals("Burlungan")) {
            sitioList = new String[]{"Burl"};

        }else if (barangay.equals("Cigaras")) {
            sitioList = new String[]{"Cig"};

        }else if (barangay.equals("Ibabang Atingay")) {
            sitioList = new String[]{"IAT"};

        }else if (barangay.equals("Ibabang Butnong")) {
            sitioList = new String[]{"IBU"};

        }else if (barangay.equals("Ilayang Atingay")) {
            sitioList = new String[]{"IATN"};

        }else if (barangay.equals("Ilayang Butnong")) {
            sitioList = new String[]{"IBUG"};

        }else if (barangay.equals("Ilog")) {
            sitioList = new String[]{"Ilog"};

        }else if (barangay.equals("Malinao")) {
            sitioList = new String[]{"Mal"};

        }else if (barangay.equals("Maravilla")) {
            sitioList = new String[]{"Mar"};

        }else if (barangay.equals("Poblacion")) {
            sitioList = new String[]{"Pobl"};

        }else if (barangay.equals("Sabang")) {
            sitioList = new String[]{"Sab"};

        }else if (barangay.equals("Salasad")) {
            sitioList = new String[]{"Salad"};

        }else if (barangay.equals("Tanawan")) {
            sitioList = new String[]{"Tan"};

        }else if (barangay.equals("Halayhayin")) {
            sitioList = new String[]{"Hal"};

        }else if (barangay.equals("Tipunan")){
            sitioList = new String[]{"Tip"};
        }
        else{
            sitioList = new String[]{"Sitio"};
        }

        return sitioList;
    }

}
