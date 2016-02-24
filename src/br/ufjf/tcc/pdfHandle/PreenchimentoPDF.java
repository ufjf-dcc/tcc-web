package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.business.ParticipacaoBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;

public class PreenchimentoPDF {

	public static void preencherFichaAvaliacaoIndividual(String nomeAluno, String nomeAvaliador,
			String dia, String mes, String ano, int numeroFicha, int idAluno,String pathAta,TCC tcc)
			throws Exception {
		
		String template =   pathAta + Ata.FICHA_AVALIACAO_INDIVIDUAL + tcc.getAluno().getCurso().getCodigoCurso()+".pdf";
		String Arquivo_Saida = Ata.PASTA_ARQUIVOS_TEMP + idAluno + "-" + numeroFicha + ".pdf";

		FileOutputStream saida = new FileOutputStream(Arquivo_Saida);

		PdfReader leitor = new PdfReader(new PdfReader(new FileInputStream(template)));

		PdfStamper stamper = new PdfStamper(leitor, saida);

		PdfContentByte over;

		BaseFont bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		BaseFont campoStyle = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		over = stamper.getOverContent(1);
		AcroFields form = stamper.getAcroFields();
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);

		
		form.setFieldProperty("nomeAluno", "textcolor", Color.BLACK ,null);
		form.setField("nomeAluno", nomeAluno);
		form.setFieldProperty("nomeAluno", "textfont", campoStyle ,null);
		form.setFieldProperty("nomeAluno", "textsize", new Float(12) ,null);
		
		form.setFieldProperty("nomeAvaliador", "textfont", campoStyle ,null);
		form.setFieldProperty("nomeAvaliador", "textsize", new Float(12) ,null);
		form.setFieldProperty("nomeAvaliador", "textcolor", Color.BLACK ,null);
		form.setField("nomeAvaliador", nomeAvaliador);

		form.setFieldProperty("dia", "textfont", campoStyle ,null);
		form.setFieldProperty("dia", "textsize", new Float(12) ,null);
		form.setFieldProperty("dia", "textcolor", Color.BLACK ,null);
		form.setField("dia", dia);
		
		form.setFieldProperty("mes", "textfont", campoStyle ,null);
		form.setFieldProperty("mes", "textsize", new Float(12) ,null);
		form.setFieldProperty("mes", "textcolor", Color.BLACK ,null);
		form.setField("mes", Ata.getMesPeloNumero(mes));
		
		form.setFieldProperty("ano", "textfont", campoStyle ,null);
		form.setFieldProperty("ano", "textsize", new Float(12) ,null);
		form.setFieldProperty("ano", "textcolor", Color.BLACK ,null);
		form.setField("ano", ano);

		stamper.setFormFlattening(true);
		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
		System.out.println("Gerou ficha: " + numeroFicha + " - "+Ata.PASTA_ARQUIVOS_TEMP + idAluno + "-" + numeroFicha + ".pdf");

	}
	
	public static void preencherBancaPDF(TCC tcc, int numeroPdf, int idAluno,String pathAta)
			throws Exception {
		String template =   pathAta + Ata.COMPOSICAO_BANCA + Ata.EXTENSAO_PDF;
		String Arquivo_Saida = Ata.PASTA_ARQUIVOS_TEMP + Ata.COMPOSICAO_BANCA + idAluno + "-" + numeroPdf + ".pdf";

		FileOutputStream saida = new FileOutputStream(Arquivo_Saida);

		PdfReader leitor = new PdfReader(new FileInputStream(template));

		PdfStamper stamper = new PdfStamper(leitor, saida);

		PdfContentByte over;
		
		BaseFont tituloFont = BaseFont.createFont(BaseFont.TIMES_BOLD,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		
		
		BaseFont bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		over = stamper.getOverContent(1);
		AcroFields form = stamper.getAcroFields();
		over.beginText();
		over.setFontAndSize(bfTextoSimples, 12);
		
		form.setFieldProperty("curso", "textfont", tituloFont,null);
		form.setFieldProperty("curso", "textcolor", Color.BLACK ,null);
		form.setField("curso", tcc.getAluno().getCurso().getNomeCurso());
		
		form.setFieldProperty("nomeAluno", "textcolor", Color.BLACK ,null);
		form.setField("nomeAluno", tcc.getAluno().getNomeUsuario());
		
		form.setFieldProperty("matricula", "textcolor", Color.BLACK ,null);
		form.setField("matricula", tcc.getAluno().getMatricula());

		form.setFieldProperty("titulo", "textcolor", Color.BLACK ,null);
		form.setField("titulo", tcc.getNomeTCC());
		
		form.setFieldProperty("resumo", "textcolor", Color.BLACK ,null);
		form.setField("resumo", tcc.getResumoTCC());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tcc.getDataApresentacao()
				.getTime());
		
		Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
		Integer mes = calendar.get(Calendar.MONTH) + 1;
		Integer ano = calendar.get(Calendar.YEAR);
		String hora = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));

		
		form.setFieldProperty("dia", "textcolor", Color.BLACK ,null);
		form.setField("dia", formatarCamposData(dia) );
		form.setFieldProperty("mes", "textcolor", Color.BLACK ,null);
		form.setField("mes", formatarCamposData(mes));
		form.setFieldProperty("ano", "textcolor", Color.BLACK ,null);
		form.setField("ano", formatarCamposData(ano));
		
		form.setFieldProperty("hora", "textcolor", Color.BLACK ,null);
		form.setField("hora", hora);

		form.setFieldProperty("sala", "textcolor", Color.BLACK ,null);
		form.setField("sala", tcc.getSalaDefesa());
		
		String avaliadores = "";
		String suplente = "";
		List<Participacao> participacoes = new ParticipacaoBusiness().getParticipacoesUsuarioByTCC(tcc);
		
		avaliadores += tcc.getOrientador().getNomeUsuario()+" - Orientador(a)";
		avaliadores += "\n"+retirarNull(tcc.getOrientador().getTitulacao());
		
		if (tcc.getCoOrientador() != null) {

			avaliadores += "\n\n"+ tcc.getCoOrientador().getNomeUsuario()+" - Coorientador(a)";
			avaliadores += "\n" + retirarNull(participacoes.get(0).getProfessor().getTitulacao());
			
		}

		for(int i=0;i<participacoes.size();i++){
			if (participacoes.get(i).getSuplente() == 0) {
				avaliadores += "\n\n" + participacoes.get(i).getProfessor().getNomeUsuario();
				avaliadores += "\n" + retirarNull(participacoes.get(i).getProfessor().getTitulacao());
			}else{
				suplente+=participacoes.get(i).getProfessor().getNomeUsuario();
				suplente+="\n"+retirarNull(participacoes.get(i).getProfessor().getTitulacao());
			}
		}
		
		form.setFieldProperty("avaliadores", "textcolor", Color.BLACK ,null);
		form.setField("avaliadores",avaliadores);
		
		form.setFieldProperty("suplente", "textcolor", Color.BLACK ,null);
		form.setField("suplente",suplente);
		
		stamper.setFormFlattening(true);
		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
		System.out.println("gerou o Last" + numeroPdf + "pdf");

	}
	
	public static String retirarNull(String valor){
		if(valor==null)
			return "";
		else
			return valor;
	}
	
	public static String formatarCamposData(int campo){
		
		if(campo<10){
			return "0"+campo;
		}
		return String.valueOf(campo);
	}

}
