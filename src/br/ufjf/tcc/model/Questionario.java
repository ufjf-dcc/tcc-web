package br.ufjf.tcc.model;

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
 * DTO da Tabela {@code Questionario} contém os atributos e relacionamentos da
 * mesma.
 * 
 */
@Entity
@Table(name = "Questionario")
public class Questionario implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do Questionario. Relaciona com a coluna
	 * {@code idQuestionario} do banco e é gerado por autoincrement do MySQL
	 * através das anotações {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idQuestionario", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idQuestionario;

	/**
	 * Campo com a situação do questionario(ativo ou inativo). Relaciona com a
	 * coluna {@code ativo} do banco através da anotação
	 * {@code @Column(name = "ativo", nullable = false)}.
	 */
	@Column(name = "ativo", nullable = false)
	private boolean ativo;

	/**
	 * Relacionamento N para 1 entre Questionario e Curso. Mapeando
	 * {@link Curso} na variável {@code curso} e retorno do tipo {@code LAZY}
	 * que indica que não será carregado automáticamente este dado quando
	 * retornarmos o {@link Questionario}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCurso", nullable = false)
	private Curso curso;

	/**
	 * Relacionamento 1 para N entre Questionario e Pergunta. Mapeada em
	 * {@link Pergunta} pela variável {@code questionario} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Questionario} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionario")
	private List<Pergunta> perguntas = new ArrayList<Pergunta>();

	/**
	 * Relacionamento N para 1 entre Questionario e CalendarioSemestre. Mapeando
	 * {@link CalendarioSemestre} na variável {@code calendarioSemestre} e
	 * retorno do tipo {@code LAZY} que indica que não será carregado
	 * automáticamente este dado quando retornarmos o {@link CalendarioSemestre}
	 * .
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCalendarioSemestre", nullable = false)
	private CalendarioSemestre calendarioSemestre;

	public int getIdQuestionario() {
		return idQuestionario;
	}

	public void setIdQuestionario(int idQuestionario) {
		this.idQuestionario = idQuestionario;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

	public CalendarioSemestre getCalendarioSemestre() {
		return calendarioSemestre;
	}

	public void setCalendarioSemestre(CalendarioSemestre calendarioSemestre) {
		this.calendarioSemestre = calendarioSemestre;
	}

}
