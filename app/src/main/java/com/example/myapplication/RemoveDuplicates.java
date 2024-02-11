package com.example.myapplication;

import java.util.ArrayList;

public class RemoveDuplicates{
    public static ArrayList<Child> removeDuplicates(ArrayList<Child> arrayList){
        ArrayList<Child> filteredArrayList = new ArrayList<>();
        for(Child child: arrayList){
            boolean isDuplicate = false;
            String fullname = child.getChildFirstName()+child.getChildMiddleName()+child.getChildLastName();
            String gmail = child.getGmail();
            for(Child child2: filteredArrayList){
                String fullname2 = child2.getChildFirstName()+child2.getChildMiddleName()+child2.getChildLastName();
                String gmail2 = child2.getGmail();
                if(filteredArrayList.contains(child2) &&
                        fullname.equals(fullname2) && gmail.equals(gmail2)){
                    isDuplicate = true;
                }
            }
            if(!isDuplicate){
                filteredArrayList.add(child);
            }
        }
        return filteredArrayList;
    }
}
