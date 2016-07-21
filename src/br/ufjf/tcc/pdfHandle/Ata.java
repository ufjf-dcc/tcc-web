package br.ufjf.tcc.pdfHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;

public abstract class Ata {
	
	protected static int qtAvaliador;

	protected TCC tcc;
	protected int idAluno;
	protected String[] avaliadores;
	protected String dia;
	protected String mes;
	protected String ano;
	protected String hora;
	protected String sala;
	protected List<TCC> trabMarcados;
	
	protected String template;
	protected FileOutputStream saida;
	protected PdfReader leitor;
	protected PdfStamper stamper;
	protected PdfContentByte over;
	protected AcroFields form;
	protected BaseFont bfTextoSimples ;
	protected BaseFont campoStyle ;
	
	public static final String COMPOSICAO_BANCA = "ComposicaoBanca";
	public static final String COMPOSICAO_BANCA_FINAL = "ComposicaoBancaFinal";
	
	protected static final String FICHA_AVALIACAO_FINAL = "FichaAvaliacaoFinal";
	protected static final String FICHA_AVALIACAO_INDIVIDUAL = "FichaAvaliacaoIndividual";
	protected static final String TEMPLATE_SEM_COORIENTADOR = "TemplateSCoorientador";
	protected static final String TEMPLATE_COM_COORIENTADOR = "TemplateCoorientador";
	
	public static final String FICHA_COMPLETA = "FichaCompleta" ;
	public static final String EXTENSAO_PDF = ".pdf";
	public static final String PASTA_ARQUIVOS_TEMP = ConfHandler.getConf("FILE.PATH") + "arquivosTemporarios/";
	public static final String PASTA_COM_TEMPLATE_ATAS = ConfHandler.getConf("FILE.PATH") + "templatePDF/";
	
	public Ata(TCC tcc){
		if(tcc==null)
			return;
		
		this.tcc = tcc;
		idAluno = tcc.getAluno().getIdUsuario();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tcc.getDataApresentacao().getTime());
		Integer dia = calendar.get(5);
		Integer mes = calendar.get(2);
		Integer ano = calendar.get(1);
		String hora = Integer.toString(calendar.get(11)) + "h";
		
		this.dia = dia.toString() ; 
		this.mes = mes.toString();
		this.ano = ano.toString();
		this.hora = hora;
		this.sala = tcc.getSalaDefesa() ;
		
		List<Participacao> part = new ArrayList<Participacao>();
		Participacao orientador = new Participacao();
		Participacao coorientador = new Participacao();
		orientador.setProfessor(tcc.getOrientador());
		orientador.setTcc(tcc);
		part.add(orientador);
		
		if (tcc.getCoOrientador() != null) {
			coorientador.setProfessor(tcc.getCoOrientador());
			part.add(coorientador);
		}
		
		for (Participacao p : tcc.getParticipacoes()) {
			if (p.getSuplente() != 1) {
				part.add(p);
			}
		}
		inicializarParticipacoes(part);
		
	}

	public abstract void preencherPDF() throws Exception;
	protected abstract String getPathTemplate() throws Exception;

	public void inicializarParticipacoes(List<Participacao> ps) {
		int qt = ps.size();

		qtAvaliador = qt;
		avaliadores = new String[qtAvaliador];
		if (qtAvaliador != 0) {
			for (int i = 0; i < qtAvaliador; i++) {
				avaliadores[i] = ps.get(i).getProfessor().getNomeUsuario();

			}
		}

	}

	protected void iniciarParametros() throws Exception {
		
		String ARQUIVO_SAIDA = PASTA_ARQUIVOS_TEMP + FICHA_AVALIACAO_FINAL + idAluno + EXTENSAO_PDF;
		template = getPathTemplate();
		saida = new FileOutputStream(ARQUIVO_SAIDA);
		leitor = new PdfReader(new FileInputStream(template));
		stamper = new PdfStamper(leitor, saida);
		bfTextoSimples = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		form = stamper.getAcroFields();

	}
	
	protected void preencherFichasAvaliacaoIndividual() {
		try {

			for (int i = 0; i < avaliadores.length; i++) {
				PreenchimentoPDF.preencherFichaAvaliacaoIndividual(tcc.getAluno().getNomeUsuario(), avaliadores[i], dia, mes, ano,
						i, idAluno,PASTA_COM_TEMPLATE_ATAS,tcc);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	protected void fecharFluxos() throws DocumentException, IOException {
		stamper.setFormFlattening(true);
		over.endText();
		stamper.close();
		saida.close();
		leitor.close();
	}
	
	public boolean existe(){
		if(tcc==null)
			return false;
		
		File arquivoAta=null;
		File arquivoFichaAvaliacaoIndividual = null;
		try{
			arquivoFichaAvaliacaoIndividual = new File(PASTA_COM_TEMPLATE_ATAS+FICHA_AVALIACAO_INDIVIDUAL+tcc.getAluno().getCurso().getCodigoCurso()+EXTENSAO_PDF);
			if (tcc.getCoOrientador() == null){
				arquivoAta = new File(PASTA_COM_TEMPLATE_ATAS+TEMPLATE_SEM_COORIENTADOR+tcc.getAluno().getCurso().getCodigoCurso()+EXTENSAO_PDF);
			}else {
				arquivoAta = new File(PASTA_COM_TEMPLATE_ATAS+TEMPLATE_COM_COORIENTADOR+tcc.getAluno().getCurso().getCodigoCurso()+EXTENSAO_PDF);
			}			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		if(arquivoAta!=null && arquivoAta.exists() 
				&& arquivoFichaAvaliacaoIndividual!=null && arquivoFichaAvaliacaoIndividual.exists())
			return true;		
		return false;
	}
	
	public void deletarPDFsFichaGerados() {

		for (int i = 0; i < qtAvaliador; i++) {
			deleteFile(PASTA_ARQUIVOS_TEMP + idAluno + "-" + i + ".pdf");
		}
		deleteFile(PASTA_ARQUIVOS_TEMP + FICHA_AVALIACAO_FINAL + idAluno + ".pdf");
		deleteFile(PASTA_ARQUIVOS_TEMP + FICHA_AVALIACAO_INDIVIDUAL + idAluno + ".pdf");

	}
	
	public void deletarPDFsExibicaoBanca(){
		if(trabMarcados!=null){
			for (int i = 0; i < trabMarcados.size(); i++) {
				deleteFile(PASTA_ARQUIVOS_TEMP + Ata.COMPOSICAO_BANCA + idAluno + "-" + i + ".pdf");
			}			
		}
	}
	
	private void deleteFile(String path){
		File file = new File(path);
		if(file.exists()){
			if(file.delete())
				System.out.println(path+" - DELETADO");
		}
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno;
	}

	public String[] getAvaliadores() {
		return avaliadores;
	}

	public void setAvaliadores(String[] avaliadores) {
		this.avaliadores = avaliadores;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public static String getMesPeloNumero(String x) {
		int x1 = Integer.parseInt(x);

		String meses[] = new String[12];
		meses[0] = "janeiro";
		meses[1] = "fevereiro";
		meses[2] = "março";
		meses[3] = "abril";
		meses[4] = "maio";
		meses[5] = "junho";
		meses[6] = "julho";
		meses[7] = "agosto";
		meses[8] = "setembro";
		meses[9] = "outubro";
		meses[10] = "novembro";
		meses[11] = "dezembro";

		return meses[x1];

	}

	public List<TCC> getTrabMarcados() {
		return trabMarcados;
	}

	public void setTrabMarcados(List<TCC> trabMarcados) {
		this.trabMarcados = trabMarcados;
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}
	
}
