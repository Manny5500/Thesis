package com.example.myapplication;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ML_PdfUtils{
    Rectangle customsize;
    private ArrayList<Child> arrayList;
    public ML_PdfUtils(Rectangle customsize, ArrayList<Child> arrayList){
        this.customsize = customsize;
        this.arrayList = arrayList;

    }
    public byte[] PdfSetter(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfContentMaker(byteArrayOutputStream);
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return pdfBytes;
    }


    public void pdfContentMaker(ByteArrayOutputStream byteArrayOutputStream){
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
            PdfContentUtils.addCell(table, "Child Seq", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Address or Location of Residence", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Name of mother or caregiver", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Full Name of Child", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Indigenous Preschool Child", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Sex", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Date of Birth", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Actual Date of Weighing", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Weight", baseColor, BaseColor.WHITE);
            PdfContentUtils.addCell(table, "Height", baseColor, BaseColor.WHITE);

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
                SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                table.addCell(new Phrase(""+sdf.format(child.getDateAdded())));
                table.addCell(new Phrase("" + child.getHeight() + " cm"));
                table.addCell(new Phrase("" + child.getWeight() + " kg"));
            }

            // Add the table to the document
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
