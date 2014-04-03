package br.ufjf.tcc.model;

import java.io.Serializable;
import java.util.Date;

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
 * DTO da Tabela {@code Prazo} contém os atributos e relacionamentos da mesma.
 * 
 */

@Entity
@Table(name = "Prazo")
public class Prazo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Prazos do processo do TCC
	public static final int ENTREGA_TCC_BANCA = 0, ENTREGA_FORM_BANCA = 1, DEFESA = 2, ENTREGA_FINAL = 3;

	/**
	 * Campo com ID do Prazo. Relaciona com a coluna {@code idPrazo} do banco e
	 * é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idPrazo", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idPrazo;

	/**
	 * Campo com a data final do prazo. Relaciona com a coluna {@code dataFinal}
	 * do banco através da anotação
	 * {@code @Column(name = "dataFinal", nullable = false)}.
	 */
	@Column(name = "dataFinal", nullable = false)
	private Date dataFinal;

	/**
	 * Campo com o tipo do prazo. Relaciona com a coluna {@code tipo} do banco
	 * através da anotação {@code @Column(name = "tipo", nullable = false)}.
	 */
	@Column(name = "tipo", nullable = false)
	private int tipo;

	/**
	 * Relacionamento N para 1 entre Prazo e CalendarioSemestre. Mapeando
	 * {@link CalendarioSemestre} na variável {@code calendarioSemestre} e
	 * retorno do tipo {@code LAZY} que indica que não será carregado
	 * automáticamente este dado quando retornarmos o {@link Prazo}.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCalendarioSemestre", nullable = false)
	private CalendarioSemestre calendarioSemestre;

	public int getIdPrazo() {
		return idPrazo;
	}

	public void setIdPrazo(int idPrazo) {
		this.idPrazo = idPrazo;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public CalendarioSemestre getCalendarioSemestre() {
		return calendarioSemestre;
	}

	public void setCalendarioSemestre(CalendarioSemestre calendarioSemestre) {
		this.calendarioSemestre = calendarioSemestre;
	}

}
