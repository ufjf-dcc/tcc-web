package com.ufjf.DTO;

import java.io.Serializable;
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
 * DTO da Tabela {@code Usuario} contém os atributos e relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do Usuario. Relaciona com a coluna {@code idUsuario} do
	 * banco e é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idUsuario", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idUsuario;

	/**
	 * Campo com a matricula do usuario. Relaciona com a coluna
	 * {@code matricula} do banco através da anotação
	 * {@code @Column(name = "matricula", unique = true, length = 45, nullable = false)}
	 * .
	 */
	@Column(name = "matricula", unique = true, length = 45, nullable = false)
	private String matricula;

	/**
	 * Campo com a senha do usuario. Relaciona com a coluna {@code senha} do
	 * banco através da anotação
	 * {@code @Column(name = "senha", length = 45, nullable = false)}.
	 */
	@Column(name = "senha", length = 45, nullable = false)
	private String senha;

	/**
	 * Campo com o email do usuario. Relaciona com a coluna {@code email} do
	 * banco através da anotação
	 * {@code @Column(name = "email", length = 45, nullable = false)}.
	 */
	@Column(name = "email", length = 45, nullable = false)
	private String email;

	/**
	 * Campo com o nome do usuario. Relaciona com a coluna {@code nomeUsuario}
	 * do banco através da anotação
	 * {@code @Column(name = "nomeUsuario", length = 45, nullable = false)}.
	 */
	@Column(name = "nomeUsuario", length = 45, nullable = false)
	private String nomeUsuario;

	/**
	 * Campo com a titulacao do professor/coordenador. Relaciona com a coluna
	 * {@code titulacao} do banco através da anotação
	 * {@code @Column(name = "titulacao", length = 45, nullable = true)}.
	 */
	@Column(name = "titulacao", length = 45, nullable = true)
	private String titulacao;

	/**
	 * Relacionamento N para 1 entre Usuario e TipoUsuario. Mapeando
	 * {@link TipoUsuario} na variável {@code tipoUsuario} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Usuario}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipoUsuario", nullable = false)
	private TipoUsuario tipoUsuario;

	/**
	 * Relacionamento N para 1 entre Usuario e Curso. Mapeando {@link Curso} na
	 * variável {@code curso} e retorno do tipo {@code EAGER} que indica que
	 * será carregado automáticamente este dado quando retornarmos o
	 * {@link Usuario}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idCurso", nullable = true)
	private Curso curso;

	/**
	 * Relacionamento 1 para N entre Usuario e TCC. Mapeada em {@link TCC} pela
	 * variável {@code aluno} e retorno do tipo {@code LAZY} que indica que não
	 * será carregado automáticamente este dado quando retornarmos o
	 * {@link Usuario} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "aluno")
	private List<TCC> tcc = new ArrayList<TCC>();

	/**
	 * Relacionamento 1 para N entre Usuario e TCC. Mapeada em {@link TCC} pela
	 * variável {@code orientador} e retorno do tipo {@code LAZY} que indica que
	 * não será carregado automáticamente este dado quando retornarmos o
	 * {@link Usuario} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orientador")
	private List<TCC> orienta = new ArrayList<TCC>();

	/**
	 * Relacionamento 1 para N entre Participacao e TCC. Mapeada em
	 * {@link Participacao} pela variável {@code professor} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Usuario} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Participacao> participacoes = new ArrayList<Participacao>();

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getTitulacao() {
		return titulacao;
	}

	public void setTitulacao(String titulacao) {
		this.titulacao = titulacao;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<TCC> getTcc() {
		return tcc;
	}

	public void setTcc(List<TCC> tcc) {
		this.tcc = tcc;
	}

	public List<TCC> getOrienta() {
		return orienta;
	}

	public void setOrienta(List<TCC> orienta) {
		this.orienta = orienta;
	}

	public List<Participacao> getParticipacoes() {
		return participacoes;
	}

	public void setParticipacoes(List<Participacao> participacoes) {
		this.participacoes = participacoes;
	}

}
