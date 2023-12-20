package com.example.myapplication;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;

public class Prevailance_Reports extends AppCompatActivity {
    MaterialAutoCompleteTextView textType, textDate;
    TextInputLayout textTypeLayout;

    View up_View;
    TextView textThisMonth,  textTypeTitle, textDataThisMonth,
            textIncreased, textDataIncreased;

    FloatingActionButton pdfMaker;
    FirebaseFirestore db;
    RecyclerView barangayRecycler;
    private BarangayAdapter userAdapter;
    private int statusPointer = -1;

    Dialog dialog2;
    String methodType = "WEIGHT FOR AGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevailance_reports);
        db = FirebaseFirestore.getInstance();
        textType = findViewById(R.id.textType);
        textDate = findViewById(R.id.textDate);
        up_View = findViewById(R.id.up_View);
        textThisMonth = findViewById(R.id.textThisMonth);
        textIncreased = findViewById(R.id.textIncreased);
        pdfMaker = findViewById(R.id.floatingActionButton);
        textTypeLayout = findViewById(R.id.textTypeLayout);
        textTypeTitle = findViewById(R.id.textTypeTitle);
        textDataThisMonth = findViewById(R.id.textDataThisMonth);
        textDataIncreased = findViewById(R.id.textDataIncreased);

        dialog2 = new Dialog(Prevailance_Reports.this);
        dialog2.setContentView(R.layout.dialog_loader);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);

        String[] mType = {"Underweight", "Overweight", "Stunting", "Wasting"};
        FormUtils.setAdapter(mType, textType, this);
        dateAdapter();
        preSelected();
        barangayRecycler = findViewById(R.id.barangayRecycler);
        textType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text_Date = textDate.getText().toString();
                String selectedType = (String) parent.getItemAtPosition(position);
                contentMaker(text_Date, selectedType);
            }
        });

        textDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text_Status = textType.getText().toString();
                String text_Date =  textDate.getText().toString();
                contentMaker(text_Date, text_Status);
            }
        });
    }

    public void contentMaker(String text_Date, String text_Status){
        String query_Type = "";
        if(text_Status.equals("Stunting")){
            themeSetter("Stunting");
            query_Type = setQueryType("S");
            statusPointer = 1;
            methodType = "HEIGHT FOR AGE";
        } else if (text_Status.equals("Overweight")) {
            themeSetter("Overweight");
            query_Type = setQueryType("O");
            statusPointer = 2;
            methodType = "WEIGHT FOR LENGTH/HEIGHT";
        } else if (text_Status.equals("Underweight")) {
            themeSetter("Underweight");
            query_Type = setQueryType("U");
            statusPointer = 3;
            methodType = "WEIGHT FOR LENGTH/HEIGHT";
        } else if (text_Status.equals("Wasting")){
            themeSetter("Wasting");
            query_Type = setQueryType("W");
            statusPointer = 4;
            methodType = "WEIGHT FOR AGE";
        }
        String[] status_array = getStatusArray();
        Populate(status_array, query_Type, text_Date);

    }
    public String[] getStatusArray(){
        String[] status_arrays = {"",""};
        if(statusPointer==1){
            status_arrays[0] = "Stunted";
            status_arrays[1] = "Severe Stunted";
        } else if (statusPointer==2) {
            status_arrays[0] = "Overweight";
            status_arrays[1] = "Obese";
        } else if(statusPointer==3){
            status_arrays[0] = "Underweight";
            status_arrays[1] = "Severe Underweight";
        } else if (statusPointer==4){
            status_arrays[0] = "Wasted";
            status_arrays[1] = "Severe Wasted";
        }
        return status_arrays;
    }
    public void themeSetter(String choice){
        int colorString;
        if(choice.equals("Stunting")){
            colorString = Color.parseColor("#77DD77");
            UIChanger(colorString, choice);
        } else if (choice.equals("Underweight")) {
            colorString = Color.parseColor("#51ADE5");
            UIChanger(colorString, choice);
        } else if (choice.equals("Overweight")) {
            colorString = Color.parseColor("#FF6961");
            UIChanger(colorString, choice);
        } else if (choice.equals("Wasting")) {
            colorString = Color.parseColor("#FFB347");
            UIChanger(colorString, choice);
        }
    }
    public void statusBarChanger(int color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor((color));
        }
    }
    public void UIChanger(int colorString, String type){
        ColorStateList colorStateList = ColorStateList.valueOf(colorString);
        up_View.setBackgroundTintList(colorStateList);
        textThisMonth.setTextColor(colorStateList);
        textIncreased.setTextColor(colorStateList);
        pdfMaker.setBackgroundTintList(colorStateList);
        statusBarChanger(colorString);
        textChanger(type);
        if(type.equals("Stunting")){
            textChanger("Stunted and Severe Stunted");
        } else if (type.equals("Underweight")) {
            textChanger("Underweight and Severe Underweight");
        } else if (type.equals("Overweight")) {
            textChanger("Overweight and Severe Overweight");
        } else if (type.equals("Wasting")) {
            textChanger("Wasted and Severe Wasted");
        }
    }
    public void textChanger(String typeTitle){
        textTypeTitle.setText(typeTitle);
    }
    public void dateAdapter(){
        LocalDate currentDate = LocalDate.now();
        YearMonth targetMonth = YearMonth.of(2023,6);
        ArrayList<String> filteredMonths  = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (YearMonth month = targetMonth;
             !month.isAfter(YearMonth.from(currentDate));
             month = month.plusMonths(1)) {
            filteredMonths.add(month.atDay(1).format(formatter));
        }
        Collections.reverse(filteredMonths);
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,filteredMonths);
        textDate.setAdapter(adapter);
    }
    public void preSelected(){
        String text_Date = getDateNowFormatted();
        String[] status_array = {"Underweight", "Severe Underweight"};
        textDate.setText(getDateNowFormatted(), false);
        textType.setText("Underweight",false);
        int preColor = Color.parseColor("#51ADE5");
        UIChanger(preColor,"Underweight");
        statusBarChanger(preColor);
        Populate(status_array, "U and SU", text_Date);
    }
    public String getDateNowFormatted(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

    public void Populate(String[] status_array, String query_Type, String text_Date){
        dialog2.show();
        db.collection("children")
                .whereEqualTo("monthAdded", text_Date)
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
                    ArrayList<BarangayModel> barangayModels = dataFiltering(arrayList, status_array ,query_Type);
                    barangayQuery(barangayModels, arrayList.size(), status_array);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Prevailance_Reports.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<BarangayModel> dataFiltering(ArrayList<Child> arrayList, String[] status_array ,String queryType){
        ArrayList<BarangayModel> barangayModels = new ArrayList<>();
        String[] barangayList = getResources().getStringArray(R.array.barangay);
        for(String barangay: barangayList){
            BarangayModel barangayModel = new BarangayModel();
            int count = 0;
            int count_normal = 0;
            int count_barangay = 0;
            for(Child arrayList1: arrayList){
                boolean isBarangay = arrayList1.getBarangay().equals(barangay);
                boolean isStatus1 = arrayList1.getStatus().contains(status_array[0]);
                boolean isStatus2 = arrayList1.getStatus().contains(status_array[1]);
                boolean isNormalWFA = arrayList1.getStatus().get(0).equals("Normal");
                boolean isNormalHFA = arrayList1.getStatus().get(1).equals("Normal");
                boolean isNormalWFH = arrayList1.getStatus().get(2).equals("Normal");

                if(isBarangay && (isStatus1|| isStatus2)){
                    count++;
                }
                if(isBarangay){
                    count_barangay++;
                    if(queryType.equals("U and SU")){
                        if(isNormalWFA){
                            count_normal++;
                        }
                    } else if (queryType.equals("OW and OB")) {
                        if(isNormalWFH){
                            count_normal++;
                        }
                    } else if (queryType.equals(("S and SS"))) {
                        if(isNormalHFA){
                            count_normal++;
                        }
                    } else if (queryType.equals("W and SW")) {
                        if(isNormalWFH){
                            count_normal++;
                        }
                    }
                }
            }
            barangayModel.setNormal(count_normal);
            barangayModel.setTotalAssess(count_barangay);
            barangayModel.setBarangay(barangay);
            barangayModel.setTotalCase(count);
            barangayModel.setQueryType(queryType);
            barangayModels.add(barangayModel);
        }
        Collections.sort(barangayModels, new Comparator<BarangayModel>() {
            @Override
            public int compare(BarangayModel b1, BarangayModel b2) {
                return Integer.compare(b2.getTotalCase(), b1.getTotalCase());
            }
        });
        int count_rank = 1;
        for(BarangayModel barangayNow: barangayModels){
            barangayNow.setRank(count_rank);
            count_rank++;
        }
        return barangayModels;
    }

    public String setQueryType(String type){
        String queryType = "";
        if(type.equals("S")){
            queryType = "S and SS";
        }else if(type.equals("O")){
            queryType = "OW and OB";
        }
        else if(type.equals("U")){
            queryType = "U and SU";
        }
        else if(type.equals("W")){
            queryType = "W and SW";
        }
        return  queryType;
    }

    public void barangayQuery(ArrayList<BarangayModel> barangayModels, int childTotal, String[] status_array){
        db.collection("barangay")
                .whereNotEqualTo("identifier", "All Beneficiaries")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){


                            ArrayList<BarangayModel> arrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                BarangayModel barangayModel = doc.toObject(BarangayModel.class);
                                barangayModel.setBarangay(doc.getId());
                                arrayList.add(barangayModel);
                            }
                            int count = 0;
                            for(BarangayModel myFinal: barangayModels){
                                for(BarangayModel list: arrayList){
                                    if(myFinal.getBarangay().equals(list.getBarangay())){
                                        myFinal.setEstimatedChildren(list.getEstimatedChildren());
                                    }
                                }
                                count = count + myFinal.getTotalCase();
                            }

                            int totalChildren = count;
                            float result = (float) totalChildren / childTotal ;
                            float prevPer = Math.round(result * 100.0f);
                            textDataThisMonth.setText("" + totalChildren);
                            textDataIncreased.setText("" + prevPer + "%");
                            userAdapter = new BarangayAdapter(Prevailance_Reports.this, barangayModels);
                            barangayRecycler.setAdapter(userAdapter);
                            dialog2.dismiss();

                            pdfMaker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createAndUploadPdf(barangayModels, status_array);
                                }
                            });
                            userAdapter.setOnItemClickListener(new BarangayAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(BarangayModel barangayModel) {

                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Prevailance_Reports.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void createAndUploadPdf(ArrayList<BarangayModel> arrayList, String[] status_array){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Rectangle customsize = new Rectangle(
                8.5f*72,
                13.0f*72
        );

        String type = status_array[0] + " + " + status_array[1];
        try {
            // Create a PDF document
            Document document = new Document(customsize);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            addText("Region IVA: CALABARZON", document);
            addText("MUNICIPALITY OF MAGDALENA", document, true);
            addText("PROVINCE: LAGUNA", document);
            addText("OPERATION TIMBANG PLUS 2023", document);
            addText(type.toUpperCase(), document, true);
            addText("PREVALANCE AND NUMBER OF AFFECTED CHILDREN UNDER FIVE, BY BARANGAY", document);
            addText("\n", document);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            float[] columnWidths = {10f, 24f, 15f, 17f, 17f, 17f};
            table.setWidths(columnWidths);

            BaseColor baseColor = getBaseColor("#a6aa91");
            addCell(table, "Rank", baseColor, BaseColor.BLACK,0,2);
            addCell(table, "Barangay", baseColor, BaseColor.BLACK,0,2);
            addCell(table, "0-59 Months OPT Plus Coverage (%)", baseColor, BaseColor.BLACK, 0, 2);
            addCell(table, "" + methodType, getBaseColor("#c4c6bb"), BaseColor.BLACK, 3,1);
            addCell(table, "Normal" + " (%)", baseColor, BaseColor.BLACK);
            addCell(table, status_array[0] + " + " + status_array[1] + " (%)", baseColor, BaseColor.BLACK);
            addCell(table, "Number of " + status_array[0] + " + " + status_array[1] , baseColor, BaseColor.BLACK);
            baseColor = getBaseColor("#c4c6bb");
            for(BarangayModel barangayModel: arrayList){
                int position = arrayList.indexOf(barangayModel) + 1;
                addCell(table,"" + position );
                leftAlignedCell(table, "" + barangayModel.getBarangay());
                addCell(table,"" + percentage(barangayModel.getTotalAssess(),barangayModel.getEstimatedChildren()) + "%" );
                addCell(table,"" + percentage(barangayModel.getNormal(), barangayModel.getTotalAssess())+ "%" );
                specificCell(table, "" + percentage(barangayModel.getTotalCase(),barangayModel.getTotalAssess()) + "%", baseColor);
                addCell(table,"" + barangayModel.getTotalCase());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child("pdfs/" + "PdfTestListChild");

        final Dialog dialog = new Dialog(Prevailance_Reports.this);
        dialog.setContentView(R.layout.pdf_viewer);
        PDFView pdfView = dialog.findViewById(R.id.pdfView);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        Button cancelBtn = dialog.findViewById(R.id.btnCancel);
        Button exportBtn = dialog.findViewById(R.id.btnSavePdf);
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
        displayPdfFromBytes(pdfBytes, pdfView, progressBar);

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePDFToStorage(pdfBytes, status_array);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static void addCell(PdfPTable table, String text, BaseColor backgroundColor, BaseColor textColor) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, textColor)));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);
    }
    private static void addCell(PdfPTable table, String text, BaseColor backgroundColor, BaseColor textColor, int colspan, int rowspan) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, textColor)));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);
    }
    private static void addText(String text, Document document)throws DocumentException {
        Paragraph paragraph = new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12));
        paragraph.setAlignment(paragraph.ALIGN_CENTER);
        document.add(paragraph);
    }
    private static void addText(String text, Document document, boolean isBold )throws DocumentException {
        Paragraph paragraph = new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        paragraph.setAlignment(paragraph.ALIGN_CENTER);
        document.add(paragraph);
    }

    public float percentage(int number1, int number2){
        float result = (float) number1 / number2;
        float percentage = Math.round(result * 100.0f);
        return percentage;
    }

    public static void addCell(PdfPTable table, String text){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }
    public static void specificCell(PdfPTable table, String text, BaseColor cellColor){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(cellColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }
    public static void leftAlignedCell(PdfPTable table, String text){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }

    public BaseColor getBaseColor(String hexColor){
        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(5, 7), 16);
        BaseColor baseColor = new BaseColor(red, green, blue);
        return baseColor;
    }

    private void displayPdfFromBytes(byte[] pdfBytes, PDFView pdfView, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);

                    pdfView.fromBytes(pdfBytes)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .load();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    e.printStackTrace();
                    Log.e("PDFViewer", "Error loading PDF: " + e.getMessage());
                });
            }
        });
    }
    private void savePDFToStorage(byte[] byteArray, String[] status_array) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "PrevailanceReports" + status_array[0] + "_" + status_array[1] +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS")) + ".pdf");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "NutriAssist");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + File.separator + "NutriAssist");

        ContentResolver resolver = getContentResolver();
        Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        Uri pdfUri = resolver.insert(externalUri, values);
        try {
            if (pdfUri != null) {
                resolver.openOutputStream(pdfUri).write(byteArray);
                Toast.makeText(this, "PDF Saved Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error Creating PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Saving PDF", Toast.LENGTH_SHORT).show();
        }
    }

}