package br.ufjf.tcc.model;

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
 * DTO da Tabela {@code Aviso} contém os atributos e relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "Aviso")
public class Aviso implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do Aviso. Relaciona com a coluna {@code idAviso} do banco e
	 * é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idAviso", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idAviso;

	/**
	 * Relacionamento N para 1 entre Aviso e Curso. Mapeando {@link Curso} na
	 * variável {@code curso} e retorno do tipo {@code LAZY} que indica que não
	 * será carregado automáticamente este dado quando retornarmos o
	 * {@link Aviso}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCurso", nullable = false)
	private Curso curso;

	/**
	 * Campo com a informação. Relaciona com a coluna {@code aviso} do banco
	 * através da anotação
	 * {@code @Column(name = "mensagem", length = 255, nullable = false)}.
	 */
	@Column(name = "mensagem", length = 255, nullable = false)
	private String mensagem;

	public int getIdAviso() {
		return idAviso;
	}

	public void setIdAviso(int idAviso) {
		this.idAviso = idAviso;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
