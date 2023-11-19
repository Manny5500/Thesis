package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
                break;
            } else if (weight >= sd1[position] && weight <= sd2[position]) {
                status = "Overweight";
                break;
            } else if (weight >= sd3[position]) {
                status = "Obese";
                break;
            }
        }
        return status;
    }


    public static ArrayList<String> CalculateMalnourished(Context context, int age, double weight, double height, String sex){
        ArrayList<String> status = new ArrayList<>();
        ArrayList<String> statusdb = new ArrayList<>();

        if(sex.equals("Male") && age<60 && age>= 0){
            WFA_Boys wfa = new WFA_Boys();
            if(!wfa.WFA_Boys_M(age,weight).equals("")){
                status.add(wfa.WFA_Boys_M(age, weight));
            }
            if(age>=0 && age<24)
            {
                LFA_Boys lfaBoys = new LFA_Boys();
                if(!lfaBoys.LFA_Boys_M(height,age).equals("")){
                    status.add(lfaBoys.LFA_Boys_M(height, age));
                }

                WFL_Boys wflBoys = new WFL_Boys();
                if(!wflBoys.WFL_Boys_M(weight,height).equals("")){
                    status.add(wflBoys.WFL_Boys_M(weight, height));
                }

            } else if (age>= 24 && age <60) {
                HFA_Boys hfaBoys = new HFA_Boys();
                if(!hfaBoys.HFA_Boys_M(height, age).equals("")){
                    status.add(hfaBoys.HFA_Boys_M(height, age));
                }

                WFH_Boys wfhBoys = new WFH_Boys();
                if(!wfhBoys.WFH_Boys_M(weight, height).equals("")){
                    status.add(wfhBoys.WFH_Boys_M(weight, height));
                }
            }else{
                Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            }
            if(status.isEmpty()){
                status.add("Normal");
            }
            statusdb = showDialogMalnourished(context, status);

        } else if (sex.equals("Female") && age<60 && age>=0) {
            WFA_Girls wfag = new WFA_Girls();
            if(!wfag.WFA_Girls_M(age,weight).equals("")){
                status.add(wfag.WFA_Girls_M(age, weight));
            }
            if(age>=0 && age<24)
            {
                LFA_Girls lfaGirls = new LFA_Girls();
                if(!lfaGirls.LFA_Girls_M(height,age).equals("")){
                    status.add(lfaGirls.LFA_Girls_M(height, age));
                }
                WFL_Girls wflGirls = new WFL_Girls();
                if(!wflGirls.WFL_Girls_M(weight, age).equals("")){
                    status.add(wflGirls.WFL_Girls_M(weight, height));
                }

            } else if (age>= 24 && age <60) {
                HFA_Girls  hfaGirls = new HFA_Girls();
                if(!hfaGirls.HFA_Girls_M(height,age).equals("")){
                    status.add(hfaGirls.HFA_Girls_M(height, age));
                }
                WFH_Girls wfhGirls = new WFH_Girls();
                if(!wfhGirls.WFH_Girls_M(weight, height).equals("")){
                    status.add(wfhGirls.WFH_Girls_M(weight, height));
                }
            }else{
                Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            }

            if(status.isEmpty()){
                status.add("Normal");
            }
            statusdb = showDialogMalnourished(context, status);

        } else {
            Toast.makeText(context, "Invalid ages", Toast.LENGTH_SHORT).show();
        }

        return statusdb;
    }

    public static ArrayList<String> showDialogMalnourished(Context context, ArrayList<String> status){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinkedHashSet<String> setWithoutDuplicates = new LinkedHashSet<>(status);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(setWithoutDuplicates);

        String message = "";
        for(String element: listWithoutDuplicates){
            message = message + "\t" + element + "\n";
        }
        builder.setTitle("Child Status")
                .setMessage(message);

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        return listWithoutDuplicates;
    };

    public static Set<String> Recommendations(ArrayList<String> statusList, int age){

        Set<String> recommendationSet = new HashSet<>();
        StringBuilder recommendation = new StringBuilder();
        ArrayList<String> statuses = new ArrayList<>();
        //ArrayList<String> recommendationSet = new ArrayList<>();
        statuses.add("Severe Underweight");
        statuses.add("Severe Wasted");
        String[] recommendations = {
                "Exclusive breastfeeding", "Refer to pediatrician",
                "Regularly monitor your child's growth", "Immediate Medical Care",
                "Go to the nearest medical facility", "Emergency",
                "Portion control", "Give nutritious food",
                "Frequent, Balanced Meal", "Refer to pediatrician if necessary",
                "Encourage balanced diet", "Control the food portion",
                "Go to the nearest hospital", "Encourage physical activity"
        };

        for(String status:statusList){
            Boolean isUnderweight = status.equals("Underweight");
            Boolean isSevereUnderweight = status.equals("Severe Underweight");
            Boolean isWasted = status.equals("Wasted");
            Boolean isSevereWasted = status.equals("Severe Wasted");
            Boolean isStunted = status.equals("Stunted");
            Boolean isSevereStunted = status.equals("Severe Stunted");
            Boolean isOverweight = status.equals("Overweight");
            if(age<=6){
                recommendationSet.add(recommendations[0]);

                if(isUnderweight || isOverweight){
                    recommendationSet.add(recommendations[1]);
                    recommendationSet.add(recommendations[2]);
                }
                if(isSevereUnderweight||isStunted||isWasted){
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
                if(isSevereStunted||isSevereWasted){
                    recommendationSet.add(recommendations[5]);
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
            }

            if(age>6 && age<=23){
                if(isUnderweight){
                    recommendationSet.add(recommendations[1]);
                }
                if(isOverweight){
                    recommendationSet.add(recommendations[6]);
                }
                if(isStunted){
                    recommendationSet.add(recommendations[1]);
                    recommendationSet.add(recommendations[7]);
                }
                if(isSevereUnderweight||isSevereWasted||isSevereStunted||isWasted){
                    recommendationSet.add(recommendations[5]);
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
            }
            if(age>=23 && age<=59){
                if(isUnderweight||isStunted){
                    recommendationSet.add(recommendations[7]);
                    recommendationSet.add(recommendations[8]);
                    recommendationSet.add(recommendations[9]);
                }
                if(isOverweight){
                    recommendationSet.add(recommendations[10]);
                    recommendationSet.add(recommendations[11]);
                }
                
                if(isSevereUnderweight||isSevereWasted||isSevereStunted||isWasted){
                    recommendationSet.add(recommendations[5]);
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
            }

        }
        return recommendationSet;
    }
}
