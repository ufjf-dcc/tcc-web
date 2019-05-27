package br.ufjf.tcc.pdfHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.model.Participacao;

public class CartaParticipacaoBanca {
	private static final String PASTA_ARQUIVOS_TEMP = ConfHandler.getConf("FILE.PATH") + "arquivosTemporarios/";
	private static final String PASTA_COM_TEMPLATE = ConfHandler.getConf("FILE.PATH") + "templatePDF/";
	private static final String TEMPLATE = "CartaParticipacaoBanca";
	
	private String nomeArquivo;
	
	public CartaParticipacaoBanca() {
	}
	
	public void gerarCartaParticipacao(String nomeCurso, String nomeMembroDaBanca, String nomeAluno, String nomeOrientador, int idTCC,
			String nomeCoorientador, String tituloTrabalho, String dataDefesa, List<Participacao> membrosDaBanca, String siapeProfessor, String certificadoDigital)
	{
		String template = PASTA_COM_TEMPLATE + TEMPLATE + ".pdf";
		String Arquivo_Saida = PASTA_ARQUIVOS_TEMP + TEMPLATE + "-" + idTCC + "-" + siapeProfessor + ".pdf";
		this.nomeArquivo = TEMPLATE + "-" + idTCC + "-" + siapeProfessor + ".pdf";
		try {
			FileOutputStream saida = new FileOutputStream(Arquivo_Saida);
			PdfReader leitor = new PdfReader(new PdfReader(new FileInputStream(template)));
			PdfStamper stamper = new PdfStamper(leitor, saida);
			AcroFields form = stamper.getAcroFields();
			
			form.setField("nomeCursoNoCabecalho", nomeCurso);
			form.setField("nomeMembroDaBanca", nomeMembroDaBanca);
			form.setField("nomeDoCursoNoTexto", nomeCurso);
			form.setField("nomeAluno", nomeAluno);
			form.setField("nomeOrientador", nomeOrientador);
			form.setField("Coorientador", nomeCoorientador);
			form.setField("tituloTrabalho", tituloTrabalho);
			form.setField("dataDefesa", dataDefesa);
			//form.setField("nomeCoordenadorDoCurso", nomeCoordenadorCurso);
			//form.setField("nomeDoCursoNaAssinatura", nomeCurso);
			form.setField("certificadoDigital", certificadoDigital);
			
			String NomeMembrosBanca = "";
			
			for(Participacao p : membrosDaBanca)
			{
				NomeMembrosBanca += "\n\n" + p.getProfessor().getNomeUsuario();
			}
			
			form.setField("membrosDaBanca", NomeMembrosBanca);
			
			stamper.setFormFlattening(true);
			stamper.close();
			saida.close();
			leitor.close();
			
			System.out.println("Gerou Carta Participacao");
		} catch (Exception e) {
			System.out.println("Ocorreu algum problema ao gerar o pdf");
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public String obterNomeArquivo() {
		if(this.nomeArquivo != null)
			return this.nomeArquivo;
		return "";
	}
	
	public void apagarArquivo() {
		deleteFile(PASTA_ARQUIVOS_TEMP + this.nomeArquivo);
	}
	
	private void deleteFile(String path){
		File file = new File(path);
		if(file.exists()){
			if(file.delete())
				System.out.println(path+" - DELETADO");
		}
	}
}
