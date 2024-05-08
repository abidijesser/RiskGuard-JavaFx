package controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import models.Employee;
import com.itextpdf.text.BaseColor;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


public class PDF {

    private static String filePath = "La Liste des employee.pdf";

    public static void generateEmployeeListPDF(List<Employee> employeeList) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLUE);
            Font tableFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph title = new Paragraph("La List des Employee", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Add some space below the title
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Email");
            table.addCell("Salary");

            for (Employee employee : employeeList) {
                table.addCell(String.valueOf(employee.getId()));
                table.addCell(employee.getNom());
                table.addCell(employee.getEmail());
                table.addCell(String.valueOf(employee.getSalaire()));
            }

            document.add(table);
            document.close();

            System.out.println("Employee list PDF generated successfully.");
        } catch (FileNotFoundException | DocumentException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}

