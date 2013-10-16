package br.ufjf.tcc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
 * DTO da Tabela {@code CalendarioSemestre} contém os atributos e
 * relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "CalendarioSemestre")
public class CalendarioSemestre implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do CalendarioSemestre. Relaciona com a coluna
	 * {@code idCalendarioSemestre} do banco e é gerado por autoincrement do
	 * MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idCalendarioSemestre", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idCalendarioSemestre;

	/**
	 * Campo com a data do início do semestre. Relaciona com a coluna
	 * {@code inicioSemestre} do banco através da anotação
	 * {@code @Column(name = "inicioSemestre", nullable = false)}.
	 */
	@Column(name = "inicioSemestre", nullable = false)
	private Date inicioSemestre;

	/**
	 * Campo com a data do final do semestre. Relaciona com a coluna
	 * {@code finalSemestre} do banco através da anotação
	 * {@code @Column(name = "finalSemestre", nullable = false)}.
	 */
	@Column(name = "finalSemestre", nullable = false)
	private Date finalSemestre;

	/**
	 * Campo com a nome do calendário(Ex: '2013/1'). Relaciona com a coluna
	 * {@code nomeCalendarioSemestre} do banco através da anotação
	 * {@code @Column(name = "nomeCalendarioSemestre", nullable = false)}.
	 */
	@Column(name = "nomeCalendarioSemestre", nullable = false)
	private String nomeCalendarioSemestre;

	/**
	 * Relacionamento N para 1 entre CalendarioSemestre e Curso. Mapeando
	 * {@link Curso} na variável {@code curso} e retorno do tipo {@code LAZY}
	 * que indica que não será carregado automáticamente este dado quando
	 * retornarmos o {@link CalendarioSemestre}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCurso", nullable = false)
	private Curso curso;

	/**
	 * Relacionamento 1 para N entre CalendarioSemestre e Questionario. Mapeada
	 * em {@link Questionario} pela variável {@code calendarioSemestre} e
	 * retorno do tipo {@code LAZY} que indica que não será carregado
	 * automáticamente este dado quando retornarmos o {@link CalendarioSemestre}
	 * .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "calendarioSemestre")
	private List<Questionario> questionarios = new ArrayList<Questionario>();

	public int getIdCalendarioSemestre() {
		return idCalendarioSemestre;
	}

	public void setIdCalendarioSemestre(int idCalendarioSemestre) {
		this.idCalendarioSemestre = idCalendarioSemestre;
	}

	public Date getInicioSemestre() {
		return inicioSemestre;
	}

	public void setInicioSemestre(Date inicioSemestre) {
		this.inicioSemestre = inicioSemestre;
	}

	public Date getFinalSemestre() {
		return finalSemestre;
	}

	public void setFinalSemestre(Date finalSemestre) {
		this.finalSemestre = finalSemestre;
	}

	public String getNomeCalendarioSemestre() {
		return nomeCalendarioSemestre;
	}

	public void setNomeCalendarioSemestre(String nomeCalendarioSemestre) {
		this.nomeCalendarioSemestre = nomeCalendarioSemestre;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Questionario> getQuestionarios() {
		return questionarios;
	}

	public void setQuestionarios(List<Questionario> questionarios) {
		this.questionarios = questionarios;
	}

}
