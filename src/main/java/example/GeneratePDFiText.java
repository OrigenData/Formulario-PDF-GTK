package example;


import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePDFiText {
	
	public static final String RESULT = "hello.pdf";
	public String name =null;
	public String email =null;
	public String address =null;
	public String phone =null;
	

    public void createPDF(String name, String email, String address, String phone, String date, String sex)
	throws DocumentException, IOException {

        Document document = new Document();
        
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        
        document.open();
        
        document.add(new Paragraph(name));
        document.add(new Paragraph(email));
        document.add(new Paragraph(address));
        document.add(new Paragraph(phone));
        document.add(new Paragraph(date));
        document.add(new Paragraph(sex));
        
        document.close();
	

    }
}