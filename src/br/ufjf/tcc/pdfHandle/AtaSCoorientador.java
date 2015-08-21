package br.ufjf.tcc.pdfHandle;

import java.io.FileOutputStream;
import br.ufjf.tcc.library.ConfHandler;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class AtaSCoorientador extends Ata {

	@Override
	public void preenchePrincipal() throws Exception {

		String Arquivo_Saida = ConfHandler.getConf("FILE.PATH") + "saida"
				+ idAluno + ".pdf";
		String template;

		template = "/br/ufjf/tcc/pdfHandle/TemplateSemCoorientador.pdf";

		FileOutputStream saida = new FileOutputStream(Arquivo_Saida);

		PdfReader leitor = new PdfReader(
				AtaSCoorientador.class.getResourceAsStream(template));

		PdfStamper stamper = new PdfStamper(leitor, saida);

		PdfContentByte over;

		BaseFont bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_BOLD,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		// SEGUNDA
		// PAGINA------------------------------------------------------------------------------------------

		over = stamper.getOverContent(2);
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PREENCHE TABELA
		Image tabela = null;
		if (qtAvaliador == 3) {
			tabela = Image.getInstance(ConfHandler.getConf("FILE.PATH")+"tablec3.png");
			tabela.setAbsolutePosition(98, 459);
			over.addImage(tabela);
			over.setTextMatrix(102, 467);
			over.showText("Avaliador(a) 3");

		}

		if (qtAvaliador == 4) {
			tabela = Image.getInstance(ConfHandler.getConf("FILE.PATH")+"tablec4.png");
			tabela.setAbsolutePosition(98, 439);
			over.addImage(tabela);
			over.setTextMatrix(102, 467);
			over.showText("Avaliador(a) 3");
			over.setTextMatrix(102, 447);
			over.showText("Avaliador(a) 4");

		}

		// PREENCHIMENTO DAS PRIMEIRAS INFORMACOES
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

		over.setTextMatrix(150, 646);
		over.showText(avaliadores[0]);
		over.setTextMatrix(150, 626);
		over.showText(avaliadores[1]);
		if (qtAvaliador >= 3) {
			over.setTextMatrix(85, 605);
			over.showText("Avaliador 3:___________________________________________");
			over.setTextMatrix(150, 606);
			over.showText(avaliadores[2]);

			// ASSINATURA
			over.setTextMatrix(113, 145);
			over.showText("__________________________");
			over.setTextMatrix(154, 132);
			over.showText("(Avaliador 03)");

		}
		if (qtAvaliador >= 4) {
			over.setTextMatrix(85, 584);
			over.showText("Avaliador 4:___________________________________________");
			over.setTextMatrix(150, 585);
			over.showText(avaliadores[3]);

			// ASSINATURA
			over.setTextMatrix(353, 145);
			over.showText("__________________________");
			over.setTextMatrix(394, 132);
			over.showText("(Avaliador 04)");

		}

		// DATA SEGUNDA PAGINA
		over.setTextMatrix(157, 70);
		over.showText(dia);
		over.setTextMatrix(210, 70);
		over.showText(getMesPeloNumero(mes));
		over.setTextMatrix(293, 70);
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
		over.setTextMatrix(155, 498);
		over.showText(aluno);
		// TCC ENTITULADO
		over.setTextMatrix(88, 456);
		over.showText(Divide.titulo(tituloTCC)[0]);
		over.setTextMatrix(88, 436);
		over.showText(Divide.titulo(tituloTCC)[1]);

		// examinadores
		over.setTextMatrix(90, 600);
		over.showText("1. " + orientador);

		over.setTextMatrix(90, 580);
		over.showText("2. " + avaliadores[0]);
		over.setTextMatrix(90, 560);
		over.showText("3. " + avaliadores[1]);

		if (qtAvaliador >= 3) {
			over.setTextMatrix(90, 540);
			over.showText("4. " + avaliadores[2]);

			// ASSINATURA
			over.setTextMatrix(115, 125);
			over.showText("__________________________");
			over.setTextMatrix(155, 112);
			over.showText("(Avaliador 03)");

			if (qtAvaliador >= 4) {
				over.setTextMatrix(90, 520);
				over.showText("5. " + avaliadores[3]);

				// ASSINATURA
				over.setTextMatrix(358, 125);
				over.showText("__________________________");
				over.setTextMatrix(397, 112);
				over.showText("(Avaliador 04)");
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
