package br.ufjf.tcc.teste;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

class HeaderFooter extends PdfPageEventHelper {
	/** Alternating phrase for the header. */
	Phrase[] header = new Phrase[2];
	/** Current page number (will be reset for every chapter). */
	int pagenumber;

	/**
	 * Initialize one of the headers.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
	}

	/**
	 * Initialize one of the headers, based on the chapter title; reset the page
	 * number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document, float, com.itextpdf.text.Paragraph)
	 */
	public void onChapter(PdfWriter writer, Document document,
			float paragraphPosition, Paragraph title) {

	}

	/**
	 * Increase the page number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
	}

	/**
	 * Adds the header and the footer.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		switch (writer.getPageNumber() % 2) {
		case 0:
			ColumnText.showTextAligned(writer.getDirectContent(),
					Element.ALIGN_RIGHT, header[0], rect.getRight(),
					rect.getTop(), 0);
			break;
		case 1:
			ColumnText.showTextAligned(writer.getDirectContent(),
					Element.ALIGN_LEFT, header[1], rect.getLeft(),
					rect.getTop(), 0);
			break;
		}
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER,
				new Phrase("Campus Universitário, Bairro Martelos, 36.036-330, Juiz de Fora, MG, Brasil tel.: +55 +32 2102-3311 e +55 +32 2102-3316; fax +55 +32 2102 -3319"),
				(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,
				0);
	}
}