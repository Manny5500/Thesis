package com.example.myapplication;

public class FindStatusWFA {

    public String StatusFinder(int age, double weight,
                               double[] nsd3, double[] nsd2,
                               double[] nsd1, double[] median,
                               double[] sd1, double[] sd2, double[] sd3) {

        String status = "";

        for (int i = 0; i <= 59; i++) {
            if (weight < nsd2[age] && weight >= nsd3[age]) {
                status = "Underweight";
                break;
            } else if (weight < nsd3[age]) {
                status = "Severe Underweight";
                break;
            }
        }
        return status;
    }


    public String StatusFinder_Stunted(int age, double height,
                               double[] nsd3, double[] nsd2,
                               double[] nsd1, double[] median,
                               double[] sd1, double[] sd2, double[] sd3) {

        String status = "";

        for (int i = 0; i <= nsd1.length; i++) {
            if (height < nsd2[age] && height >= nsd3[age]) {
                status = "Stunted";
                break;
            } else if (height < nsd3[age]) {
                status = "Severe Stunted";
                break;
            } else if (height >= sd3[age]) {
                status = "Above Normal";
                break;
            }
        }
        return status;
    }

    public String StatusFinder_Wasted(double weight, double height,
                                       double[] nsd3, double[] nsd2,
                                       double[] nsd1, double[] median,
                                       double[] sd1, double[] sd2, double[] sd3, int position) {

        String status = "";

        for (int i = 0; i <= nsd1.length; i++) {
            if (weight < nsd2[position] && weight >= nsd3[position]) {
                status = "Wasted";
                break;
            } else if (weight < nsd3[position]) {
                status = "Severe Wasted";
            } else if (weight > sd1[position] && weight <= sd2[position]) {
                status = "Overweight";
            } else if (weight >= sd3[position]) {
                status = "Obese";
            }
        }
        return status;
    }

}
