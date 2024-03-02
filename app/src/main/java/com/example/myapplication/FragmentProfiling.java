package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentProfiling extends Fragment {

    View view;
    TextView namText, polBod, chilBod, falCont, vulCont, gBCont, fPCont;
    MaterialCardView polView, chilView, falView, vulView, gBView, fPView;
    FirebaseFirestore db;

    ArrayList<Child> childList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profiling, container, false);
        db = FirebaseFirestore.getInstance();
        namText = view.findViewById(R.id.namText);
        falCont = view.findViewById(R.id.falCont);
        polBod = view.findViewById(R.id.polBod);
        chilBod = view.findViewById(R.id.chilBod);
        polView = view.findViewById(R.id.polView);
        chilView = view.findViewById(R.id.chilView);
        falView = view.findViewById(R.id.falView);
        vulView = view.findViewById(R.id.vulView);
        gBView = view.findViewById(R.id.gBView);
        gBCont = view.findViewById(R.id.gBCont);
        vulCont = view.findViewById(R.id.vulCont);
        fPView = view.findViewById(R.id.fPView);
        fPCont = view.findViewById(R.id.fPCont);
        namText.setText(App.user.getBarangay());
        setPopulation();
        cBevent();
        pBevent();
        setFamily();
        if(App.user.getBarangay()!=null){
            getChildData();
            vulViewEvent();
            falViewEvent();
            gBViewEvent();
            fPViewEvent();

        }

        return view;
    }


    private void setPieChart(){
        int lightBlueColor = Color.parseColor("#3498db");
        int darkBlueColor = Color.parseColor("#2980b9");
        int whiteColor = Color.parseColor("#FFFFFFFF");
        int[] colors1 = {lightBlueColor, darkBlueColor, whiteColor};
        int cNor =0;
        int cMal = 0;
        for(Child child: childList){
            boolean isNormal = child.getStatusdb().contains("Normal");
            if(isNormal){
                cNor++;
            }else{
                cMal++;
            }
        }
        PieChart rTBod =  ChartMaker.cPNM(view, R.id.rTBod, colors1, cNor, cMal, cNor+cMal, "Normal vs Malnourished");
    }


    private void getChildData(){
        db.collection("children").whereEqualTo("barangay", App.user.getBarangay()).orderBy("dateAdded", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<Child> arrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                Child child = doc.toObject(Child.class);
                                child.setId(doc.getId());
                                arrayList.add(child);
                            }
                            childList = RemoveDuplicates.removeDuplicates(arrayList);
                            vulCont.setText(String.valueOf(getVulnerable()));
                            gBCont.setText(String.valueOf(getGB()));
                            fPCont.setText(String.valueOf(getFP()));
                            setPieChart();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase myexception", ""+e);
                        Toast.makeText(requireContext(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setPopulation(){
        DocumentReference docRefs = db.collection("barangay").document(App.user.getBarangay());
        docRefs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        BarangayModel bM = document.toObject(BarangayModel.class);
                        bM.setBarangay(document.getId());
                        if(bM.getEstimatedChildren()>0){
                            chilBod.setText(String.valueOf(bM.getEstimatedChildren()));
                        }
                        if(bM.getPopulation()>0){
                            polBod.setText(String.valueOf(bM.getPopulation()));
                        }
                    } else {
                        Log.d("Firetore No Docu", "No such document");
                    }
                } else {
                    Log.e("Firestore Exception", "get failed with ", task.getException());
                }
            }
        });
    }

    private int  getVulnerable(){
        int count = 0;
        ArrayList<Child> fGmail = RemoveDuplicates.rDGmail(childList);
        for(Child child: fGmail){
            boolean isMal = !child.getStatusdb().contains("Normal");
            boolean isLowest = child.getMonthlyIncome().equals("Less than 9,100");
            boolean isLower = child.getMonthlyIncome().equals("9,100 to 18,200");
            boolean isLow = child.getMonthlyIncome().equals("18,200 to 36,400");
            boolean isLowIncome = isLowest || isLower || isLow;

            int count_child = 0;
            for(Child child1: childList){
                if(child1.getGmail().equals(child.getGmail())){
                    count_child++;
                }
            }

            boolean isGOne = count_child>1;

            if(isMal||isLowIncome||isGOne){
                count++;
            }
        }
        return count;

    }

    private int  getGB(){
        int count = 0;
        ArrayList<Child> fGmail = RemoveDuplicates.rDGmail(childList);
        for(Child child: fGmail){
            if(child.getForgulayan()!=null){
                boolean isGB = child.getForgulayan().equals("Yes");
                if(isGB){
                    count++;
                }
            }
        }
        return count;
    }

    private int getFP(){
        int count = 0;
        for(Child child: childList){
            if(child.getForfeeding()!=null){
                boolean isFP = child.getForfeeding().equals("Yes");
                if(isFP){
                    count++;
                }
            }
        }
        return count;
    }

    private void setFamily(){
        db.collection("tempEmail").whereEqualTo("barangay", App.user.getBarangay())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<TempEmail> tEList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                TempEmail tEm = doc.toObject(TempEmail.class);
                                tEList.add(tEm);
                            }
                            falCont.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase myexception", ""+e);
                        Toast.makeText(requireContext(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cBevent(){
        chilBod.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBDDialog("EC");
                return true;
            }
        });
    }
    private void pBevent(){
        polBod.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBDDialog("pop");
                return true;
            }
        });
    }
    private void falViewEvent(){
        falView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), FamilyList.class));
            }
        });
    }
    private void vulViewEvent(){
        vulView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), VulnerableFamily.class));
            }
        });
    }

    private void gBViewEvent(){

        gBView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), GSBList.class));
            }
        });
    }

    private void fPViewEvent(){

        fPView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), FPList.class));
            }
        });
    }

    private void showBDDialog(String type){
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.bd_dialog);

        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        TextInputLayout textView12 = dialog.findViewById(R.id.textView12);
        TextInputEditText textNum = dialog.findViewById(R.id.textNum);




        if(type.equals("pop")){
            textView12.setHint("Enter the population");
        } else if (type.equals("EC")) {
            textView12.setHint("Enter the estimated children");
        }


        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!textNum.getText().toString().trim().equals("")) {
                    int num = Integer.parseInt(textNum.getText().toString().trim());
                    BDUtils bdUtils = new BDUtils(requireContext(), App.user.getBarangay(), num, type, dialog);
                    bdUtils.updateBar();
                }

            }
        });
    }



}