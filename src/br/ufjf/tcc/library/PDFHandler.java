package br.ufjf.tcc.library;

import java.io.FileOutputStream;

import br.ufjf.tcc.model.TCC;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFHandler {
	private static String FILE = "/Users/Matheus/Desktop/FirstPdf.pdf";
	private static Font defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.NORMAL);
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,
			Font.NORMAL);
	private static Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN,
			12, Font.UNDERLINE);
	private static Font infoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);

	public void generateAta(TCC tcc) throws Exception {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();

			Paragraph paragraph = new Paragraph("CURSO DE "
					+ tcc.getAluno().getCurso().getNomeCurso().toUpperCase(),
					titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(25);
			document.add(paragraph);

			paragraph = new Paragraph(
					"Composição da Banca do Trabalho de Conclusão do curso",
					subTitleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			Phrase phrase = new Phrase("");
			phrase.add(new Phrase("Acadêmico (a): ", infoFont));
			phrase.add(new Phrase(tcc.getAluno().getNomeUsuario(), defaultFont));
			phrase.add(new Phrase("    Matrícula: ", infoFont));
			phrase.add(new Phrase(tcc.getAluno().getMatricula(), defaultFont));

			paragraph = new Paragraph(phrase);
			paragraph.setSpacingAfter(15);
			document.add(paragraph);

			paragraph = new Paragraph("Título do Trabalho:", infoFont);
			paragraph.setSpacingAfter(6);
			document.add(paragraph);

			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100);
			table.setSpacingAfter(15);
			table.addCell(new Phrase(tcc.getNomeTCC() + "\n", defaultFont));
			document.add(table);

			paragraph = new Paragraph("Resumo do Trabalho:", infoFont);
			paragraph.setSpacingAfter(6);
			document.add(paragraph);

			table = new PdfPTable(1);
			table.setWidthPercentage(100);
			table.setSpacingAfter(15);
			table.addCell(new Phrase(tcc.getResumoTCC() + "\n", defaultFont));
			document.add(table);

			paragraph = new Paragraph("Dados da Defesa:", infoFont);
			paragraph.setSpacingAfter(6);
			document.add(paragraph);

			document.close();
		} catch (Exception e) {
			throw e;
		}
	}

}
