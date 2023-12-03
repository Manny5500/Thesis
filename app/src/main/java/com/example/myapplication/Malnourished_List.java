package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Table;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Phrase;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Malnourished_List extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;

    private ChildAdapter userAdapter;

    Dialog dialog2;
    String pdfUrl = "https://firebasestorage.googleapis.com/v0/b/my-application-dfd69.appspot.com/o/pdfs%2FPdfTestListChild?alt=media&token=f9ae544a-083d-42b7-9a93-53643c104f15";
    FloatingActionButton pdfMaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malnourished_list);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler);
        Populate();
        SearchView searchView = findViewById(R.id.searchView);
        dialog2 = new Dialog(Malnourished_List.this);
        dialog2.setContentView(R.layout.dialog_loader);
        pdfMaker = findViewById(R.id.pdfMaker);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAdapter.getFilter().filter(s);
                return true;
            }
        });
    }
    public void Populate(){
        db.collection("children").whereArrayContainsAny("statusdb",
                        Arrays.asList("Severe Wasted", "Severe Stunted", "Severe Underweight",
                                "Underweight", "Wasted", "Stunted", "Obese", "Overweight",
                                "Above Normal")).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<Child> arrayList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc: task.getResult()){
                                Child child = doc.toObject(Child.class);
                                child.setId(doc.getId());
                                arrayList.add(child);
                                db.collection("users").whereEqualTo("user","parent").
                                        whereEqualTo("email", child.getGmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for(QueryDocumentSnapshot doc1:task.getResult()){
                                                    User user = doc1.toObject(User.class);
                                                    child.setPhoneNumber(user.getContact());
                                                }
                                            }
                                        });
                            }

                            userAdapter = new ChildAdapter(Malnourished_List.this, arrayList);
                            recyclerView.setAdapter(userAdapter);

                            userAdapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(Child child) {
                                    final Dialog dialog = new Dialog(Malnourished_List.this);
                                    dialog.setContentView(R.layout.pdf_viewer);
                                    PDFView pdfView = dialog.findViewById(R.id.pdfView);
                                    ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                                    if(pdfUrl!=null){
                                        dialog.show();
                                        displayFromUrl(pdfUrl, pdfView, progressBar);
                                    }else{
                                        Toast.makeText(Malnourished_List.this, "No referral form", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            pdfMaker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createAndUploadPdf(arrayList);

                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Malnourished_List.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void createAndUploadPdf(ArrayList<Child> arrayList){
        dialog2.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Rectangle customsize = new Rectangle(
                13.0f*72,
                8.5f*72
        );
        try {
            // Create a PDF document
            Document document = new Document(customsize);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Create a table with 3 columns
            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);
            float[] columnWidths = {10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f};
            table.setWidths(columnWidths);

            // Add table headers
            String hexColor = "#2596be";
            int red = Integer.valueOf(hexColor.substring(1, 3), 16);
            int green = Integer.valueOf(hexColor.substring(3, 5), 16);
            int blue = Integer.valueOf(hexColor.substring(5, 7), 16);
            BaseColor baseColor = new BaseColor(red, green, blue);
            addCell(table, "Child Seq", baseColor, BaseColor.WHITE);
            addCell(table, "Address or Location of Residence", baseColor, BaseColor.WHITE);
            addCell(table, "Name of mother or caregiver", baseColor, BaseColor.WHITE);
            addCell(table, "Full Name of Child", baseColor, BaseColor.WHITE);
            addCell(table, "Indigenous Preschool Child", baseColor, BaseColor.WHITE);
            addCell(table, "Sex", baseColor, BaseColor.WHITE);
            addCell(table, "Date of Birth", baseColor, BaseColor.WHITE);
            addCell(table, "Actual Date of Weighing", baseColor, BaseColor.WHITE);
            addCell(table, "Weight", baseColor, BaseColor.WHITE);
            addCell(table, "Height", baseColor, BaseColor.WHITE);

            // Add sample data
            for(Child child: arrayList){
                int position = arrayList.indexOf(child) + 1;
                table.addCell(new Phrase("" + position));
                table.addCell(new Phrase("" + child.getBarangay()));
                table.addCell(new Phrase(""+ child.getParentFirstName()
                        + " " + child.getParentMiddleName() + " " + child.getParentLastName()));
                table.addCell(new Phrase(""+ child.getChildFirstName()
                        + " " + child.getChildMiddleName() + " " + child.getChildLastName()));
                table.addCell(new Phrase(""+child.getBelongtoIP()));
                table.addCell(new Phrase("" + child.getSex()));
                table.addCell(new Phrase(""+child.getBirthDate()));
                table.addCell(new Phrase("placeholder"));
                table.addCell(new Phrase("" + child.getHeight() + " cm"));
                table.addCell(new Phrase("" + child.getWeight() + " kg"));
            }

            // Add the table to the document
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child("pdfs/" + "PdfTestListChild");

        UploadTask uploadTask = pdfRef.putBytes(pdfBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Malnourished_List.this, "Success", Toast.LENGTH_SHORT).show();
                // Get the download URL
                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Store the download URL in Firestore
                        pdfUrl=uri.toString();
                        storeDownloadUrlInFirestore(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Malnourished_List.this, "Error" + e, Toast.LENGTH_SHORT).show();
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
    private static void addCell(PdfPTable table, String text, BaseColor backgroundColor, BaseColor textColor) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, textColor)));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.BLACK);
        table.addCell(cell);
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

    private void storeDownloadUrlInFirestore(String downloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference pdfCollection = db.collection("pdfs");

        // Create a Map with the PDF data
        Map<String, Object> pdfData = new HashMap<>();
        pdfData.put("downloadUrl", downloadUrl);
        pdfData.put("fname", "placeholder");

        // Add the PDF data to Firestore
        pdfCollection.document("placeholder").update(pdfData)
                .addOnSuccessListener(aVoid -> {
                    dialog2.dismiss();
                    Toast.makeText(this, "Successfully generated the document", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                });
    }
}