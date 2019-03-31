package br.ufjf.tcc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.ufjf.tcc.business.TCCBusiness;

/**
 * DTO da Tabela {@code TCC} contém os atributos e relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "TCC")
public class TCC implements Serializable,Comparable<TCC> {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do TCC. Relaciona com a coluna {@code idTCC} do banco e é
	 * gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idTCC", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idTCC;

	/**
	 * Campo com o nome do TCC. Relaciona com a coluna {@code nomeTCC} do banco
	 * através da anotação
	 * {@code @Column(name = "nomeTCC", length = 255, nullable = true)}.
	 */
	@Column(name = "nomeTCC", length = 255, nullable = true)
	private String nomeTCC;

	/**
	 * Campo com o sub nome do TCC. Relaciona com a coluna {@code subNomeTCC} do
	 * banco através da anotação
	 * {@code @Column(name = "subNomeTCC", length = 255, nullable = true)}.
	 */
	@Column(name = "subNomeTCC", length = 255, nullable = true)
	private String subNomeTCC;

	/**
	 * Campo com o resumo do TCC. Relaciona com a coluna {@code resumoTCC} do
	 * banco através da anotação
	 * {@code @Column(name = "resumoTCC", nullable = true)}.
	 */
	@Column(name = "resumoTCC", nullable = true)
	private String resumoTCC;

	/**
	 * Campo com as palavras chaves do TCC. Relaciona com a coluna
	 * {@code palavrasChave} do banco através da anotação
	 * {@code @Column(name = "palavrasChave", length = 255, nullable = true)}.
	 */
	@Column(name = "palavrasChave", length = 255, nullable = true)
	private String palavrasChave;

	/**
	 * Campo com o arquivo do TCC. Relaciona com a coluna
	 * {@code arquivoTCCBanca} do banco através da anotação
	 * {@code @Column(name = "arquivoTCCBanca", length = 255, nullable = true)}.
	 */
	@Column(name = "arquivoTCCBanca", length = 255, nullable = true)
	private String arquivoTCCBanca;

	/**
	 * Campo com o arquivo final do TCC. Relaciona com a coluna
	 * {@code arquivoTCCFinal} do banco através da anotação
	 * {@code @Column(name = "arquivoTCCFinal", length = 255, nullable = true)}.
	 */
	@Column(name = "arquivoTCCFinal", length = 255, nullable = true)
	private String arquivoTCCFinal;

	/**
	 * Campo com o arquivo extra final do TCC. Relaciona com a coluna
	 * {@code arquivoExtraTCCBanca} do banco através da anotação
	 * {@code @Column(name = "arquivoExtraTCCBanca", length = 255, nullable = true)}
	 * .
	 */
	@Column(name = "arquivoExtraTCCBanca", length = 255, nullable = true)
	private String arquivoExtraTCCBanca;

	/**
	 * Campo com o arquivo extra final do TCC. Relaciona com a coluna
	 * {@code arquivoExtraTCCFinal} do banco através da anotação
	 * {@code @Column(name = "arquivoExtraTCCFinal", length = 255, nullable = true)}
	 * .
	 */
	@Column(name = "arquivoExtraTCCFinal", length = 255, nullable = true)
	private String arquivoExtraTCCFinal;

	/**
	 * Campo com a sala de defesa. Relaciona com a coluna {@code salaDefesa} do
	 * banco através da anotação
	 * {@code @Column(name = "salaDefesa", length = 50, nullable = true)}.
	 */
	@Column(name = "salaDefesa", length = 50, nullable = true)
	private String salaDefesa;

	/**
	 * Campo com o parecer da banca. Relaciona com a coluna {@code parecerBanca}
	 * do banco através da anotação
	 * {@code @Column(name = "parecerBanca", nullable = true)}.
	 */
	@Column(name = "parecerBanca", nullable = true)
	private String parecerBanca;

	/**
	 * Campo com o conceito final. Relaciona com a coluna {@code conceitoFinal}
	 * do banco através da anotação
	 * {@code @Column(name = "conceitoFinal", nullable = true)}.
	 */
	@Column(name = "conceitoFinal", nullable = true)
	private float conceitoFinal = -1;

	/**
	 * Campo com a data da aprensetação. Relaciona com a coluna
	 * {@code dataApresentacao} do banco através da anotação
	 * {@code @Column(name = "dataApresentacao", nullable = true)}.
	 */
	@Column(name = "dataApresentacao", nullable = true)
	private Timestamp dataApresentacao;

	/**
	 * Campo com a data do envio do arquivo do tcc. Relaciona com a coluna
	 * {@code dataEnvioBanca} do banco através da anotação
	 * {@code @Column(name = "dataEnvioBanca", nullable = true)}.
	 */
	@Column(name = "dataEnvioBanca", nullable = true)
	private Timestamp dataEnvioBanca;

	/**
	 * Campo com a data do envio do arquivo do tcc. Relaciona com a coluna
	 * {@code dataEnvioFinal} do banco através da anotação
	 * {@code @Column(name = "dataEnvioFinal", nullable = true)}.
	 */
	@Column(name = "dataEnvioFinal", nullable = true)
	private Timestamp dataEnvioFinal;

	/**
	 * Campo com a situação do tcc (publicado ou não). Relaciona com a coluna
	 * {@code publicado} do banco através da anotação
	 * {@code @Column(name = "publicado", nullable = false)}.
	 */
	@Column(name = "publicado", nullable = true)
	private boolean publicado;
	
	/**

	Campo com a situação do tcc (projeto ou não). Relaciona com a coluna
	{@code projeto} do banco através da anotação
	{@code @Column(name = "projeto", nullable = false)}. */ 
	@Column(name = "projeto", nullable = false) 
	private boolean projeto;
	
	@Column(name = "entregouDoc", nullable = false) 
	private boolean entregouDoc;
	
	@Column(name = "trabFinal", nullable = false) 
	private boolean trabFinal;
	
	@Column(name = "arqProjFinal", length = 255, nullable = true)
	private String arqProjFinal;
	
	@Column(name = "arqExtraProjFinal", length = 255, nullable = true)
	private String arqExtraProjFinal;

	/**
	 * Relacionamento N para 1 entre TCC e Usuario. Mapeando {@link Usuario} na
	 * variável {@code aluno} e retorno do tipo {@code LAZY} que indica que não
	 * será carregado automáticamente este dado quando retornarmos o {@link TCC}
	 * .
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idAluno", nullable = false)
	private Usuario aluno;

	/**
	 * Relacionamento N para 1 entre TCC e Usuario. Mapeando {@link Usuario} na
	 * variável {@code orientador} e retorno do tipo {@code LAZY} que indica que
	 * não será carregado automáticamente este dado quando retornarmos o
	 * {@link TCC}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrientador", nullable = false)
	private Usuario orientador;

	/**
	 * Relacionamento N para 1 entre TCC e Usuario. Mapeando {@link Usuario} na
	 * variável {@code coOrientador} e retorno do tipo {@code LAZY} que indica
	 * que não será carregado automáticamente este dado quando retornarmos o
	 * {@link TCC}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCoOrientador", nullable = true)
	private Usuario coOrientador;

	/**
	 * Relacionamento 1 para N entre TCC e Participacao. Mapeada em
	 * {@link Participacao} pela variável {@code tcc} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link TCC} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tcc")
	private List<Participacao> participacoes = new ArrayList<Participacao>();

	/**
	 * Relacionamento N para 1 entre TCC e CalendarioSemestre. Mapeando
	 * {@link CalendarioSemestre} na variável {@code calendarioSemestre} e
	 * retorno do tipo {@code LAZY} que indica que não será carregado
	 * automáticamente este dado quando retornarmos o {@link TCC}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCalendarioSemestre", nullable = true)
	private CalendarioSemestre calendarioSemestre = null;
	
	@Column(name = "qtVisualizacoes", nullable = false) 
	private int qtVisualizacoes;
	
	@Column(name = "qtDownloads", nullable = false) 
	private int qtDownloads;

	public TCC() {

	}

	public TCC(String nomeTCC, String subNomeTCC, String resumoTCC,
			String palavrasChave, String arquivoTCCFinal,
			String arquivoExtraTCCFinal, Timestamp dataEnvioFinal,
			Usuario aluno, Usuario orientador) {
		super();
		this.nomeTCC = nomeTCC;
		this.subNomeTCC = subNomeTCC;
		this.resumoTCC = resumoTCC;
		this.palavrasChave = palavrasChave;
		this.arquivoTCCFinal = arquivoTCCFinal;
		this.arquivoExtraTCCFinal = arquivoExtraTCCFinal;
		this.dataEnvioFinal = dataEnvioFinal;
		this.aluno = aluno;
		this.orientador = orientador;
	}

	public int getIdTCC() {
		return idTCC;
	}

	public void setIdTCC(int idTCC) {
		this.idTCC = idTCC;
	}

	public String getNomeTCC() {
		return nomeTCC;
	}

	public void setNomeTCC(String nomeTCC) {
		this.nomeTCC = nomeTCC;
	}

	public String getSubNomeTCC() {
		return subNomeTCC;
	}

	public void setSubNomeTCC(String subNomeTCC) {
		this.subNomeTCC = subNomeTCC;
	}

	public String getArquivoTCCBanca() {
		return arquivoTCCBanca;
	}

	public void setArquivoTCCBanca(String arquivoTCCBanca) {
		this.arquivoTCCBanca = arquivoTCCBanca;
	}

	public String getArquivoTCCFinal() {
		return arquivoTCCFinal;
	}

	public void setArquivoTCCFinal(String arquivoTCCFinal) {
		this.arquivoTCCFinal = arquivoTCCFinal;
	}

	public String getArquivoExtraTCCBanca() {
		return arquivoExtraTCCBanca;
	}

	public void setArquivoExtraTCCBanca(String arquivoExtraTCCBanca) {
		this.arquivoExtraTCCBanca = arquivoExtraTCCBanca;
	}

	public String getArquivoExtraTCCFinal() {
		return arquivoExtraTCCFinal;
	}

	public void setArquivoExtraTCCFinal(String arquivoExtraTCCFinal) {
		this.arquivoExtraTCCFinal = arquivoExtraTCCFinal;
	}

	public String getSalaDefesa() {
		return salaDefesa;
	}

	public void setSalaDefesa(String salaDefesa) {
		this.salaDefesa = salaDefesa;
	}

	public String getResumoTCC() {
		return resumoTCC;
	}

	public void setResumoTCC(String resumoTCC) {
		this.resumoTCC = resumoTCC;
	}

	public String getParecerBanca() {
		return parecerBanca;
	}

	public void setParecerBanca(String parecerBanca) {
		this.parecerBanca = parecerBanca;
	}

	public float getConceitoFinal() {
		return conceitoFinal;
	}

	public void setConceitoFinal(float conceitoFinal) {
		this.conceitoFinal = conceitoFinal;
	}

	public Timestamp getDataApresentacao() {
		return dataApresentacao;
	}

	public void setDataApresentacao(Timestamp dataApresentacao) {
		this.dataApresentacao = dataApresentacao;
	}

	public Timestamp getDataEnvioBanca() {
		return dataEnvioBanca;
	}

	public void setDataEnvioBanca(Timestamp dataEnvioBanca) {
		this.dataEnvioBanca = dataEnvioBanca;
	}

	public Timestamp getDataEnvioFinal() {
		return dataEnvioFinal;
	}

	public void setDataEnvioFinal(Timestamp dataEnvioFinal) {
		this.dataEnvioFinal = dataEnvioFinal;
	}

	public boolean isPublicado() {
		return publicado;
	}

	public void setPublicado(boolean publicado) {
		this.publicado = publicado;
	}

	public Usuario getAluno() {
		return aluno;
	}

	public void setAluno(Usuario aluno) {
		this.aluno = aluno;
	}

	public Usuario getOrientador() {
		return orientador;
	}

	public void setOrientador(Usuario orientador) {
		this.orientador = orientador;
	}

	public Usuario getCoOrientador() {
		return coOrientador;
	}

	public void setCoOrientador(Usuario coOrientador) {
		this.coOrientador = coOrientador;
	}

	public List<Participacao> getParticipacoes() {
		return participacoes;
	}

	public void setParticipacoes(List<Participacao> participacoes) {
		this.participacoes = participacoes;
	}

	public String getPalavrasChave() {
		return palavrasChave;
	}

	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}

	public CalendarioSemestre getCalendarioSemestre() {
		return calendarioSemestre;
	}

	public void setCalendarioSemestre(CalendarioSemestre calendarioSemestre) {
		this.calendarioSemestre = calendarioSemestre;
	}
	
	public boolean isProjeto() {
		return projeto;
		}

	public void setProjeto(boolean projeto) {
		this.projeto = projeto;
	}

	public String getStatusTCC()
	{
		return (new TCCBusiness()).getStatusTCC(this);
	}
	
	public int compareTo(TCC outroTcc){
		if(this.getDataEnvioFinal()!=null && outroTcc.getDataEnvioFinal()!=null){
			if(this.getDataEnvioFinal().before(outroTcc.getDataEnvioFinal()))
				return -1;
			if(this.getDataEnvioFinal().after(outroTcc.getDataEnvioFinal()))
				return 1;
			return 0;
			
		}
		if(this.getDataEnvioFinal()==null && outroTcc.getDataEnvioFinal()!=null){
			return 1;
		}
		return -1;
	}

	public boolean isEntregouDoc() {
		return entregouDoc;
	}

	public void setEntregouDoc(boolean entregouDoc) {
		this.entregouDoc = entregouDoc;
	}

	public String getArqProjFinal() {
		return arqProjFinal;
	}

	public void setArqProjFinal(String arqProjFinal) {
		this.arqProjFinal = arqProjFinal;
	}

	public String getArqExtraProjFinal() {
		return arqExtraProjFinal;
	}

	public void setArqExtraProjFinal(String arqExtraProjFinal) {
		this.arqExtraProjFinal = arqExtraProjFinal;
	}

	public boolean isTrabFinal() {
		return trabFinal;
	}

	public void setTrabFinal(boolean trabFinal) {
		this.trabFinal = trabFinal;
	}

	public int getQtVisualizacoes() {
		return qtVisualizacoes;
	}

	public void setQtVisualizacoes(int qtVisualizacoes) {
		this.qtVisualizacoes = qtVisualizacoes;
	}

	public int getQtDownloads() {
		return qtDownloads;
	}

	public void setQtDownloads(int qtDownloads) {
		this.qtDownloads = qtDownloads;
	}
	
	public List<Usuario> getProfessoresParticipacoes() {
		List<Usuario> usuarioParticipacoes = new ArrayList<>();
		for (Participacao participacao : getParticipacoes()) {
			usuarioParticipacoes.add(participacao.getProfessor());
		}
		return usuarioParticipacoes;
	}
	
	public boolean possuiCoorientador() {
		return coOrientador!=null;
	}
	
	public boolean isQuantidadeParticipacoesValidas(){
		return getParticipacoes()!=null && getParticipacoes().size() >= 3;
	}
	
	public boolean isTarefasDentroDoPrazo()
	{
		return (new TCCBusiness()).isTarefasDentroDoPrazo(this);
	}
}
