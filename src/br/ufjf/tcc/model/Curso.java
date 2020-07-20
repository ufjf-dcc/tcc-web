package br.ufjf.tcc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * DTO da Tabela {@code Curso} contém os atributos e relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "curso")
public class Curso implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do Curso. Relaciona com a coluna {@code idCurso} do banco e
	 * é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idCurso", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idCurso;

	/**
	 * Campo com o nome do curso. Relaciona com a coluna {@code codigoCurso} do
	 * banco através da anotação
	 * {@code @Column(name = "codigoCurso", length = 80, nullable = false)}.
	 */
	@Column(name = "codigoCurso", length = 50, nullable = false)
	private String codigoCurso;

	/**
	 * Campo com o nome do curso. Relaciona com a coluna {@code nomeCurso} do
	 * banco através da anotação
	 * {@code @Column(name = "nomeCurso", length = 250, nullable = false)}.
	 */
	@Column(name = "nomeCurso", length = 250, nullable = false)
	private String nomeCurso;

	/**
	 * Relacionamento 1 para N entre Curso e Usuários. Mapeada em
	 * {@link Usuario} pela variável {@code curso} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Curso}.
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curso")
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	/**
	 * Relacionamento 1 para N entre Curso e Aviso. Mapeada em
	 * {@link Aviso} pela variável {@code curso} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Curso}.
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curso")
	private List<Aviso> avisos = new ArrayList<Aviso>();

	/**
	 * Relacionamento 1 para N entre Curso e Questionario. Mapeada em
	 * {@link Questionario} pela variável {@code curso} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Curso}.
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curso")
	private List<Questionario> questionarios = new ArrayList<Questionario>();

	/**
	 * Relacionamento 1 para N entre Curso e CalendarioSemestre. Mapeada em
	 * {@link CalendarioSemestre} pela variável {@code curso} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Curso}.
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curso")
	private List<CalendarioSemestre> calendarios = new ArrayList<CalendarioSemestre>();

	@Transient
	private boolean editingStatus;

	public Curso() {

	}

	public Curso(String codigoCurso, String nomeCurso) {
		this.codigoCurso = codigoCurso;
		this.nomeCurso = nomeCurso;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getCodigoCurso() {
		return codigoCurso;
	}

	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Questionario> getQuestionarios() {
		return questionarios;
	}

	public void setQuestionarios(List<Questionario> questionarios) {
		this.questionarios = questionarios;
	}

	public List<CalendarioSemestre> getCalendarios() {
		return calendarios;
	}

	public void setCalendarios(List<CalendarioSemestre> calendarios) {
		this.calendarios = calendarios;
	}

	public List<Aviso> getAvisos() {
		return avisos;
	}

	public void setAvisos(List<Aviso> avisos) {
		this.avisos = avisos;
	}

	public boolean getEditingStatus() {
		return editingStatus;
	}

	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}

	public void copy(Curso another) {
		this.idCurso = another.idCurso;
		this.codigoCurso = another.codigoCurso;
		this.nomeCurso = another.nomeCurso;
		this.usuarios = another.usuarios;
		this.questionarios = another.questionarios;
		this.calendarios = another.calendarios;
		this.editingStatus = another.editingStatus;
	}

}
