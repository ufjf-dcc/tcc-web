package br.ufjf.tcc.pdfHandle;

import java.io.File;
import java.util.List;

import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;

public abstract class Ata {

	protected static int qtAvaliador;

	protected int idAluno;
	protected String aluno;
	protected String tituloTCC;
	protected String orientador;
	protected String coorientador = null;
	protected String[] avaliadores;
	protected byte[] pdfByteArray = null;
	protected String pathTemplateAta = null ;
	protected String dia;
	protected String mes;
	protected String ano;
	protected String hora;
	protected String sala;
	protected List<TCC> trabMarcados;

	public abstract void preenchePrincipal() throws Exception;

	public void preencheParticipacoes(List<Participacao> ps) {
		int qt = ps.size();

		qtAvaliador = qt;
		avaliadores = new String[qtAvaliador];
		if (qtAvaliador != 0) {
			for (int i = 0; i < qtAvaliador; i++) {
				avaliadores[i] = ps.get(i).getProfessor().getNomeUsuario();

			}
		} else {

			System.out.println("participacao 0");
		}

	}

	public void deleteLasts() {

		File d;

		for (int i = 0; i < qtAvaliador; i++) {
			d = new File(ConfHandler.getConf("FILE.PATH") + "last" + idAluno
					+ "-" + i + ".pdf");
			if (d.delete())
				System.out.println("deletado");
			else
				System.out.println("NAO deletado");
		}
		
		if(trabMarcados!=null){
			for (int i = 0; i < trabMarcados.size(); i++) {
				d = new File(ConfHandler.getConf("FILE.PATH") + "last" + idAluno
						+ "-" + i + ".pdf");
				if (d.delete())
					System.out.println("deletado");
				else
					System.out.println("NAO deletado");
			}			
		}
		

		d = new File(ConfHandler.getConf("FILE.PATH") + "saida" + idAluno
				+ ".pdf");
		if (d.delete())
			System.out.println("deletado");
		else
			System.out.println("NAO deletado");

		d = new File(ConfHandler.getConf("FILE.PATH") + "saidaLP" + idAluno
				+ ".pdf");
		if (d.delete())
			System.out.println("deletado");
		else
			System.out.println("NAO deletado");
		
		

	}

	public String getAluno() {
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public String getOrientador() {
		return orientador;
	}

	public void setOrientador(String orientador) {
		this.orientador = orientador;
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

	public String getCoorientador() {
		return coorientador;
	}

	public void setCoorientador(String coorientador) {
		this.coorientador = coorientador;
	}

	public String[] getAvaliadores() {
		return avaliadores;
	}

	public void setAvaliadores(String[] avaliadores) {
		this.avaliadores = avaliadores;
	}

	public String getTituloTCC() {
		return tituloTCC;
	}

	public void setTituloTCC(String tituloTCC) {
		this.tituloTCC = tituloTCC;
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
		meses[2] = "mar�o";
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

	public byte[] getPdfByteArray() {
		return pdfByteArray;
	}

	public void setPdfByteArray(byte[] pdfByteArray) {
		this.pdfByteArray = pdfByteArray;
	}

	public String getPathTemplateAta() {
		return pathTemplateAta;
	}

	public void setPathTemplateAta(String pathTemplateAta) {
		this.pathTemplateAta = pathTemplateAta;
	}

	public List<TCC> getTrabMarcados() {
		return trabMarcados;
	}

	public void setTrabMarcados(List<TCC> trabMarcados) {
		this.trabMarcados = trabMarcados;
	}
	
	
	

}
