package br.ufjf.tcc.teste;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

public class Teste {
	private static String FILE = "/Users/Matheus/Desktop/FirstPdf.pdf";
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,
			Font.BOLD);
	private static Font defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL);
	private static Font infoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			Document document = new Document(PageSize.A4, 80, 80, 20, 20);
			PdfWriter.getInstance(document, new FileOutputStream(FILE));

			document.open();

			Paragraph paragraph;
			Phrase phrase;

			Image image = Image.getInstance("/Users/Matheus/Desktop/ufjf.png");
			image.scalePercent(7);
			image.setAlignment(Element.ALIGN_RIGHT);
			document.add(image);

			paragraph = new Paragraph(
					"AVALIAÇÃO FINAL DO TRABALHO DE BACHARELADO", titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(35);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Acadêmico (a): ", infoFont));
			phrase.add(new Phrase("Matheus de Oliveira do Carmo Marques",
					defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Número de Matrícula: ", infoFont));
			phrase.add(new Phrase("201235027", defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Título da Monografia: ", infoFont));
			phrase.add(new Phrase("Inferência de Contexto", defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph("APRESENTAÇÃO DO TRABALHO:", infoFont);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Data: ", infoFont));
			phrase.add(new Phrase("03/07/2014", defaultFont));
			phrase.add(new Phrase("  Horário: ", infoFont));
			phrase.add(new Phrase("17:00", defaultFont));
			phrase.add(new Phrase("  Local: ", infoFont));
			phrase.add(new Phrase("sala 304", defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph("Parecer da Banca Examinadora: ",
					infoFont);
			paragraph.setSpacingAfter(6);
			document.add(paragraph);

			paragraph = new Paragraph(
					"________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________",
					defaultFont);
			paragraph.setSpacingAfter(30);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Conceito Final:  ", infoFont));
			phrase.add(new Phrase(
					"______ (__________________________________________)",
					defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Banca Examinadora:  ", infoFont));
			phrase.add(new Phrase(
					"_____________________________________________",
					defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph(
					"_____________________________________________",
					defaultFont);
			paragraph.setIndentationLeft(115);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph(
					"_____________________________________________",
					defaultFont);
			paragraph.setIndentationLeft(115);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			phrase = new Phrase("");
			phrase.add(new Phrase("Em: ", infoFont));
			phrase.add(new Phrase("___/___/_____", defaultFont));
			paragraph = new Paragraph(phrase);
			paragraph.setSpacingBefore(20);
			document.add(paragraph);

			document.close();
		} catch (Exception e) {
			throw e;
		}
	}

}
