package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class PriorityClicked extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 1;
    String phoneNumber = "123456789";
    String name = "", birthdate = "", age = "", referraldate = "",
            mothername = "", sex = "", municipality="", barangay="", houseno="";
    Dialog dialog2;
    String pdfUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_clicked);

        phoneNumber = App.child.getPhoneNumber();
        pdfUrl = App.child.getDownloadUrl();
        Button callButton = findViewById(R.id.btnCallDynamics);
        Button referral = findViewById(R.id.btnReferralDynamics);
        Button viewReferral = findViewById(R.id.btnViewReferral);
        dialog2 = new Dialog(PriorityClicked.this);
        dialog2.setContentView(R.layout.dialog_loader);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PriorityClicked.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PriorityClicked.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    // Permission has already been granted
                    makePhoneCall(phoneNumber);
                }
            }
        });

        referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndUploadPdf();
            }
        });

        viewReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PriorityClicked.this);
                dialog.setContentView(R.layout.pdf_viewer);
                PDFView pdfView = dialog.findViewById(R.id.pdfView);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                Window window = dialog.getWindow();
                window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                if(pdfUrl!=null){
                    dialog.show();
                    displayFromUrl(pdfUrl, pdfView, progressBar);
                }else{
                    Toast.makeText(PriorityClicked.this, "No referral form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textName, textAge, textSex, textWeight,
                textHeight, textStatus;

        TextView textNameLabel, textAgeLabel, textGenderLabel,
                textWeightLabel, textHeightLabel, textStatusLabel;

        textName = findViewById(R.id.textNameDynamics);
        textAge = findViewById(R.id.textAgeDynamics);
        textSex = findViewById(R.id.textMaleDynamics);
        textWeight = findViewById(R.id.textWeightDynamics);
        textHeight = findViewById(R.id.textHeightDynamics);
        textStatus = findViewById(R.id.textStatusDynamics);

        textNameLabel = findViewById(R.id.labelPCName);
        textAgeLabel = findViewById(R.id.labelAge);
        textGenderLabel = findViewById(R.id.labelGenderDynamics);
        textWeightLabel = findViewById(R.id.labelWeightDynamics);
        textHeightLabel = findViewById(R.id.labelHeightDynamics);
        textStatusLabel = findViewById(R.id.labelStatusDynamics);

        textName.setText(App.child.getChildFirstName()+" "+App.child.getChildLastName());
        textSex.setText(App.child.getSex());
        textWeight.setText(App.child.getWeight() + " kg");
        textHeight.setText(App.child.getHeight() + " cm");
        String statusmaker = "";

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(String status : App.child.getStatusdb()){
            statusmaker = statusmaker + status + "\n";
        }
        textStatus.setText(statusmaker);


        name = App.child.getChildFirstName()+App.child.getChildFirstName();
        birthdate = App.child.getBirthDate();
        referraldate = currentDate.format(formatter);
        mothername = App.child.getParentFirstName() + " " + App.child.getParentLastName();
        municipality = "Magdalena, Laguna";
        barangay = App.child.getBarangay();
        houseno = App.child.getHouseNumber();
        sex = App.child.getSex();

        FormUtils formUtils = new FormUtils();
        Date parsedDate = formUtils.parseDate(App.child.getBirthDate());

        int monthdiff = 0;

        if (parsedDate != null) {
            monthdiff = formUtils.calculateMonthsDifference(parsedDate);
            age = String.valueOf(monthdiff);
            textAge.setText(String.valueOf(monthdiff)+ " months");

        } else {
            Toast.makeText(this, "Failed to parse the date", Toast.LENGTH_SHORT).show();
        }

        setTextSize(textAge,textStatus, textSex, textName, textWeight, textHeight);
        setTextSize(textAgeLabel, textStatusLabel, textGenderLabel, textNameLabel, textWeightLabel, textHeightLabel);


    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phoneNumber);
            } else {
                // Permission denied, inform the user and handle accordingly
                Toast.makeText(this, "Please allow the phone permission in the settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setTextSize(TextView... textViews){
        for(TextView textView: textViews){
            textView.setTextSize(18);
        }
    }

    public void createAndUploadPdf(){
        dialog2.show();
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(13);
        Paint painttitle = new Paint();
        painttitle.setColor(Color.BLACK);
        painttitle.setTextSize(16);
        painttitle.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Referral Transfer Form/SAM", 80, 50, painttitle);
        String[] label = {"Name", "Sex", "Date of Birth", "Age in month", "Date of Referral",
                "Name of mother/caregiver", "Registration Number (mother/caregiver)", "Contact Number",
                "Municipality", "Barangay", "House No"};
        String[] value = {name, sex, birthdate, age, referraldate, mothername, "", phoneNumber, municipality,
        barangay, houseno};
        int yaxis=70;
        for(int i= 0; i<label.length;i++){
            canvas.drawText(label[i]+" :  " + value[i], 80, yaxis, paint );
            yaxis = yaxis+20;
        }
        pdfDocument.finishPage(page);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            pdfDocument.writeTo(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfDocument.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child("pdfs/" + name);

        UploadTask uploadTask = pdfRef.putBytes(pdfBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL
                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                    @Override
                    public void onSuccess(android.net.Uri uri) {
                        // Store the download URL in Firestore
                        pdfUrl=uri.toString();
                        storeDownloadUrlInFirestore(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.e("TAG", "Error getting download URL", e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Log.e("TAG", "Error uploading PDF", e);
            }
        });

    }
    private void storeDownloadUrlInFirestore(String downloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference pdfCollection = db.collection("children");

        // Create a Map with the PDF data
        Map<String, Object> pdfData = new HashMap<>();
        pdfData.put("downloadUrl", downloadUrl);
        pdfData.put("fname", name);

        // Add the PDF data to Firestore
        pdfCollection.document(App.child.getId()).update(pdfData)
                .addOnSuccessListener(aVoid -> {
                    dialog2.dismiss();
                    Toast.makeText(this, "Successfully generated the document", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                });
    }

    private void displayFromUrl(String pdfUrl, PDFView pdfView, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {

                URL url = new URL(pdfUrl);
                File file = File.createTempFile("temp", "pdf");

                // Download the PDF from the URL and save it to a temporary file
                FileUtils.copyURLToFile(url, file);
                Log.d("PDFViewer", "PDF file path: " + file.getPath());
                // Load the PDF from the temporary file
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);

                    // Display the PDF
                    pdfView.fromFile(file)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .load();
                });
            } catch (IOException e) {
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
                Log.e("PDFViewer", "Error loading PDF: " + e.getMessage());
            }
        });
    }
}
