package br.ufjf.tcc.teste;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Teste {
	private static String FILE = "/Users/Matheus/Desktop/FirstPdf.pdf";
	private static Font defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL);
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,
			Font.NORMAL);
	private static Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN,
			12, Font.UNDERLINE);
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();

			Paragraph paragraph = new Paragraph(
					"CURSO DE CIÊNCIA DA COMPUTAÇÃO", titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph);

			addEmptyLine(document, 2);

			paragraph = new Paragraph(
					"Composição da Banca do Trabalho de Conclusão do curso",
					subTitleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph);

			// Left
			paragraph = new Paragraph("This is left aligned text");
			paragraph.setAlignment(Element.ALIGN_LEFT);
			document.add(paragraph);
			// Left with indentation
			paragraph = new Paragraph(
					"Aliquam erat volutpat. Ut hendrerit, eros adipiscing cursus sodales, ipsum diam posuere tortor, eget venenatis turpis magna non ante. Nulla euismod et tellus posuere suscipit. Sed quis lectus ut ipsum scelerisque ultricies. Etiam vitae congue velit. Integer sagittis laoreet dapibus. Donec condimentum, tortor quis rutrum consequat, arcu mauris commodo ipsum, a pretium felis libero id neque. Vivamus sed tortor in ipsum sodales venenatis nec eget ligula. Vivamus nec tellus a mi varius aliquet et non mi. Phasellus luctus purus eu dignissim mollis. Aliquam vitae mi nisi. Sed vitae diam volutpat lorem porttitor euismod. Cras tincidunt enim sed adipiscing mollis. Phasellus consectetur nibh vitae nibh hendrerit ultricies vitae eget leo. Integer pharetra libero et dolor egestas, vel venenatis neque porta.");
			document.add(paragraph);

			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100);
			table.addCell("Aliquam erat volutpat. Ut hendrerit, eros adipiscing cursus sodales, ipsum diam posuere tortor, eget venenatis turpis magna non ante. Nulla euismod et tellus posuere suscipit. Sed quis lectus ut ipsum scelerisque ultricies. Etiam vitae congue velit. Integer sagittis laoreet dapibus. Donec condimentum, tortor quis rutrum consequat, arcu mauris commodo ipsum, a pretium felis libero id neque. Vivamus sed tortor in ipsum sodales venenatis nec eget ligula. Vivamus nec tellus a mi varius aliquet et non mi. Phasellus luctus purus eu dignissim mollis. Aliquam vitae mi nisi. Sed vitae diam volutpat lorem porttitor euismod. Cras tincidunt enim sed adipiscing mollis. Phasellus consectetur nibh vitae nibh hendrerit ultricies vitae eget leo. Integer pharetra libero et dolor egestas, vel venenatis neque porta.");
			document.add(table);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addEmptyLine(Document document, int number)
			throws DocumentException {
		for (int i = 0; i < number; i++) {
			document.add(new Paragraph(" "));
		}
	}

}
