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
 * DTO da Tabela {@code Pergunta} contém os atributos e relacionamentos da
 * mesma.
 * 
 */
@Entity
@Table(name = "Pergunta")
public class Pergunta implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID da Pergunta. Relaciona com a coluna {@code idPergunta} do
	 * banco e é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idPergunta", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idPergunta;

	/**
	 * Campo com a pergunta. Relaciona com a coluna {@code pergunta} do banco
	 * através da anotação
	 * {@code @Column(name = "pergunta", length = 45, nullable = false)}.
	 */
	@Column(name = "pergunta", length = 45, nullable = false)
	private String pergunta;

	/**
	 * Campo com a ordem da pergunta. Relaciona com a coluna {@code ordem} do
	 * banco através da anotação
	 * {@code @Column(name = "ordem", nullable = false)}.
	 */
	@Column(name = "ordem", nullable = false)
	private int ordem;

	/**
	 * Relacionamento N para 1 entre Pergunta e Questionario. Mapeando
	 * {@link Questionario} na variável {@code questionario} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos a {@link Pergunta}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idQuestionario", nullable = false)
	private Questionario questionario;

	/**
	 * Relacionamento 1 para N entre Pergunta e Resposta. Mapeada em
	 * {@link Resposta} pela variável {@code pergunta} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos a {@link Pergunta} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pergunta")
	private List<Resposta> respostas = new ArrayList<Resposta>();

	public int getIdPergunta() {
		return idPergunta;
	}

	public void setIdPergunta(int idPergunta) {
		this.idPergunta = idPergunta;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public Questionario getQuestionario() {
		return questionario;
	}

	public void setQuestionario(Questionario questionario) {
		this.questionario = questionario;
	}

	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
	}

}
