package br.ufjf.tcc.pdfHandle;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.business.ParticipacaoBusiness;
import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;

public class PreenchimentoPDF {

	public static void lastPag(String nomeAluno, String nomeAvaliador,
			String dia, String mes, String ano, int n, int idAluno,String pathAta)
			throws Exception {
		String template =   pathAta+"LastPagNew.pdf";
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
	
	public static void bancaPDF(TCC tcc, int n, int idAluno,String pathAta)
			throws Exception {
		String template =   pathAta+"ComposicaoBanca.pdf";
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
		form.setField("nomeAluno", tcc.getAluno().getNomeUsuario());
//		over.setTextMatrix(100, 208);		
//		over.showText(nomeAluno);
		
		form.setFieldProperty("matricula", "textcolor", Color.BLACK ,null);
		form.setField("matricula", tcc.getAluno().getMatricula());
//		over.setTextMatrix(100, 155);
//		over.showText(nomeAvaliador);

		form.setFieldProperty("titulo", "textcolor", Color.BLACK ,null);
		form.setField("titulo", tcc.getNomeTCC());
//		over.setTextMatrix(152, 72);
//		over.showText(dia);
		
		form.setFieldProperty("resumo", "textcolor", Color.BLACK ,null);
		form.setField("resumo", tcc.getResumoTCC());
//		over.setTextMatrix(195, 72);
//		over.showText(Ata.getMesPeloNumero(mes));
		
		form.setFieldProperty("dia", "textcolor", Color.BLACK ,null);
		form.setField("dia", String.valueOf(tcc.getDataApresentacao().getDay()) );
		form.setFieldProperty("ano", "textcolor", Color.BLACK ,null);
		form.setField("mes", String.valueOf(tcc.getDataApresentacao().getMonth()));
		form.setFieldProperty("ano", "textcolor", Color.BLACK ,null);
		form.setField("ano", String.valueOf(tcc.getDataApresentacao().getYear()));
//		over.setTextMatrix(290, 72);
//		over.showText(ano);
		
		form.setFieldProperty("hora", "textcolor", Color.BLACK ,null);
		form.setField("hora", String.valueOf(tcc.getDataApresentacao().getHours()));

		form.setFieldProperty("sala", "textcolor", Color.BLACK ,null);
		form.setField("sala", tcc.getSalaDefesa());
		
		String avaliadores = "";
		String suplente = "";
		List<Participacao> participacoes = new ParticipacaoBusiness().getParticipacoesUsuarioByTCC(tcc);
		avaliadores += tcc.getOrientador().getNomeUsuario()+" - Orientador(a)";
		avaliadores += "\n"+tcc.getOrientador().getTitulacao();
		if (tcc.getCoOrientador() != null) {
			avaliadores += "\n\n"+participacoes.get(0).getProfessor().getNomeUsuario()+" - CoOrientador(a)";
			avaliadores += "\n" + participacoes.get(0).getProfessor().getTitulacao();
		}
		
		
		if (participacoes.get(0).getSuplente() == 0) {
			avaliadores +=  participacoes.get(0).getProfessor().getNomeUsuario();
			avaliadores += "\n" + participacoes.get(0).getProfessor().getTitulacao();
		}else{
			suplente+=participacoes.get(0).getProfessor().getNomeUsuario();
			suplente+="\n"+participacoes.get(0).getProfessor().getTitulacao();
		}
		for(int i=1;i<participacoes.size();i++){
			if (participacoes.get(i).getSuplente() == 0) {
				avaliadores += "\n\n" + participacoes.get(i).getProfessor().getNomeUsuario();
				avaliadores += "\n" + participacoes.get(i).getProfessor().getTitulacao();
			}else{
				suplente+=participacoes.get(i).getProfessor().getNomeUsuario();
				suplente+="\n"+participacoes.get(i).getProfessor().getTitulacao();
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
		System.out.println("gerou o Last" + n + "pdf");

	}

}
