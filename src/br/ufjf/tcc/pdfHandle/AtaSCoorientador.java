package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.library.ConfHandler;

public class AtaSCoorientador extends Ata {

	// PREENCHE AS PAGINA 2 E 3 DO PDF
	@Override
	public void preenchePrincipal() throws Exception {

		String Arquivo_Saida = ConfHandler.getConf("FILE.PATH") + "saida"
				+ idAluno + ".pdf";
		String template = pathTemplateAta+"TemplateSCoorientador2.pdf";

		FileOutputStream saida = new FileOutputStream(Arquivo_Saida);

		PdfReader leitor = new PdfReader(
				AtaCCoorientador.class.getResourceAsStream(template));

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
		if (qtAvaliador == 4) {
			tabela = Image.getInstance(ConfHandler.getConf("FILE.PATH")
					+ "/tablec3.png");
			tabela.setAbsolutePosition(98, 437);
			over.addImage(tabela);
			over.setTextMatrix(102, 444);
			over.showText("Avaliador(a) 3");
			
		}

		if (qtAvaliador == 5) {
			tabela = Image.getInstance(ConfHandler.getConf("FILE.PATH")
					+ "/tablec4.png");
			tabela.setAbsolutePosition(98, 417);
			over.addImage(tabela);
			over.setTextMatrix(102, 445);
			over.showText("Avaliador(a) 3");
			over.setTextMatrix(102, 425);
			over.showText("Avaliador(a) 4");

		}

		bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		over.setFontAndSize(bfTextoSimples, 12);
		form.setField("nomeAluno", aluno);
		//over.setTextMatrix(122, 729);
		//over.showText(aluno);
		
		over.setFontAndSize(bfTextoSimples, 12);
		form.setField("titulo1", Divide.titulo(tituloTCC)[0]);
//		over.setTextMatrix(122, 708);
//		over.showText(Divide.titulo(tituloTCC)[0]);
		
		over.setFontAndSize(bfTextoSimples, 12);
		form.setField("titulo2", Divide.titulo(tituloTCC)[1]);
//		over.setTextMatrix(90, 687);
//		over.showText(Divide.titulo(tituloTCC)[1]);
		
		
		form.setField("orientador1", orientador);
//		over.setTextMatrix(158, 666);
//		over.showText(orientador);
		
	

		over.setFontAndSize(bfTextoSimples, 12);
		form.setField("avaliador1", avaliadores[1]);
//		over.setTextMatrix(150, 624);
//		over.showText(avaliadores[2]);
		
		over.setFontAndSize(bfTextoSimples, 12);
		form.setField("avaliador2", avaliadores[2]);
//		over.setTextMatrix(150, 604);
//		over.showText(avaliadores[3]);
		
		
		if (qtAvaliador == 4) {
			form.setFieldProperty("campoAvaliador3", "textcolor", Color.BLACK ,null);
			form.setField("campoAvaliador3", "Avaliador 3:");
//			over.setTextMatrix(85, 583);
//			over.showText("Avaliador 3:___________________________________________");
			over.setFontAndSize(bfTextoSimples, 12);
			form.setField("avaliador3", avaliadores[3]);
//			over.setTextMatrix(150, 583);
//			over.showText(avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(113, 125);
			over.showText("__________________________");
			over.setTextMatrix(154, 112);
			over.showText("(Avaliador 03)");

		}
		if (qtAvaliador == 5) {
			form.setFieldProperty("campoAvaliador4", "textcolor", Color.BLACK ,null);
			form.setField("campoAvaliador4", "Avaliador 4:");
//			over.setTextMatrix(85, 563);
//			over.showText("Avaliador 4:___________________________________________");
			over.setFontAndSize(bfTextoSimples, 12);
			form.setField("avaliador4", avaliadores[4]);
//			over.setTextMatrix(150, 563);
//			over.showText(avaliadores[5]);

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
//		over.setTextMatrix(157, 61);
//		over.showText(dia);
//		over.setTextMatrix(210, 61);
//		over.showText(getMesPeloNumero(mes));
//		over.setTextMatrix(293, 61);
//		over.showText(ano);
//		over.endText();

		// TERCEIRA PAGINA
		// ----------------------------------------------------------------------------------------------
		over = stamper.getOverContent(3);
		
		//form = stamper.getAcroFields();
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PRIMEIRAS INFORMACOES
		form.setFieldProperty("dia2", "textcolor", Color.BLACK ,null);
		form.setField("dia2", dia);
//		over.setTextMatrix(150, 704);
//		over.showText(dia);
		
		form.setFieldProperty("mes2", "textcolor", Color.BLACK ,null);
		form.setField("mes2", getMesPeloNumero(mes));
//		over.setTextMatrix(275, 704);
//		over.showText(getMesPeloNumero(mes));
		
		form.setFieldProperty("ano2", "textcolor", Color.BLACK ,null);
		form.setField("ano2", ano);
//		over.setTextMatrix(440, 704);
//		over.showText(ano);
		
		form.setFieldProperty("hora1", "textcolor", Color.BLACK ,null);
		form.setField("hora1", hora);		
//		over.setTextMatrix(130, 683);
//		over.showText(hora);
		
		form.setFieldProperty("sala1", "textcolor", Color.BLACK ,null);
		form.setField("sala1",sala);
//		over.setTextMatrix(355, 683);
//		over.showText(sala);
		
		form.setFieldProperty("orientador2", "textcolor", Color.BLACK ,null);
		form.setField("orientador2", orientador);
//		over.setTextMatrix(100, 643);
//		over.showText(orientador);

		// ALUNO
		form.setFieldProperty("nomeAluno2", "textcolor", Color.BLACK ,null);
		form.setField("nomeAluno2", aluno);
//		over.setTextMatrix(155, 477);
//		over.showText(aluno);
		// TCC ENTITULADO
		form.setFieldProperty("titulo1_2", "textcolor", Color.BLACK ,null);
		form.setField("titulo1_2", Divide.titulo(tituloTCC)[0]);
//		over.setTextMatrix(88, 435);
//		over.showText(Divide.titulo(tituloTCC)[0]);
		form.setFieldProperty("titulo2_2", "textcolor", Color.BLACK ,null);
		form.setField("titulo2_2", Divide.titulo(tituloTCC)[1]);
//		over.setTextMatrix(88, 415);
//		over.showText(Divide.titulo(tituloTCC)[1]);

		// EXAMINADORES
		
		form.setFieldProperty("orientador3", "textcolor", Color.BLACK ,null);
		form.setField("orientador3", "1. "+orientador);
//		over.setTextMatrix(90, 600);
//		over.showText("1. " + orientador);

	
		
		form.setFieldProperty("avaliador1_2", "textcolor", Color.BLACK ,null);
		form.setField("avaliador1_2", "3. "+avaliadores[1]);
//		over.setTextMatrix(90, 560);
//		over.showText("3. " + avaliadores[2]);
		
		form.setFieldProperty("avaliador2_2", "textcolor", Color.BLACK ,null);
		form.setField("avaliador2_2", "4. "+avaliadores[2]);
//		over.setTextMatrix(90, 540);
//		over.showText("4. " + avaliadores[3]);
		if (qtAvaliador >= 4) {
			form.setFieldProperty("avaliador3_2", "textcolor", Color.BLACK ,null);
			form.setField("avaliador3_2", "5. "+avaliadores[3]);
//			over.setTextMatrix(90, 520);
//			over.showText("5. " + avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(118, 128);
			over.showText("__________________________");
			over.setTextMatrix(159, 115);
			over.showText("(Avaliador 04)");

			if (qtAvaliador >= 5) {
				form.setFieldProperty("avaliador4_2", "textcolor", Color.BLACK ,null);
				form.setField("avaliador4_2", "6. "+avaliadores[4]);
//				over.setTextMatrix(90, 500);
//				over.showText("6. " + avaliadores[5]);
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
						i, idAluno,pathTemplateAta);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		Unir.lastPDFs(qtAvaliador, idAluno);
		Unir.tudo(idAluno);

	}

}
