package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.library.ConfHandler;

public class AtaCCoorientador extends Ata {

	// PREENCHE AS PAGINA 2 E 3 DO PDF
	@Override
	public void preencherPrincipal() throws Exception {

		String Arquivo_Saida = ConfHandler.getConf("FILE.PATH") + "saida"
				+ idAluno + ".pdf";
		pathTemplateAta = ConfHandler.getConf("FILE.PATH")+"templatePDF/";
		String template = pathTemplateAta+"TemplateCoorientador"+tcc.getAluno().getCurso().getCodigoCurso()+".pdf";

		FileOutputStream saida = new FileOutputStream(Arquivo_Saida);

//		PdfReader leitor = new PdfReader(
//				AtaCCoorientador.class.getResourceAsStream(template));
		PdfReader leitor = new PdfReader(new FileInputStream(template));

		PdfStamper stamper = new PdfStamper(leitor, saida);

		PdfContentByte over;

		BaseFont bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_BOLD,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		// SEGUNDA
		// PAGINA------------------------------------------------------------------------------------------
		// PREENCHIMENTO DAS PRIMEIRAS INFORMACOES

		over = stamper.getOverContent(2);
		AcroFields form = stamper.getAcroFields(); 
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PREENCHE TABELA DE ACORDO COM A QUANTIDADE DE EXAMINADORES
		Image tabela = null;
		if (qtAvaliador == 5) {
			tabela = Image.getInstance(pathTemplateAta + "tablec3.png");
			tabela.setAbsolutePosition(98, 417);
			over.addImage(tabela);
			over.setTextMatrix(102, 424);
			over.showText("Avaliador(a) 3");

		}

		if (qtAvaliador == 6) {
			tabela = Image.getInstance(pathTemplateAta + "tablec4.png");
			tabela.setAbsolutePosition(98, 397);
			over.addImage(tabela);
			over.setTextMatrix(102, 424);
			over.showText("Avaliador(a) 3");
			over.setTextMatrix(102, 404);
			over.showText("Avaliador(a) 4");

		}

		bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		form.setField("nomeAluno", aluno);
		
		form.setField("titulo1", DivisorString.dividirTitulo(tituloTCC)[0]);
		
		form.setField("titulo2", DivisorString.dividirTitulo(tituloTCC)[1]);
		
		form.setField("orientador1", orientador);
		
		form.setField("coorientador1", coorientador);

		form.setField("avaliador1", avaliadores[2]);
		
		form.setField("avaliador2", avaliadores[3]);
		
		
		if (qtAvaliador >= 5) {
			form.setFieldProperty("campoAvaliador3", "textcolor", Color.BLACK ,null);
			form.setField("campoAvaliador3", "Avaliador 3:");

			over.setFontAndSize(bfTextoSimples, 12);
			form.setField("avaliador3", avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(113, 125);
			over.showText("__________________________");
			over.setTextMatrix(154, 112);
			over.showText("(Avaliador 03)");

		}
		if (qtAvaliador >= 6) {
			form.setFieldProperty("campoAvaliador4", "textcolor", Color.BLACK ,null);
			form.setField("campoAvaliador4", "Avaliador 4:");

			form.setField("avaliador4", avaliadores[5]);

			// ASSINATURA
			over.setTextMatrix(357, 125);
			over.showText("__________________________");
			over.setTextMatrix(398, 112);
			over.showText("(Avaliador 04)");

		}

		// DATA SEGUNDA PAGINA
		form.setFieldProperty("dia1", "textcolor", Color.BLACK ,null);
		form.setField("dia1", dia);
		form.setFieldProperty("mes1", "textcolor", Color.BLACK ,null);
		form.setField("mes1", getMesPeloNumero(mes));
		form.setFieldProperty("ano1", "textcolor", Color.BLACK ,null);
		form.setField("ano1", ano);


		// TERCEIRA PAGINA
		// ----------------------------------------------------------------------------------------------
		over = stamper.getOverContent(3);
		
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PRIMEIRAS INFORMACOES
		form.setFieldProperty("dia2", "textcolor", Color.BLACK ,null);
		form.setField("dia2", dia);
		
		form.setFieldProperty("mes2", "textcolor", Color.BLACK ,null);
		form.setField("mes2", getMesPeloNumero(mes));
		
		form.setFieldProperty("ano2", "textcolor", Color.BLACK ,null);
		form.setField("ano2", ano);
		
		form.setFieldProperty("hora1", "textcolor", Color.BLACK ,null);
		form.setField("hora1", hora);		
		
		form.setFieldProperty("sala1", "textcolor", Color.BLACK ,null);
		form.setField("sala1",sala);
		
		form.setFieldProperty("orientador2", "textcolor", Color.BLACK ,null);
		form.setField("orientador2", orientador);

		// ALUNO
		form.setFieldProperty("nomeAluno2", "textcolor", Color.BLACK ,null);
		form.setField("nomeAluno2", aluno);

		// TCC ENTITULADO
		form.setFieldProperty("titulo1_2", "textcolor", Color.BLACK ,null);
		form.setField("titulo1_2", DivisorString.dividirTitulo(tituloTCC)[0]);

		form.setFieldProperty("titulo2_2", "textcolor", Color.BLACK ,null);
		form.setField("titulo2_2", DivisorString.dividirTitulo(tituloTCC)[1]);

		// EXAMINADORES
		form.setFieldProperty("orientador3", "textcolor", Color.BLACK ,null);
		form.setField("orientador3", "1. "+orientador);

		form.setFieldProperty("coorientador2", "textcolor", Color.BLACK ,null);
		form.setField("coorientador2", "2. "+coorientador);
		
		form.setFieldProperty("avaliador1_2", "textcolor", Color.BLACK ,null);
		form.setField("avaliador1_2", "3. "+avaliadores[2]);
		
		form.setFieldProperty("avaliador2_2", "textcolor", Color.BLACK ,null);
		form.setField("avaliador2_2", "4. "+avaliadores[3]);

		if (qtAvaliador >= 5) {
			form.setFieldProperty("avaliador3_2", "textcolor", Color.BLACK ,null);
			form.setField("avaliador3_2", "5. "+avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(118, 128);
			over.showText("__________________________");
			over.setTextMatrix(159, 115);
			over.showText("(Avaliador 04)");

			if (qtAvaliador >= 6) {
				form.setFieldProperty("avaliador4_2", "textcolor", Color.BLACK ,null);
				form.setField("avaliador4_2", "6. "+avaliadores[5]);

				// ASSINATURA
				over.setTextMatrix(358, 128);
				over.showText("__________________________");
				over.setTextMatrix(399, 115);
				over.showText("(Avaliador 05)");
			}
		}

		// QUARTA PAGINA
		// ----------------------------------------------------------------------------------------------
		stamper.setFormFlattening(true);
		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
		System.out.println("gerou o pdf");

		try {

			for (int i = 0; i < avaliadores.length; i++) {
				PreenchimentoPDF.lastPag(aluno, avaliadores[i], dia, mes, ano,
						i, idAluno,pathTemplateAta,tcc);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		UniaoPDF.unirPDFsFichaAvaliacaoIndividual(qtAvaliador, idAluno);
		UniaoPDF.unirPrincipalComFichaAvaliacaoIndividual(idAluno);

	}

}
