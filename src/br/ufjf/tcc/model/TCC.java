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

/**
 * DTO da Tabela {@code TCC} contém os atributos e relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "TCC")
public class TCC implements Serializable {

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
	 * {@code @Column(name = "nomeTCC", length = 45, nullable = true)}.
	 */
	@Column(name = "nomeTCC", length = 45, nullable = true)
	private String nomeTCC;

	/**
	 * Campo com o arquivo do TCC. Relaciona com a coluna
	 * {@code arquivoTCCBanca} do banco através da anotação
	 * {@code @Column(name = "arquivoTCCBanca", length = 45, nullable = true)}.
	 */
	@Column(name = "arquivoTCCBanca", length = 45, nullable = true)
	private String arquivoTCCBanca;

	/**
	 * Campo com o arquivo final do TCC. Relaciona com a coluna
	 * {@code arquivoTCCFinal} do banco através da anotação
	 * {@code @Column(name = "arquivoTCCFinal", length = 45, nullable = true)}.
	 */
	@Column(name = "arquivoTCCFinal", length = 45, nullable = true)
	private String arquivoTCCFinal;

	/**
	 * Campo com a sala de defesa. Relaciona com a coluna {@code salaDefesa} do
	 * banco através da anotação
	 * {@code @Column(name = "salaDefesa", length = 45, nullable = true)}.
	 */
	@Column(name = "salaDefesa", length = 45, nullable = true)
	private String salaDefesa;

	/**
	 * Campo com o resumo do TCC. Relaciona com a coluna {@code resumoTCC} do
	 * banco através da anotação
	 * {@code @Column(name = "resumoTCC", length = 45, nullable = true)}.
	 */
	@Column(name = "resumoTCC", length = 45, nullable = true)
	private String resumoTCC;

	/**
	 * Campo com o parecer da banca. Relaciona com a coluna {@code parecerBanca}
	 * do banco através da anotação
	 * {@code @Column(name = "parecerBanca", length = 45, nullable = true)}.
	 */
	@Column(name = "parecerBanca", length = 45, nullable = true)
	private String parecerBanca;

	/**
	 * Campo com o conceito final. Relaciona com a coluna {@code conceitoFinal}
	 * do banco através da anotação
	 * {@code @Column(name = "conceitoFinal", nullable = true)}.
	 */
	@Column(name = "conceitoFinal", nullable = true)
	private float conceitoFinal;

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
	 * Relacionamento 1 para N entre TCC e Participacao. Mapeada em
	 * {@link Participacao} pela variável {@code tcc} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link TCC} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tcc")
	private List<Participacao> participacoes = new ArrayList<Participacao>();

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

	public List<Participacao> getParticipacoes() {
		return participacoes;
	}

	public void setParticipacoes(List<Participacao> participacoes) {
		this.participacoes = participacoes;
	}

}
