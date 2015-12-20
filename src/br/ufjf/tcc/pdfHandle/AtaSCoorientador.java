package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.model.TCC;

public class AtaSCoorientador extends Ata {
	
	public AtaSCoorientador(TCC tcc){
		super(tcc);
	}

	private void iniciarParametros() throws Exception {
		
		String ARQUIVO_SAIDA = PASTA_ARQUIVOS_TEMP +  FICHA_AVALIACAO_FINAL + idAluno + EXTENSAO_PDF;
		template = PASTA_COM_TEMPLATE_ATAS + TEMPLATE_SEM_COORIENTADOR + tcc.getAluno().getCurso().getCodigoCurso() + EXTENSAO_PDF;
		saida = new FileOutputStream(ARQUIVO_SAIDA);
		leitor = new PdfReader(new PdfReader(new FileInputStream(template)));
		stamper = new PdfStamper(leitor, saida);
		bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		form = stamper.getAcroFields();

	}
	
	private void iniciarPropriedadeCampos(){
		form.setFieldProperty("nomeAluno", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("titulo1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("titulo2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("orientador1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("campoAvaliador3", "textcolor", Color.BLACK  ,null);
		form.setFieldProperty("campoAvaliador4", "textcolor", Color.BLACK  ,null);
		form.setFieldProperty("avaliador3", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador4", "textcolor", Color.BLACK ,null);
		
		form.setFieldProperty("dia1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("mes1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("ano1", "textcolor", Color.BLACK ,null);
		
		form.setFieldProperty("dia2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("mes2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("ano2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("hora1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("sala1", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("orientador2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("nomeAluno2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("titulo1_2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("titulo2_2", "textcolor", Color.BLACK ,null);
		
		form.setFieldProperty("orientador3", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador1_2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador2_2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador3_2", "textcolor", Color.BLACK ,null);
		form.setFieldProperty("avaliador4_2", "textcolor", Color.BLACK ,null);
		
	}
	
	// PREENCHE AS PAGINAS QUE CONTEM INFORMAÇÕES
	@Override
	public void preencherPDF() throws Exception {
		iniciarParametros();
		iniciarPropriedadeCampos();		
		
		over = stamper.getOverContent(leitor.getNumberOfPages()-1);
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		inserirTabelaDinamica();

		inserirDadosPrimeiraPagina();

		inserirDadosSegundaPagina();

		fecharFluxos();

		preencherFichasAvaliacaoIndividual();

		UniaoPDF.unirPDFsFichaAvaliacaoIndividual(qtAvaliador, idAluno);
		UniaoPDF.unirFichaAvaliacaoFinalComFichaAvaliacaoIndividual(idAluno);

	}

	private void preencherFichasAvaliacaoIndividual() {
		try {
			//PREENCHE FICHA INDIVIDUAL PARA CADA AVALIADOR
			for (int i = 0; i < avaliadores.length; i++) {
				PreenchimentoPDF.preencherFichaAvaliacaoIndividual(tcc.getAluno().getNomeUsuario(), avaliadores[i], dia, mes, ano,
						i, idAluno,PASTA_COM_TEMPLATE_ATAS,tcc);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void inserirDadosSegundaPagina() throws IOException, DocumentException {
		over = stamper.getOverContent(leitor.getNumberOfPages());
		
		//form = stamper.getAcroFields();
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		// PRIMEIRAS INFORMACOES
		
		form.setField("dia2", dia);
		
		form.setField("mes2", getMesPeloNumero(mes));
		
		form.setField("ano2", ano);
		
		form.setField("hora1", hora);		
		
		form.setField("sala1",sala);
		
		form.setField("orientador2", tcc.getOrientador().getNomeUsuario());

		form.setField("nomeAluno2", tcc.getAluno().getNomeUsuario());
		
		form.setField("titulo1_2", DivisorString.dividirTitulo(tcc.getNomeTCC())[0]);
		
		form.setField("titulo2_2", DivisorString.dividirTitulo(tcc.getNomeTCC())[1]);

		// EXAMINADORES
		form.setField("orientador3", "1. "+tcc.getOrientador().getNomeUsuario());
		
		form.setField("avaliador1_2", "2. "+avaliadores[1]);
		
		form.setField("avaliador2_2", "3. "+avaliadores[2]);

		if (qtAvaliador >= 4) {
			
			form.setField("avaliador3_2", "4. "+avaliadores[3]);

			// ASSINATURA
			over.setTextMatrix(118, 128);
			over.showText("__________________________");
			over.setTextMatrix(159, 115);
			over.showText("(Avaliador 04)");

			if (qtAvaliador >= 5) {
				
				form.setField("avaliador4_2", "5. "+avaliadores[4]);

				// ASSINATURA
				over.setTextMatrix(358, 128);
				over.showText("__________________________");
				over.setTextMatrix(399, 115);
				over.showText("(Avaliador 05)");
			}
		}
	}

	private void inserirDadosPrimeiraPagina() throws DocumentException, IOException {
		bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		
		form.setField("nomeAluno", tcc.getAluno().getNomeUsuario());
		
		form.setField("titulo1", DivisorString.dividirTitulo(tcc.getNomeTCC())[0]);
		
		form.setField("titulo2", DivisorString.dividirTitulo(tcc.getNomeTCC())[1]);
		
		form.setField("orientador1", tcc.getOrientador().getNomeUsuario());

		form.setField("avaliador1", avaliadores[1]);
		
		form.setField("avaliador2", avaliadores[2]);
		
		
		if (qtAvaliador >= 4) {
			
			form.setField("campoAvaliador3", "Avaliador 3:");

			over.setFontAndSize(bfTextoSimples, 12);
			form.setField("avaliador3", avaliadores[3]);

			// ASSINATURA
			over.setTextMatrix(113, 125);
			over.showText("__________________________");
			over.setTextMatrix(154, 112);
			over.showText("(Avaliador 03)");

		}
		if (qtAvaliador >= 5) {
			
			form.setField("campoAvaliador4", "Avaliador 4:");

			over.setFontAndSize(bfTextoSimples, 12);
			form.setField("avaliador4", avaliadores[4]);

			// ASSINATURA
			over.setTextMatrix(357, 125);
			over.showText("__________________________");
			over.setTextMatrix(398, 112);
			over.showText("(Avaliador 04)");

		}

		// DATA SEGUNDA PAGINA
		
		form.setField("dia1", dia);
		
		form.setField("mes1", getMesPeloNumero(mes));
		
		form.setField("ano1", ano);
	}

	private void inserirTabelaDinamica()
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		Image tabela = null;
		if (qtAvaliador == 4) {
			
			tabela = Image.getInstance(PASTA_COM_TEMPLATE_ATAS+ "tablec3.png");
			tabela.setAbsolutePosition(98, 437);
			over.addImage(tabela);
			over.setTextMatrix(102, 444);
			over.showText("Avaliador(a) 3");
			
		}

		if (qtAvaliador == 5) {
			tabela = Image.getInstance(PASTA_COM_TEMPLATE_ATAS + "tablec4.png");
			tabela.setAbsolutePosition(98, 417);
			over.addImage(tabela);
			over.setTextMatrix(102, 445);
			over.showText("Avaliador(a) 3");
			over.setTextMatrix(102, 425);
			over.showText("Avaliador(a) 4");

		}
	}

	private void fecharFluxos() throws DocumentException, IOException {
		stamper.setFormFlattening(true);
		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
	}

}
