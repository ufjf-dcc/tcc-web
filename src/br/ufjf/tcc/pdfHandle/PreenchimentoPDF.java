package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileOutputStream;

import br.ufjf.tcc.library.ConfHandler;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PreenchimentoPDF {

	public static void lastPag(String nomeAluno, String nomeAvaliador,
			String dia, String mes, String ano, int n, int idAluno)
			throws Exception {
		String template = "/br/ufjf/tcc/pdfHandle/LastPagNew.pdf";
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
		AcroFields form = stamper.getAcroFields();
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		form.setFieldProperty("nomeAluno", "textcolor", Color.BLACK ,null);
		form.setField("nomeAluno", nomeAluno);
//		over.setTextMatrix(100, 208);		
//		over.showText(nomeAluno);
		
		form.setFieldProperty("nomeAvaliador", "textcolor", Color.BLACK ,null);
		form.setField("nomeAvaliador", nomeAvaliador);
//		over.setTextMatrix(100, 155);
//		over.showText(nomeAvaliador);

		form.setFieldProperty("dia", "textcolor", Color.BLACK ,null);
		form.setField("dia", dia);
//		over.setTextMatrix(152, 72);
//		over.showText(dia);
		
		form.setFieldProperty("mes", "textcolor", Color.BLACK ,null);
		form.setField("mes", Ata.getMesPeloNumero(mes));
//		over.setTextMatrix(195, 72);
//		over.showText(Ata.getMesPeloNumero(mes));
		
		form.setFieldProperty("ano", "textcolor", Color.BLACK ,null);
		form.setField("ano", ano);
//		over.setTextMatrix(290, 72);
//		over.showText(ano);

		stamper.setFormFlattening(true);
		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
		System.out.println("gerou o Last" + n + "pdf");

	}

}
