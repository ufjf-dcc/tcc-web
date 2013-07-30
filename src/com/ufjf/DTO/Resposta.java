package com.ufjf.DTO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * DTO da Tabela {@code Resposta} contém os atributos e relacionamentos da
 * mesma.
 * 
 */
@Entity
@Table(name = "Resposta")
public class Resposta implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID da Resposta. Relaciona com a coluna {@code idResposta} do
	 * banco e é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idResposta", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idResposta;

	/**
	 * Campo com a resposta. Relaciona com a coluna {@code resposta} do banco
	 * através da anotação {@code @Column(name = "resposta", nullable = false)}.
	 */
	@Column(name = "resposta", nullable = false)
	private float resposta;

	/**
	 * Relacionamento N para 1 entre Resposta e Pergunta. Mapeando
	 * {@link Pergunta} na variável {@code pergunta} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos a {@link Resposta}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPergunta", nullable = false)
	private Pergunta pergunta;

	/**
	 * Relacionamento N para 1 entre Resposta e Participacao. Mapeando
	 * {@link Participacao} na variável {@code participacao} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos a {@link Resposta}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idParticipacao", nullable = false)
	private Participacao participacao;

	public int getIdResposta() {
		return idResposta;
	}

	public void setIdResposta(int idResposta) {
		this.idResposta = idResposta;
	}

	public float getResposta() {
		return resposta;
	}

	public void setResposta(float resposta) {
		this.resposta = resposta;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public Participacao getParticipacao() {
		return participacao;
	}

	public void setParticipacao(Participacao participacao) {
		this.participacao = participacao;
	}

}
