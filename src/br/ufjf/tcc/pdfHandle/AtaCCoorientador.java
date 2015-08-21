package br.ufjf.tcc.pdfHandle;

import java.io.FileOutputStream;
import br.ufjf.tcc.library.ConfHandler;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class AtaCCoorientador extends Ata {

	// PREENCHE AS PAGINA 2 E 3 DO PDF
	@Override
	public void preenchePrincipal() throws Exception {

		String Arquivo_Saida = ConfHandler.getConf("FILE.PATH") + "saida"
				+ idAluno + ".pdf";
		String template = "/br/ufjf/tcc/pdfHandle/TemplateCoorientador.pdf";

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
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PREENCHE TABELA DE ACORDO COM A QUANTIDADE DE EXAMINADORES
		Image tabela = null;
		if (qtAvaliador == 5) {
			tabela = Image.getInstance(ConfHandler.getConf("FILE.PATH")
					+ "/tablec3.png");
			tabela.setAbsolutePosition(98, 418);
			over.addImage(tabela);
			over.setTextMatrix(102, 426);
			over.showText("Avaliador(a) 3");

		}

		if (qtAvaliador == 6) {
			tabela = Image.getInstance(ConfHandler.getConf("FILE.PATH")
					+ "/tablec4.png");
			tabela.setAbsolutePosition(98, 398);
			over.addImage(tabela);
			over.setTextMatrix(102, 426);
			over.showText("Avaliador(a) 3");
			over.setTextMatrix(102, 406);
			over.showText("Avaliador(a) 4");

		}

		bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		over.setFontAndSize(bfTextoSimples, 12);
		over.setTextMatrix(122, 729);
		over.showText(aluno);
		over.setTextMatrix(122, 708);
		over.showText(Divide.titulo(tituloTCC)[0]);
		over.setTextMatrix(90, 687);
		over.showText(Divide.titulo(tituloTCC)[1]);
		over.setTextMatrix(158, 666);
		over.showText(orientador);

		over.setTextMatrix(170, 645);
		over.showText(coorientador);

		over.setTextMatrix(150, 624);
		over.showText(avaliadores[2]);
		over.setTextMatrix(150, 604);
		over.showText(avaliadores[3]);
		if (qtAvaliador >= 5) {
			over.setTextMatrix(85, 583);
			over.showText("Avaliador 3:___________________________________________");
			over.setTextMatrix(150, 583);
			over.showText(avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(113, 125);
			over.showText("__________________________");
			over.setTextMatrix(154, 112);
			over.showText("(Avaliador 03)");

		}
		if (qtAvaliador >= 6) {
			over.setTextMatrix(85, 563);
			over.showText("Avaliador 4:___________________________________________");
			over.setTextMatrix(150, 563);
			over.showText(avaliadores[5]);

			// ASSINATURA
			over.setTextMatrix(357, 125);
			over.showText("__________________________");
			over.setTextMatrix(398, 112);
			over.showText("(Avaliador 04)");

		}

		// DATA SEGUNDA PAGINA
		over.setTextMatrix(157, 61);
		over.showText(dia);
		over.setTextMatrix(210, 61);
		over.showText(getMesPeloNumero(mes));
		over.setTextMatrix(293, 61);
		over.showText(ano);
		over.endText();

		// TERCEIRA PAGINA
		// ----------------------------------------------------------------------------------------------
		over = stamper.getOverContent(3);
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PRIMEIRAS INFORMACOES
		over.setTextMatrix(150, 704);
		over.showText(dia);
		over.setTextMatrix(275, 704);
		over.showText(getMesPeloNumero(mes));
		over.setTextMatrix(440, 704);
		over.showText(ano);
		over.setTextMatrix(130, 683);
		over.showText(hora);
		over.setTextMatrix(355, 683);
		over.showText(sala);
		over.setTextMatrix(100, 643);
		over.showText(orientador);

		// ALUNO
		over.setTextMatrix(155, 477);
		over.showText(aluno);
		// TCC ENTITULADO
		over.setTextMatrix(88, 435);
		over.showText(Divide.titulo(tituloTCC)[0]);
		over.setTextMatrix(88, 415);
		over.showText(Divide.titulo(tituloTCC)[1]);

		// EXAMINADORES
		over.setTextMatrix(90, 600);
		over.showText("1. " + orientador);

		over.setTextMatrix(90, 580);
		over.showText("2. " + coorientador);
		over.setTextMatrix(90, 560);
		over.showText("3. " + avaliadores[2]);
		over.setTextMatrix(90, 540);
		over.showText("4. " + avaliadores[3]);
		if (qtAvaliador >= 5) {
			over.setTextMatrix(90, 520);
			over.showText("5. " + avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(118, 128);
			over.showText("__________________________");
			over.setTextMatrix(159, 115);
			over.showText("(Avaliador 04)");

			if (qtAvaliador >= 6) {
				over.setTextMatrix(90, 500);
				over.showText("6. " + avaliadores[5]);
				// ASSINATURA
				over.setTextMatrix(358, 128);
				over.showText("__________________________");
				over.setTextMatrix(399, 115);
				over.showText("(Avaliador 05)");
			}
		}

		// QUARTA PAGINA
		// ----------------------------------------------------------------------------------------------

		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
		System.out.println("gerou o pdf");

		try {

			for (int i = 0; i < avaliadores.length; i++) {
				PreenchimentoPDF.lastPag(aluno, avaliadores[i], dia, mes, ano,
						i, idAluno);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		Unir.lastPDFs(qtAvaliador, idAluno);
		Unir.tudo(idAluno);

	}

}
