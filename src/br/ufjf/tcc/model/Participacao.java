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
 * DTO da Tabela {@code Participacao} contém os atributos e relacionamentos da
 * mesma.
 * 
 */
@Entity
@Table(name = "Participacao")
public class Participacao implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID da Participacao. Relaciona com a coluna
	 * {@code idParticipacao} do banco e é gerado por autoincrement do MySQL
	 * através das anotações {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idParticipacao", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idParticipacao;

	/**
	 * Campo com a titulacao do professor. Relaciona com a coluna
	 * {@code titulacao} do banco através da anotação
	 * {@code @Column(name = "titulacao", length = 45, nullable = false)}.
	 */
	@Column(name = "titulacao", length = 45, nullable = false)
	private String titulacao;

	/**
	 * Relacionamento 1 para N entre Participacao e Resposta. Mapeada em
	 * {@link Resposta} pela variável {@code participacao} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos a {@link Pariticipacao} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "participacao")
	private List<Resposta> respostas = new ArrayList<Resposta>();

	/**
	 * Relacionamento N para 1 entre Participacao e Usuario. Mapeando
	 * {@link Usuario} na variável {@code professor} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos a {@link Participacao}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario professor;

	/**
	 * Relacionamento N para 1 entre Participacao e TCC. Mapeando {@link TCC} na
	 * variável {@code tecc} e retorno do tipo {@code LAZY} que indica que não
	 * será carregado automáticamente este dado quando retornarmos o
	 * {@link Participacao}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTCC", nullable = false)
	private TCC tcc;

	public int getIdParticipacao() {
		return idParticipacao;
	}

	public void setIdParticipacao(int idParticipacao) {
		this.idParticipacao = idParticipacao;
	}

	public String getTitulacao() {
		return titulacao;
	}

	public void setTitulacao(String titulacao) {
		this.titulacao = titulacao;
	}

	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
	}

	public Usuario getProfessor() {
		return professor;
	}

	public void setProfessor(Usuario professor) {
		this.professor = professor;
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}

}
