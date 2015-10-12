package br.ufjf.tcc.teste;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;



public class TestePDFFORM {

	public static void main(String[] args) throws Exception {
	    PdfReader reader;
	    PdfStamper stamper;
	    AcroFields form;
	    reader = new PdfReader("/home/fanu/Desktop/fanu.pdf");
	    stamper = new PdfStamper(reader, new FileOutputStream(
	        "/home/fanu/Desktop/fanu2.pdf"));
	    form = stamper.getAcroFields();
	    form.setField("nome", "Fanuca Nuelson");
	  
	    System.out.println("Its completed perfectly.");
	    stamper.setFormFlattening(true);
	    stamper.close();

	}

}
