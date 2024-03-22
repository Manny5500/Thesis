package com.example.myapplication;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;


public class SR_PdfUtils {
    private BarangayModel bModel;
    private int estimatedChildren;
    private int population;

    String barangay;
    private Rectangle customsize;

    SRDPPdf srdpPdf;

    public SR_PdfUtils(Rectangle customsize, String barangay,
                       int estimatedChildren, int population,
                       SRDPPdf srdpPdf){
        this.customsize = customsize;
        this.barangay = barangay;
        this.estimatedChildren = estimatedChildren;
        this.population = population;
        this.srdpPdf = srdpPdf;
    }

    public byte[] PdfSetter(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfContentMaker(byteArrayOutputStream);
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return pdfBytes;
    }

    public void pdfContentMaker(ByteArrayOutputStream byteArrayOutputStream){
        try {
            String[] totalHeader = {"PSGC: 0403416019","Total WFA:", "56", "Total HFA:", "56", "Total WFL/H:", "56", "Birth to 5 years",
            "F1K", "#IP Children"};
            String[] header1 = {"ACRONYMS & ABBREVATIONS", " 0-5 Months", "6- 11 Months", "12-23 Months",
            "24-35 Months", "36-47 Months", "48-59 Months", "0-59 Months", "0-23 Months", "Boys", "Girls", "Total"};
            String[] header2 = {"Boys", "Girls", "Total"};
            String[] header2_1 = {"Total", "Prev"};
            // Create a PDF document
            Document document = new Document(customsize);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            //header
            PdfContentUtils.addText("Total Popn Barangay" + population, document);

            PdfPTable table = new PdfPTable(26);
            table.setWidthPercentage(100);
            float[] columnWidths = {12.11f,
                    3.17f, 3.17f, 3.17f,
                    3.17f, 3.17f, 3.17f,
                    3.17f, 3.17f, 3.17f,
                    3.17f, 3.17f, 3.17f,
                    3.17f, 3.17f, 3.17f,
                    3.17f, 3.17f, 3.17f,
                    3.76f, 6.80f,
                    3.76f, 6.80f,
                    3.17f, 3.17f, 3.17f};
            table.setWidths(columnWidths);

            BaseColor baseColor = SRPDF_CU.getBaseColor("#a6aa91");
            BaseColor textColor = SRPDF_CU.getBaseColor("#ffffff");
            BaseColor textColor2 = SRPDF_CU.getBaseColor("#000000");

            //table-header
            for(int i=0; i<10;i++){
                if(i==0){
                    SRPDF_CU.addCell(table, totalHeader[i], baseColor, textColor,10);
                }
                if(i==1 || i==3 || i==5 || i==7 || i==8){
                    SRPDF_CU.addCell(table, totalHeader[i], baseColor, textColor, 2);
                }
                if(i==2 || i==4 || i==6){
                    SRPDF_CU.addCell(table, totalHeader[i]);
                }
                if(i==9){
                    SRPDF_CU.addCell(table, totalHeader[i], baseColor, textColor, 3);
                }
            }


            for(int i=0; i<12; i++){
                if(i==0){
                    SRPDF_CU.addCellR(table,header1[i], baseColor, textColor,  2);
                }
                if(i>0 && i<7){
                    SRPDF_CU.addCell(table,header1[i], baseColor, textColor, 3);
                }
                if(i>6 && i<9){
                    SRPDF_CU.addCell(table,header1[i], baseColor, textColor, 2);
                }
                if(i>8){
                    SRPDF_CU.addCellwRotates(table,header1[i], baseColor, textColor,  2);
                }
            }

            for(int i=0; i<6; i++){
                for(int j=0; j<3; j++){
                    SRPDF_CU.addCellGender(table, header2[j], textColor2);
                }
            }

            for(int i=0; i<2; i++){
                for(int j=0; j<2; j++){
                    SRPDF_CU.addCellPrev(table,header2_1[j], textColor2);
                }
            }

            //table-data
            for(int i=0; i<13; i++){
                for(int j=0; j<26; j++){
                    SRPDF_CU.addCell(table, srdpPdf.masterData[i][j]);
                }
            }
            for(int i=0; i<20; i++){
                if(i<19){
                    SRPDF_CU.addCell(table, srdpPdf.tfAges[i]);
                }else{
                    SRPDF_CU.addCell(table, "", baseColor, textColor, 7, 0);
                }
            }

            for(int i=0; i<3; i++){
                if(i==0){
                    SRPDF_CU.addCellTitle(table, "adfsfadssdf", baseColor, textColor, 8);
                }
                if(i==1){
                    SRPDF_CU.addCellTitle(table, "afdadsds", baseColor, textColor, 11);
                }
                if(i==2){
                    SRPDF_CU.addCellTitle(table, "adfsasdf", baseColor, textColor, 7);
                }
            }




            for(int i=0; i<5; i++){
                for(int j=0; j<6; j++){
                    if(j==0){
                        SRPDF_CU.addCell(table, srdpPdf.sumData[i][j],baseColor,textColor,7);
                    } else if (j==2) {
                        SRPDF_CU.addCell(table, srdpPdf.sumData[i][j],baseColor,textColor,10);
                    } else if (j==4) {
                        SRPDF_CU.addCell(table, srdpPdf.sumData[i][j],baseColor,textColor,6);
                    }else{
                        SRPDF_CU.addCellDataSum(table, srdpPdf.sumData[i][j],baseColor,textColor,1);
                    }
                }

            }

            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
