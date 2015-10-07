package br.ufjf.tcc.pdfHandle;

import java.io.FileOutputStream;

import br.ufjf.tcc.library.ConfHandler;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PreenchimentoPDF {

	public static void lastPag(String nomeAluno, String nomeAvaliador,
			String dia, String mes, String ano, int n, int idAluno)
			throws Exception {
		String template = "/br/ufjf/tcc/pdfHandle/LastPag.pdf";
		String Arquivo_Saida = ConfHandler.getConf("FILE.PATH") + "last"
				+ idAluno + "-" + n + ".pdf";

		FileOutputStream saida = new FileOutputStream(Arquivo_Saida);

		PdfReader leitor = new PdfReader(
				PreenchimentoPDF.class.getResourceAsStream(template));

		PdfStamper stamper = new PdfStamper(leitor, saida);

		PdfContentByte over;

		BaseFont bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		over = stamper.getOverContent(1);
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		over.setTextMatrix(100, 208);
		
		over.showText(nomeAluno);
		over.setTextMatrix(100, 155);
		over.showText(nomeAvaliador);

		// DATA QUARTA PAGINA
		over.setTextMatrix(152, 72);
		over.showText(dia);
		over.setTextMatrix(195, 72);
		over.showText(Ata.getMesPeloNumero(mes));
		over.setTextMatrix(290, 72);
		over.showText(ano);

		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
		System.out.println("gerou o Last" + n + "pdf");

	}

}
